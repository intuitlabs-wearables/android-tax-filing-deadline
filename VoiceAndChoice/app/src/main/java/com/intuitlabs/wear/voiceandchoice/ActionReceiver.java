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

import com.intuit.intuitwear.utils.PhoneticSearch;

import java.util.Calendar;

public class ActionReceiver extends BroadcastReceiver {
    public static final String LOG_TAG = ActionReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent _intent) {
        /* notificationId used to issue the notification, so we can cancel the notification */
        final int notificationId = _intent.getIntExtra(Intent.EXTRA_UID, -1);

        /* choices, dynamically provided in JSON object, e.g. passed in through a push notification */
        final String[] items = _intent.getStringArrayExtra(context.getString(R.string.EXTRA_ITEMS_LIST));
        /* bundle created during the speech  recognition process */
        final Bundle remoteInput = RemoteInput.getResultsFromIntent(_intent);

        /* user's choice, either directly selected or speech recognition result. */
        final String reply = remoteInput != null ? remoteInput.getCharSequence(context.getString(R.string.EXTRA_VOICE_REPLY)).toString() : "";

        /* use PhoneticSearch to find the best match */
        final int selectedId = new PhoneticSearch(items).bestMatch(reply);

        Log.v(LOG_TAG, "Speech Recognition result: " + reply);
        Log.v(LOG_TAG, "Selected Item: " + items[selectedId]);

        /* Cancel the Notification, makes it disappear on phone and watch */
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);

        switch (selectedId) {
            // Purchase Turbo Tax / Go to Web Page
            case 0:
            case 4:
                String url = "https://turbotax.intuit.com/personal-taxes/";
                context.startActivity(new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(url))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            // create a calendar Event
            case 1:
            case 5:
                Calendar cal = Calendar.getInstance();
                cal.set(2015, 3, 15);

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis() + 60 * 60 * 1000)
                        .putExtra(CalendarContract.Events.TITLE, "Turbo Tax, getting me my maximum tax refund.")
                        .putExtra(CalendarContract.Events.DESCRIPTION, "File Tax Return")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "Home Office")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                        .putExtra(Intent.EXTRA_EMAIL, "support@turbotax.com")
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;

            // set a reminder
            case 2:
            case 6:
                // todo, set a reminder ..
                break;

            // dismiss
            case 3:
            case 7:
            default:
                // do nothing
        }
    }
}
