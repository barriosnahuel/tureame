/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

package org.nbempire.android.tourguide.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.nbempire.android.tourguide.R;

/**
 * @author Nahuel Barrios.
 * @since 1
 */
public class MapActivity extends Activity {

    /**
     * Tag for class' log.
     */
    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (isDeviceUnableTuRunApp(this)) {
            //  TODO : Close app.
            Log.i(TAG, "The application will be closed because of the device is unable to run without the Google Play Services API.");
        }
    }

    /**
     * Check if the active device has the Google Play Services available. If not, then shoy an error dialog to let user install it.
     *
     * @param activity
     *         The activity where to show the error dialog.
     *
     * @return {@code true} if the device is unable to run the app. {@code false} if the device is able to run the app.
     */
    private boolean isDeviceUnableTuRunApp(Activity activity) {
        boolean unable = false;

        int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (ConnectionResult.SUCCESS != googlePlayServicesAvailable) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable, activity, 0).show();
            //  TODO : Put cancelButton handler and close the app
            unable = true;
        }
        return unable;
    }

}
