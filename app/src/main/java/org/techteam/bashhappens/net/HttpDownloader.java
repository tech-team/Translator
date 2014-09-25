package org.techteam.bashhappens.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpDownloader {

    class Header {
        String name;
        String value;

        Header(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    public static String httpGet(String url) throws IOException {
        return httpGet(url, null);
    }

    public static String httpGet(String url, List<Header> headers) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        if (headers != null) {
            for (Header h : headers) {
                connection.setRequestProperty(h.name, h.value);
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





    private static String handleInputStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
