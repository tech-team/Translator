package org.techteam.bashhappens.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.techteam.bashhappens.api.API;
import org.techteam.bashhappens.net.HttpDownloader;

import java.io.IOException;

public class LanguageListService extends IntentService {

    private static final String NAME = LanguageListService.class.getName();

    public LanguageListService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String ui = intent.getStringExtra("ui");

        HttpDownloader.Request request = API.REQUEST_BUILDER.getLangsRequest(ui);
        Intent localIntent = new Intent(BroadcastIntents.LANGUAGE_LIST_BROADCAST_ACTION);
        try {
            String response = HttpDownloader.httpGet(request);

            localIntent.putExtra(ResponseKeys.DATA, response);
        }
        catch (IOException exc) {
            localIntent.putExtra(ResponseKeys.DATA, (String) null)
                       .putExtra(ResponseKeys.ERROR, exc.getMessage());
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
