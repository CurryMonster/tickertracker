package com.example.parthivnaresh.myfirstapp.MyList;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parthivnaresh.myfirstapp.MoreInfo.BlankLayout_MoreInfo;
import com.example.parthivnaresh.myfirstapp.R;
import com.example.parthivnaresh.myfirstapp.StockObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parthiv Naresh on 3/31/2018.
 */

public class Adapter_MyList extends RecyclerView.Adapter<Adapter_MyList.myViewHolder> {

    List<StockObject> ticker_list;

    public Adapter_MyList() {
        this.ticker_list = new ArrayList<>();
    }

    public void addAll(List<StockObject> stocks) {
        for (int i = 0; i < stocks.size(); i++) {
            ticker_list.add(stocks.get(i));
        }
        notifyDataSetChanged();
    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eachstock_mylist, parent, false);
        myViewHolder holder = new myViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {
        try {
            String current_ticker = ticker_list.get(position).getTicker();
            holder.stockticker.setText(current_ticker);

            String current_ticker_company_name = ticker_list.get(position).getCompanyName();
            holder.stockcompanyname.setText(current_ticker_company_name);
            holder.stockcompanyname.setSelected(true);
//            holder.stockcompanyname.setMovementMethod(new ScrollingMovementMethod());

            String current_price = ticker_list.get(position).getPrice();
            holder.stockprice.setText("$" + current_price.toString());

            String price_change = ticker_list.get(position).getPriceChange();
            holder.stockpricechange.setText("$" + price_change);

            String percent_change = ticker_list.get(position).getPercentChange();
            holder.stockpercentchange.setText(percent_change + "%");

            if (Double.valueOf(percent_change) < 0) {
                holder.stockpercentchange.setTextColor(Color.parseColor("#FF0000"));
                holder.stockpricechange.setTextColor(Color.parseColor("#FF0000"));
            } else if (Double.valueOf(percent_change) > 0) {
                holder.stockpercentchange.setTextColor(Color.parseColor("#3EB489"));
                holder.stockpricechange.setTextColor(Color.parseColor("#3EB489"));
            }

            holder.moreinfo.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(v.getContext(), BlankLayout_MoreInfo.class);
                    intent.putExtra("ticker", (CharSequence) current_ticker);
                    v.getContext().startActivity(intent);
                } catch(IndexOutOfBoundsException e) {
                    Toast.makeText(v.getContext(), "Can't provide more information", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IndexOutOfBoundsException e) {
            holder.stockprice.setText("---");
        }
    }

    @Override
    public int getItemCount() {
        return ticker_list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView stockticker, stockcompanyname, stockprice, stockpricechange, stockpercentchange;
        Button moreinfo;

        public myViewHolder(View view){
            super(view);
            stockticker = view.findViewById(R.id.each_ticker);
            stockcompanyname = view.findViewById(R.id.each_company_name);
            stockprice = view.findViewById(R.id.each_price);
            stockpricechange = view.findViewById(R.id.price_change);
            stockpercentchange = view.findViewById(R.id.percent_change);
            moreinfo = view.findViewById(R.id.remove_each_stock);
        }
    }
}
