package com.example.parthivnaresh.myfirstapp.SearchFragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.parthivnaresh.myfirstapp.Fragment_Search;
import com.example.parthivnaresh.myfirstapp.MoreInfo.Fragment_MoreInfo;
import com.example.parthivnaresh.myfirstapp.R;

public class BlankLayout_Search extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);

        Fragment_Search fragment_search = new Fragment_Search();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.blank_template, fragment_search);
        fragmentTransaction.commit();
    }
}
