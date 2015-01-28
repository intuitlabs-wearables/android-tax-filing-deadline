///////////////////////////////////////////////////////////////////////////////////////////////
//  Copyright (c) 1/2/15 Intuit Inc. All rights reserved. Unauthorized reproduction is a
//  violation of applicable law. This material contains certain confidential and proprietary
//  information and trade secrets of Intuit Inc.
///////////////////////////////////////////////////////////////////////////////////////////////

package com.intuitlabs.wear.voiceandchoice;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.intuit.intuitwear.notifications.AndroidNotification;
import com.intuit.intuitwear.utils.PhoneticSearch;

import java.util.Calendar;

/**
 * <code>BroadcastReceiver</code> to handle user requests, entered on the wearable.
 * <p>
 * The fully qualified class-name of this {@link BroadcastReceiver} needs to be mentioned in the
 * JSON document inside the ListStyle, that gets pushed to the phone, like here:
 * <p>
 * "ListStyle": {
 * "intentName": "com.intuitlabs.wear.voiceandchoice.ActionReceiver",
 * ...
 * </p>
 * If the user engages with the notification and select one of the list-items,
 * either via touch or speech-input, this BroadcastReceiver's onReceive methods get eventually called,
 * with the speech-recognition already performed.
 * </p>
 * We now lookup the key that was associated (in the JSON), with the selected item and perform the
 * appropriate next steps in a <code>switch</code> statement.
 * But before, since the user has now acted on the notification, we remove the notification from all screens.
 */
public class ActionReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = ActionReceiver.class.getSimpleName();

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(final Context context, final Intent _intent) {

        /** notificationId used to issue the notification, so we can cancel the notification */
        final int notificationId = _intent.getIntExtra(Intent.EXTRA_UID, -1);

        /** The bundle that was created during the speech recognition process */
        final Bundle remoteInput = RemoteInput.getResultsFromIntent(_intent);

        /* The user's choice, either directly selected or as a speech recognition result. */
        final String reply = remoteInput != null ? remoteInput.getCharSequence(AndroidNotification.EXTRA_VOICE_REPLY).toString() : "";

        /* The integer value, associated with the command string in the original json document that was used to generate the notification */
        final int selectedId = PhoneticSearch.MATCH(context, reply);

        Log.v(LOG_TAG, "Selection / Speech Recognition result: " + reply);
        Log.i(LOG_TAG, "Selection / Selected ID " + selectedId);

        /* Cancel the Notification, which makes it disappear on phone and watch */
        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);

        switch (selectedId) {
            // Purchase Turbo Tax / Go to Web Page
            case 0:
            case 4:
                final String url = context.getString(R.string.turbotax_url);
                context.startActivity(new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(url))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            // create a calendar Event
            case 1:
            case 5:
                final Calendar cal = Calendar.getInstance();
                cal.set(2015, 3, 15);

                final Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis() + 60 * 60 * 1000)
                        .putExtra(CalendarContract.Events.TITLE, context.getString(R.string.turbotax_title))
                        .putExtra(CalendarContract.Events.DESCRIPTION, context.getString(R.string.turbotax_description))
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, context.getString(R.string.turbotax_location))
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                        .putExtra(Intent.EXTRA_EMAIL, context.getString(R.string.turbotax_email))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;

            // set a reminder
            case 2:
            case 6:
                // todo, set a reminder ..
                break;

            // dismiss, do nothing
            case 3:
            case 7:
            default:
        }
    }
}
