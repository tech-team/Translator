package org.techteam.bashhappens.api;


import org.json.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LanguagesList extends ServerResponse {

    private ArrayList<LanguageEntry> langs = new ArrayList<LanguageEntry>();
    private Set<String> dirs = new HashSet<String>();

    public LanguagesList(String e) {
        setException(e);
    }

    private LanguagesList() {

    }

    public static LanguagesList fromJsonString(String json) {
        if (json == null) {
            return null;
        }

        try {
            JSONObject obj = new JSONObject(json);
            LanguagesList languagesList = new LanguagesList();

            JSONObject langs = obj.getJSONObject("langs");
            Iterator<?> keys = langs.keys();

            while (keys.hasNext()) {
                String key = (String)keys.next();
                languagesList.langs.add(
                        new LanguageEntry(
                                (String) langs.get(key),
                                key));
            }

            Collections.sort(languagesList.langs);


            JSONArray dirsJson = obj.getJSONArray("dirs");
            for (int i = 0; i < dirsJson.length(); ++i) {
                String dir = dirsJson.getString(i);
                languagesList.dirs.add(dir);
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

