package com.example.parthivnaresh.myfirstapp.NewsFeed;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class StockNewsObject {

    private String title, summary, link;

    public StockNewsObject(String title, String summary, String link) {
        this.title = title;
        this.summary = summary;
        this.link = link;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getLink() { return this.link; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
