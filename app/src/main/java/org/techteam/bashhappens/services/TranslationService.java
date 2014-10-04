package org.techteam.bashhappens.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.techteam.bashhappens.api.API;
import org.techteam.bashhappens.net.HttpDownloader;

import java.io.IOException;

public class TranslationService extends IntentService {

    public TranslationService() {
        super("TranslationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String text = intent.getStringExtra("text");
        String lang = intent.getStringExtra("lang");

        HttpDownloader.Request request = API.REQUEST_BUILDER.translateRequest(text, lang);
        Intent localIntent = new Intent(Constants.TRANSLATE_BROADCAST_ACTION);
        try {
            String response = HttpDownloader.httpGet(request);

            localIntent.putExtra("data", response);

            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }
        catch (IOException exc) {
            localIntent.putExtra("data", (String)null)
                       .putExtra("exception", exc.getMessage());
        }
        finally {
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }
    }
}