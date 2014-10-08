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
    private Map<String, String> langsMap = new HashMap<String, String>();
    private ArrayList<LanguageEntry> langs = new ArrayList<LanguageEntry>();
    private Set<String> dirs = new HashSet<String>();
    private String rawJson = null;

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
            languagesList.rawJson = json;

            JSONObject langs = obj.getJSONObject("langs");
            Iterator<?> keys = langs.keys();

            while (keys.hasNext()) {
                String uid = (String) keys.next();
                String name = (String) langs.get(uid);
                languagesList.langs.add(new LanguageEntry(name, uid));
                languagesList.langsMap.put(uid, name);
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
    public String getLanguageName(String uid) {
        return langsMap.get(uid);
    }
    public Set<String> getDirections() {
        return dirs;
    }

    public String getRawJson() {
        return rawJson;
    }
}

