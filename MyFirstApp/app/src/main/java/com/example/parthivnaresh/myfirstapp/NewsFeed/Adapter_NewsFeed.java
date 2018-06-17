package com.example.parthivnaresh.myfirstapp.NewsFeed;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parthivnaresh.myfirstapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parthiv Naresh on 3/31/2018.
 */

public class Adapter_NewsFeed extends RecyclerView.Adapter<Adapter_NewsFeed.myViewHolder>{

    List<Object> newsList;

    public Adapter_NewsFeed() {
        this.newsList = new ArrayList<>();
    }

    public void addAll(List<StockNewsObject> news_list) {
        for (int i = 0; i < news_list.size(); i++) {
            newsList.add(news_list.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eachnewsitem_moreinfo, parent, false);
        myViewHolder holder = new myViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        try {
            StockNewsObject current_news = (StockNewsObject) newsList.get(position);
            String title = current_news.getTitle();
            holder.news_header.setText(title);

            String summary = current_news.getSummary();
            holder.news_summary.setText(summary);

            String link = current_news.getLink();
            holder.news_header.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    v.getContext().startActivity(intent);
                } catch(Exception e) {
                    Toast.makeText(v.getContext(), "Can't open browser", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView news_header, news_summary;

        public myViewHolder(View view){
            super(view);
            news_header = view.findViewById(R.id.news_headline);
            news_header.setMovementMethod(LinkMovementMethod.getInstance());
            news_summary = view.findViewById(R.id.news_summary);
        }
    }
}
