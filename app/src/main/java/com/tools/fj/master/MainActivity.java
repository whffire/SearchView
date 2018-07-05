package com.tools.fj.master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tools.fj.searchview.OnSearchClikckListener;
import com.tools.fj.searchview.SearchView;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private LinearLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_view);
        searchView.setTYPE(6);
        searchView.sethint("请输入检索内容");
        // searchView.setBackoundColor(getResources().getColor(R.color.color_light_blue));
        mSwipeRefreshLayout = (LinearLayout) findViewById(R.id.swiperefreshlayout);

        searchView.setOnSearchClikckListener(new OnSearchClikckListener() {
            @Override
            public void onSearchClick(String keyword) {
                Toast.makeText(MainActivity.this, "onSearchClick="+keyword, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVisible(int Visible) {
                Toast.makeText(MainActivity.this, "onVisible=" + Visible, Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setVisibility(Visible);
            }
        });


    }
    }

