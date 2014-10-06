package org.techteam.bashhappens.api;


import org.json.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LanguagesList extends ServerResponse {

    private ArrayList<LanguageEntry> langs = new ArrayList<LanguageEntry>();
    private Set<String> dirs = new HashSet<String>();

    private ArrayList<LanguageEntry> fromLangs = new ArrayList<LanguageEntry>();
    private ArrayList<LanguageEntry> toLangs = new ArrayList<LanguageEntry>();
    private Map<LanguageEntry, ArrayList<LanguageEntry>> fromToLangs = new TreeMap<LanguageEntry, ArrayList<LanguageEntry>>();
    private Map<LanguageEntry, ArrayList<LanguageEntry>> toFromLangs = new TreeMap<LanguageEntry, ArrayList<LanguageEntry>>();

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

//            for (String dir: languagesList.dirs) {
//                String[] langPair = dir.split("-");
//                LanguageEntry fromLang = new LanguageEntry(
//                                            (String) langs.get(langPair[0]),
//                                            langPair[0]);
//                LanguageEntry toLang = new LanguageEntry(
//                        (String) langs.get(langPair[1]),
//                        langPair[1]);
//
//                languagesList.fromLangs.add(fromLang);
//                languagesList.toLangs.add(toLang);
//
//
//                if (!languagesList.fromToLangs.containsKey(fromLang)) {
//                    languagesList.fromToLangs.put(fromLang, new ArrayList<LanguageEntry>());
//                }
//                languagesList.fromToLangs.get(fromLang).add(toLang);
//
//
//                if (!languagesList.toFromLangs.containsKey(toLang)) {
//                    languagesList.toFromLangs.put(toLang, new ArrayList<LanguageEntry>());
//                }
//                languagesList.toFromLangs.get(toLang).add(fromLang);
//
//            }

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

    public ArrayList<LanguageEntry> getFromLangs() {
        return fromLangs;
    }

    public ArrayList<LanguageEntry> getFromLangs(LanguageEntry toLang) {
        return toFromLangs.get(toLang);
    }

    public ArrayList<LanguageEntry> getToLangs() {
        return toLangs;
    }

    public ArrayList<LanguageEntry> getToLangs(LanguageEntry fromLang) {
        return fromToLangs.get(fromLang);
    }
}

