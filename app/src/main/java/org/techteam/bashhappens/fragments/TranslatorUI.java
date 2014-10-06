package org.techteam.bashhappens.fragments;

import org.techteam.bashhappens.api.LanguageEntry;

public interface TranslatorUI {
    LanguageEntry getFromLanguage();
    LanguageEntry getToLanguage();

    void setFromLanguage(LanguageEntry lang);
    void setToLanguage(LanguageEntry lang);

    void setTextToTranslate(String text);
    void setTranslatedText(String text);

    void disableControls();
}
