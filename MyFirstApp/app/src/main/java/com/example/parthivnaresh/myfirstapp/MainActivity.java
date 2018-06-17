package com.example.parthivnaresh.myfirstapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parthivnaresh.myfirstapp.MoreInfo.BlankLayout_MoreInfo;
import com.example.parthivnaresh.myfirstapp.MyList.Tab_MyList;
import com.example.parthivnaresh.myfirstapp.SearchFragment.BlankLayout_Search;
import com.example.parthivnaresh.myfirstapp.SearchFragment.Tab_Search;


public class MainActivity extends AppCompatActivity {

    Tab_MyList fragmentTwo;
    Tab_Search fragmentThree;
    Button search;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = findViewById(R.id.search);

        fragmentTwo = new Tab_MyList();
        fragmentThree = new Tab_Search();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final TabManager adapter = new TabManager(getSupportFragmentManager(), tabLayout.getTabCount());

        search.setOnClickListener((View v1) -> {
            try {
                Intent intent = new Intent(v1.getContext(), BlankLayout_Search.class);
                v1.getContext().startActivity(intent);
            } catch(IndexOutOfBoundsException e) {
                Toast.makeText(v1.getContext(), "You need to add foods first", Toast.LENGTH_SHORT).show();
            }
        });

        viewPager.setAdapter(adapter);

        //Updates the tabs when swiping the fragment in viewpager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}