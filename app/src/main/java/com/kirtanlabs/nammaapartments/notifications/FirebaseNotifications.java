package com.kirtanlabs.nammaapartments.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kirtanlabs.nammaapartments.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ACCEPT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CABS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PACKAGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.MESSAGE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOTIFICATION_EXPAND_MSG;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOTIFICATION_EXPAND_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOTIFICATION_ID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REJECT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_MESSAGE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_USER_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_VISITOR_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.USER_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VISITOR_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VISITOR_TYPE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/13/2018
 */
public class FirebaseNotifications extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();

        String remoteMessageType = remoteMessageData.get(REMOTE_TYPE);

        //E-Intercom Notification requires Users Action
        if (remoteMessageType.equals("E-Intercom")) {
            String message = remoteMessageData.get(REMOTE_MESSAGE);
            String profilePhoto = remoteMessageData.get(REMOTE_PROFILE_PHOTO);
            String notificationUID = remoteMessageData.get(REMOTE_NOTIFICATION_UID);
            String userUID = remoteMessageData.get(REMOTE_USER_UID);
            String visitorType = remoteMessageData.get(REMOTE_VISITOR_TYPE);

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);

            remoteViews.setTextViewText(R.id.textNotificationMessage, message);

            /*We don't want to show Profile image if cabs and packages enter into society*/
            if (visitorType.equals(FIREBASE_CHILD_CABS) || visitorType.equals(FIREBASE_CHILD_PACKAGES)) {
                remoteViews.setViewVisibility(R.id.eIntercomProfilePic, View.GONE);
            } else {
                remoteViews.setImageViewBitmap(R.id.eIntercomProfilePic, getBitmapFromURL(profilePhoto));
            }

            Notification notification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.namma_apartment_notification)
                    .setContentTitle(NOTIFICATION_EXPAND_TITLE)
                    .setContentText(NOTIFICATION_EXPAND_MSG)
                    .setAutoCancel(true)
                    .setCustomBigContentView(remoteViews)
                    .setSound(RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND))
                    .setPriority(PRIORITY_DEFAULT)
                    .build();

            int mNotificationID = (int) System.currentTimeMillis();

            Intent acceptButtonIntent = new Intent(ACCEPT_BUTTON_CLICKED);
            acceptButtonIntent.putExtra(NOTIFICATION_ID, mNotificationID);
            acceptButtonIntent.putExtra(NOTIFICATION_UID, notificationUID);
            acceptButtonIntent.putExtra(USER_UID, userUID);
            acceptButtonIntent.putExtra(MESSAGE, message);
            acceptButtonIntent.putExtra(VISITOR_TYPE, visitorType);
            acceptButtonIntent.putExtra(VISITOR_PROFILE_PHOTO, profilePhoto);
            PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 123, acceptButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.buttonAccept, acceptPendingIntent);

            Intent rejectButtonIntent = new Intent(REJECT_BUTTON_CLICKED);
            rejectButtonIntent.putExtra(NOTIFICATION_ID, mNotificationID);
            rejectButtonIntent.putExtra(NOTIFICATION_UID, notificationUID);
            rejectButtonIntent.putExtra(USER_UID, userUID);
            rejectButtonIntent.putExtra(VISITOR_TYPE, visitorType);
            PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 123, rejectButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.buttonReject, rejectPendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            /*To support Android Oreo Devices and higher*/
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        getString(R.string.default_notification_channel_id), "Namma Apartments Channel", NotificationManager.IMPORTANCE_HIGH);
                Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
            }

            Objects.requireNonNull(notificationManager).notify(mNotificationID, notification);
        } else {

            //General Notification - These do not require any user actions
            String message = remoteMessageData.get("message");

            Notification notification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.namma_apartment_notification)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(PRIORITY_DEFAULT)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            /*To support Android Oreo Devices and higher*/
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        getString(R.string.default_notification_channel_id), "Namma Apartments Channel", NotificationManager.IMPORTANCE_HIGH);
                Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
            }

            int mNotificationID = (int) System.currentTimeMillis();
            Objects.requireNonNull(notificationManager).notify(mNotificationID, notification);
        }
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return getCircleBitmap(myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
