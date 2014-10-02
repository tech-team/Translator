package org.techteam.bashhappens.fragments;


import org.json.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LanguagesList {

    private Map<String, String> langs = new HashMap<String, String>();
    private Set<String> dirs = new HashSet<String>();

    private LanguagesList() {

    }

    public static LanguagesList fromJsonString(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            LanguagesList languagesList = new LanguagesList();

            JSONArray dirs = obj.getJSONArray("posts");
            for (int i=0; i<dirs.length(); ++i) {
                languagesList.dirs.add(dirs.getString(i));
            }

            JSONObject langs = obj.getJSONObject("langs");
            Iterator<?> keys = langs.keys();

            while (keys.hasNext()) {
                String key = (String)keys.next();
                languagesList.langs.put(key, (String) langs.get(key));
            }

            return languagesList;
        }
        catch (JSONException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public Map<String, String> getLanguages() {
        return langs;
    }

    public Set<String> getDirections() {
        return dirs;
    }
}

