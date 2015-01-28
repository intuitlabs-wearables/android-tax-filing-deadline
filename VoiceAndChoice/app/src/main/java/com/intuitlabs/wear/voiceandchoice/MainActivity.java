///////////////////////////////////////////////////////////////////////////////////////////////
//  Copyright (c) 1/2/15 Intuit Inc. All rights reserved. Unauthorized reproduction is a
//  violation of applicable law. This material contains certain confidential and proprietary
//  information and trade secrets of Intuit Inc.
///////////////////////////////////////////////////////////////////////////////////////////////

package com.intuitlabs.wear.voiceandchoice;

import android.app.Activity;
import android.os.Bundle;

/**
 * The main and only {@link android.app.Activity} in this demo app, doesn't even need or have a UI.
 * <p>
 * It only here, to register the user's device with the push notification services.
 * Therefore, we have the GCM Project number and the Intuit SenderID stored here. A better place
 * might be the Application class, if an app has/need one.
 * These two ids that identify the application at the push service, are used in the
 * {@link com.intuitlabs.wear.voiceandchoice.GCMIntentService}, and therefore, if they get moved elsewhere,
 * the sourcecode of the service needs to be updated accordingly.
 * </p>
 * The GCMIntentService's static register method if called with an arbitrary id but it needs to be
 * unique among all users of this application. Providing group names is optional, but provides an
 * efficient way to send a single message to a group of users. Again, segmentation is arbitrary,
 * could be a geo. region, age-group, etc.
 */
public class MainActivity extends Activity {
    /**
     * This is an example value. You will need to replace with your Google API's Project Number
     */
    static final String GCM_PROJECT_NUMBER = "1024976853493";
    /**
     * This is an example value. You will need to replace with your PNG Sender ID
     */
    static final String INTUIT_SENDER_ID = "91085311-58c4-4ad0-987c-03019cec72c7";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GCMIntentService.register(this, "Mickey Mouse", new String[]{"Characters", "Disney"});
        finish();
    }
}