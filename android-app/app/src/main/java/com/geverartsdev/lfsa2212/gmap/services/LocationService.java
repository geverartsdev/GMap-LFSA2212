package com.geverartsdev.lfsa2212.gmap.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class LocationService {

    private static final String TAG = "LocationUpdate";

    private static final int MY_PERMISSION_REQUEST_CODE = 7192;

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private static final int MIN_UPDATE_TIME = 500;

    private static Location currentBestLocation = null;

    LocationManager locationManagerGPS;
    LocationManager locationManagerNetwork;
    LocationListener locationListener;

    Activity activity;

    public LocationService(Activity activity) {
        this.activity = activity;
        this.setUpLocationUpdate();
    }

    public Location getCurrentBestLocation() {
        return currentBestLocation;
    }

    public boolean start() {
        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissions needed to get location");
            return false;
        }
        Log.d(TAG, "Updates started");
        locationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, 0, locationListener);
        locationManagerGPS.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_UPDATE_TIME, 0, locationListener);
        return true;
    }

    public void stop() {
        Log.d(TAG, "Service destroyed");

        currentBestLocation = null;

        if(locationManagerGPS != null)
            locationManagerGPS.removeUpdates(locationListener);
        if(locationManagerNetwork != null)
            locationManagerNetwork.removeUpdates(locationListener);
    }

    private void setUpLocationUpdate() {

        locationManagerGPS = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationManagerNetwork = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                if(currentBestLocation == null)
                    currentBestLocation = location;
                else if (isBetterLocation(location, currentBestLocation))
                    currentBestLocation = location;
                else return;

                if (location == null) return;
                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();

                Log.d(TAG, String.format("Your location was changed : %f / %f", latitude, longitude));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        ActivityCompat.requestPermissions(this.activity, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, MY_PERMISSION_REQUEST_CODE);
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}