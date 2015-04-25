package org.liangxw.travelfinder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.model.Globe;
import org.liangxw.travelfinder.util.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class GroupMapActivity extends BaseActivity {

    private final static String TAG = GroupMapActivity.class.getSimpleName();
    String groupId;

    @InjectView(R.id.map)
    MapView mapView;

    AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_map);
        super.onCreate(savedInstanceState);
        addAction(R.id.btn_group_qr_code, "码", null);
        ButterKnife.inject(this);

        groupId = getIntent().getStringExtra(Globe.GROUP_ID);
        String groupName = getIntent().getStringExtra(Globe.GROUP_NAME);
        setTitle(groupName);

        //初始化地图
        mapView.onCreate(savedInstanceState);
    }

    void initMap(){
        if(aMap==null){
            aMap = mapView.getMap();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
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

    public static void startActivity(Context context, String groupId, String groupName) {
        Intent intent = new Intent(context, GroupMapActivity.class);
        intent.putExtra(Globe.GROUP_NAME, groupName);
        intent.putExtra(Globe.GROUP_ID, groupId);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_group_qr_code)
    void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_group_qr_code:
//                startActivity(GroupQRCodeActivity.class);
                break;
        }
    }
}
