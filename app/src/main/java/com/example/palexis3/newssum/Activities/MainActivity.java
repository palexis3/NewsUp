package com.example.palexis3.newssum.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.palexis3.newssum.Adapters.SourcesAdapter;
import com.example.palexis3.newssum.Fragments.FilterDialogFragment;
import com.example.palexis3.newssum.Models.Sources;
import com.example.palexis3.newssum.Networking.NewsClient;
import com.example.palexis3.newssum.Networking.ServiceGenerator;
import com.example.palexis3.newssum.R;
import com.example.palexis3.newssum.Responses.NewsSourceResponse;
import com.example.palexis3.newssum.Utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SourcesAdapter.ListItemClickListener {

    private RecyclerView.Adapter adapter;
    private ArrayList<Sources> sourcesArrayList;
    private SourcesAdapter.ListItemClickListener listener;

    @BindView(R.id.tv_error_message) TextView mErrorMessage;
    @BindView(R.id.pg_loading_indication) ProgressBar mLoadingIndication;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // set our list item click listener
        listener = this;

        // call setup for initialization
        setUp();

        // swipe refresh layout callback
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setViewsEmpty();
                setUp();
            }
        });
    }

    private void setUp() {
        // check if device can connect to internet
        if(Utility.isOnline() && Utility.isNetworkAvailable(this)) {
            initViews();
        } else {
            // set progress bar to invisible
            mLoadingIndication.setVisibility(View.GONE);

            // Now we call setRefreshing(false) to signal refresh has finished
            swipeRefreshLayout.setRefreshing(false);

            // show error message
            showErrorMessage();
        }
    }

    private void initViews() {

        recyclerView.setHasFixedSize(true);

        // apply grid layout manager with 3 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        // set recyclerview to invisible
        recyclerView.setVisibility(View.GONE);

        // get json items from server
        getJson();
    }

    private void getJson() {

        // create an instance of our news client
        NewsClient client = ServiceGenerator.createService(NewsClient.class);
        Call<NewsSourceResponse> call = client.getSources();

        call.enqueue(new Callback<NewsSourceResponse>() {
            @Override
            public void onResponse(Call<NewsSourceResponse> call, Response<NewsSourceResponse> response) {

                // set progress bar to invisible
                mLoadingIndication.setVisibility(View.GONE);

                if(response.isSuccessful()) {

                    NewsSourceResponse sourceResponse = response.body();
                    sourcesArrayList = new ArrayList<>(sourceResponse.getSources());

                    if(sourcesArrayList != null && sourcesArrayList.size() > 0) {

                        showJsonData();

                        // now add list of sources to grid layout recycler view, using adapter
                        adapter = new SourcesAdapter(getApplicationContext(), sourcesArrayList, listener);

                        // add adapter to recyclerview
                        recyclerView.setAdapter(adapter);
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeRefreshLayout.setRefreshing(false);
                        showErrorMessage();
                    }
                } else {
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeRefreshLayout.setRefreshing(false);
                    showErrorMessage();
                    Log.d("Call", call.request().body().toString());
                }
            }

            @Override
            public void onFailure(Call<NewsSourceResponse> call, Throwable t) {
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage();
                Log.d("Call", t.getMessage());
            }
        });
    }

    // keep this empty since there is no appropriate function
    // for the ListItemClickListener
    @Override
    public void onListItemClick(int clickedItem) {
    }

    // sets all views invisible
    private void setViewsEmpty() {
        mErrorMessage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    // method shows the current status of items
    private void showJsonData() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    // there was an error, so show appropriate items
    private void showErrorMessage() {
        recyclerView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.miFilter:
                showFilterDialog();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    // method shows the filter dialog
    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance("Filter");
        filterDialogFragment.show(fm, "filter");
    }
}
