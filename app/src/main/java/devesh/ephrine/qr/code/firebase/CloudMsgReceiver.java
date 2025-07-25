package  devesh.ephrine.qr.code.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import devesh.ephrine.qr.code.MainActivity;
import devesh.ephrine.qr.code.R;


public class CloudMsgReceiver extends FirebaseMessagingService {
    private static final String TAG = "FB_Cloud_Msg_Receiver";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String channelId = "App Notifications";

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String title = "";
        String msg = "";
        String url = "";

        // Check if message contains a data payload.
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            title = data.get("title");
            msg = data.get("message");
            url = data.get("url");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getTitle();
            msg = remoteMessage.getNotification().getBody();

            if (remoteMessage.getNotification().getChannelId() != null && !remoteMessage.getNotification().getChannelId().isEmpty()) {
                channelId = remoteMessage.getNotification().getChannelId();
            }
        }

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        // Add extra data to the intent
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("message", msg);
        notificationIntent.putExtra("url", url);

        sendNotification(title, msg, notificationIntent, channelId);
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: Firebase Token : " + token);
    }

    public void sendNotification(String title, String msg, Intent notificationIntent, String channelId) {
        int notificationId = new Random().nextInt(1000);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        // Create the notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = channelId; // Using channelId as channelName as in Kotlin
            // String channelDescription = "General Notifications"; // Not used in Kotlin version
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            // channel.setDescription(channelDescription); // Not set in Kotlin version

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // send notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(devesh.ephrine.qr.common.R.drawable.ic_baseline_qr_code_scanner_30) // Make sure this drawable exists
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // Add any additional configuration to the notificationBuilder as needed

            // Show the notification
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }
}
