package com.example.parthivnaresh.myfirstapp;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Tab_Alerts extends Fragment {

    LinearLayout linearLayout;
    Button set_alert, add_constraint, delete_constraint, saved_alerts;
    EditText alert_name, alert_value;
    CustomSpinnerAdapter industry_adapter, term_adapter, sheet_adapter, comparison_adapter, income_statement_adapter,
            balance_sheet_adapter, cash_flow_adapter;
    Spinner industry_list, term_list, sheet_list, metric_list, comparison_list;
    SeekBar seekBar;
    List<String> industries, terms, sheets, comparisons, income_statement_values, balance_sheet_values, cash_flow_values;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_filters, container, false);

        linearLayout = v.findViewById(R.id.filter_list);
        set_alert = v.findViewById(R.id.SetAlert);
        saved_alerts = v.findViewById(R.id.SavedAlerts);

        set_alert.setOnClickListener(v1 -> {

            View child = getLayoutInflater().inflate(R.layout.custom_alert, null);

            try {
                linearLayout.removeViewAt(1);
                linearLayout.addView(child);
            } catch (NullPointerException e) {
                linearLayout.addView(child);
            }

            alert_name = child.findViewById(R.id.alert_name);
            industry_list = child.findViewById(R.id.industry_spinner);
            term_list = child.findViewById(R.id.term_spinner);
            sheet_list = child.findViewById(R.id.sheet_spinner);
            metric_list = child.findViewById(R.id.metric_spinner);
            comparison_list = child.findViewById(R.id.comparison_spinner);
            alert_value = child.findViewById(R.id.constraint_value);
            seekBar = v.findViewById(R.id.simpleSeekBar);

            settingAdapters();

            sheet_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String sheet = sheet_list.getSelectedItem().toString();

                    if (sheet.equals("Income Statement")) {
                        metric_list.setAdapter(income_statement_adapter);
                    } else if (sheet.equals("Balance Sheet")) {
                        metric_list.setAdapter(balance_sheet_adapter);
                    } else if (sheet.equals("Cash Flow")) {
                        metric_list.setAdapter(cash_flow_adapter);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            add_constraint = child.findViewById(R.id.add_constraint);
            add_constraint.setOnClickListener(v2 -> {
                try {
                    String filename = alert_name.getText().toString().trim();
                    if (alert_name.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Please name your alert", Toast.LENGTH_SHORT).show();
                    } else if (alert_value.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Alert " + alert_name.getText().toString() + " added!", Toast.LENGTH_SHORT).show();
                        writeAlertsToInternal(filename);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            delete_constraint = child.findViewById(R.id.delete_constraint);
            delete_constraint.setOnClickListener(v2 -> {
                linearLayout.removeView(child);
            });

            seekBar.setMax(500);
            seekBar.setMin(-500);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    alert_value.setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        });
        saved_alerts.setOnClickListener(v2 -> {
            File alerts_dir = getContext().getDir("Alerts", Context.MODE_PRIVATE);
            File[] files = alerts_dir.listFiles();
            if (files.length == 0 || !alerts_dir.exists()) {
                Toast.makeText(getContext(), "You don't have any saved alerts", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(v2.getContext(), BlankLayout_Alerts.class);
                v2.getContext().startActivity(intent);
            }
        });

        return v;
    }

    public void writeAlertsToInternal(String file_name_to_write) throws IOException {
        try {
            JSONObject alert_values = new JSONObject();
            alert_values.put("Industry",industry_list.getSelectedItem().toString());
            alert_values.put("Term",term_list.getSelectedItem().toString());
            alert_values.put("Sheet",sheet_list.getSelectedItem().toString());
            alert_values.put("Metric",metric_list.getSelectedItem().toString());
            alert_values.put("Comparison",comparison_list.getSelectedItem().toString());
            alert_values.put("Value", alert_value.getText().toString());
            JSONObject alert = new JSONObject();
            alert.put(alert_name.getText().toString(), alert_values);

            File alerts_dir = getContext().getDir("Alerts", Context.MODE_PRIVATE); //Creating an internal dir;
            File fileWithinMyDir = new File(alerts_dir, file_name_to_write); //Getting a file within the dir.
            FileOutputStream alert_to_write = new FileOutputStream(fileWithinMyDir);
            alert_to_write.write(alert.toString().getBytes());
            alert_to_write.close();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    public void readAlertsFromInternal(String file_name_to_read) throws IOException {
        try {
            BufferedReader input;
            input = new BufferedReader(new InputStreamReader(getContext().openFileInput(file_name_to_read)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println(buffer.toString() + " PANDA ALERT");
            System.out.println(file_name_to_read + " PANDA ALERT FILE NAME");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "That alert doesn't exist", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settingAdapters() {
        industries = Arrays.asList(getResources().getStringArray(R.array.industry));
        industry_adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, industries);
        industry_list.setAdapter(industry_adapter);

        terms = Arrays.asList(getResources().getStringArray(R.array.term));
        term_adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, terms);
        term_list.setAdapter(term_adapter);

        sheets = Arrays.asList(getResources().getStringArray(R.array.sheet));
        sheet_adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, sheets);
        sheet_list.setAdapter(sheet_adapter);

        comparisons = Arrays.asList(getResources().getStringArray(R.array.comparison));
        comparison_adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, comparisons);
        comparison_list.setAdapter(comparison_adapter);

        income_statement_values = Arrays.asList(getResources().getStringArray(R.array.income_statement));
        income_statement_adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,
                income_statement_values);

        balance_sheet_values = Arrays.asList(getResources().getStringArray(R.array.balance_sheet));
        balance_sheet_adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,
                balance_sheet_values);

        cash_flow_values = Arrays.asList(getResources().getStringArray(R.array.cash_flow));
        cash_flow_adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,
                cash_flow_values);
    }
}
