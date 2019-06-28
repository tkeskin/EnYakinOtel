package com.thkskn.enyakinotel;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private AdView adView;
    private InterstitialAd interstitial;
    private static final String AD_ID = "ca-app-pub-2367630010898474/9125033967";
    //AIzaSyAD_BXLA58RZf8yEWSJAV7OxwgpLbiQn68
    final String GOOGLE_KEY = "";

    // we will need to take the latitude and the logntitude from a certain point
    // this is the default location
    public String latitude = "41.103621";
    public String longtitude = "29.023936";

    ListView listview;
    ListViewAdapter adapter;
    Spinner spinner;

    public ArrayList<HashMap<String, String>> arraylist;
    static String COUNTER = "counter";
    static String NAME = "name";
    static String RATING = "rating";
    static String VICINITY = "vicinity";
    static String PLACEID = "place_id";
    static String LONG = "lng";
    static String LAT = "lat";
    static String CURRENTLAT = "currentLat";
    static String CURRENTLNG = "currentLng";

    // URL Address
    String feedUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    Context context;
    private JsonObjectRequest request;

    // GPSTracker class
    GPSTracker gps;
    KeskinDatabaseAdapter keskinHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        keskinHelper = new KeskinDatabaseAdapter(context);

        if(checkConnection()) {
            loadAds();
            startAnalytics();
            getLocation();
            getHotelInfo();
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.list);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(context, arraylist);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
        }else{
            new AlertDialog.Builder(this)
                    //.setIcon(android.R.drawable.ic_dialog_info)
                    //.setTitle("Hata")
                    .setMessage("Bağlantı yok,Kontrol eder misin?")
                    .setNegativeButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }

        //assign toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        //home icon shows for navigationDrawer
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //assign navigationDrawer
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(this,R.array.dizi,android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.i("uyarı", String.valueOf(arg2));
                if(keskinHelper.getAllStatu().contains("true")) spinner.setSelection(0);
                else spinner.setSelection(5);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    //load Analytics start -------------
    public void startAnalytics(){
        // Get tracker.
        Tracker t = ((AppController) getApplication()).getTracker(
                AppController.TrackerName.APP_TRACKER);
        // Enable Advertising Features.
        t.enableAdvertisingIdCollection(true);
        // Set screen name.
        t.setScreenName("Anasayfa Otel");
        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

    }
    //load Analytics end -------------

    public void getLocation() {
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

    public void getHotelInfo() {

        feedUrl = feedUrl + latitude + "," + longtitude + "&radius=50000&types=lodging&sensor=true&key="
                + GOOGLE_KEY;
        Log.d("Bune : ",feedUrl);
        // Create an array
        arraylist = new ArrayList<HashMap<String, String>>();
        request = new JsonObjectRequest(feedUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for (int i = 0; i < 20 ; i++){
                            try{
                                JSONObject j = response.getJSONArray("results").getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put("counter", String.valueOf(i+1));
                                map.put("name",j.getString("name"));
                                map.put("rating", j.getString("rating"));
                                map.put("vicinity", j.getString("vicinity"));
                                map.put("place_id", j.getString("place_id"));
                                map.put("lat",j.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                                map.put("lng",j.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                                map.put("currentLat",latitude);
                                map.put("currentLng",longtitude);
                                arraylist.add(map);

                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request);
    }

    //checkInternet connection start -------------
    boolean checkConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }
    //checkInternet connection end -------------

    //load ad start -------------
    public void loadAds() {
        // Java code required.
        // testDevices and loadAdOnCreate attributes are
        // no longer available.
        adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("0B9B9AAF554192C06B891F7B14DD7F7F")
                .build();
        adView.loadAd(adRequest);
        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(AD_ID);
        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
        displayInterstitial();
    }
    //load ad end -------------

    //if use ad you must ad this section start -------------
    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

    // Invoke displayInterstitial() when you are ready to display an interstitial.
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
    //if use ad you must ad this section end -------------

    //onBackPressed start -------------
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                //.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.text_close)
                .setMessage(R.string.text_close_message)
                .setPositiveButton(R.string.text_close_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.text_close_no, null)
                .show();
        if(checkConnection())
            displayInterstitial();
    }
    //onBackPressed end -------------

    //menu start -------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    //.setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(R.string.action_about)
                    .setMessage(R.string.action_about_text)
                    .setNegativeButton(R.string.action_ok, null)
                    .show();
        }
        if (id == R.id.maps){
            startActivity(new Intent(this,MapsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    public void onExit(View view) {
        onBackPressed();
    }

    public void onSettings(View view) {
        startActivity(new Intent(this,SettingsActivity.class));
    }

    public void onInfo(View view) {
        startActivity(new Intent(this,SubActivity.class));
    }
    //menu end -------------
}