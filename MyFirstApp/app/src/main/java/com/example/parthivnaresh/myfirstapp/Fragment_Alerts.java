package com.example.parthivnaresh.myfirstapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parthivnaresh.myfirstapp.NewsFeed.APICall_NewsFeed;
import com.example.parthivnaresh.myfirstapp.NewsFeed.Adapter_NewsFeed;
import com.example.parthivnaresh.myfirstapp.NewsFeed.StockNewsObject;
import com.intrinio.realtime.RealTimeClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Alerts extends Fragment {

    LinearLayout saved_alerts;

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
        View v = inflater.inflate(R.layout.fragment_savedalerts, container, false);

        saved_alerts = v.findViewById(R.id.my_alerts_list);

        File alerts_dir = getContext().getDir("Alerts", Context.MODE_PRIVATE);
        try {
            traverse(alerts_dir);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void traverse (File dir) throws IOException, JSONException {
        File[] files = dir.listFiles();

        for (File file : files) {
            View child = getLayoutInflater().inflate(R.layout.saved_alert, null);
            TextView name_of_alert = child.findViewById(R.id.name_of_alert);
            Button remove_alret = child.findViewById(R.id.remove_alert);

            BufferedReader input;

            File alerts_dir = getContext().getDir("Alerts", Context.MODE_PRIVATE);

            File fileWithinMyDir = new File(alerts_dir, file.getName());

            FileInputStream alert_to_write = new FileInputStream(fileWithinMyDir);

            input = new BufferedReader(new InputStreamReader(alert_to_write));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject alert = new JSONObject(buffer.toString());
            name_of_alert.setText(alert.keys().next());

            remove_alret.setOnClickListener(v -> {
                saved_alerts.removeView(child);
                file.delete();
                File[] new_files = dir.listFiles();
                if (new_files.length == 0) {
                    getActivity().finish();
                }
            });
            saved_alerts.addView(child);
        }
    }
}
