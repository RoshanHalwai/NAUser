package com.kirtanlabs.nammaapartments.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.activities.NoticeBoard;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ACCEPT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CABS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_GATE_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_IGNORED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PACKAGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.MESSAGE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOTIFICATION_EXPAND_MSG;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOTIFICATION_EXPAND_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOTIFICATION_ID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REJECT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_MESSAGE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_USER_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_VISITOR_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.REMOTE_VISITOR_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.USER_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VISITOR_MOBILE_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VISITOR_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VISITOR_TYPE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/13/2018
 */
public class FirebaseNotifications extends FirebaseMessagingService {

    String remoteMessageType;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> remoteMessageData = remoteMessage.getData();
        remoteMessageType = remoteMessageData.get(REMOTE_TYPE);
        if (remoteMessageType.equals(getString(R.string.e_intercom))) {
            eIntercomNotification(remoteMessageData);
        } else {
            generalNotification(remoteMessageData);
        }
    }

    /**
     * Shows notification in the device which requires user actions. Following cases are possible;
     * ACCEPTED - user presses on accept button
     * REJECTED - user presses on reject button
     * SWIPED-OFF - user swipes it off
     * IGNORED - user ignores the notification
     *
     * @param remoteMessageData Payload data sent from server
     */
    private void eIntercomNotification(final Map<String, String> remoteMessageData) {
        String message = remoteMessageData.get(REMOTE_MESSAGE);
        String profilePhoto = remoteMessageData.get(REMOTE_PROFILE_PHOTO);
        String notificationUID = remoteMessageData.get(REMOTE_NOTIFICATION_UID);
        String userUID = remoteMessageData.get(REMOTE_USER_UID);
        String visitorType = remoteMessageData.get(REMOTE_VISITOR_TYPE);
        String visitorMobileNumber = remoteMessageData.get(REMOTE_VISITOR_MOBILE_NUMBER);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);
        remoteViews.setTextViewText(R.id.textNotificationMessage, message);

        /*We don't want to show Profile image if cabs and packages enter into society*/
        if (visitorType.equals(FIREBASE_CHILD_CABS) || visitorType.equals(FIREBASE_CHILD_PACKAGES)) {
            remoteViews.setViewVisibility(R.id.eIntercomProfilePic, View.GONE);
        } else {
            remoteViews.setImageViewBitmap(R.id.eIntercomProfilePic, getBitmapFromURL(profilePhoto));
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId;

        /*To support Android Oreo Devices and higher*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    getString(R.string.intercom_notification_channel_id), getString(R.string.namma_apartments_channel), NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.e_intercom_sound), attributes);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
            channelId = mChannel.getId();
            IntentFilter actionIntents = new IntentFilter();
            actionIntents.addAction(ACCEPT_BUTTON_CLICKED);
            actionIntents.addAction(REJECT_BUTTON_CLICKED);
            getApplicationContext().registerReceiver(new ActionButtonListener(), actionIntents);
        } else {
            channelId = getString(R.string.intercom_notification_channel_id);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.namma_apartment_notification)
                .setContentTitle(NOTIFICATION_EXPAND_TITLE)
                .setContentText(NOTIFICATION_EXPAND_MSG)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setPriority(PRIORITY_DEFAULT)
                .build();

        /* Setting Push Notification Custom Sound */
        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.e_intercom_sound);

        int mNotificationID = (int) System.currentTimeMillis();

        /*Action to be performed when user presses ACCEPT Button*/
        Intent acceptButtonIntent = new Intent(ACCEPT_BUTTON_CLICKED);
        acceptButtonIntent.putExtra(NOTIFICATION_ID, mNotificationID);
        acceptButtonIntent.putExtra(NOTIFICATION_UID, notificationUID);
        acceptButtonIntent.putExtra(USER_UID, userUID);
        acceptButtonIntent.putExtra(MESSAGE, message);
        acceptButtonIntent.putExtra(VISITOR_TYPE, visitorType);
        acceptButtonIntent.putExtra(VISITOR_PROFILE_PHOTO, profilePhoto);
        acceptButtonIntent.putExtra(VISITOR_MOBILE_NUMBER, visitorMobileNumber);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 123, acceptButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonAccept, acceptPendingIntent);

        /*Action to be performed when user presses REJECT Button*/
        Intent rejectButtonIntent = new Intent(REJECT_BUTTON_CLICKED);
        rejectButtonIntent.putExtra(NOTIFICATION_ID, mNotificationID);
        rejectButtonIntent.putExtra(NOTIFICATION_UID, notificationUID);
        rejectButtonIntent.putExtra(USER_UID, userUID);
        rejectButtonIntent.putExtra(VISITOR_TYPE, visitorType);
        PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 123, rejectButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.buttonReject, rejectPendingIntent);

        /*Action to be performed when user swipes from left-to-right*/
        notification.deleteIntent = rejectPendingIntent;
        Objects.requireNonNull(notificationManager).notify(mNotificationID, notification);

        /*Action to be performed when user ignore the notification - Timeout 30 Seconds*/
        Handler h = new Handler(Looper.getMainLooper());
        long delayInMilliseconds = 30000;
        h.postDelayed(() -> {
            notificationManager.cancel(mNotificationID);
            setNotificationToIgnored(userUID, visitorType, notificationUID);
        }, delayInMilliseconds);
    }

    /**
     * Sets the status of the notification to IGNORED since the notification timedout
     *
     * @param currentUserID   - userUID of the the LoggedIn user
     * @param visitorType     - can be either one of these Guest/Cab/Package
     * @param notificationUID - unique UID to identify each gate notifications
     */
    private void setNotificationToIgnored(final String currentUserID, final String visitorType, final String notificationUID) {
        DatabaseReference databaseReference = PRIVATE_USERS_REFERENCE.child(currentUserID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFlatDetails userFlatDetails = Objects.requireNonNull(dataSnapshot.getValue(NammaApartmentUser.class)).getFlatDetails();
                DatabaseReference currentUserDataReference = PRIVATE_USER_DATA_REFERENCE
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber());

                /*Here we are setting the notification status so that Guard know that this notification has been ignored by the user*/
                DatabaseReference currentUserNotificationReference = currentUserDataReference
                        .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                        .child(currentUserID)
                        .child(visitorType)
                        .child(notificationUID);

                currentUserNotificationReference.child(FIREBASE_CHILD_STATUS).setValue(FIREBASE_CHILD_IGNORED);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Displays notification to users which do not require any user action
     *
     * @param remoteMessageData - Payload data sent from server
     */
    private void generalNotification(final Map<String, String> remoteMessageData) {
        String message = remoteMessageData.get(REMOTE_MESSAGE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId;
        int mNotificationID = (int) System.currentTimeMillis();

        /*To support Android Oreo Devices and higher*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    getString(R.string.general_notification_channel_id), getString(R.string.namma_apartments_channel), NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
            channelId = mChannel.getId();
        } else {
            channelId = getString(R.string.general_notification_channel_id);
        }

        Notification generalNotification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.namma_apartment_notification)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(PRIORITY_DEFAULT)
                .build();

        /*After the Admin adds a notice and user receives notification, making sure user is navigated
         * to 'Notice Board' screen on press of notification in notification panel*/
        if (remoteMessageType.equals(getString(R.string.notice_board_notification))) {
            Intent noticeBoardIntent = new Intent(this, NoticeBoard.class);
            generalNotification.contentIntent = PendingIntent.getActivity(this, Constants.NEW_NOTICE_CODE,
                    noticeBoardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Objects.requireNonNull(notificationManager).notify(mNotificationID, generalNotification);
    }

    private Bitmap getBitmapFromURL(String strURL) {
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
