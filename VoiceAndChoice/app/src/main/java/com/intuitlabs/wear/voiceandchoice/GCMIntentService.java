package com.intuitlabs.wear.voiceandchoice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.intuit.intuitwear.exceptions.IntuitWearException;
import com.intuit.intuitwear.notifications.IWearAndroidNotificationSender;
import com.intuit.intuitwear.notifications.IWearNotificationSender;
import com.intuit.intuitwear.notifications.IWearNotificationType;
import com.intuit.mobile.png.sdk.PushNotifications;
import com.intuit.mobile.png.sdk.UserTypeEnum;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String LOG_TAG = GCMIntentService.class.getName();
    /**
     * This is an example value. You will need to replace with your Google API's Project Number
     */
    private static final String GCM_PROJECT_NUMBER = "1024976853493";
    /**
     * This is an example value. You will need to replace with your PNG Sender ID
     */
    private static final String INTUIT_SENDER_ID = "91085311-58c4-4ad0-987c-03019cec72c7";
    /**
     * UserId - a value to identify the current user (e.g. user's mobile number, email address, Intuit Id, etc)
     */
    private static final String END_USER_ID = "Mickey Mouse";
    /**
     * User Groups - an optional list of group names to which the user may belong
     */
    private static final String[] END_USER_GROUPS = {"Characters", "Disney"};


    /**
     * register the current user to receive notification on their device
     */
    protected static void register(final Context context) {
        PushNotifications.register(
                context,
                GCM_PROJECT_NUMBER,
                END_USER_ID,
                END_USER_GROUPS,
                UserTypeEnum.OTHER,
                INTUIT_SENDER_ID,
                false);
    }


    public GCMIntentService() {
        super(GCMIntentService.GCM_PROJECT_NUMBER);
    }

    /*
     * This callback method is invoked after a successful registration with GCM.
     * Here we are passing the new registrationId to the PNG SDK.
     * The SDK will send the registrationId along with any user and userGroup mappings to the PNG servers.
     */
    @Override
    protected void onRegistered(Context context, String regId) {
        Log.i(LOG_TAG, "Received onRegistered call. Updating the PNG servers.");
        PushNotifications.updateServer(context, regId);

    }

    /*
     * This callback method is invoked when GCM delivers a notification to the device
     * Here we are simply providing an example of how to display the notification to the user.
     * There are many other implementation options.
     * Older API versions of Android may need to use different classes and methods.
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.v(LOG_TAG, "Received onMessage call. Will now display a notification");
        final String message = intent.getStringExtra("payload");
        createAndSendNotification(IWearNotificationType.ANDROID, message);
    }

    @Override
    protected void onError(Context arg0, String arg1) {
        Log.e(LOG_TAG, "Error related to GCM: " + arg1);
    }

    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        Log.i(LOG_TAG, "Received unregistered call");
    }

    private void createAndSendNotification(IWearNotificationType type, String tstJson) {
        // Create and send the notification from the test json file.
        IWearNotificationSender.Factory iWearSender = IWearNotificationSender.Factory.getsInstance();
        IWearAndroidNotificationSender androidNotificationSender;
        try {
            androidNotificationSender = (IWearAndroidNotificationSender) iWearSender.createNotificationSender(type, this, tstJson);
            androidNotificationSender.sendNotification(this);
        } catch (IntuitWearException e) {
            e.printStackTrace();
        }
    }
}

