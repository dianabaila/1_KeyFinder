package com.example.a1_keyfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.Map;

public class FavoritePlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.showFavPlaces();
    }

    public String x = "bgg";

    public void showFavPlaces() {
        TextView fpView = (TextView) findViewById(R.id.tvFavPlaces);
       /* SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String name = preferences.getString("Place", "");
        String crd=preferences.getString("Coord","");
        String msg = name + " ---> " + crd;
        fpView.setText(msg);
*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> allEntries = preferences.getAll();
        String msg = "";
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            msg += entry.getKey() + "\n" + entry.getValue().toString() + "\n";
        }
        fpView.setText(msg);

    }
}
