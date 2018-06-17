package com.example.parthivnaresh.myfirstapp.SearchFragment;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.parthivnaresh.myfirstapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.parthivnaresh.myfirstapp.SearchFragment.Tab_Search.fragmentManager;

public class Graph_Inflater extends Fragment {

    Button one_month, three_month, one_year, three_year;

    LineGraphSeries<DataPoint> series;
    LinearLayout item;
    View child;
    GraphView graph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.graph_view, container, false);

        graph = v.findViewById(R.id.graph);

        one_month = v.findViewById(R.id.one_month);
        three_month = v.findViewById(R.id.three_month);
        one_year = v.findViewById(R.id.one_year);
        three_year = v.findViewById(R.id.three_year);

        one_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Graph_Inflater graph_inflater = new Graph_Inflater();

                FragmentTransaction fragmentTransaction = Tab_Search.fragmentManager.beginTransaction();
                fragmentTransaction.remove(graph_inflater);
                fragmentTransaction.replace(R.id.graph_framelayout, graph_inflater);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                GetGraph getGraph = new GetGraph();
                //getGraph.execute("SNAP", "1");
            }
        });

        return v;
    }

    protected class GetGraph extends AsyncTask<String, String, LinearLayout> {

        String ticker;
        Integer period;
        JSONObject current_day;

        protected GetGraph() {
            item = getActivity().findViewById(R.id.search_view_layout);
            child = getLayoutInflater().inflate(R.layout.graph_view, item);
            graph = child.findViewById(R.id.graph);
            item.removeView(child);
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

            return item;
        }

        protected void onPostExecute(LinearLayout item) {
            item.addView(child);

            one_month = child.findViewById(R.id.one_month);
            three_month = child.findViewById(R.id.three_month);
            one_year = child.findViewById(R.id.one_year);
            three_year = child.findViewById(R.id.three_year);
        }
    }
}
