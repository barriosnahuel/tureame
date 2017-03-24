/*
 * Copyright (c) 2012-2013 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/17/12 at 6:05 PMhs.
 */
package org.nbempire.android.tourguide.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.nbempire.android.tourguide.R;

/**
 * Required activity to display to the user legal notices about Google Maps Android API.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class LegalNoticesActivity extends Activity {

    /**
     * Tag for class' log.
     */
    private static final String TAG = "LegalNoticesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_notices);

        //  TODO : Functionality : Display legal notices as a part of an "About" menu item, is recommended.

        String legalNotices = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this);
        if (legalNotices != null) {
            ((TextView) findViewById(R.id.legal_notices_text_view)).setText(legalNotices);

        } else {
            //  TODO : Functionality : Display an error or let user download Google Play services. But here should be a bug I think.
            Log.w(TAG, "There isn't any legal notices to display because Google Play services is not available on this device.");
        }
    }
}
