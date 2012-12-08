/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/8/12 at 3:42 PMhs.
 */
package org.nbempire.android.tourguide;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Nahuel Barrios.
 * @since 1
 */
public class MyLocationListener implements LocationListener {

    /**
     * Tag for class' log.
     */
    private static final String TAG = "MyLocationListener";

    /**
     * The map to control
     */
    private GoogleMap map;
    private Context context;

    /**
     * TODO : Javadoc for MyLocationListener
     *
     * @param aContext
     * @param aMap
     */
    public MyLocationListener(Context aContext, GoogleMap aMap) {
        this.context = aContext;
        this.map = aMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Log.e(TAG, "location is null.");
        } else {
            Log.i(TAG, "Current location is: (" + location.getLatitude() + ", " + location.getLongitude() + ")");

            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition position =
                    new CameraPosition.Builder().target(currentLocation)
                            .zoom(map.getMaxZoomLevel() - 2)
                            .bearing(0)
                            .tilt((float) 67.5)
                            .build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(position),
                                     new GoogleMap.CancelableCallback() {
                                         @Override
                                         public void onFinish() {
                                             Toast.makeText(context, "Animation complete", Toast.LENGTH_SHORT).show();
                                         }

                                         @Override
                                         public void onCancel() {
                                             Toast.makeText(context, "Animation canceled", Toast.LENGTH_SHORT).show();
                                         }
                                     });
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //  TODO : Implementation of org.nbempire.android.tourguide.MyLocationListener.onStatusChanged() method.
        Log.e(TAG, "status changed for provider: " + s);
    }

    @Override
    public void onProviderEnabled(String s) {
        //  TODO : Implementation of org.nbempire.android.tourguide.MyLocationListener.onProviderEnabled() method.
        Log.e(TAG, "provider: " + s + " is enabled.");
    }

    @Override
    public void onProviderDisabled(String s) {
        //  TODO : Implementation of org.nbempire.android.tourguide.MyLocationListener.onProviderDisabled() method.
        Log.e(TAG, "provider: " + s + " is disabled.");
    }
}
