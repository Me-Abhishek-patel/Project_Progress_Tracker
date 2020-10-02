package com.example.projectprogresstracker.Ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.projectprogresstracker.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings);
    }
}