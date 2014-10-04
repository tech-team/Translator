package org.techteam.bashhappens;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import org.techteam.bashhappens.services.Constants;
import org.techteam.bashhappens.services.IntentBuilder;
import org.techteam.bashhappens.services.LanguageListService;

public class SplashActivity extends Activity {
    private LanguageListBroadcastReceiver languageListBroadcastReceiver;

    private static final String LOG_TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        registerBroadcastReceiver();

        Toast.makeText(this.getBaseContext(), R.string.splash_text, Toast.LENGTH_LONG).show();

        startService(IntentBuilder.getLangsIntent(SplashActivity.this, "ru"));
    }

    private void registerBroadcastReceiver() {
        languageListBroadcastReceiver = new LanguageListBroadcastReceiver();
        IntentFilter languageListIntentFilter = new IntentFilter(
                Constants.LANGUAGE_LIST_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this)
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
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(languageListBroadcastReceiver);
    }

    private final class LanguageListBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onListFetched(intent.getExtras());
        }
    }
}
