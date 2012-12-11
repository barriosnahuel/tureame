/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

package org.nbempire.android.tourguide.component.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import org.nbempire.android.tourguide.R;

/**
 * Land-Activity of this application. It contains the main app screen where users can see the map with all its layers.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class MapActivity extends FragmentActivity {

    /**
     * Time (in milliseconds) to wait between location updates.
     */
    private static final long MIN_TIME_FOR_LOCATION_UPDATES = 15000;

    /**
     * Minimum distance (in meters) to request location updates.
     */
    private static final long MIN_DISTANCE_FOR_LOCATION_UPDATES = 0;

    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap mMap;

    /**
     * Tag for class' log.
     */
    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //  TODO : Performance : Should I leave only this call to the method? Review "Activities lifecycle" topic.
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly installed) and the map has not already been
     * instantiated.. This will ensure that we only ever call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user
     * to install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this Activity after following the prompt and correctly installing/updating/enabling the Google Play services. Since
     * the Activity may not have been completely destroyed during this process (it is likely that it would only be stopped or paused), {@link
     * #onCreate(Bundle)} may not be called again so we should call this method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                           .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            } else {
                //  TODO : Functionality : Close app when the user doesn't install/upgrade Google Play Services and display an error message.
                Log.i(TAG, "The application will be closed because of the device is unable to run without the Google Play Services API.");
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                Log.i(TAG, "Showing last known location on map...");
                updateLocationOnMap(lastKnownLocation);
            } else {
                Log.i(TAG, "There isn't any last known location to display.");
                //  TODO : Functionality : Display a warn to the user saying that he will have to wait a moment.
            }

        } else {
            //  TODO : Functionality : Show an error to the user.
            Log.e(TAG, "The retrieved locationManager is null.");
        }

        displayCurrentLocation(locationManager);

        //  TODO : Functionality : show layers.
        //addWikipediaLayer();
    }

    /**
     * This returns moon tiles.
     */
    private static final String MOON_MAP_URL_FORMAT = "http://mw1.google.com/mw-planetary/lunar/lunarmaps_v1/clem_bw/%d/%d/%d.jpg";

    /**
     * TODO : Javadoc for addWikipediaLayer
     */
    private void addWikipediaLayer() {

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {

                // The moon tile coordinate system is reversed.  This is not normal.
                int reversedY = (1 << zoom) - y - 1;

                String formattedUrl = String.format(Locale.US, MOON_MAP_URL_FORMAT, zoom, x, reversedY);

                URL url;
                try {
                    url = new URL(formattedUrl);
                } catch (MalformedURLException malformedUrlException) {
                    throw new AssertionError(malformedUrlException);
                }

                return url;
            }
        };

        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
    }

    /**
     * Gets the current location from {@link #mMap} enabling MyLocation layer when needed. If there's no location data available then shows an
     * error message to the user.
     *
     * @param locationManager
     *         The {@link LocationManager} used to retrieve the information.
     */
    private void displayCurrentLocation(LocationManager locationManager) {
        LocationListener locationListener = createLocationListener(locationManager);

        // Register the listener with the Location Manager to receive location updates
        List<String> providers = new ArrayList<String>();
        addLocationProviderIfEnabled(providers, locationManager, LocationManager.GPS_PROVIDER);
        addLocationProviderIfEnabled(providers, locationManager, LocationManager.NETWORK_PROVIDER);

        if (providers.isEmpty()) {
            //  TODO : Functionality : display an warning to the user and try to activate with his confirmation.
            Log.w(TAG, "There isn't any enabled provider to retrieve current location.");
        } else {
            for (String eachProvider : providers) {
                Log.i(TAG, "Request location updates for provider: " + eachProvider);
                locationManager.requestLocationUpdates(eachProvider, MIN_TIME_FOR_LOCATION_UPDATES, MIN_DISTANCE_FOR_LOCATION_UPDATES, locationListener);
            }
        }
    }

    /**
     * Add the specified {@code provider} into {@code locationProviders} when the {@code locationManager} says that the {@code provider} is
     * enabled.
     *
     * @param locationProviders
     *         List of enabled providers.
     * @param locationManager
     *         The {@link LocationManager} to retrieve information about enabled providers.
     * @param provider
     *         The provider to add.
     */
    private void addLocationProviderIfEnabled(List<String> locationProviders, LocationManager locationManager, String provider) {
        if (locationManager.isProviderEnabled(provider)) {
            locationProviders.add(provider);
        }
    }

    /**
     * TODO : Javadoc for createLocationListener
     *
     * @param locationManager
     *
     * @return
     */
    private LocationListener createLocationListener(final LocationManager locationManager) {
        // Define a listener that responds to location updates
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location == null) {
                    Log.e(TAG, "location is null.");
                } else {
                    Log.i(TAG, "Current location is: (" + location.getLatitude() + ", " + location.getLongitude() + ")");

                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    updateLocationOnMap(currentLocation);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //  TODO : Functionality : do something onStatusChanged for a provider.
                Log.i(TAG, "status changed for provider: " + provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i(TAG, "Enabled provider: " + provider);
                locationManager.requestLocationUpdates(provider, MIN_TIME_FOR_LOCATION_UPDATES, MIN_DISTANCE_FOR_LOCATION_UPDATES, this);
            }

            @Override
            public void onProviderDisabled(String provider) {
                //  TODO : Functionality : do something onProviderDisabled
                Log.w(TAG, "Disabled provider: " + provider);

                //  TODO : Functionality : Check if both providers are disabled, then warn to the user that te application will not work and
                // let him choose which provider set as enabled just selecting one.
            }
        };
    }

    /**
     * Update the displayed location on the map with the specified one.
     *
     * @param location
     *         The location to show.
     */
    private void updateLocationOnMap(LatLng location) {
        CameraPosition position =
                new CameraPosition.Builder().target(location)
                        .zoom(mMap.getMaxZoomLevel() - 2)
                        .bearing(0)
                        .tilt((float) 67.5)
                        .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),
                                  new GoogleMap.CancelableCallback() {
                                      @Override
                                      public void onFinish() {
                                          //  TODO : Implementation of .onFinish() method.
                                      }

                                      @Override
                                      public void onCancel() {
                                          //  TODO : Implementation of .onCancel() method.
                                      }
                                  });
    }

    /**
     * Update the displayed location on the map with the specified one.
     *
     * @param location
     *         The location to show.
     */
    private void updateLocationOnMap(Location location) {
        updateLocationOnMap(new LatLng(location.getLatitude(), location.getLongitude()));
    }

}
