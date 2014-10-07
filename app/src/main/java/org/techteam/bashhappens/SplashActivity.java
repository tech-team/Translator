package org.techteam.bashhappens;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.techteam.bashhappens.services.BroadcastIntents;
import org.techteam.bashhappens.services.IntentBuilder;

public class SplashActivity extends Activity {
    private LanguageListBroadcastReceiver languageListBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        registerBroadcastReceiver();

        startService(IntentBuilder.getLangsIntent(SplashActivity.this, "ru"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void registerBroadcastReceiver() {
        languageListBroadcastReceiver = new LanguageListBroadcastReceiver();
        IntentFilter languageListIntentFilter = new IntentFilter(
                BroadcastIntents.LANGUAGE_LIST_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(SplashActivity.this)
                .registerReceiver(languageListBroadcastReceiver, languageListIntentFilter);
    }

    private void onListFetched(Bundle bundle) {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        for (String key: bundle.keySet()) {
            mainIntent.putExtra(key, bundle.getString(key));
        }
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(SplashActivity.this)
                .unregisterReceiver(languageListBroadcastReceiver);
    }

    private final class LanguageListBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onListFetched(intent.getExtras());
        }
    }
}
