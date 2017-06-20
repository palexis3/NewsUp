package com.example.palexis3.newsup.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.palexis3.newsup.Adapters.ArticlesAdapter;
import com.example.palexis3.newsup.Models.Articles;
import com.example.palexis3.newsup.R;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SourceArticleListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<Articles> articlesArrayList;
    private String name;

    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pg_loading_indication) ProgressBar pgLoadingIndication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        ButterKnife.bind(this);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        articlesArrayList = (ArrayList<Articles>) Parcels.unwrap(getIntent().getParcelableExtra("sourcesList"));
        name =  getIntent().getStringExtra("title");

        // set the title for this source with articles
        getSupportActionBar().setTitle(name);

        // add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize views
        initViews();
    }

    public void initViews() {

        // set necessary recyclerview items
        recyclerView = (RecyclerView) findViewById(R.id.sourceRecyclerview);

        // set recycler view invisible
        recyclerView.setVisibility(View.GONE);

        // set the progess bar to invisible
        pgLoadingIndication.setVisibility(View.GONE);

        if(articlesArrayList != null && articlesArrayList.size() > 0) {

            showJsonData();

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            adapter = new ArticlesAdapter(this, articlesArrayList);

            recyclerView.setAdapter(adapter);
        } else {
            showErrorMessage();
        }

    }

    // initialize method to going back home
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showJsonData() {
        recyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.GONE);
    }

    private void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}
