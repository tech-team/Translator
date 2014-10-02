package org.techteam.bashhappens;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.techteam.bashhappens.api.API;
import org.techteam.bashhappens.api.LanguagesList;
import org.techteam.bashhappens.net.HttpDownloader;

import java.io.IOException;

public class SplashActivity extends Activity {
    private static final String LOG_TAG = SplashActivity.class.getName();

    private static LanguagesList languagesList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new FetchLanguagesAsync().execute();
    }

    private void onListFetched(LanguagesList list) {
        languagesList = list;
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    public static LanguagesList getLanguagesList() {
        return languagesList;
    }

    private class FetchLanguagesAsync extends AsyncTask<Void, Void, LanguagesList> {
        private Throwable exception = null;
        private static final int RETRY_COUNT = 3;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected LanguagesList doInBackground(Void... voids) {
            HttpDownloader.Request req = API.REQUEST_BUILDER.getLangsRequest("ru");

            LanguagesList list = null;

            for (int i = 0; i < RETRY_COUNT; ++i) {
                Log.d(LOG_TAG, "retry #" + i);
                try {
                    String response = HttpDownloader.httpGet(req);
                    Log.d(LOG_TAG, response);

                    list = LanguagesList.fromJsonString(response);
                    if (list != null)
                        break;
                } catch (IOException e) {
                    exception = e;
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(LanguagesList list) {
            if (exception != null) {
                exception.printStackTrace();
                String text = "Error happened: " + exception.getMessage();
                Toast.makeText(SplashActivity.this.getApplicationContext(), text, Toast.LENGTH_LONG).show();
            } else {
                onListFetched(list);
            }
        }
    }
}
