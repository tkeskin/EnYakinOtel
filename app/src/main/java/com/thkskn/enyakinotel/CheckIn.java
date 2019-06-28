package com.thkskn.enyakinotel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckIn extends AppCompatActivity {

    TextView checkedIn;
    KeskinDatabaseAdapter keskinHelper;
    private String data;
    LinearLayout linearLayout;
    /*private View.OnClickListener mSnackBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        linearLayout = (LinearLayout) findViewById(R.id.linear_check);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkedIn = (TextView) findViewById(R.id.check_in_name);
        keskinHelper = new KeskinDatabaseAdapter(this);

        //data = keskinHelper.getAllData();
        data = keskinHelper.getLastRecord();
        checkedIn.setText(data);
        //Message.message(this,data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_in, menu);
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
            //startActivity(new Intent(this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCheckOut(View view) {
        //String test = keskinHelper.getData("Zeynep Sultan Hotel");
        keskinHelper.updateStatu("true","false");
        //Toast.makeText(this, "Çıkış yapıldı!", Toast.LENGTH_LONG).show();
        /*Snackbar.make(linearLayout,"Çıkış yapıldı",9000)
                .setAction("Oteller", mSnackBarListener)
                .setDuration(9000)
                .show();*/
        finish();

    }
}
