package org.nbempire.android.tourguide;

import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable, this, 0).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
