package com.geverartsdev.lfsa2212.gmap.objects;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;

public class Pothole {

    private float intensity;
    private Long time;
    private Location location;

    Pothole(float intensity, Long time, Location location) {
        this.intensity = intensity;
        this.time = time;
        this.location = location;
    }

    public Pothole(float intensity, Location location) {
        this(intensity, System.currentTimeMillis(), location);
    }

    public void updateIntensityIfGreater(float intensity) {
        if (intensity > this.intensity) this.intensity = intensity;
    }

    public float getIntensity() {
        return intensity;
    }

    public Long getTime() {
        return time;
    }

    public String getLocationString() {
        if (location == null) return "Unknown";
        return location.getLatitude() + ", " + location.getLongitude();
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", new Date(this.time).toString());
        hashMap.put("intensity", this.intensity);
        if (location != null)
            hashMap.put("location", new GeoPoint(location.getLatitude(), location.getLongitude()));

        return hashMap;
    }
}
