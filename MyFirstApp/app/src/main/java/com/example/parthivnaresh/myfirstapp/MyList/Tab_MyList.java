package com.example.parthivnaresh.myfirstapp.MyList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.parthivnaresh.myfirstapp.APICall_DataPoint;
import com.example.parthivnaresh.myfirstapp.IEXFeedParser;
import com.example.parthivnaresh.myfirstapp.R;
import com.example.parthivnaresh.myfirstapp.StockObject;
import com.intrinio.realtime.RealTimeClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tab_MyList extends Fragment {

    private List<String> holder = new ArrayList();

    private List<StockObject> stockObjectList = new ArrayList<>();

    private String intrinio_username = "6830329235637b2de2961364c5a70952";
    private String intrinio_password = "0b5ee0ad9abdd4dbf3f1c16ac516af5e";

    RealTimeClient client = new RealTimeClient(intrinio_username, intrinio_password, RealTimeClient.Provider.IEX);

    RecyclerView rec;
    ProgressBar circ;
    Adapter_MyList stock_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_mylist, container, false);

        circ = v.findViewById(R.id.circle);

        //  Recycler view for stock list
        stock_list = new Adapter_MyList();
        rec = v.findViewById(R.id.mylist_stocks_recycler);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        rec.setAdapter(stock_list);

        ((SimpleItemAnimator) rec.getItemAnimator()).setSupportsChangeAnimations(false);
        rec.setVisibility(v.GONE);

        String filename = "StocksList";
        String first_portfolio = "SNAP, TWLO, TWTR, MU, CLDR, ICHR, AXTI, NXPI, MDB, UCTT, ZOES, CGNX, TSRO, MDXG";

        try {
            FileOutputStream outputStream;
            outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(first_portfolio.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> ticker_list = null;
        try {
            BufferedReader input;
            input = new BufferedReader(new InputStreamReader(getContext().openFileInput(filename)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            ticker_list = Arrays.asList(buffer.toString().split("\\s*,\\s*"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StockObject stockObject;
        for (String ticker : ticker_list) {
            stockObject = new StockObject(ticker, "", "0.00", "0.00", "0.00");
            stockObjectList.add(stockObject);
        }

        DataPointCall dataPointCall = new DataPointCall(v);
        StartFeed startFeed = new StartFeed();

        dataPointCall.execute();
        startFeed.execute();

        return v;
    }

    protected class StartFeed extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            try {
                client.registerQuoteHandler(quote -> {

                    try {
                        IEXFeedParser current_quote = new IEXFeedParser(quote);

                        if (current_quote.getCallType().equals("last")){

                            APICall_DataPoint api_call = new APICall_DataPoint(current_quote.CurrentTicker());

                            String ticker = current_quote.CurrentTicker();
                            String currentprice = current_quote.LastPrice();
                            String price_change = api_call.getPriceChange();
                            String percent_change = api_call.getPercentChange();

                            publishProgress(ticker, currentprice, price_change, percent_change);
                            client.getNextQuote();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                String[] channelss = holder.toArray(new String[0]);

                client.join(channelss);
                client.connectAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onProgressUpdate(String... ticker) {
            for (StockObject each_ticker : stockObjectList) {
                if (each_ticker.getTicker().equals(ticker[0])) {
                    each_ticker.setPrice(ticker[1]);
                    each_ticker.setPriceChange(ticker[2]);
                    each_ticker.setPercentChange(ticker[3]);
                    stock_list.notifyItemChanged(stockObjectList.indexOf(each_ticker));
                }
            }
        }
    }

    private class DataPointCall extends AsyncTask<String, String, APICall_DataPoint> {

        private final View v;
        APICall_DataPoint dp_call;

        public DataPointCall(View v) {
            this.v = v;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected APICall_DataPoint doInBackground(String... strings) {
            for (StockObject each_ticker : stockObjectList) {
                dp_call = new APICall_DataPoint(each_ticker.getTicker());
                try {
                    each_ticker.setPrice(dp_call.getLastPrice());
                    each_ticker.setCompanyName(dp_call.getCompanyName());
                    each_ticker.setPriceChange(dp_call.getPriceChange());
                    each_ticker.setPercentChange(dp_call.getPercentChange());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return dp_call;
        }

        @Override
        protected void onPostExecute(APICall_DataPoint apiCall_dataPoint) {
            super.onPostExecute(apiCall_dataPoint);
            Collections.sort(stockObjectList, (first_stock, second_stock) -> {
                Double percentchange1 = Double.valueOf(first_stock.getPercentChange());
                Double percentchange2 = Double.valueOf(second_stock.getPercentChange());
                return percentchange2.compareTo(percentchange1);
            });
            stock_list.addAll(stockObjectList);
            circ.setVisibility(this.v.GONE);
            rec.setVisibility(this.v.VISIBLE);

        }
    }
}
