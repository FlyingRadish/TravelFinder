package org.liangxw.travelfinder.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.avos.avoscloud.AVGeoPoint;

import org.liangxw.travelfinder.component.Master;
import org.liangxw.travelfinder.model.GEOEvent;
import org.liangxw.travelfinder.model.Globe;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.util.SharedPreferencesTool;
import org.liangxw.travelfinder.util.logger.Log;

public class LocationUpdateService extends Service implements AMapLocationListener {

    public final static String TAG = LocationUpdateService.class.getSimpleName();

    public final static String ACTION_RUN = "liangxw.action.updateLocation.run";
    public final static String ACTION_STOP = "liangxw.action.updateLocation.stop";
    public final static String ACTION_MY_LOCATION_RUN = "liangxw.action.updateLocation.my.location.run";
    public final static String ACTION_MY_LOCATION_STOP = "liangxw.action.updateLocation.my.location.stop";


    public final static int PERIOD_LONG = 60 * 1000;
    public final static int PERIOD_SHORT = 5 * 1000;

    LocationManagerProxy locationManagerProxy;

    public LocationUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            return START_STICKY;
        }
        String action = intent.getAction();
        if (action == null) {
            removeListener();
            addListener(PERIOD_LONG);
            return START_STICKY;
        }
        boolean enableUpdate = SharedPreferencesTool.read(this, Globe.CONFIG_NAME, Globe.ENABLE_UPDATE_LOCATION, false);
        if (action.equals(ACTION_RUN)) {
            removeListener();
            addListener(PERIOD_LONG);
        }
        if (action.equals(ACTION_STOP)) {
            removeListener();
            stopSelf();
        }
        if (action.equals(ACTION_MY_LOCATION_RUN)) {
            removeListener();
            addListener(PERIOD_SHORT);
        }
        if (action.equals(ACTION_MY_LOCATION_STOP)) {
            if (enableUpdate) {
                removeListener();
                addListener(PERIOD_LONG);
            } else {
                removeListener();
                stopSelf();
            }
        }

        return START_STICKY;
    }

    private void addListener(int period) {
        if (locationManagerProxy == null) {
            locationManagerProxy = LocationManagerProxy.getInstance(this);
            locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
        }
    }

    private void removeListener() {
        if (locationManagerProxy != null) {
            locationManagerProxy.removeUpdates(this);
            locationManagerProxy.destroy();
            locationManagerProxy = null;
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            //获取位置信息
            Double geoLat = aMapLocation.getLatitude(); //维度
            Double geoLng = aMapLocation.getLongitude();    //经度
            float accuracy = aMapLocation.getAccuracy();
            Log.i(TAG, "post");
            Master.getEventBus().post(new GEOEvent(aMapLocation));
            boolean enableUpdate = SharedPreferencesTool.read(this, Globe.CONFIG_NAME, Globe.ENABLE_UPDATE_LOCATION, false);
            if (enableUpdate) {
                updateGEO(geoLat, geoLng, accuracy);
            }
            Log.i(TAG, aMapLocation.getProvider() + ", " + "Lat:" + geoLat + ", " + "Lng:" + geoLng + ", accuracy:" + aMapLocation.getAccuracy());
        }
    }

    private void updateGEO(Double geoLat, Double geoLng, float accuracy) {
        AVGeoPoint geoPoint = new AVGeoPoint(geoLat, geoLng);
        UserWrapper userWrapper = UserWrapper.getCurrentUser();
        userWrapper.setLocation(geoPoint);
        userWrapper.setAccuracy(accuracy);
        userWrapper.setLocationUpdateState(true);
        userWrapper.setLocationUpdateTime(System.currentTimeMillis());
        userWrapper.saveInBackground();
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
}
