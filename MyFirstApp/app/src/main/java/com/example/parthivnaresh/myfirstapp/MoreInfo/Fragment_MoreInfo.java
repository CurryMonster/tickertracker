package com.example.parthivnaresh.myfirstapp.MoreInfo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.parthivnaresh.myfirstapp.APICall_DataPoint;
import com.example.parthivnaresh.myfirstapp.Fragment_MoreCompanyDetails;
import com.example.parthivnaresh.myfirstapp.IEXFeedParser;
import com.example.parthivnaresh.myfirstapp.NewsFeed.Adapter_NewsFeed;
import com.example.parthivnaresh.myfirstapp.NewsFeed.APICall_NewsFeed;
import com.example.parthivnaresh.myfirstapp.NewsFeed.StockNewsObject;
import com.example.parthivnaresh.myfirstapp.R;
import com.intrinio.realtime.RealTimeClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Fragment_MoreInfo extends Fragment {

    //  More Info fragment displaying real time price, metrics, and news

    //  Credentials for Intrinio API
    private String intrinio_username = "6830329235637b2de2961364c5a70952";
    private String intrinio_password = "0b5ee0ad9abdd4dbf3f1c16ac516af5e";

    private List<StockNewsObject> news_list = new ArrayList<>();

    RecyclerView news_rec;

    ArrayList<String> holder = new ArrayList();
    Adapter_NewsFeed news_adapter;

    TextView company_name, share_price, perc_change, mkt_cap, enterprisevalue, price_earnings, basic_eps,
            bid_price, ask_price, volume, open_price;
    Button more_data;

    String ticker;

    RealTimeClient client = new RealTimeClient(intrinio_username, intrinio_password, RealTimeClient.Provider.IEX);

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
        View v = inflater.inflate(R.layout.fragment_moreinfo, container, false);

        //  Ticker company name
        company_name = v.findViewById(R.id.stock_title);

        //  Fundamental stock info
        share_price = v.findViewById(R.id.share_price);
        perc_change = v.findViewById(R.id.percent_change);
        mkt_cap = v.findViewById(R.id.mktcap_value);
        enterprisevalue = v.findViewById(R.id.ev_value);
        price_earnings = v.findViewById(R.id.pe_value);
        basic_eps = v.findViewById(R.id.eps_value);
        bid_price = v.findViewById(R.id.bid_value);
        ask_price = v.findViewById(R.id.ask_value);
        volume = v.findViewById(R.id.volume_value);
        open_price = v.findViewById(R.id.open_value);

        //  Button for more stock data
        more_data = v.findViewById(R.id.more_data);

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            ticker = bundle.getString("myticker");
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        holder.add("0");
        holder.add("1");
        holder.add("2");
        holder.add("3");
        holder.add("4");
        holder.add("5");
        holder.add("6");
        holder.add("7");
        holder.add("8");
        holder.add("9");

        //  Recycler view for latest stock news
        news_adapter = new Adapter_NewsFeed();
        news_rec = v.findViewById(R.id.newsfeed);
        news_rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        news_rec.setAdapter(news_adapter);

        try {
            new DataPointCall(ticker).execute();
            new StartFeed(ticker).execute();
            new NewsFeed(ticker).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StockNewsObject stockNewsObject;
        for (String ticker : holder) {
            stockNewsObject = new StockNewsObject("","", "");
            news_list.add(stockNewsObject);
        }

        more_data.setOnClickListener((View v1) -> {
            try {
                Bundle bundle1 = new Bundle();
                bundle1.putString("ticker", ticker);

                Fragment_MoreCompanyDetails fragmentMoreCompanyDetails = new Fragment_MoreCompanyDetails();
                fragmentMoreCompanyDetails.setArguments(bundle1);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.blank_template, fragmentMoreCompanyDetails);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } catch(IndexOutOfBoundsException e) {

            }
        });

        return v;
    }


    private class DataPointCall extends AsyncTask<String, String, APICall_DataPoint> {

        private final String ticker;
        APICall_DataPoint dp_call;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public DataPointCall(String ticker) throws IOException {
            System.out.println("PANDA STARTED DATAPOINTCALL" + ticker);
            this.ticker = ticker;
            dp_call = new APICall_DataPoint(this.ticker);
            company_name.setText(dp_call.getCompanyName());
        }

        @Override
        protected APICall_DataPoint doInBackground(String... strings) {

            return dp_call;
        }

        protected void onPostExecute(APICall_DataPoint data_call) {
            try {
                System.out.println("PANDA STARTED DATAPOINTCALL" + data_call.getMarketCap());
                mkt_cap.setText(data_call.getMarketCap());
                enterprisevalue.setText(data_call.getEV());
                perc_change.setText(data_call.getPercentChange() + "%");
                price_earnings.setText(data_call.getPE());
                basic_eps.setText(data_call.getBasicEPS());
                bid_price.setText(data_call.getBidPrice());
                ask_price.setText(data_call.getAskPrice());
                volume.setText(data_call.getVolume());
                open_price.setText(data_call.getOpenPrice());
            } catch (JSONException | IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private class StartFeed extends AsyncTask<String, String, Void> {

        private final String ticker;

        public StartFeed(String ticker) {
            System.out.println("PANDA STARTED STARTFEED");
            this.ticker = ticker;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(String... strings) {
            try {
                client.registerQuoteHandler(quote -> {

                    try {
                        IEXFeedParser current_quote = new IEXFeedParser(quote);

                        if (current_quote.getCallType().equals("last")){

                            String lastprice = current_quote.LastPrice();

                            publishProgress(lastprice);
                            client.getNextQuote();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                String[] channels = new String[]{this.ticker};
                client.join(channels);
                client.connectAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... ticker) {
            share_price.setText("$" + ticker[0]);
        }
    }

    private class NewsFeed extends AsyncTask<String, String, ArrayList<ArrayList<String>>> {

        private final String ticker;
        private final APICall_NewsFeed news_call;
        private JSONArray newsList;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public NewsFeed(String ticker) {
            this.ticker = ticker;
            news_call = new APICall_NewsFeed(this.ticker);
        }

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(String... strings) {
            ArrayList<ArrayList<String>> newsHolder = new ArrayList<>();
            try {
                newsList = news_call.getNewsList();

                for (int i = 0; i < newsList.length(); i++) {

                    ArrayList<String> holderList = new ArrayList<String>();

                    JSONObject eachNews = (JSONObject) newsList.get(i);

                    String title_formatted = eachNews.get("title").toString()
                            .replace("&apos;", "'")
                            .replace("&quot;", "\"");

                    String summary_formatted = eachNews.get("summary").toString()
                            .replace("&apos;", "'")
                            .replace("&quot;", "\"");

                    holderList.add(title_formatted);
                    holderList.add(summary_formatted);
                    holderList.add(eachNews.get("url").toString());
                    newsHolder.add(holderList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return newsHolder;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> newslist) {
            for (int i = 0; i < newslist.size(); i++) {

                news_list.get(i).setTitle(newslist.get(i).get(0));
                news_list.get(i).setSummary(newslist.get(i).get(1));
                news_list.get(i).setLink(newslist.get(i).get(2));
            }
            news_adapter.addAll(news_list);
        }
    }
}
