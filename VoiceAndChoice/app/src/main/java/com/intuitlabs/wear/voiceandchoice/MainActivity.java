/*
 * Copyright (c) 2015 Intuit Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
    static final String GCM_PROJECT_NUMBER = "1024976853493"; //Todo replace with empty string before publishing into public github repo
    /**
     * This is an example value. You will need to replace with your PNG Sender ID
     */
    static final String INTUIT_SENDER_ID = "91085311-58c4-4ad0-987c-03019cec72c7"; //Todo replace with empty string before publishing into public github repo
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