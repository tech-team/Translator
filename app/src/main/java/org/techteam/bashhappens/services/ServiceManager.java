package org.techteam.bashhappens.services;

import android.content.Context;
import android.content.Intent;

import org.techteam.bashhappens.api.LanguageEntry;

public final class ServiceManager {
    private final Context context;
    private Intent translationIntent;

    public ServiceManager(final Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void startTranslationService(String text, LanguageEntry fromLang, LanguageEntry toLang) {
        if (translationIntent != null) {
            context.stopService(translationIntent);
        }
        translationIntent = IntentBuilder.translateIntent(context, text, fromLang.getUid(), toLang.getUid());
        context.startService(translationIntent);
    }

    public void startGetLangsService(String locale) {
        context.startService(IntentBuilder.getLangsIntent(context, locale));
    }
}
