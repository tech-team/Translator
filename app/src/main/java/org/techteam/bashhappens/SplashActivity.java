package org.techteam.bashhappens;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.techteam.bashhappens.api.API;
import org.techteam.bashhappens.api.LanguageList;
import org.techteam.bashhappens.net.HttpDownloader;
import org.techteam.bashhappens.services.Constants;
import org.techteam.bashhappens.services.LanguageListService;

import java.io.IOException;

public class SplashActivity extends Activity {
    private LanguageListBroadcastReceiver languageListBroadcastReceiver;

    private static final String LOG_TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        languageListBroadcastReceiver = new LanguageListBroadcastReceiver();
        IntentFilter languageListIntentFilter = new IntentFilter(
                Constants.LANGUAGE_LIST_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(languageListBroadcastReceiver, languageListIntentFilter);

        Toast.makeText(this.getBaseContext(), R.string.splash_text, Toast.LENGTH_LONG).show();

        Intent languageListServiceIntent = new Intent(this, LanguageListService.class);
        languageListServiceIntent.putExtra("ui", "ru");
        startService(languageListServiceIntent);

        //new FetchLanguagesAsync().execute();
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

    /*
    private class FetchLanguagesAsync extends AsyncTask<Void, Void, LanguageList> {
        private Throwable exception = null;
        private static final int RETRY_COUNT = 3;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected LanguageList doInBackground(Void... voids) {
            HttpDownloader.Request req = API.REQUEST_BUILDER.getLangsRequest("ru");
            LanguageList list = null;
            for (int i = 0; i < RETRY_COUNT; ++i) {
                Log.d(LOG_TAG, "retry #" + i);
                try {
                    String response = HttpDownloader.httpGet(req);
                    Log.d(LOG_TAG, response);

                    list = LanguageList.fromJsonString(response);
                    if (list != null)
                        break;
                } catch (IOException e) {
                    exception = e;
                }
            }         

            return list;
        }

        @Override
        protected void onPostExecute(LanguageList list) {
            if (exception != null) {
                exception.printStackTrace();
                String text = "Error happened: " + exception.getMessage();
                Toast.makeText(SplashActivity.this.getApplicationContext(), text, Toast.LENGTH_LONG).show();
                onListFetched(list);
            } else {
                onListFetched(list);
            }
        }
    }
    */
}
