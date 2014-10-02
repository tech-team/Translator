package org.techteam.bashhappens.api;

public class LanguageEntry {
    private String name;
    private String uid;

    public LanguageEntry(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
}
