package org.techteam.bashhappens.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class HttpDownloader {

    public static class Request {
        private String url;
        private List<UrlParams> params;
        private List<Header> headers;

        public Request(String url, List<UrlParams> params, List<Header> headers) {
            this.url = url;
            this.params = params;
            this.headers = headers;
        }

        public String getUrl() {
            return url;
        }

        public List<UrlParams> getParams() {
            return params;
        }

        public List<Header> getHeaders() {
            return headers;
        }
    }

    public static String httpGet(String url) throws IOException {
        return httpGet(url, null, null);
    }

    public static String httpGet(Request request) throws IOException {
        return httpGet(request.getUrl(), request.getParams(), request.getHeaders());
    }

    public static String httpGet(String url, List<UrlParams> params, List<Header> headers) throws IOException {
        URL urlObj = constructUrl(url, params);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();

        connection.setRequestMethod("GET");
        if (headers != null) {
            for (Header h : headers) {
                connection.setRequestProperty(h.getName(), h.getValue());
            }
        }
        connection.connect();

        String res = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream in = connection.getInputStream();
            res = handleInputStream(in);
        }
        connection.disconnect();
        return res;
    }

    private static URL constructUrl(String url, List<UrlParams> params) throws MalformedURLException {
        if (params == null || params.isEmpty()) {
            return new URL(url);
        }
        String newUrl = url + "?";
        for (UrlParams p : params) {
            newUrl += p.getKey() + "=" + p.getValue() + "&";
        }

        return new URL(newUrl.substring(0, newUrl.length() - 1));
    }


    private static String handleInputStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
