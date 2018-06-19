package com.example.parthivnaresh.myfirstapp.NewsFeed;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class APICall_NewsFeed extends JSONObject {

    private String ticker, encoding;
    private String intrinio_username = "6830329235637b2de2961364c5a70952";
    private String intrinio_password = "0b5ee0ad9abdd4dbf3f1c16ac516af5e";

    private String urlBase = "https://api.intrinio.com/news?identifier=";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public APICall_NewsFeed(String ticker) {
        this.ticker = ticker;

        String userPassword = intrinio_username + ":" + intrinio_password;
        this.encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());
    }

    public JSONArray getNewsList() throws IOException {
        JSONArray newsfeed = jsonParser("10");
        System.out.println(newsfeed.toString() + " PANDA NEWSFEED");
        return newsfeed;
    }

    protected JSONArray jsonParser(String pagesize) throws IOException {

        URL url = new URL(urlBase + this.ticker + "&page_size=" + pagesize);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + this.encoding);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isw = new InputStreamReader(is);
        JSONObject output;
        JSONArray latest_news = null;
        try {
            StringBuilder sb = new StringBuilder();
            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                sb.append(current);
                data = isw.read();
            }
            String jsonText = sb.toString();
            output = new JSONObject(jsonText);
            latest_news = output.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return latest_news;
    }
}
