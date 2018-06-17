package com.example.parthivnaresh.myfirstapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.parthivnaresh.myfirstapp.MoreInfo.Fragment_MoreInfo;

public class BlankLayout_Alerts extends AppCompatActivity{

    //  Blank layout activity to inflate fragment into when "More Info" is clicked on the first tab
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);

        Fragment_Alerts fragment_alerts = new Fragment_Alerts();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.blank_template, fragment_alerts); // Adds fragment to blank layout
        fragmentTransaction.commit();
    }
}
