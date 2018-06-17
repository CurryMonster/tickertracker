package com.example.parthivnaresh.myfirstapp.MoreInfo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.parthivnaresh.myfirstapp.R;

public class BlankLayout_MoreInfo extends AppCompatActivity{

    //  Blank layout activity to inflate fragment into when "More Info" is clicked on the first tab
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);

        //  Retrieving passed ticker
        String ticker = (String) getIntent().getExtras().get("ticker");

        Bundle mylist_bundle = new Bundle();
        mylist_bundle.putString("myticker", ticker);
        Fragment_MoreInfo fragmentMoreInfo = new Fragment_MoreInfo();
        fragmentMoreInfo.setArguments(mylist_bundle);   // Passes ticker to bundle

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.blank_template, fragmentMoreInfo); // Adds fragment to blank layout
        fragmentTransaction.commit();
    }
}
