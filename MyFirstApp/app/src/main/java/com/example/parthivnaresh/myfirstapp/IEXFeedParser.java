package com.example.parthivnaresh.myfirstapp;

import com.intrinio.realtime.Quote;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class IEXFeedParser {

    private String type;
    private JSONObject call_info;

    DecimalFormat twodecimalplaces = new DecimalFormat("#0.00");

    public IEXFeedParser(Quote quote) {
        String current_quote = quote.toString()
                .substring(quote.toString().indexOf("("))
                .replace("(", "{")
                .replace(")", "}");
        try {
            this.call_info = new JSONObject(current_quote);
            this.type = this.call_info.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String LastPrice() throws JSONException {
        Double lastPrice = Double.valueOf(this.call_info.getString("price"));
        return twodecimalplaces.format(lastPrice);
    }

    protected String BidPrice() throws JSONException {
        return this.call_info.getString("price");
    }

    protected String AskPrice() throws JSONException {
        return this.call_info.getString("price");
    }

    public String CurrentTicker() throws JSONException {
        return this.call_info.getString("ticker");
    }

    protected String getCall() {
        return this.call_info.toString();
    }

    public String getCallType() throws JSONException {
        return this.call_info.getString("type");
    }
}
