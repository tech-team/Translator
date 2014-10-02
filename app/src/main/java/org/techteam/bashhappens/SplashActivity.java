package org.techteam.bashhappens;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.techteam.bashhappens.api.API;
import org.techteam.bashhappens.api.LanguageList;
import org.techteam.bashhappens.net.HttpDownloader;

import java.io.IOException;

public class SplashActivity extends Activity {
    private static final String LOG_TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new FetchLanguagesAsync().execute();
    }

    private void onListFetched(LanguageList list) {
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    private class FetchLanguagesAsync extends AsyncTask<Void, Void, LanguageList> {
        private Throwable exception = null;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected LanguageList doInBackground(Void... voids) {
            HttpDownloader.Request req = API.REQUEST_BUILDER.getLangsRequest("ru");
            try {
                String response = HttpDownloader.httpGet(req);
                Log.d(LOG_TAG, response);

                return LanguageList.fromJsonString(response);
            } catch (IOException e) {
                exception = e;
            }

            return null;
        }

        @Override
        protected void onPostExecute(LanguageList list) {
            if (exception != null) {
                exception.printStackTrace();
//                String text = "Error happened: " + exception.getMessage();
//                Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
            } else {
                onListFetched(list);
//                String text = "Done!";
//                Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
            }
        }
    }
}
