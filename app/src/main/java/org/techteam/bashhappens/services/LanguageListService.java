package org.techteam.bashhappens.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.techteam.bashhappens.api.API;
import org.techteam.bashhappens.net.HttpDownloader;

import java.io.IOException;

public class LanguageListService extends IntentService {

    public LanguageListService() {
        super("LanguageListService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String ui = intent.getStringExtra("ui");

        HttpDownloader.Request request = API.REQUEST_BUILDER.getLangsRequest(ui);
        Intent localIntent = new Intent(Constants.LANGUAGE_LIST_BROADCAST_ACTION);
        try {
            String response = HttpDownloader.httpGet(request);
            System.out.println(response);

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
