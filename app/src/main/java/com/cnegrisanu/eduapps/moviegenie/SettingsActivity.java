package com.cnegrisanu.eduapps.moviegenie;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by vollulin on 7/12/2015.
 * Movie Genie App for the Udacity Android Nanodegree Course
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();
    }
}
