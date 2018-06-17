package com.example.parthivnaresh.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    LayoutInflater inflater;
    List<String> spinnerItems;

    public CustomSpinnerAdapter(Context applicationContext, int resource, List<String> spinnerItems) {
        super(applicationContext, resource, spinnerItems);
        this.spinnerItems = spinnerItems;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_spinner_items, null);
        TextView type = (TextView) view.findViewById(R.id.textView);
        type.setText(spinnerItems.get(i));
        return view;
    }
}