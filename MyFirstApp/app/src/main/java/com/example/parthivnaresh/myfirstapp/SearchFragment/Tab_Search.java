package com.example.parthivnaresh.myfirstapp.SearchFragment;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parthivnaresh.myfirstapp.APICall_DataPoint;
import com.example.parthivnaresh.myfirstapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tab_Search extends Fragment {
    EditText search_ticker;
    TextView company_name, company_title, stock_price, stock_percent_change, stock_price_change;
    Button search;
    ImageButton refresh;

    LineGraphSeries<DataPoint> series;
    LinearLayout search_view_layout;
    View child;
    GraphView graph;

    APICall_DataPoint APICallDataPoint;

    public static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        search_ticker = v.findViewById(R.id.search_bar);
        company_name = v.findViewById(R.id.company_name);
        search = v.findViewById(R.id.search_button);

        company_title = v.findViewById(R.id.company_title);
        stock_price = v.findViewById(R.id.share_price);
        stock_percent_change = v.findViewById(R.id.sharepercent_change);
        stock_price_change = v.findViewById(R.id.shareprice_change);

        refresh = v.findViewById(R.id.action_refresh);
        refresh.setVisibility(View.INVISIBLE);

        search_ticker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void afterTextChanged(Editable s) {

                SearchCompany get_name = new SearchCompany();
                get_name.execute(search_ticker.getText().toString().toUpperCase());
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                company_title.setText(company_name.getText());
                stock_price.setText("");
                stock_percent_change.setText("");
                stock_price_change.setText("");
                refresh.clearAnimation();
                refresh.setVisibility(View.INVISIBLE);

                fragmentManager = getFragmentManager();

                Graph_Inflater graph_inflater = new Graph_Inflater();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.add(R.id.graph_framelayout, graph_inflater);
                fragmentTransaction.commit();
            }
        });

        refresh.setOnClickListener(v1 -> {
            refresh.clearAnimation();
            RotateAnimation anim = new RotateAnimation(30, 360,
                    refresh.getWidth()/2, refresh.getHeight()/2);
            anim.setFillAfter(true);
            anim.setRepeatCount(0);
            anim.setDuration(3000);
            refresh.startAnimation(anim);

            GetPrice getPrice = new GetPrice();
            getPrice.execute(search_ticker.getText().toString());
        });

        return v;
    }

    public class SearchCompany extends AsyncTask<String, String, String> {

        String name;
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            APICallDataPoint = new APICall_DataPoint(strings[0]);
            try {
                name = APICallDataPoint.getCompanyName();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return name;
        }

        protected void onPostExecute(String name) {
            try {
                if (name.equals("na")) {
                    company_name.setHint("");
                    company_name.setText("");
                } else {
                    company_name.setText(name);
                }
            } catch (NullPointerException e) {
                company_name.setHint("");
                company_name.setText("");
            }
        }
    }

    protected class GetPrice extends AsyncTask<String, String, List> {

        String ticker, last_price, percent_change, price_change;
        List holder;
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected List doInBackground(String... strings) {
            ticker = strings[0].toUpperCase();
            APICallDataPoint = new APICall_DataPoint(ticker);
            holder = new ArrayList();
            try {
                last_price = APICallDataPoint.getLastPrice();
                percent_change = APICallDataPoint.getPercentChange();
                price_change = APICallDataPoint.getPriceChange();
                holder.add(last_price);
                holder.add(percent_change);
                holder.add(price_change);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e1) {
                refresh.clearAnimation();
                refresh.setVisibility(View.INVISIBLE);
            }
            return holder;
        }

        protected void onPostExecute(List price_and_change) {
            try {
                stock_price.setText("$" + price_and_change.get(0).toString());
                stock_percent_change.setText(price_and_change.get(1).toString() + "%");
                stock_price_change.setText(price_and_change.get(2).toString());
                if (Double.valueOf((String) price_and_change.get(1)) < 0) {
                    stock_percent_change.setTextColor(Color.parseColor("#FF0000"));
                    stock_price_change.setTextColor(Color.parseColor("#FF0000"));
                } else if (Double.valueOf((String) price_and_change.get(1)) > 0) {
                    stock_percent_change.setTextColor(Color.parseColor("#3EB489"));
                    stock_price_change.setTextColor(Color.parseColor("#3EB489"));
                }
            } catch (IndexOutOfBoundsException e) {
                refresh.clearAnimation();
                refresh.setVisibility(View.INVISIBLE);
            } catch (NumberFormatException e1) {
                Toast.makeText(getContext(), "Couldn't find ticker", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected class GetGraph extends AsyncTask<String, String, LinearLayout> {

        String ticker;
        Integer period;
        JSONObject current_day;

        protected GetGraph() {
            search_view_layout = getActivity().findViewById(R.id.search_view_layout);
            child = getLayoutInflater().inflate(R.layout.graph_view, search_view_layout);
            graph = child.findViewById(R.id.graph);
            search_view_layout.removeView(child);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected LinearLayout doInBackground(String... strings) {
            ticker = strings[0].toUpperCase();
            period = Integer.valueOf(strings[1]);

            GetHistoricalPrices getHistoricalPrices = new GetHistoricalPrices(ticker);
            JSONArray historical_prices = null;
            JSONArray newJsonArray = new JSONArray();
            ArrayList<Double> running_values = null;
            try {
                if (period == 1) {
                    historical_prices = getHistoricalPrices.getOneMonth();
                    System.out.println("PANDA CASE 1");
                } else if (period == 2) {
                    historical_prices = getHistoricalPrices.getThreeMonth();
                    System.out.println("PANDA CASE 2");
                } else if (period == 3) {
                    historical_prices = getHistoricalPrices.getOneYear();
                    System.out.println("PANDA CASE 3");
                } else if (period == 4) {
                    historical_prices = getHistoricalPrices.getThreeYear();
                    System.out.println("PANDA CASE 4");
                }
                for (int i = historical_prices.length()-1; i>=0; i--) {
                    newJsonArray.put(historical_prices.get(i));
                }
                series = new LineGraphSeries<>(new DataPoint[] {});
                running_values = new ArrayList();
                for (int i = 0; i < newJsonArray.length(); i++) {
                    current_day = (JSONObject) newJsonArray.get(i);
                    running_values.add(Double.valueOf(current_day.get("value").toString()));
                    series.appendData(new DataPoint(i, Double.valueOf(current_day.get("value").toString())), true, 1000);
                }

                graph.addSeries(series);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(historical_prices.length());
            try {
                graph.getViewport().setMinY(Collections.min(running_values));
                graph.getViewport().setMaxY(Collections.max(running_values));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);

            return search_view_layout;
        }

        protected void onPostExecute(LinearLayout item) {
            ((ViewGroup) child.getParent()).removeView(child);
            item.addView(child);
        }
    }
}
