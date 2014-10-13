package org.techteam.translator.services;

import android.content.Context;
import android.content.Intent;

public abstract class IntentBuilder {
    public static Intent getLangsIntent(Context context, String ui) {
        Intent intent = new Intent(context, LanguageListService.class);
        intent.putExtra("ui", ui);
        return intent;
    }

    public static Intent translateIntent(Context context, String text, String langFrom, String langTo) {
        Intent intent = new Intent(context, TranslationService.class);
        intent.putExtra("text", text);
        intent.putExtra("lang", langFrom + "-" + langTo);
        return intent;
    }

    public static Intent translateIntent(Context context, String text, String langTo) {
        Intent intent = new Intent(context, TranslationService.class);
        intent.putExtra("text", text);
        intent.putExtra("lang", langTo);
        return intent;
    }
}
