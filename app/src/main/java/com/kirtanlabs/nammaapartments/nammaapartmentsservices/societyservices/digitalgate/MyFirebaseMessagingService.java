package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate;

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
import static com.kirtanlabs.nammaapartments.Constants.NOTIFICATION_EXPAND_MSG;
import static com.kirtanlabs.nammaapartments.Constants.NOTIFICATION_EXPAND_TITLE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/13/2018
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();

        String remoteMessageType = remoteMessageData.get("type");

        //E-Intercom Notification requires Users Action
        if (remoteMessageType.equals("E-Intercom")) {
            String message = remoteMessageData.get("message") + ". Please Confirm?";
            String profilePhoto = remoteMessageData.get("profile_photo");
            String notificationUID = remoteMessageData.get("notification_uid");
            String userUID = remoteMessageData.get("user_uid");

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);

            remoteViews.setTextViewText(R.id.textNotificationMessage, message);

            remoteViews.setImageViewBitmap(R.id.eIntercomProfilePic, getBitmapFromURL(profilePhoto));

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

            Intent acceptButtonIntent = new Intent("accept_button_clicked");
            acceptButtonIntent.putExtra("Notification_Id", mNotificationID);
            acceptButtonIntent.putExtra("Notification_UID", notificationUID);
            acceptButtonIntent.putExtra("User_UID", userUID);
            PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 123, acceptButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.buttonAccept, acceptPendingIntent);

            Intent rejectButtonIntent = new Intent("reject_button_clicked");
            rejectButtonIntent.putExtra("Notification_UID", notificationUID);
            rejectButtonIntent.putExtra("Notification_Id", mNotificationID);
            rejectButtonIntent.putExtra("User_UID", userUID);
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
