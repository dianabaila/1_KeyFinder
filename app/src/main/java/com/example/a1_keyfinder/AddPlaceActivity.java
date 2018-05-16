package com.example.a1_keyfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class AddPlaceActivity extends AppCompatActivity {

    public LatLng userCoord;
    private GeofencingClient mGeofencingClient;

    private List<Geofence> mGeofenceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LatLng currentLocationCoord=getIntent().getParcelableExtra("LatLong");
        userCoord=currentLocationCoord;

       // mGeofencingClient = LocationServices.getGeofencingClient(this);
     //   mGeofenceList=new ArrayList<Geofence>();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    /*private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
       // if (mGeofencePendingIntent != null) {
         //   return mGeofencePendingIntent;
        //}
        Intent intent = new Intent(this, GeoFenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
       PendingIntent mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }
*/
    public void onSaveLocation(View view) {
        EditText placeName_tf=(EditText) findViewById(R.id.tfPlaceName);
        String placeName=placeName_tf.getText().toString();

        if(placeName!=null && !(placeName.equals("")))
        {
          //INCERCARE StackOverflow
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();

            //Next 2 rows -> clear SharedPref file if needed.
            //editor.clear();
            //editor.apply();

            editor.putString(placeName, userCoord.toString());
            editor.apply();


            Snackbar.make(view, "SAVED", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            placeName_tf.setText("");

            Toast.makeText(this,placeName + "  " + userCoord.toString(), Toast.LENGTH_LONG).show();




            //GEOFENCING

           /* mGeofenceList.add(new Geofence.Builder()
            .setRequestId(placeName)
            .setCircularRegion(userCoord.latitude,userCoord.longitude, 20)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
*/
        }
        else
        {
            Toast.makeText(this,"Field must not be empty!", Toast.LENGTH_LONG).show();

        }

        //Debugging checks
       /* SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Place", "");
        String crd=preferences.getString("Coord","");
        Log.d("AddPlaceActivity ---->",name + " ------> " + crd);
        //END debugging checks
        */
    }


   /* private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    @SuppressLint("MissingPermission")
    private void addGeoF()
    {
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                        Log.d("GEOFENCE", "ADDED");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...

                        Log.d("GEOFENCE", "ERROR!!!!");
                    }
                });
    }
    */
}
