package com.thkskn.enyakinotel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thkskn.enyakinotel.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	//ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();

	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		//imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView counter;
		TextView name;
		TextView rating;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.listview_item, parent, false);
		// Get the position
		resultp = data.get(position);

		// Locate the TextViews in listview_item.xml
		counter = (TextView) itemView.findViewById(R.id.counter);
		name = (TextView) itemView.findViewById(R.id.name);
		rating = (TextView) itemView.findViewById(R.id.rating);

		// Locate the ImageView in listview_item.xml
		//flag = (ImageView) itemView.findViewById(R.id.flag);

		// Capture position and set results to the TextViews
		counter.setText(resultp.get(MainActivity.COUNTER));
		name.setText(resultp.get(MainActivity.NAME));
		rating.setText(resultp.get(MainActivity.RATING));

		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class
		//imageLoader.DisplayImage(resultp.get(MainActivity.FLAG), flag);
		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Get the position
				resultp = data.get(position);
				Intent intent = new Intent(context, SingleItemView.class);

				// Pass all data name
				intent.putExtra("name", resultp.get(MainActivity.NAME));
				// Pass all data rating
				intent.putExtra("rating", resultp.get(MainActivity.RATING));
				// Pass all data vicinity
				intent.putExtra("vicinity", resultp.get(MainActivity.VICINITY));
				intent.putExtra("place_id", resultp.get(MainActivity.PLACEID));

				intent.putExtra("lat", resultp.get(MainActivity.LAT));
				intent.putExtra("lng", resultp.get(MainActivity.LONG));

				intent.putExtra("currentLat",resultp.get(MainActivity.CURRENTLAT));
				intent.putExtra("currentLng",resultp.get(MainActivity.CURRENTLNG));

				// Pass all data flag
				//intent.putExtra("flag", resultp.get(MainActivity.FLAG));
				// Start SingleItemView Class
				context.startActivity(intent);

			}
		});
		return itemView;
	}
}