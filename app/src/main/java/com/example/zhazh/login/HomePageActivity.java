package com.example.zhazh.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentHome1 fragmentHome1;
    private FragmentHome2 fragmentHome2;
    private FragmentMessage fragmentMessage;
    private FragmentFunction fragmentFunction;
    private int lastfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page1);
        initFragment();
    }

    private void initFragment() {
        fragmentHome1 = new FragmentHome1();
        fragmentHome2 = new FragmentHome2();
        fragmentMessage = new FragmentMessage();
        fragmentFunction = new FragmentFunction();
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragmentHome1).show(fragmentHome1).commit();
        bottomNavigationView =  findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    if (lastfragment != 0) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragmentHome1).show(fragmentHome1).commit();
                        lastfragment = 0;
                    }
                    return true;
                }

                case R.id.navigation_history: {
                    if (lastfragment != 1) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragmentHome2).show(fragmentHome2).commit();
                        lastfragment = 1;
                    }
                    return true;
                }

                case R.id.navigation_message: {
                    if (lastfragment != 2 ) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragmentMessage).show(fragmentMessage).commit();
                        lastfragment = 2;
                    }
                    return true;
                }
                case R.id.navigation_function: {
                    if (lastfragment != 3) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragmentFunction).show(fragmentFunction).commit();
                        lastfragment = 3;
                    }
                    return true;
                }
            }
            return false;
        }

    };

    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("id", 0);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview, fragmentMessage).show(fragmentMessage).commit();
        bottomNavigationView =  findViewById(id);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        super.onResume();
        }
}

