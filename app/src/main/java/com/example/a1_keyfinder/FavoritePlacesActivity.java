package com.example.a1_keyfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class FavoritePlacesActivity extends AppCompatActivity {

    ListView listView;
    public ArrayAdapter<String> adapter;
    public Map<String, ?> allEntries;
    public double latitudine;
    public double longitudine ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        allEntries = preferences.getAll();


        listView=(ListView) findViewById(R.id.favPlaces_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.showFavPlaces();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  itemValue    = (String) listView.getItemAtPosition(position);
                String sendLocation= (String) allEntries.get(itemValue);
                splitLocation(sendLocation);

                Intent data=new Intent();
                data.putExtra("latit", latitudine);
                data.putExtra("longit",longitudine);
                setResult(1,data);
                finish();
            }



        });
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
    public void showFavPlaces() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> allEntries = preferences.getAll();

        ArrayList<String> favPlacesList=new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
          //  msg += entry.getKey() + "\n" + entry.getValue().toString() + "\n";
            String myloc=entry.getValue().toString();
            favPlacesList.add(entry.getKey());
        }

         adapter=new ArrayAdapter<String>(this, R.layout.favorite_places_list_view,R.id.tvPlaceName,favPlacesList);
        listView.setAdapter(adapter);


    }


}
