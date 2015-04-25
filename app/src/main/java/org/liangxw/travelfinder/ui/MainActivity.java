package org.liangxw.travelfinder.ui;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.model.Group;
import org.liangxw.travelfinder.model.UGMap;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.ui.guide.CreateGroupActivity;
import org.liangxw.travelfinder.ui.visitor.AddGroupActivity;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.adapter.ListAdapter;
import org.liangxw.travelfinder.util.logger.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements View.OnClickListener, ActivityStack.ActivityRule, AMapLocationListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    boolean needLogin = false;
    UserWrapper userWrapper;

    GroupAdapter adapter;

    @InjectView(R.id.list_group)
    ListView listGroup;

    @InjectView(R.id.text_no_group)
    TextView textNoGroup;
    @InjectView(R.id.text_report_my_location)
    TextView textReport;

    LocationManagerProxy locationManagerProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setLeftActionText("我");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "start");
        if (!isLogin()) {
            needLogin = true;
            Log.i(TAG, "need login");
            finish();
            return;
        }
        if (userWrapper.getType() == UserWrapper.TYPE_GUIDE) {
            addAction(R.id.btn_create_group, "建", this);
        } else if (userWrapper.getType() == UserWrapper.TYPE_VISITOR) {
            addAction(R.id.btn_add_group, "加", this);
        }

        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGroupInfo();
    }

    private void updateGroupInfo() {
        AVQuery<UGMap> avQuery = new AVQuery<>(UGMap.CLASS_NAME);
        avQuery.whereEqualTo(UGMap.USER_ID, userWrapper.getObjectId());
        avQuery.findInBackground(new FindCallback<UGMap>() {
            @Override
            public void done(List<UGMap> ugMaps, AVException e) {
                if (e == null) {
                    if (ugMaps.size() == 0) {
                        Log.i(TAG, "no user-group map found");
                        handler.obtainMessage(NO_GROUP).sendToTarget();
                    } else {
                        Log.i(TAG, "found some user-group map, size:" + ugMaps.size());
                        Log.printList(TAG, ugMaps);
                        getAllGroups(ugMaps);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAllGroups(List<UGMap> ugMaps) {

        final List<String> groupIds = new ArrayList<>(ugMaps.size());

        for (UGMap ugMap : ugMaps) {
            groupIds.add(ugMap.getGroupId());
        }

        AVQuery<Group> query = new AVQuery<>(Group.CLASS_NAME);
        query.whereContainedIn(Group.OBJECT_ID, groupIds);
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> groups, AVException e) {
                if (e == null) {
                    if (groups.size() == 0) {
                        Log.i(TAG, "no group found???");
                        handler.obtainMessage(NO_GROUP).sendToTarget();
                    } else {
                        Log.i(TAG, "found some group, size:" + groups.size());
                        Log.printList(TAG, groups);
                        Message msg = handler.obtainMessage(UPDATE_GROUP);
                        msg.obj = groups;
                        msg.sendToTarget();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    final static int NO_GROUP = 1;
    final static int UPDATE_GROUP = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NO_GROUP:
                    listGroup.setVisibility(View.INVISIBLE);
                    textNoGroup.setVisibility(View.VISIBLE);
                    break;
                case UPDATE_GROUP:
                    listGroup.setVisibility(View.VISIBLE);
                    textNoGroup.setVisibility(View.INVISIBLE);
                    adapter = new GroupAdapter(MainActivity.this, (List<Group>) msg.obj);
                    listGroup.setAdapter(adapter);
                    listGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Group group = (Group) parent.getItemAtPosition(position);
                            GroupMapActivity.startActivity(MainActivity.this, group.getObjectId(), group.getName());
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            //获取位置信息
            Double geoLat = aMapLocation.getLatitude(); //维度
            Double geoLng = aMapLocation.getLongitude();    //经度

            AVGeoPoint geoPoint = new AVGeoPoint(geoLat, geoLng);
            userWrapper.setLocation(geoPoint);
            userWrapper.setLocationUpdateState(true);
            userWrapper.setLocationUpdateTime(System.currentTimeMillis());
            userWrapper.saveInBackground();


            StringBuilder builder = new StringBuilder();
            builder.append(aMapLocation.getProvider()).append(", ");
            builder.append("Lat:").append(geoLat).append(", ");
            builder.append("Lng:").append(geoLng);

            textReport.setText(builder.toString());
            Log.i(TAG, builder.toString());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    static class GroupAdapter extends ListAdapter<Group> {

        public GroupAdapter(Context context, List<Group> data) {
            super(context, data, R.layout.item_group, new int[]{R.id.text_group_name});
        }

        @Override
        protected void bindData(ViewHolder holder, Group item, int position) {
            holder.getTextView(R.id.text_group_name).setText(item.getName());
            holder.getTextView(R.id.text_group_name).setTag(item);
        }
    }


    private boolean isLogin() {
        userWrapper = UserWrapper.getCurrentUser();
        Log.e(TAG, "now user:" + userWrapper);
        if (userWrapper == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_group:
                startActivity(CreateGroupActivity.class);
                break;
            case R.id.btn_add_group:
                startActivity(AddGroupActivity.class);
                break;
        }
    }


    @Override
    protected ActivityStack.ActivityRule getActivityRule() {
        Log.i(TAG, "go here");
        return this;
    }

    @Override
    protected void onRightButtonClicked() {
        toast("haha");
        if (locationManagerProxy == null) {
            locationManagerProxy = LocationManagerProxy.getInstance(this);
        }
        locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 2 * 1000, 20, this);
    }

    @Override
    public boolean onPop(Activity prev, Activity now) {
        Log.i(TAG, "onPop");
        if (needLogin && !(prev instanceof LoginActivity)) {
            Log.i(TAG, "go login");
            startActivity(LoginActivity.class);
        }
        return true;
    }
}
