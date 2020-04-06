package com.saveup.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.saveup.android.R;

public class NotificationManagerHelper {

    public final static int NOTIFICATION_ID = NotificationManagerHelper.class.hashCode();
    private static final String TAG = NotificationManagerHelper.class.getSimpleName();
    private static final String CHANNEL_ONE_ID = "com.saveup.android";// The id of the channel.
    private static final String CHANNEL_ONE_NAME = "Notifications";


    public static void sendNotificaiton(Context context, RemoteMessage remoteMessage) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIfNotExist(notificationManager);
        }

      //  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ONE_ID)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true);
              //  .setContentIntent(contentIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createChannelIfNotExist(NotificationManager notificationManager) {
        if (notificationManager.getNotificationChannel(CHANNEL_ONE_ID) == null) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorAccent);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);


            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private static int getNotificationIcon() {
        boolean whiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        // TODO need to add other icon
        return whiteIcon ? R.drawable.ic_launcher_notification : R.drawable.ic_launcher_notification;
    }
}
