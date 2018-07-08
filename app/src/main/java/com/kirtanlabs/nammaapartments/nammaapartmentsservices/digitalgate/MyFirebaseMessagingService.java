package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kirtanlabs.nammaapartments.R;

import java.util.Map;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/13/2018
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();
        String notificationUID = remoteMessageData.get("notification_uid");

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setSound(RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND))
                .setPriority(PRIORITY_MAX);

        int mNotificationID = (int) System.currentTimeMillis();

        Intent acceptButtonIntent = new Intent("accept_button_clicked");
        acceptButtonIntent.putExtra("Notification_UID", notificationUID);
        acceptButtonIntent.putExtra("Notification_Id", mNotificationID);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 123, acceptButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonAccept, acceptPendingIntent);

        Intent rejectButtonIntent = new Intent("reject_button_clicked");
        rejectButtonIntent.putExtra("Notification_UID", notificationUID);
        acceptButtonIntent.putExtra("Notification_Id", mNotificationID);
        PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 123, rejectButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonReject, rejectPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(mNotificationID, mBuilder.build());
    }

}
