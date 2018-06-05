package com.example.a1_keyfinder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

/**
 * Created by Diana B on 6/3/2018.
 */

public class BluetoothBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        String action=intent.getAction();
        if(TextUtils.equals(action, BluetoothDevice.ACTION_ACL_DISCONNECTED))
        {
            BluetoothDevice device = intent.getExtras()
                    .getParcelable(BluetoothDevice.EXTRA_DEVICE);
            createNotificationChannel(context);
            //createNotification(context,"Connection lost");
            sendNotification(context);
        }
    }

    private void createNotificationChannel(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "bt_channel_id";
        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(R.color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void createNotification(Context context, String details) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyId = 1;
        String channelId = "bt_channel_id";

        Notification notification = new Notification.Builder(context, channelId)
                .setContentTitle("KeyFinder")
                .setContentText(details)
                .setSmallIcon(R.drawable.ic_check_black_12dp)
                .build();

        notificationManager.notify(notifyId, notification);
    }

    private void sendNotification(Context context) {
        // Get an instance of the Notification manager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(context, MapsActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MapsActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.ic_check_black_12dp)
                .setContentTitle("KeyFinder")
                .setContentText("Connection to keys was lost!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Bluetooth connection lost. Are your keys close? Check your connectivity!"))
                .setContentIntent(notificationPendingIntent);

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("bt_channel_id"); // Channel ID
        }

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Issue the notification
        notificationManager.notify(0, builder.build());
    }

}
