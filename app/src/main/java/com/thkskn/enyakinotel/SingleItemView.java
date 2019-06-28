package com.thkskn.enyakinotel;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleItemView extends AppCompatActivity {
	// Declare Variables
	String name;
	String rating;
	String vicinity;
	String place_id;
	String lat;
	String lng;
	String currentLat;
	String currentLng;
	private TextView time;
	final String GOOGLE_KEY = "AIzaSyAD_BXLA58RZf8yEWSJAV7OxwgpLbiQn68";

	private String phone_number;
	KeskinDatabaseAdapter keskinHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from singleitemview.xml
		setContentView(R.layout.single_item_view);

		Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		keskinHelper = new KeskinDatabaseAdapter(this);

		Intent i = getIntent();
		// Get the result of name
		name = i.getStringExtra("name");
		// Get the result of rating
		rating = i.getStringExtra("rating");
		// Get the result of vicinity
		vicinity = i.getStringExtra("vicinity");
		place_id = i.getStringExtra("place_id");
		lat = i.getStringExtra("lat");
		lng = i.getStringExtra("lng");

		currentLat = i.getStringExtra("currentLat");
		currentLng = i.getStringExtra("currentLng");

		// Locate the TextViews in singleitemview.xml
		TextView txtname= (TextView) findViewById(R.id.counter);
		TextView txtadress = (TextView) findViewById(R.id.adress);
		time = (TextView) findViewById(R.id.time);

		// Set results to the TextViews
		txtname.setText(name);
		txtadress.setText(vicinity);

		calculateDistanceTwoLocation();
		getPhoneNumber();
	}

	private void getPhoneNumber() {
		// URL Address
		String feedUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key=";
		feedUrl = feedUrl + GOOGLE_KEY;
		//Log.d("Single : ", feedUrl);

		// Create an array
		JsonObjectRequest request = new JsonObjectRequest  (feedUrl,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
							try{
								phone_number = response.getJSONObject("result").getString("international_phone_number");
								Log.d("phone_number",phone_number);
							}catch(JSONException e){
								e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
		AppController.getInstance().addToRequestQueue(request);
	}
	public void calculateDistanceTwoLocation(){

		/*Location locationA = new Location("point A");

		locationA.setLatitude(Double.parseDouble(currentLat));
		locationA.setLongitude(Double.parseDouble(currentLng));

		Location locationB = new Location("point B");

		locationB.setLatitude(Double.parseDouble(lat));
		locationB.setLongitude(Double.parseDouble(lng));

		float distance = locationA.distanceTo(locationB);*/
		//time.setText(String.valueOf(distance));

		// URL Address
		String feedUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+Double.parseDouble(currentLat)+","+Double.parseDouble(currentLng)+
				"&destinations="+Double.parseDouble(lat)+","+Double.parseDouble(lng)+"&mode=driving&language=tr-TR";
		Log.d("Distance",feedUrl);

		// Create an array
		JsonObjectRequest request = new JsonObjectRequest  (feedUrl,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
							try{
								JSONArray rowsArray =response.getJSONArray("rows");
								JSONObject rows = rowsArray.getJSONObject(0);

								JSONArray elementsArray = rows.getJSONArray("elements");
								JSONObject newDisTimeOb = elementsArray.getJSONObject(0);

								JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                                JSONObject durationOb = newDisTimeOb.getJSONObject("duration");
								time.setText(distOb.getString("text")+"\n"+
                                "Yaklaşık "+durationOb.getString("text")+" sonra hedeftesiniz");
							}catch(JSONException e){
								e.printStackTrace();
							}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
		AppController.getInstance().addToRequestQueue(request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_single, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == android.R.id.home) {
			finish();
		}
		if(id == R.id.call){
			try {
				Intent my_callIntent = new Intent(Intent.ACTION_CALL);
				my_callIntent.setData(Uri.parse("tel:" + phone_number));
				//here the word 'tel' is important for making a call...
				startActivity(my_callIntent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}

		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	public void routeMaps(View view) {
        String uri = String.format("google.navigation:q="+Double.parseDouble(lat)+","+Double.parseDouble(lng));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx) {
                Toast.makeText(this, "Lütfen Google Maps uygulamasını edinin", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onCancel(View view) {
        finish();
    }

    public void onCheckIn(View view) {

		if(keskinHelper.getAllStatu().contains("true")){
			Toast.makeText(this,"Yeni check-in yapılamaz",Toast.LENGTH_LONG).show();
		}
		else {
			switch ((int) Double.parseDouble(rating)) {
                case 5:
                    long id = keskinHelper.insertData(name, "100.50", "true", "1");
                    if (id < 0) Message.message(this, "Fail");
                    else Message.message(this, "Check-in yapıldı!");
                    break;

				case 4:
					long id1 = keskinHelper.insertData(name, "70", "true", "1");
					if (id1 < 0) Message.message(this, "Fail");
					else Message.message(this, "Check-in yapıldı!");
					break;
				case 3:
					long id2 = keskinHelper.insertData(name, "45", "true", "1");
					if (id2 < 0) Message.message(this, "Fail");
					else Message.message(this, "Check-in yapıldı!");
					break;
				default:
					break;
			}
		}
        startActivity(new Intent(this, CheckIn.class));
        finish();
    }
}