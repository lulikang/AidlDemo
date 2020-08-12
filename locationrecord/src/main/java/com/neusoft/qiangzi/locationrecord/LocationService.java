package com.neusoft.qiangzi.locationrecord;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class LocationService extends Service {
    private static final String TAG = "LocationService";
    LocationManager locaManager = null;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ILocationBinder.Stub() {
            @Override
            public Location getLocation() {
                if (locaManager != null) {
                    if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    return locaManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                return null;
            }
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: is called");

        setLocationManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: is called");
    }

    private void setLocationManager() {
        if (locaManager == null) {
            locaManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            locaManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0.1f,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.d(TAG, "onLocationChanged: Longtitude="
                                    + location.getLongitude() + ",Latitude=" + location.getLatitude());
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {
                            Log.d(TAG, "onProviderEnabled: provider=" + s);
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                            Log.d(TAG, "onProviderDisabled: provider=" + s);
                        }
                    });
        }
    }
}
