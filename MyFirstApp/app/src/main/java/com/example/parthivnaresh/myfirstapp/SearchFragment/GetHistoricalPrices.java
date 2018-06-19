package com.example.parthivnaresh.myfirstapp.SearchFragment;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class GetHistoricalPrices extends JSONObject {

    private String ticker, encoding;
    private String intrinio_username = "6830329235637b2de2961364c5a70952";
    private String intrinio_password = "0b5ee0ad9abdd4dbf3f1c16ac516af5e";

    private String urlBase = "https://api.intrinio.com/historical_data?identifier=";

    InputStream is;

    Date date = new Date();
    DateTime dateTime = new DateTime();
    String year, month, day, current_date;

    DecimalFormat df1 = new DecimalFormat();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public GetHistoricalPrices(String ticker) {
        this.ticker = ticker;

        String userPassword = intrinio_username + ":" + intrinio_password;
        this.encoding = Base64.getEncoder().encodeToString(userPassword.getBytes());

        df1.applyPattern("##00");

        this.year = String.valueOf(Integer.valueOf(df1.format(date.getYear())) + 1900);
        this.month = String.valueOf(df1.format(Integer.valueOf(date.getMonth()) + 1));
        this.day = String.valueOf(df1.format(Integer.valueOf(date.getDate())));
        this.current_date = this.year + "-" + this.month + "-" + this.day;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONArray getOneMonth() throws IOException {
        String current_month = String.valueOf(df1.format(dateTime.minusMonths(1).getMonthOfYear()));

        JSONArray one_month_prices = jsonParser(this.year + "-" + current_month + "-" + this.day);
        System.out.println(this.year + "-" + current_month + "-" + this.day + " PANDA ONE MONTH");
        return one_month_prices;
    }

    public JSONArray getThreeMonth() throws IOException {
        String current_month = String.valueOf(df1.format(dateTime.minusMonths(3).getMonthOfYear()));

        JSONArray three_month_prices = jsonParser(this.year + "-" + current_month + "-" + this.day);

        return three_month_prices;
    }

    public JSONArray getOneYear() throws IOException {
        String current_year = String.valueOf(df1.format(dateTime.minusYears(1).getYear()));

        JSONArray one_year_prices = jsonParser(current_year + "-" + this.month + "-" + this.day);
        System.out.println(current_year + "-" + this.month + "-" + this.day + " PANDA ONE YEAR");

        return one_year_prices;
    }

    public JSONArray getThreeYear() throws IOException {
        String current_year = String.valueOf(df1.format(dateTime.minusYears(3).getYear()));

        JSONArray three_year_prices = jsonParser(current_year + "-" + this.month + "-" + this.day);

        return three_year_prices;
    }

    private JSONArray jsonParser(String date) throws IOException {

        URL url = new URL(urlBase + this.ticker + "&item=adj_close_price&frequency=daily&start_date=" + date);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + this.encoding);
        try {
            is = urlConnection.getInputStream();
        } catch (FileNotFoundException e) {
            return new JSONArray();
        }
        InputStreamReader isw = new InputStreamReader(is);
        JSONObject output;
        JSONArray hist_prices = null;
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
            hist_prices = output.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hist_prices;
    }
}
