package com.example.a1_keyfinder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Diana B on 5/17/2018.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = "GeofenceTransitionsIntentService" ;
    private GoogleApiClient mGoogleApiClient;

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.e(TAG, "" + getErrorString(geofencingEvent.getErrorCode()));
            return;
        }

        Log.i(TAG, geofencingEvent.toString());

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            String transitionDetails = getGeofenceTransitionInfo(
                    triggeringGeofences);

            String transitionType = getTransitionString(geofenceTransition);


            notifyLocationAlert(transitionType, transitionDetails);
          //  createNotificationChannel();
            //createNotification();
        }
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "location entered";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "location exited";
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return "dwell at location";
            default:
                return "location transition";
        }
    }

    private String getGeofenceTransitionInfo(List<Geofence> triggeringGeofences) {
        ArrayList<String> locationNames = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            locationNames.add(getLocationName(geofence.getRequestId()));
        }
        String triggeringLocationsString = TextUtils.join(", ", locationNames);

        return triggeringLocationsString;
    }

    private String getLocationName(String key) {
        String[] strs = key.split("-");

        String locationName = null;
        if (strs != null && strs.length == 2) {
            double lat = Double.parseDouble(strs[0]);
            double lng = Double.parseDouble(strs[1]);

            locationName = getLocationNameGeocoder(lat, lng);
        }
        if (locationName != null) {
            return locationName;
        } else {
            return key;
        }
    }

    private String getLocationNameGeocoder(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (Exception ioException) {
            Log.e("", "Error in getting location name for the location");
        }

        if (addresses == null || addresses.size() == 0) {
            Log.d("", "no location name");
            return null;
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressInfo = new ArrayList<>();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressInfo.add(address.getAddressLine(i));
            }

            return TextUtils.join(System.getProperty("line.separator"), addressInfo);
        }
    }


    private String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "Geofence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "geofence too many_geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "geofence too many pending_intents";
            default:
                return "geofence error";
        }
    }


    private void createNotificationChannel()
    {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "some_channel_id";
        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(R.color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void createNotification()
    {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyId = 1;
        String channelId = "some_channel_id";

        Notification notification = new Notification.Builder(this,channelId)
                .setContentTitle("KeyFinder")
                .setContentText("Are you leaving? Don't forget your keys!")
                .setSmallIcon(R.drawable.ic_check_black_12dp)
                .build();

        notificationManager.notify(notifyId, notification);
    }


    private void notifyLocationAlert(String locTransitionType, String locationDetails) {

        String CHANNEL_ID = "Second_notification";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_check_black_12dp)
                        .setContentTitle(locTransitionType)
                        .setContentText(locationDetails);

        builder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, builder.build());
    }

}
