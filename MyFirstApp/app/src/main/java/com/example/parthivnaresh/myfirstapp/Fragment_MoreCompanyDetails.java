package com.example.parthivnaresh.myfirstapp;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;

public class Fragment_MoreCompanyDetails extends Fragment {

    String symbol, name, eachstock;

    TextView ticker, average_volume, week_high, volume, week_low, enterprise_value, market_value,
            price_earnings, eps, price_rev, ev_rev, price_book, ev_ebitda, rev_growth, revqoq_growth,
            total_rev, cost_of_rev, gross_margin, opt_margin;

    DecimalFormat df1 = new DecimalFormat();

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
        View v = inflater.inflate(R.layout.fragment_morecompanydetails, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null)
        {
            eachstock = bundle.getString("ticker");
        }

        ticker = v.findViewById(R.id.ticker);

        average_volume = v.findViewById(R.id.average_volume);
        week_high = v.findViewById(R.id.week_high);
        volume = v.findViewById(R.id.volume);
        week_low = v.findViewById(R.id.week_low);
        enterprise_value = v.findViewById(R.id.enterprise_value);
        market_value = v.findViewById(R.id.market_value);

        price_earnings = v.findViewById(R.id.price_earnings);
        eps = v.findViewById(R.id.earnings_per_share);
        price_rev = v.findViewById(R.id.price_revenue);
        ev_rev = v.findViewById(R.id.ev_revenue);
        price_book = v.findViewById(R.id.price_book);
        ev_ebitda = v.findViewById(R.id.ev_ebitda);
        rev_growth = v.findViewById(R.id.rev_growth);
        revqoq_growth = v.findViewById(R.id.revqoq_growth);
        total_rev = v.findViewById(R.id.total_revenue);
        cost_of_rev = v.findViewById(R.id.cost_of_revenue);
        gross_margin = v.findViewById(R.id.gross_margin);
        opt_margin = v.findViewById(R.id.operating_margin);

        ticker.setText(eachstock);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog().build();
        StrictMode.setThreadPolicy(policy);

        new DataPointCall(eachstock).execute();
        System.out.println("PANDA AFTER ASYNC DATAPOINTCALL");

        return v;
    }

    private class DataPointCall extends AsyncTask<String, String, APICall_DataPoint> {

        private final String ticker;
        APICall_DataPoint APICallDataPoint;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public DataPointCall(String eachstock) {
            System.out.println("PANDA STARTED DATAPOINTCALL");
            this.ticker = eachstock;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected APICall_DataPoint doInBackground(String... strings) {

            APICallDataPoint = new APICall_DataPoint(this.ticker);

            return APICallDataPoint;
        }

        protected void onPostExecute(APICall_DataPoint result) {
            df1.applyPattern("##0.00");

            try {
                average_volume.append(result.getAverageVolume());
                week_high.append(result.getHigh());
                volume.append(result.getVolume());
                week_low.append(result.getLow());
                enterprise_value.append(result.getEV());
                market_value.append(result.getMarketCap());
                price_earnings.append(result.getPE());
                eps.append(result.getBasicEPS());
                price_rev.append(result.getPRevenue());
                ev_rev.append(result.getEVRevenue());
                price_book.append(result.getPBook());
                ev_ebitda.append(result.getEVEBITDA());
                rev_growth.append(result.getRevenueGrowth() + "%");
                revqoq_growth.append(result.getRevenueQOQGrowth() + "%");
                total_rev.append(result.getTotalRevenue());
                cost_of_rev.append(result.getCostOfRevenue());
                gross_margin.append(result.getGrossMargin() + "%");
                opt_margin.append(result.getOperatingMargin() + "%");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
