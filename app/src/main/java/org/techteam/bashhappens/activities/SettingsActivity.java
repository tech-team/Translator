package org.techteam.bashhappens.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import org.techteam.bashhappens.fragments.SettingsFragment;

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
