package org.nbempire.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Main activity.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class MapActivity extends Activity {

    private static String TAG = "iamatourist";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.i(TAG, "onCreate");
        setContentView(R.layout.main);

        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (ConnectionResult.SUCCESS == available) {
            Toast.makeText(getApplicationContext(), "Success: " + available, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Otro valor: " + available, Toast.LENGTH_LONG).show();
        }

    }


}