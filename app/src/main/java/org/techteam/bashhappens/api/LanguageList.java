package org.techteam.bashhappens.api;


import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LanguageList {

    private List<LanguageEntry> langs = new ArrayList<LanguageEntry>();

    //private Map<String, String> langs = new HashMap<String, String>();
    private Set<String> dirs = new HashSet<String>();

    private LanguageList() {

    }

    public static LanguageList fromJsonString(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            LanguageList languageList = new LanguageList();

            JSONArray dirs = obj.getJSONArray("posts");
            for (int i=0; i<dirs.length(); ++i) {
                languageList.dirs.add(dirs.getString(i));
            }

            JSONObject langs = obj.getJSONObject("langs");
            Iterator<?> keys = langs.keys();

            while (keys.hasNext()) {
                String key = (String)keys.next();
                languageList.langs.add(
                        new LanguageEntry(
                                key,
                                (String) langs.get(key)));
            }

            return languageList;
        }
        catch (JSONException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public List<LanguageEntry> getLanguages() {
        return langs;
    }
    public Set<String> getDirections() {
        return dirs;
    }
}
