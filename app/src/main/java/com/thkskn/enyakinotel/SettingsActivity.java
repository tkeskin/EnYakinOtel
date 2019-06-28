package com.thkskn.enyakinotel;

/**
 * Created by thkskn on 17.03.2015.
 */
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by btk on 5.1.2015.
 */
public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.app_bar,root,false);
        root.addView(toolbar,0);
        toolbar.setNavigationIcon(R.drawable.cast_ic_notification_small_icon);
        toolbar.setTitle(R.string.action_settings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addPreferencesFromResource(R.xml.preferences);

        Preference preferences = findPreference("rateus");
        preferences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.thkskn.enyakinotel"));
                startActivity(intent);
                return false;
            }
        });
        Preference preferences2 = findPreference("allapps");
        preferences2.setOnPreferenceClickListener (new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:thkskn"));
                startActivity(intent);
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}