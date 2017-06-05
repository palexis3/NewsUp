package com.example.palexis3.newsup.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.palexis3.newsup.Adapters.ArticlesListAdapter;
import com.example.palexis3.newsup.Models.Articles;
import com.example.palexis3.newsup.R;

import org.parceler.Parcels;

import java.util.ArrayList;

public class SourcesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private ArrayList<Articles> articlesArrayList;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ArticlesListAdapter(this, articlesArrayList);

        recyclerView.setAdapter(adapter);

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
}
