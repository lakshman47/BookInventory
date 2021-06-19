package com.example.bookinventory.Preference;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.example.bookinventory.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPreferences;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preference);
        sharedPreferences = getPreferenceManager().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
    }
}
