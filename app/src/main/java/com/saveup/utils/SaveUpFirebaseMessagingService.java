package com.saveup.utils;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class SaveUpFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("FIREBASE_PUSH_MESSAGE", remoteMessage + "");

        if(remoteMessage.getNotification() != null) {
            NotificationManagerHelper.sendNotificaiton(getApplicationContext(), remoteMessage);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("FIREBASE_PUSH_TOKEN", s + "");

        SessionSave.saveSession("device_token", s, getApplicationContext());
    }
}
