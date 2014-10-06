package org.techteam.bashhappens.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class LanguageEntry implements Parcelable, Comparable<LanguageEntry> {
    private String name;
    private String uid;

    public LanguageEntry(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public LanguageEntry(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.name = data[0];
        this.uid = data[1];
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.name,
                this.uid
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public LanguageEntry createFromParcel(Parcel in) {
            return new LanguageEntry(in);
        }

        public LanguageEntry[] newArray(int size) {
            return new LanguageEntry[size];
        }
    };

    @Override
    public int compareTo(LanguageEntry languageEntry) {
        return this.getName().compareTo(languageEntry.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LanguageEntry)) {
            return false;
        }
        else {
            return (this.getName().equals(((LanguageEntry) obj).getName())
                    && this.getUid().equals(((LanguageEntry) obj).getUid()));
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
