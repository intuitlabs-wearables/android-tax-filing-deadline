///////////////////////////////////////////////////////////////////////////////////////////////
//  Copyright (c) 1/2/15 Intuit Inc. All rights reserved. Unauthorized reproduction is a
//  violation of applicable law. This material contains certain confidential and proprietary
//  information and trade secrets of Intuit Inc.
///////////////////////////////////////////////////////////////////////////////////////////////

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

/**
 * GCMIntentService extends Google's {@link GCMBaseIntentService}, setting up the communication with
 * the Push Notification Gateway. Here we are adding the registration with Intuit's Notification
 * Server and also implement a few callbacks.
 */
public class GCMIntentService extends GCMBaseIntentService {
    private static final String LOG_TAG = GCMIntentService.class.getSimpleName();

    /**
     * This method triggers the main registration flow. It should be called each
     * time the application is started to ensure the app stays in sync with the
     * Push Notification servers.
     *
     * @param context         {@link android.content.Context} Android Context
     * @param receiver_id     {@link String} Type of userId, represents intuit id, Mobile Number, Email, uniquely identifies the user
     * @param receiver_groups {@link String[]} The groups to which the userId may belong, allowing for groups messages
     */
    protected static void register(final Context context,
                                   final String receiver_id,
                                   final String[] receiver_groups) {
        PushNotifications.register(
                context,
                MainActivity.GCM_PROJECT_NUMBER,
                receiver_id,
                receiver_groups,
                UserTypeEnum.OTHER,
                MainActivity.INTUIT_SENDER_ID,
                false);
    }

    /**
     * Default Constructor, requires Google GCM PROJECT_NUMBER,
     * which must be your GCM Project number and statically available.
     */
    public GCMIntentService() {
        super(MainActivity.GCM_PROJECT_NUMBER);
    }

    /*
     * This callback method is invoked when GCM delivers a notification to the device.
     *
     * Assuming that the json encoded message is a valid (see IntuitWear JSONSchema) document,
     * we acquire an instance of a {@link IWearNotificationSender.Factory} to create a NotificationSender,
     * which will send the generated notification to the wearable device.
     *
     * @param context {@link Context} Application context
     * @param intent {@link Intent} received with the push notification
     */
    @Override
    protected void onMessage(final Context context, final Intent intent) {
        Log.v(LOG_TAG, "Received onMessage call. Will now display a notification");
        final String message = intent.getStringExtra("payload").replaceAll("[\r\n]+$", "");
        final IWearNotificationSender.Factory iWearSender = IWearNotificationSender.Factory.getsInstance();
        try {
            IWearAndroidNotificationSender androidNotificationSender =
                    (IWearAndroidNotificationSender) iWearSender.createNotificationSender(IWearNotificationType.ANDROID, this, message);
            androidNotificationSender.sendNotification(this);
        } catch (IntuitWearException e) {
            e.printStackTrace();
        }
    }


    /*
     * This callback method is invoked after a successful registration with GCM.
     * Here we are passing the new registrationId to the PNG SDK.
     * The SDK will send the registrationId along with any user and userGroup mappings to the PNG servers.
     */
    @Override
    protected void onRegistered(final Context context, final String regId) {
        Log.i(LOG_TAG, "Received onRegistered call. Updating the PNG servers.");
        PushNotifications.updateServer(context, regId);
    }

    /**
     * Callback called upon a GCM error.
     *
     * @param context {@link Context} Application context
     * @param msg     {@link String} Error string
     */
    @Override
    protected void onError(final Context context, final String msg) {
        Log.e(LOG_TAG, "Error related to GCM: " + msg);
    }

    /**
     * Callback called when device is unregistered from GCM.
     *
     * @param context {@link Context} Application context
     * @param msg     Unregister message
     */
    @Override
    protected void onUnregistered(final Context context, final String msg) {
        Log.i(LOG_TAG, "Received unregistered call");
    }
}

