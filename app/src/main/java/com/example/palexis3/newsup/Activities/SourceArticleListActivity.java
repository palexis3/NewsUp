package com.example.palexis3.newsup.Activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palexis3.newsup.Adapters.ArticlesAdapter;
import com.example.palexis3.newsup.Models.Articles;
import com.example.palexis3.newsup.Models.Sources;
import com.example.palexis3.newsup.Networking.NewsClient;
import com.example.palexis3.newsup.Networking.ServiceGenerator;
import com.example.palexis3.newsup.R;
import com.example.palexis3.newsup.Responses.NewsArticleResponse;
import com.example.palexis3.newsup.Utilities.Utility;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceArticleListActivity extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    private Sources source;
    private String name;

    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pg_loading_indication) ProgressBar pgLoadingIndication;
    @BindView(R.id.sourceRecyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        ButterKnife.bind(this);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        source = (Sources) Parcels.unwrap(getIntent().getParcelableExtra("source"));
        name =  getIntent().getStringExtra("title");

        // set the title for this source with articles
        getSupportActionBar().setTitle(name);

        // add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // swipe refresh layout callback
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setViewsEmpty();
                setUp();
            }
        });

        // call setup for initialization
        setUp();
    }

    // initialize all necessary items
    private void setUp() {

        // check if device can connect to internet
        if(Utility.isOnline() && Utility.isNetworkAvailable(this)) {
            // initialize views
            initViews();
        } else {
            // set progress bar to invisible
            pgLoadingIndication.setVisibility(View.GONE);

            // Now we call setRefreshing(false) to signal refresh has finished
            swipeRefreshLayout.setRefreshing(false);

            // show error message
            showErrorMessage();
        }
    }

    public void initViews() {

        // set recycler view invisible
        recyclerView.setVisibility(View.GONE);

        // set the progess bar to invisible
        pgLoadingIndication.setVisibility(View.GONE);

        // get json items from server
        getJson();
    }

    // start network call
    private void getJson() {

        // create an instance of our news client
        NewsClient client = ServiceGenerator.createService(NewsClient.class);
        Call<NewsArticleResponse> call = client.getArticles(source.getId(), Utility.getNewsApiKey());

        call.enqueue(new Callback<NewsArticleResponse>() {
            @Override
            public void onResponse(Call<NewsArticleResponse> call, Response<NewsArticleResponse> response) {
                if(response.isSuccessful()) {

                    NewsArticleResponse sourceResponse = response.body();
                    ArrayList<Articles> articlesArrayList = new ArrayList<>(sourceResponse.getArticles());

                    if(articlesArrayList != null && articlesArrayList.size() > 0) {

                        showJsonData();

                        recyclerView.setLayoutManager(new LinearLayoutManager(SourceArticleListActivity.this));

                        adapter = new ArticlesAdapter(SourceArticleListActivity.this, articlesArrayList);

                        recyclerView.setAdapter(adapter);

                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeRefreshLayout.setRefreshing(false);
                        showErrorMessage();
                    }

                } else{
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d("Source", call.request().body().toString());
                    Toast.makeText(SourceArticleListActivity.this, "Cannot get articles at the moment!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NewsArticleResponse> call, Throwable t) {
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeRefreshLayout.setRefreshing(false);
                Log.d("Source", t.toString());
                Toast.makeText(SourceArticleListActivity.this, "Cannot get articles at the moment!", Toast.LENGTH_LONG).show();
            }
        });
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

    // sets all views invisible
    private void setViewsEmpty() {
        recyclerView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
    }

    // show the json data and remove error message
    private void showJsonData() {
        recyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.GONE);
    }

    // show the error message and don't show the json data
    private void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}
