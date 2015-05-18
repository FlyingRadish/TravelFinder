package org.liangxw.travelfinder.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.Master;
import org.liangxw.travelfinder.model.Avatar;
import org.liangxw.travelfinder.model.GEOEvent;
import org.liangxw.travelfinder.model.Globe;
import org.liangxw.travelfinder.model.Group;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.service.LocationUpdateService;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.CancelableRunnable;
import org.liangxw.travelfinder.util.PhoneCallTool;
import org.liangxw.travelfinder.util.TimeTool;
import org.liangxw.travelfinder.util.UITool;
import org.liangxw.travelfinder.util.dialog.BaseDialog;
import org.liangxw.travelfinder.util.dialog.MessageDialog;
import org.liangxw.travelfinder.util.logger.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class GroupMapActivity extends BaseActivity implements LocationSource, AMap.OnInfoWindowClickListener {

    private final static String TAG = GroupMapActivity.class.getSimpleName();
    String groupId;

    @InjectView(R.id.map)
    MapView mapView;

    AMap aMap;
    OnLocationChangedListener mListener;

    Updater updater;
    List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addAction(R.id.btn_group_qr_code, R.drawable.qr_code, null);
        ButterKnife.inject(this);

        groupId = getIntent().getStringExtra(Globe.GROUP_ID);
        String groupName = getIntent().getStringExtra(Globe.GROUP_NAME);
        setTitle(groupName);

        //初始化地图
        mapView.onCreate(savedInstanceState);
        initMap();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_map;
    }

    void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setOnInfoWindowClickListener(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        aMap.setMyLocationEnabled(true);
        CancelableRunnable.cancelIfNotNull(updater);
        updater = new Updater(groupId, handler);
        new Thread(updater).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        CancelableRunnable.cancelIfNotNull(updater);
        aMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @OnClick({R.id.btn_group_qr_code})
    void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_group_qr_code:
                startActivity(GroupQRCodeActivity.class, groupId);
                break;
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        Log.i(TAG, "activate");
        mListener = listener;
        Intent intent = new Intent(this, LocationUpdateService.class);
        intent.setAction(LocationUpdateService.ACTION_RUN);
        startService(intent);
        Master.getEventBus().register(this);
    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        Log.i(TAG, "deactivate");
        Master.getEventBus().unregister(this);
        mListener = null;
        Intent intent = new Intent(this, LocationUpdateService.class);
        intent.setAction(LocationUpdateService.ACTION_STOP);
        startService(intent);
    }


    public void onEvent(GEOEvent event) {
        Log.i(TAG, "locationChanged");
        if (mListener != null && event.getLocation() != null) {
            Log.i(TAG, "show blue");
            mListener.onLocationChanged(event.getLocation());// 显示系统小蓝点
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String[] snippets = marker.getSnippet().split("\n");
        String phone = snippets[1].replace("手机:","");

        Log.i(TAG, "call:" + phone);
        PhoneCallTool.call(this, phone);
    }

    static class Updater extends CancelableRunnable {

        String groupId;
        Handler handler;

        Updater(String groupId, Handler handler) {
            this.groupId = groupId;
            this.handler = handler;
        }

        @Override
        public void run() {
            Log.i(TAG, "updater run");
            while (isRun) {
                Log.i(TAG, "update");
                getGroupInfo();
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "updater stop");
        }


        private void getGroupInfo() {
            AVQuery<Group> avQuery = new AVQuery<>(Group.CLASS_NAME);
            avQuery.whereEqualTo(Group.OBJECT_ID, groupId);
            avQuery.getInBackground(groupId, new GetCallback<Group>() {
                @Override
                public void done(Group group, AVException e) {
                    if (e == null && group != null) {
                        Log.printList(TAG, group.getMembers());
                        getAllMemberLocation(group.getMembers());
                    } else if (e == null && group == null) {
                        Log.i(TAG, "no group found");
                        handler.obtainMessage(NO_GROUP).sendToTarget();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void getAllMemberLocation(List<String> members) {
            if (members.size() == 0) {
                handler.obtainMessage(NO_MEMBER).sendToTarget();
                return;
            }
            AVQuery<AVUser> query = AVUser.getQuery();
            query.whereContainedIn(UserWrapper.OBJECT_ID, members);
            query.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(List<AVUser> users, AVException e) {
                    if (e == null) {
                        if (users.size() == 0) {
                            Log.i(TAG, "no members found???");
                            handler.obtainMessage(NO_MEMBER).sendToTarget();
                        } else {
                            List<UserWrapper> wrappers;
                            wrappers = UserWrapper.getList(users);
                            Log.i(TAG, "found some members, size:" + users.size());
                            Log.printList(TAG, wrappers);
                            Message msg = handler.obtainMessage(UPDATE_MEMBER);
                            msg.obj = wrappers;
                            msg.sendToTarget();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    final static int NO_MEMBER = 1;
    final static int UPDATE_MEMBER = 2;
    final static int NO_GROUP = 3;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NO_GROUP:
                    new MessageDialog.Builder(GroupMapActivity.this)
                            .setTitle("提示")
                            .setMessage("此团已经解散！")
                            .setPositiveButton("知道了", new BaseDialog.OnButtonClickListener() {
                                @Override
                                public void onClick(BaseDialog dialog, int witch) {
                                    dialog.dismiss();
                                    GroupMapActivity.this.finish();
                                }
                            })
                            .create()
                            .show();
                    break;
                case NO_MEMBER:

                    break;
                case UPDATE_MEMBER:
                    handleMapMarks((List<UserWrapper>) msg.obj);
                    break;
            }
        }


    };


    private void handleMapMarks(List<UserWrapper> userWrappers) {
        if (markers == null) {
            markers = new ArrayList<>(userWrappers.size());
            addMarks(userWrappers);
        } else if (markers.size() != userWrappers.size()) {
            for (Marker marker : markers) {
                marker.destroy();
            }
            markers = new ArrayList<>(userWrappers.size());
            addMarks(userWrappers);
        } else {
            updateMarks(userWrappers);
        }
    }

    private void updateMarks(List<UserWrapper> userWrappers) {
        for (int i = 0; i < userWrappers.size(); i++) {
            UserWrapper userWrapper = userWrappers.get(i);
            Marker marker = markers.get(i);

            marker.setTitle(userWrapper.getNickName());
            AVGeoPoint geoPoint = userWrapper.getLocation();
            marker.setPosition(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
            String snippet =  getSnippet(userWrapper);
            marker.setSnippet(snippet);
        }
    }

    private void addMarks(List<UserWrapper> userWrappers) {
        for (int i = 0; i < userWrappers.size(); i++) {
            UserWrapper userWrapper = userWrappers.get(i);

            MarkerOptions markerOptions = new MarkerOptions();

            AVGeoPoint geoPoint = userWrapper.getLocation();
            markerOptions.position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
            markerOptions.title(userWrapper.getNickName());
            String snippet = getSnippet(userWrapper);
            markerOptions.snippet(snippet);
            final Marker marker = aMap.addMarker(markerOptions);

            AVFile avFile = userWrapper.getAvatar();
            if(avFile!=null){
                avFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        if (marker != null && marker.isVisible()) {
                            Bitmap bitmap = Avatar.getThumbNailBitmap(bytes, UITool.dp2px(GroupMapActivity.this, 16), UITool.dp2px(GroupMapActivity.this, 16));
                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        }
                    }
                });
            }
            markers.add(marker);
        }
    }

    public static void getStartIntent(Context context, String groupId, String groupName) {
        Intent intent = new Intent(context, GroupMapActivity.class);
        intent.putExtra(Globe.GROUP_NAME, groupName);
        intent.putExtra(Globe.GROUP_ID, groupId);
        context.startActivity(intent);
    }


    private String getSnippet(UserWrapper userWrapper) {
        StringBuilder builder = new StringBuilder();
        builder.append("精度:");
        float accuracy = userWrapper.getAccuracy();
        if (accuracy < 0) {
            builder.append("未知\n");
        } else {
            builder.append(accuracy).append("m\n");
        }
        builder.append("手机:").append(userWrapper.getMobilePhoneNumber()).append("\n");
        builder.append(TimeTool.getBetterTime(userWrapper.getLastLocationUpdateTime()));

        return builder.toString();
    }

//    static class MemberAdapter extends ListAdapter<UserWrapper> {
//
//        public MemberAdapter(Context context, List<UserWrapper> data) {
//            super(context, data, R.layout.item_member, new int[]{R.id.text_nick_name, R.id.text_detail, R.id.btn_call});
//        }
//
//        @Override
//        protected void bindData(ViewHolder holder, UserWrapper item, int position) {
//            AVGeoPoint geoPoint = item.getLocation();
//            holder.getTextView(R.id.text_nick_name).setText(item.getNickName());
//            String detail = TimeTool.getBetterTime(item.getLastLocationUpdateTime()) + " 精度:";
//            float accuracy = item.getAccuracy();
//            if (accuracy < 0) {
//                detail += "未知";
//            } else {
//                detail += (int) accuracy + "m";
//            }
//            holder.getTextView(R.id.text_detail).setText(detail);
//        }
//    }

}
