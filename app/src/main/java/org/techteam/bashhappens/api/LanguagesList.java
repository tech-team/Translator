package org.techteam.bashhappens.api;


import org.json.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LanguagesList extends ServerResponse {

    private ArrayList<LanguageEntry> langs = new ArrayList<LanguageEntry>();

    private Set<String> dirs = new HashSet<String>();

    public LanguagesList(String e) {
        setException(e);
    }

    private LanguagesList() {

    }

    public static LanguagesList fromJsonString(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            LanguagesList languagesList = new LanguagesList();

            JSONArray dirs = obj.getJSONArray("dirs");
            for (int i = 0; i < dirs.length(); ++i) {
                languagesList.dirs.add(dirs.getString(i));
            }

            JSONObject langs = obj.getJSONObject("langs");
            Iterator<?> keys = langs.keys();

            while (keys.hasNext()) {
                String key = (String)keys.next();
                languagesList.langs.add(
                        new LanguageEntry(
                                (String) langs.get(key),
                                key));
            }

            return languagesList;
        }
        catch (JSONException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public ArrayList<LanguageEntry> getLanguages() {
        return langs;
    }
    public Set<String> getDirections() {
        return dirs;
    }
}

