/*
 * Copyright (c) 2012-2013 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

package org.nbempire.android.tourguide.component.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.nbempire.android.tourguide.R;
import org.nbempire.android.tourguide.dao.impl.PlaceDaoImpl;
import org.nbempire.android.tourguide.dao.impl.WikipediaDaoImplSpring;
import org.nbempire.android.tourguide.domain.Place;
import org.nbempire.android.tourguide.service.PlaceService;
import org.nbempire.android.tourguide.service.WikipediaService;
import org.nbempire.android.tourguide.service.impl.PlaceServiceImpl;
import org.nbempire.android.tourguide.service.impl.WikipediaServiceImpl;

/**
 * Land-Activity of this application. It contains the main app screen where users can see the map with all its layers.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class MapActivity extends FragmentActivity {

    /**
     * Time (in milliseconds) to wait between location updates. 2 minutes.
     * <p/>
     * Note: Use 10000 for testing.
     */
    private static final long MIN_TIME_FOR_LOCATION_UPDATES = 120000;

    /**
     * Minimum distance (in meters) to request location updates.
     * <p/>
     * Note: Use 0 for testing.
     */
    private static final long MIN_DISTANCE_FOR_LOCATION_UPDATES = 50;

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
    private PlaceService placeService;

    /**
     * Service for Wikipedia domain objects.
     */
    private WikipediaService wikipediaService;

    /**
     * The location manager used to retrieve location updates.
     */
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //  TODO : Functionality : Add Google Analytics tracker.

        wikipediaService = new WikipediaServiceImpl(new WikipediaDaoImplSpring());
        placeService = new PlaceServiceImpl(new PlaceDaoImpl(wikipediaService));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setUpMapIfNeeded();
        displayLastKnownLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //  TODO : Performance : Should I leave only this call to the method? Review "Activities lifecycle" topic.
        setUpMapIfNeeded();
        Log.i(TAG, "Subscribed to location updates? " + subscribeToLocationUpdates());
        //  TODO : Functionality : Display something when Google Play Services is not available on current device.
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
        if (mMap != null) {
            return;
        }

        // Try to obtain the map from the SupportMapFragment.
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(
            new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    // Check if we were successful in obtaining the map.
                    if (googleMap != null) {
                        mMap = googleMap;
                        setUpMap();
                    } else {
                        int isEnabled = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapActivity.this);
                        if (isEnabled != ConnectionResult.SUCCESS) {
                            //  TODO : Functionality : Add onCancelListener after cancel Google Play Services AlertDialog.
                            //closeApp("The application will be closed because of the device is unable to run without the Google Play Services API.");
                            GooglePlayServicesUtil.getErrorDialog(isEnabled, MapActivity.this, 0);
                        }
                    }
                }
            }
        );
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    private void setUpMap() {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i(TAG, "User will now navigate to Wikipedia's page: " + marker.getTitle());
                startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaService.getPageUrl(marker.getTitle()))));
            }
        });

        //  TODO : Functionality : Show "atractions" from Google Places on map.

        //  TODO : Functionality : Show events on map.
    }

    /**
     * Subscribe the {@link #locationManager} to location updates. If there's no location provider enabled then shows an error message to the
     * user to let him enable providers from the system location settings.
     *
     * @return {@code true} when the {@link #locationManager} is subscribed to location updates. Otherwise {@code false}.
     */
    private boolean subscribeToLocationUpdates() {
        Set<String> providers = new HashSet<String>();

        boolean subscribed = true;

        if (noLocationProvidersEnabled(locationManager, providers)) {
            Log.w(TAG, "There isn't any enabled provider to retrieve current location.");
            subscribed = false;
            buildAlertMessageNoGps();
        } else {
            Toast.makeText(this, R.string.msg_waiting_for_location, Toast.LENGTH_LONG).show();
            for (String eachProvider : providers) {
                Log.i(TAG, "Request location updates for provider: " + eachProvider);

                // Register the listener with the Location Manager to receive location updates
                //  TODO : Performance : Check about requesting location updates only once. Read Javadoc.
                locationManager.requestLocationUpdates(eachProvider, MIN_TIME_FOR_LOCATION_UPDATES,
                    MIN_DISTANCE_FOR_LOCATION_UPDATES, locationListener);
            }
        }

        return subscribed;
    }

    /**
     * Display last known location if exists in any of GPS or network providers.
     */
    private void displayLastKnownLocation() {
        Location lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnownLocationByNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location lastKnownLocation;
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
     * @param locationManager The {@link android.location.LocationManager} used to retrieve location providers information.
     * @param providers A set of location providers to update.
     * @return {@code true} when there isn't any location provider enabled. {@code false} when at least one location provider is enabled.
     */
    private boolean noLocationProvidersEnabled(LocationManager locationManager, Set<String> providers) {
        // Register the listener with the Location Manager to receive location updates
        addLocationProviderIfEnabled(providers, LocationManager.GPS_PROVIDER);
        addLocationProviderIfEnabled(providers, LocationManager.NETWORK_PROVIDER);

        return providers.isEmpty();
    }

    /**
     * It does the same as {@link #noLocationProvidersEnabled(android.location.LocationManager, java.util.Set)} but without modifying the {@code
     * providers} list.
     *
     * @param locationManager The {@link LocationManager} used to retrieve location providers information.
     * @return {@code true} when there isn't any location provider enabled. {@code false} when at least one location provider is enabled.
     */
    private boolean noLocationProvidersEnabled(LocationManager locationManager) {
        return noLocationProvidersEnabled(locationManager, new HashSet<String>());
    }

    /**
     * Add the specified {@code provider} into {@code locationProviders} when the {@code locationManager} says that the {@code provider} is
     * enabled.
     *
     * @param locationProviders Set of enabled providers.
     * @param provider The provider to add.
     */
    private void addLocationProviderIfEnabled(Set<String> locationProviders, String provider) {
        if (locationManager.isProviderEnabled(provider)) {
            locationProviders.add(provider);
        }
    }

    /**
     * Creates an {AlertDialog} to take user to his location settings to let him enable any location provider.
     */
    private void buildAlertMessageNoGps() {
        noEnabledProvidersDialog = new AlertDialog.Builder(this)
            .setMessage(R.string.msg_my_location_providers_disabled)
            .setCancelable(false)
            .setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        REQUEST_CODE_ENABLE_LOCATION_PROVIDERS);
                }
            })
            .setNegativeButton(R.string.no_close_app, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    closeApp("The application will be closed because it's unable to run without any " +
                        "location provider enabled.");
                }
            }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean subscribed = subscribeToLocationUpdates();
        if (subscribed) {
            noEnabledProvidersDialog.cancel();
        }
    }

    /**
     * Find all places near the specified {@code currentLocation} and add it to the current map.
     *
     * @param currentLocation {@link LatLng} containing the current latitud-longitude pair.
     */
    private void addMarkersOnPlacesNearBy(Location currentLocation) {
        List<Place> places = placeService.findAllNearBy(currentLocation.getLatitude(), currentLocation.getLongitude());
        Log.d(TAG, "Found: " + places.size() + " places.");

        if (places.isEmpty()) {
            String message = String.format(String.valueOf(getText(R.string.msg_there_isnt_places_to_display)),
                wikipediaService.getSearchRadiusInKm());
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            mMap.clear();

            for (Place eachPlace : places) {
                MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(eachPlace.getLatitude(), eachPlace.getLongitude()))
                    .title(eachPlace.getTitle())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_wikipedia));

                //  TODO : Functionality : Check about duplicating the marker onLocationChanged
                mMap.addMarker(markerOptions);

                Log.d(TAG, eachPlace.toString());
            }
        }
    }

    /**
     * Update the displayed location on the map with the specified one by centering the map on that location.
     *
     * @param location The location to show.
     */
    private void updateLocationOnMap(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //  TODO : Functionality : Before updating cameraPosition on map, if user has changed the zoom or tilt, maintein it without changing it!
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng)
            .zoom(mMap.getMaxZoomLevel() - 6)
            .bearing(0)
            .tilt((float) 67.5)
            .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
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
     * Finish this activity. That means that this method must be used to close the application.
     *
     * @param logMessage A message to log why we're closing the application.
     */
    private void closeApp(String logMessage) {
        Log.i(TAG, logMessage);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, LegalNoticesActivity.class));
        return true;
    }

    /**
     * The {@link LocationListener} which will be the one that request for location updates and then handle maps updates based on that location.
     */
    private LocationListener locationListener = new LocationListener() {

        /**
         * Error bound to consider when validating the distance (in meters) between two different locations.
         * <p/>
         * Note: Use 0 for testing.
         */
        public static final float MIN_DISTANCE_ERROR_BOUND = 20;

        /**
         * The current location. Should be the visible location on map.
         */
        private Location currentLocation;

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                Log.e(TAG, "location is null.");
            } else {
                Log.i(TAG, "Current location is: (" + location.getLatitude() + ", " + location.getLongitude() + ")");

                if (locationHasChanged(location)) {
                    currentLocation = location;

                    addMarkersOnPlacesNearBy(currentLocation);

                    updateLocationOnMap(currentLocation);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "status changed for provider: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "Enabled provider: " + provider);
            locationManager
                .requestLocationUpdates(provider, MIN_TIME_FOR_LOCATION_UPDATES, MIN_DISTANCE_FOR_LOCATION_UPDATES,
                    this);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "Disabled provider: " + provider);

            if (noLocationProvidersEnabled(locationManager)) {
                Log.w(TAG, "There isn't any enabled provider to retrieve current location.");
            }
        }

        /**
         * Validates when the specified location has an important difference with the last known location. That important difference will be the
         * value of the {@link #MIN_DISTANCE_ERROR_BOUND} constant.
         *
         * @param updatedLocation
         *         The updated location.
         *
         * @return {@code true} when the {@link #currentLocation} is null or the distance (in meters) between the las known location and the
         *         specified {@code updatedLocation} is greater than the value of {@link #MIN_DISTANCE_ERROR_BOUND}.
         */
        private boolean locationHasChanged(Location updatedLocation) {
            return currentLocation == null
                || currentLocation.distanceTo(updatedLocation) > MIN_DISTANCE_ERROR_BOUND;
        }
    };
}
