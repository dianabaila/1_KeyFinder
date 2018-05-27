package com.example.a1_keyfinder;

import android.content.Intent;
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
    public double latitudine;
    public double longitudine ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LatLng currentLocationCoord = getIntent().getParcelableExtra("LatLong");
        userCoord = currentLocationCoord;



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }


    public void onSaveLocation(View view) {
        EditText placeName_tf = (EditText) findViewById(R.id.tfPlaceName);
        String placeName = placeName_tf.getText().toString();

        if (placeName != null && !(placeName.equals(""))) {
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

            Toast.makeText(this, placeName + "  " + userCoord.toString(), Toast.LENGTH_LONG).show();



            splitLocation(userCoord.toString());

            Intent data=new Intent();
            data.putExtra("latit", latitudine);
            data.putExtra("longit",longitudine);
            setResult(2,data);
            finish();

        } else {
            Toast.makeText(this, "Field must not be empty!", Toast.LENGTH_LONG).show();

        }

        //Debugging checks
       /* SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Place", "");
        String crd=preferences.getString("Coord","");
        Log.d("AddPlaceActivity ---->",name + " ------> " + crd);
        //END debugging checks
        */

    }


    private void splitLocation(String location)
    {
        String[] myData= location.split(":");
        String[] myData_1=myData[1].split("\\(");
        String[] myData_2=myData_1[1].split("\\)");
        String[] myLocation=myData_2[0].split(",");
        latitudine=Double.parseDouble(myLocation[0]);
        longitudine=Double.parseDouble(myLocation[1]);
    }


}
