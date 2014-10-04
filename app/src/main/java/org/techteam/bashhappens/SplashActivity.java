package org.techteam.bashhappens;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.techteam.bashhappens.api.API;
import org.techteam.bashhappens.api.LanguageList;
import org.techteam.bashhappens.net.HttpDownloader;

import java.io.IOException;

public class SplashActivity extends Activity {
    private static final String LOG_TAG = SplashActivity.class.getName();

    private static LanguageList languagesList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Toast.makeText(this.getBaseContext(), R.string.splash_text, Toast.LENGTH_LONG).show();

        new FetchLanguagesAsync().execute();
    }

    private void onListFetched(LanguageList list) {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    public static LanguageList getLanguagesList() {
        return languagesList;
    }

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
}
