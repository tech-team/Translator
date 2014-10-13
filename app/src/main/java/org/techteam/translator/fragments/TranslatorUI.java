package org.techteam.translator.fragments;

import org.techteam.translator.api.LanguageEntry;

public interface TranslatorUI {
    LanguageEntry getFromLanguage();
    LanguageEntry getToLanguage();

    void setFromLanguage(LanguageEntry lang);
    void setToLanguage(LanguageEntry lang);
    void setLanguages(LanguageEntry fromLang, LanguageEntry toLang);

    void setTextToTranslate(String text);
    void setTranslatedText(String text);

    void disableControls();
}
