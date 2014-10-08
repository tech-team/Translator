package org.techteam.bashhappens.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import org.techteam.bashhappens.R;
import org.techteam.bashhappens.services.BroadcastIntents;
import org.techteam.bashhappens.services.IntentBuilder;
import org.techteam.bashhappens.services.ServiceManager;

import java.util.Locale;

public class SplashActivity extends Activity {
    private LanguageListBroadcastReceiver languageListBroadcastReceiver;
    private ServiceManager serviceManager = new ServiceManager(SplashActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        String locale = Locale.getDefault().getLanguage();
        serviceManager.startGetLangsService(locale);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterBroadcastReceivers();
    }

    private void registerBroadcastReceiver() {
        languageListBroadcastReceiver = new LanguageListBroadcastReceiver();
        IntentFilter languageListIntentFilter = new IntentFilter(
                BroadcastIntents.LANGUAGE_LIST_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(SplashActivity.this)
                .registerReceiver(languageListBroadcastReceiver, languageListIntentFilter);
    }


    private void unregisterBroadcastReceivers() {
        LocalBroadcastManager.getInstance(SplashActivity.this)
                .unregisterReceiver(languageListBroadcastReceiver);
    }

    private void onListFetched(Bundle bundle) {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        for (String key: bundle.keySet()) {
            mainIntent.putExtra(key, bundle.getString(key));
        }
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    private final class LanguageListBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onListFetched(intent.getExtras());
        }
    }
}
