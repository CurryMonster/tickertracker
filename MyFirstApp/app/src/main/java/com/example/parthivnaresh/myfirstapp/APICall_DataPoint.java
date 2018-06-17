package com.example.parthivnaresh.myfirstapp;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Base64;

public class APICall_DataPoint extends JSONObject {

    private String ticker, encoding;
    private String intrinio_username = "6830329235637b2de2961364c5a70952";
    private String intrinio_password = "0b5ee0ad9abdd4dbf3f1c16ac516af5e";

    private String urlBase = "https://api.intrinio.com/data_point?identifier=";
    DecimalFormat twoplacesafterdecimal = new DecimalFormat("###,##0.00");

    InputStream is;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public APICall_DataPoint(String ticker) {
        this.ticker = ticker;

        String userPassword = intrinio_username + ":" + intrinio_password;
        this.encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());
    }

    public String formatLargeNumber(String value_to_format) throws IOException {
        String formatted_value = null;
        try {
            value_to_format = String.valueOf(Math.abs(Double.valueOf(value_to_format)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Boolean isBillions = Double.valueOf(value_to_format) / 1000000000 > 1.0;
        Boolean isMillions = Double.valueOf(value_to_format) / 1000000 > 1.0;

        Double aDouble;

        if (isBillions) {
            aDouble = Double.valueOf(value_to_format) / 1000000000;
            formatted_value = twoplacesafterdecimal.format(aDouble) + "B";
        } else if (isMillions) {
            aDouble = Double.valueOf(value_to_format) / 1000000;
            formatted_value = twoplacesafterdecimal.format(aDouble) + "M";
        } else {
            return value_to_format;
        }
        return formatted_value;
    }

    public String getPercentChange() throws IOException {
        String percent_change = jsonParser("percent_change");
        if (percent_change.equals("n/a")) {
            return "n/a";
        }
        return twoplacesafterdecimal.format(Double.valueOf(percent_change)*100);
    }

    public String getPriceChange() throws IOException {
        String price_change = jsonParser("change");
        if (price_change.equals("n/a")) {
            return "n/a";
        }
        return twoplacesafterdecimal.format(Double.valueOf(price_change));
    }

    public String getTotalRevenue() throws IOException {
        String total_revenue = jsonParser("totalrevenue");

        if (total_revenue.equals("n/a")) {
            return "n/a";
        } else {
            return formatLargeNumber(total_revenue);
        }
    }

    public String getCostOfRevenue() throws IOException {
        String total_cost_of_revenue = jsonParser("totalcostofrevenue");

        if (total_cost_of_revenue.equals("n/a")) {
            return "n/a";
        } else {
            return formatLargeNumber(total_cost_of_revenue);
        }
    }

    public String getOperatingExpense() throws IOException {
        String operating_expense = jsonParser("totaloperatingexpenses");

        if (operating_expense.equals("n/a")) {
            return "n/a";
        } else {
            return formatLargeNumber(operating_expense);
        }
    }

    public String getNetIncome() throws IOException {
        String net_income = jsonParser("netincome");

        if (net_income.equals("n/a")) {
            return "n/a";
        } else {
            return formatLargeNumber(net_income);
        }
    }

    public String getRevenueGrowth() throws IOException {
        String revenue_growth = jsonParser("revenuegrowth");

        if (revenue_growth.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(revenue_growth) * 100);
        }
    }

    public String getRevenueQOQGrowth() throws IOException {
        String revenue_growth_qoq = jsonParser("revenueqoqgrowth");

        if (revenue_growth_qoq.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(revenue_growth_qoq) * 100);
        }
    }

    public String getEV() throws IOException {
        String enterprise_value = jsonParser("enterprisevalue");

        if (enterprise_value.equals("n/a")) {
            return "n/a";
        } else {
            return formatLargeNumber(enterprise_value);
        }
    }

    public String getMarketCap() throws JSONException, IOException {
        String market_cap = jsonParser("marketcap");

        if (market_cap.equals("n/a")) {
            try {
                return String.valueOf(Double.valueOf(getSharesOutstanding()) * Double.valueOf(getLastPrice()));
            } catch (NumberFormatException e) {
                return "n/a";
        }
        }
        return formatLargeNumber(market_cap);
    }

    public String getLastPrice() throws IOException {
        String last_price = jsonParser("last_price");

        if (last_price.equals("n/a")) {
            return "n/a";
        }

        return twoplacesafterdecimal.format(Double.valueOf(last_price));
    }

    public String getSharesOutstanding() throws IOException {
        String basic_shares_outstanding = jsonParser("weightedavebasicdilutedsharesos");

        if (basic_shares_outstanding.equals("n/a")) {
            return "n/a";
        } else {
            return formatLargeNumber(basic_shares_outstanding);
        }
    }

    public String getPE() throws IOException {
        String price_earnings = jsonParser("pricetoearnings");

        if (price_earnings.equals("n/a")) {
            return "n/a";
        } else {
            return price_earnings;
        }
    }

    public String getPRevenue() throws IOException {
        String price_to_revenue = jsonParser("pricetorevenue");

        if (price_to_revenue.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(price_to_revenue));
        }
    }

    public String getPBook() throws IOException {
        String price_to_book = jsonParser("pricetobook");

        if (price_to_book.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(price_to_book));
        }
    }

    public String getEVRevenue() throws IOException {
        String ev_revenue = jsonParser("evtorevenue");

        if (ev_revenue.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(ev_revenue));
        }
    }

    public String getEVEBITDA() throws IOException {
        String ev_ebitda = jsonParser("evtoebitda");

        if (ev_ebitda.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(ev_ebitda));
        }
    }

    public String getGrossMargin() throws IOException {
        String gross_margin = jsonParser("grossmargin");

        if (gross_margin.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(gross_margin)*100);
        }
    }

    public String getOperatingMargin() throws IOException {
        String operating_margin = jsonParser("operatingmargin");

        if (operating_margin.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(operating_margin)*100);
        }
    }

    public String getBasicEPS() throws IOException {
        String basic_eps = jsonParser("basiceps");

        if (basic_eps.equals("n/a")) {
            return "n/a";
        } else {
            return basic_eps;
        }
    }

    public String getBidPrice() throws IOException {
        String bid_price = jsonParser("bid_price");

        if (bid_price.equals("n/a")) {
            return "n/a";
        } else {
            return bid_price;
        }
    }

    public String getAskPrice() throws IOException {
        String ask_price = jsonParser("ask_price");

        if (ask_price.equals("n/a")) {
            return "n/a";
        } else {
            return ask_price;
        }

    }

    public String getHigh() throws IOException {
        String high = jsonParser("52_week_high");

        if (high.equals("nm") | high.equals("na")) {
            return "-";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(high));
        }
    }

    public String getLow() throws IOException {
        String low = jsonParser("52_week_low");

        if (low.equals("nm") | low.equals("na")) {
            return "-";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(low));
        }
    }

    public String getOpenPrice() throws IOException {
        String open_price = jsonParser("open_price");

        if (open_price.equals("n/a")) {
            return "n/a";
        } else {
            return twoplacesafterdecimal.format(Double.valueOf(open_price));
        }
    }

    public String getVolume() throws IOException {
        String volume = jsonParser("volume");

        if (volume.equals("n/a")) {
            return "n/a";
        } else {
            return formatLargeNumber(volume);
        }
    }

    public String getAverageVolume() throws IOException {
        String average_volume = jsonParser("average_daily_volume");

        if (average_volume.equals("n/a")) {
            return "n/a";
        } else {
            return formatLargeNumber(average_volume);
        }
    }

    public String getCompanyName() throws IOException {
        String companyname = jsonParser("legal_name");
        return companyname;
    }

    private String jsonParser(String datapoint) throws IOException {

        URL url = new URL(urlBase + this.ticker + "&item=" + datapoint);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + this.encoding);
        try {
            is = urlConnection.getInputStream();
        } catch (FileNotFoundException e) {
            return "";
        }
        InputStreamReader isw = new InputStreamReader(is);
        JSONObject output;
        String value = null;
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
            value = output.getString("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (value.equals("nm") | value.equals("na") | value.equals("-") ) {
            return "n/a";
        }
        return value;
    }
}
