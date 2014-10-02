package org.techteam.bashhappens.fragments;

public class LanguagesList {
    private static final String URL = "https://translate.yandex.net/api/v1.5/tr.json/getLangs";
    private String apiKey;
    private String ui;


    public LanguagesList(String apiKey, String ui) {
        this.apiKey = apiKey;
        this.ui = ui;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUi() {
        return ui;
    }

    public String makeRequest() {
        return null;
    }
}
