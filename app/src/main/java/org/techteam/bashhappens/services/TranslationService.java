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
        Intent localIntent = new Intent(BroadcastIntents.TRANSLATE_BROADCAST_ACTION);
        try {
            String response = HttpDownloader.httpPost(request);

            localIntent.putExtra(ResponseKeys.DATA, response);
        }
        catch (IOException exc) {
            localIntent.putExtra(ResponseKeys.DATA, (String)null)
                       .putExtra(ResponseKeys.ERROR, exc.getMessage());
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
