package org.techteam.bashhappens.api;


import org.json.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LanguagesList extends ServerResponse {

    private ArrayList<LanguageEntry> langs = new ArrayList<LanguageEntry>();
    private Set<String> dirs = new HashSet<String>();
    private Map<LanguageEntry, ArrayList<LanguageEntry>> fromLangs = new HashMap<LanguageEntry, ArrayList<LanguageEntry>>();
    private Map<LanguageEntry, ArrayList<LanguageEntry>> toLangs = new HashMap<LanguageEntry, ArrayList<LanguageEntry>>();

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

            Collections.sort(languagesList.langs);

            for (String dir: languagesList.dirs) {
                String[] langPair = dir.split("-");
                LanguageEntry fromLang = new LanguageEntry(
                                            (String) langs.get(langPair[0]),
                                            langPair[0]);
                LanguageEntry toLang = new LanguageEntry(
                        (String) langs.get(langPair[1]),
                        langPair[1]);

                if (languagesList.getFromLangs().containsKey(fromLang)) {
                    languagesList.getFromLangs().get(fromLang).add(toLang);
                }
                else {
                    languagesList.getFromLangs().put(fromLang, new ArrayList<LanguageEntry>());
                }

                if (languagesList.getToLangs().containsKey(toLang)) {
                    languagesList.getToLangs().get(toLang).add(fromLang);
                }
                else {
                    languagesList.getToLangs().put(toLang, new ArrayList<LanguageEntry>());
                }
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

    public Map<LanguageEntry, ArrayList<LanguageEntry>> getFromLangs() {
        return fromLangs;
    }

    public Map<LanguageEntry, ArrayList<LanguageEntry>> getToLangs() {
        return toLangs;
    }
}

