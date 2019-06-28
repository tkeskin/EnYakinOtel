package com.thkskn.enyakinotel;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thkskn.enyakinotel.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    GoogleMap mMap;
    GPSTracker gps;
    Context context;

    final String GOOGLE_KEY = "AIzaSyAD_BXLA58RZf8yEWSJAV7OxwgpLbiQn68";

    // this is the default location
    public String latitude = "41.103621";
    public String longtitude = "29.023936";

    // URL Address
    String feedUrl = "https://maps.googleapis.com/maps/api/place/search/json?location=";

    public double[] lat = new double[20];
    public double[] lng = new double[20];
    public String[] name = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        if(mMap != null) {
            getLocation();
            setMarker();
        }
    }

    private void getLocation() {
        gps = new GPSTracker(context);
        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = String.valueOf(gps.getLatitude());
            longtitude = String.valueOf(gps.getLongitude());
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    public void setMarker() {
        feedUrl = feedUrl + latitude + "," + longtitude + "&radius=50000&types=lodging&sensor=true&key="
                + GOOGLE_KEY;
        JsonObjectRequest request = new JsonObjectRequest(feedUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for (int i = 0; i < 20 ; i++){
                            try{
                                JSONObject j = response.getJSONArray("results").getJSONObject(i);
                                name[i] = j.getString("name");
                                lat[i] = j.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                lng[i] = j.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                LatLng focus = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longtitude));
                                mMap.addMarker(new MarkerOptions()
                                                .title(name[i])
                                                .position(new LatLng(lat[i], lng[i]))
                                );
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(focus, 13));
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppController.getInstance().addToRequestQueue(request);


    }
    public void onMapReady(final GoogleMap map) {
        this.mMap = map;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}