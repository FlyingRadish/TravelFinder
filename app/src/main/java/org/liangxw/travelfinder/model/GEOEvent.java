package org.liangxw.travelfinder.model;

import com.amap.api.location.AMapLocation;

/**
 * Created by houxg on 2015/4/25.
 */
public class GEOEvent {

    AMapLocation location;

    public GEOEvent(AMapLocation location) {
        this.location = location;
    }

    public AMapLocation getLocation() {
        return location;
    }
}
