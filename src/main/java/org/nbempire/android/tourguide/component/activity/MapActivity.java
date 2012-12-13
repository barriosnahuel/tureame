/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

package org.nbempire.android.tourguide.component.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.nbempire.android.tourguide.R;
import org.nbempire.android.tourguide.dao.impl.PlaceDaoImplSpring;
import org.nbempire.android.tourguide.domain.Place;
import org.nbempire.android.tourguide.service.PlaceService;
import org.nbempire.android.tourguide.service.impl.PlaceServiceImpl;

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
     * Request code used to handle startActivityForResult to let user enable location providers from Settings.Secure intent.
     */
    private static final int REQUEST_CODE_ENABLE_LOCATION_PROVIDERS = 1;

    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap mMap;

    /**
     * Tag for class' log.
     */
    private static final String TAG = "MapActivity";

    /**
     * {@link AlertDialog} used to let user decide between enable or not his location providers like GPS or wireless network.
     */
    private AlertDialog noEnabledProvidersDialog;

    /**
     * Service for the {@link Place} entity.
     */
    private PlaceService placeService = new PlaceServiceImpl(new PlaceDaoImplSpring());

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
                closeApp("The application will be closed because of the device is unable to run without the Google Play Services API.");
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        subscribeToLocationUpdates(locationManager);

        displayLastKnownLocation(locationManager);

        //  TODO : Functionality : show Wikipedia markers and other useful information.
    }

    /**
     * Gets the current location from {@link #mMap} enabling MyLocation layer when needed. If there's no location data available then shows an
     * error message to the user.
     *
     * @param locationManager
     *         The {@link LocationManager} used to retrieve the information.
     */
    private boolean subscribeToLocationUpdates(LocationManager locationManager) {
        LocationListener locationListener = createLocationListener(locationManager);

        List<String> providers = new ArrayList<String>();

        boolean subscribed = true;

        if (noLocationProvidersEnabled(locationManager, providers)) {
            Log.w(TAG, "There isn't any enabled provider to retrieve current location.");
            subscribed = false;
            buildAlertMessageNoGps(R.string.msg_gps_is_disabled_do_you_want_to_enable_it, R.string.yes, R.string.no);
        } else {
            for (String eachProvider : providers) {
                Log.i(TAG, "Request location updates for provider: " + eachProvider);

                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(eachProvider, MIN_TIME_FOR_LOCATION_UPDATES, MIN_DISTANCE_FOR_LOCATION_UPDATES, locationListener);
            }
        }

        return subscribed;
    }

    /**
     * Display last known location if exists in any of GPS or network providers.
     *
     * @param locationManager
     *         The location manager to use for retrieve location information.
     */
    private void displayLastKnownLocation(LocationManager locationManager) {
        Location lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnownLocationByNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location lastKnownLocation = null;
        if (lastKnownLocationByGps != null && lastKnownLocationByNetwork != null) {

            if (lastKnownLocationByGps.getTime() > lastKnownLocationByNetwork.getTime()) {
                lastKnownLocation = lastKnownLocationByGps;
            } else {
                lastKnownLocation = lastKnownLocationByNetwork;
            }

        } else if (lastKnownLocationByGps != null) {
            lastKnownLocation = lastKnownLocationByGps;

        } else {
            lastKnownLocation = lastKnownLocationByNetwork;
        }

        Toast.makeText(this, R.string.msg_we_re_finding_you_dont_hide_and_wait_a_moment_please, Toast.LENGTH_LONG).show();
        if (lastKnownLocation != null) {
            Log.i(TAG, "Showing last known location on map...");
            updateLocationOnMap(lastKnownLocation);
        } else {
            Log.i(TAG, "There isn't any last known location to display.");
        }
    }

    /**
     * Check for enabled location providers and add those ones to {@code providers} list.
     *
     * @param locationManager
     *         The {@link LocationManager} used to retrieve location providers information.
     * @param providers
     *         A list of location providers to update.
     *
     * @return {@code true} when there isn't any location provider enabled. {@code false} when at least one location provider is enabled.
     */
    private boolean noLocationProvidersEnabled(LocationManager locationManager, List<String> providers) {
        // Register the listener with the Location Manager to receive location updates
        addLocationProviderIfEnabled(providers, locationManager, LocationManager.GPS_PROVIDER);
        addLocationProviderIfEnabled(providers, locationManager, LocationManager.NETWORK_PROVIDER);

        return providers.isEmpty();
    }

    /**
     * It does the same as {@link #noLocationProvidersEnabled(android.location.LocationManager, java.util.List)} but without modifying the {@code
     * providers} list.
     *
     * @param locationManager
     *         The {@link LocationManager} used to retrieve location providers information.
     *
     * @return {@code true} when there isn't any location provider enabled. {@code false} when at least one location provider is enabled.
     */
    private boolean noLocationProvidersEnabled(LocationManager locationManager) {
        return noLocationProvidersEnabled(locationManager, new ArrayList<String>());
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
     * Creates an {AlertDialog} to take user to his location settings to let him enable any location provider.
     *
     * @param alertMessage
     *         The resource ID for the message to show in the {@link AlertDialog}.
     * @param positiveButtonLabel
     *         The resource ID for the positive button label.
     * @param negativeButtonLabel
     *         The resource ID for the negative button label.
     */
    private void buildAlertMessageNoGps(int alertMessage, int positiveButtonLabel, final int negativeButtonLabel) {
        noEnabledProvidersDialog = new AlertDialog.Builder(this)
                                           .setMessage(alertMessage)
                                           .setCancelable(false)
                                           .setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_ENABLE_LOCATION_PROVIDERS);
                                               }
                                           })
                                           .setNegativeButton(negativeButtonLabel, new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {

                                                   dialog.cancel();
                                                   if (negativeButtonLabel != R.string.close_app) {
                                                       buildAlertMessageNoGps(R.string.msg_location_providers_are_required, R.string.enable,
                                                                                     R.string.close_app);
                                                   } else {
                                                       closeApp("The application will be closed because it's unable to run without any " +
                                                                        "location provider enabled.");
                                                   }
                                               }
                                           }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean subscribed = subscribeToLocationUpdates(locationManager);
        if (subscribed) {
            noEnabledProvidersDialog.cancel();
        }
    }

    /**
     * Creates a {@link LocationListener} which will be the one that request for location updates and then handle maps updates based on that
     * location.
     *
     * @param locationManager
     *         The location manager to use.
     *
     * @return A listener ready to use.
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

                    //  TODO : Functionality : Don't update places if they already exist.
                    findPlacesNearBy(currentLocation);

                    updateLocationOnMap(currentLocation);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i(TAG, "status changed for provider: " + provider);
                //  TODO : Functionality : do something onStatusChanged for a provider.
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i(TAG, "Enabled provider: " + provider);
                locationManager.requestLocationUpdates(provider, MIN_TIME_FOR_LOCATION_UPDATES, MIN_DISTANCE_FOR_LOCATION_UPDATES, this);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.w(TAG, "Disabled provider: " + provider);

                if (noLocationProvidersEnabled(locationManager)) {
                    Log.w(TAG, "There isn't any enabled provider to retrieve current location.");
                    buildAlertMessageNoGps(R.string.msg_gps_is_disabled_do_you_want_to_enable_it, R.string.yes, R.string.no);
                }
            }
        };
    }

    /**
     * Find all places near the specified {@code currentLocation}.
     *
     * @param currentLocation
     *         {@link LatLng} containing the current latitud-longitude pair.
     */
    private void findPlacesNearBy(LatLng currentLocation) {
        List<Place> places = placeService.findAllNearBy(currentLocation.latitude, currentLocation.longitude);
        Log.d(TAG, "Found: " + places.size() + " places.");

        //  TODO : Functionality : Do something when no places are found.
        for (Place eachPlace : places) {
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(eachPlace.getLatitude(),
                                                                                         eachPlace.getLongitude())).title(eachPlace.getTitle());
            //  TODO : Functionality : Check about duplicating the marker onLocationChanged
            mMap.addMarker(markerOptions);

            Log.d(TAG, eachPlace.toString());
        }
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

    /**
     * Finish this activity. That means that this method must be used to close the application.
     *
     * @param logMessage
     *         A message to log why we're closing the application.
     */
    private void closeApp(String logMessage) {
        Log.i(TAG, logMessage);
        finish();
    }

}
