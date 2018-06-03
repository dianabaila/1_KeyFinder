package com.example.a1_keyfinder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
            createNotification(context,"Connection lost");

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

}
