package org.techteam.translator.api;

import org.json.*;

public class Translation extends ServerResponse {
    public static final int MAX_INPUT_TEXT_LENGTH = 10000;

    private int code;
    private String lang;
    private String text;

    private Translation() {

    }

    public Translation(String e) {
        setException(e);
    }

    public static Translation fromJsonString(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            Translation translation = new Translation();
            translation.code = obj.getInt("code");
            translation.lang = obj.getString("lang");
            StringBuilder text = new StringBuilder();
            JSONArray arr = obj.getJSONArray("text");
            for (int i=0; i<arr.length(); ++i) {
                text.append(arr.getString(i));
            }
            translation.text = text.toString();
            return translation;
        }
        catch (JSONException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public int getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public String getText() {
        return text;
    }
}
