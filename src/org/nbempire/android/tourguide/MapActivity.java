package org.nbempire.android.tourguide;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS != googlePlayServicesAvailable) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable, this, 0).show();
        }
    }

}
