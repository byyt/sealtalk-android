package cn.yunchuang.im.location;

import java.io.Serializable;

/**
 * Created by zhoumingjun on 3/22/14.
 */
public class LocationVO implements Serializable {

    private double latitude;
    private double longitude;
    private long lastUpdate;
    private String city;
    private int source;

    public LocationVO() {
    }

    public LocationVO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String toString() {
        return "longitude:" + longitude + ",latitude:" + latitude;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

}
