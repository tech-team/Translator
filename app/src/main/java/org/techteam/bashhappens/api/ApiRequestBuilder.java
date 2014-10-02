package org.techteam.bashhappens.api;

import org.techteam.bashhappens.net.HttpDownloader.Request;
import org.techteam.bashhappens.net.UrlParams;

import java.util.LinkedList;
import java.util.List;

public class ApiRequestBuilder {
    private String apiKey;

    private class Urls {
        public static final String GET_LANGS_URL =
                "https://translate.yandex.net/api/v1.5/tr.json/getLangs";
        public static final String TRANSLATE_URL =
                "https://translate.yandex.net/api/v1.5/tr.json/translate";
    }

    public ApiRequestBuilder(String apiKey) {
        this.apiKey = apiKey;
    }

    public Request getLangsRequest(String ui) {
        List<UrlParams> params = new LinkedList<UrlParams>();
        params.add(new UrlParams("key", apiKey));
        params.add(new UrlParams("ui", ui));
        return new Request(Urls.GET_LANGS_URL, params, null);
    }

    public Request translateRequest(String text, String lang) {
        List<UrlParams> params = new LinkedList<UrlParams>();
        params.add(new UrlParams("key", apiKey));
        params.add(new UrlParams("text", text));
        params.add(new UrlParams("lang", lang));
        return new Request(Urls.TRANSLATE_URL, params, null);
    }
}
