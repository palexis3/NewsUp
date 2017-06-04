package com.example.palexis3.newsup.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.palexis3.newsup.Adapters.SourcesAdapter;
import com.example.palexis3.newsup.Models.Sources;
import com.example.palexis3.newsup.Networking.NewsClient;
import com.example.palexis3.newsup.R;
import com.example.palexis3.newsup.Responses.NewsSourceResponse;
import com.example.palexis3.newsup.Networking.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String NEWS_API_KEY = "77b5d1cccfc04ba0a312d832ee46b4cf";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Sources> sourcesArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    public void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        // apply grid layout manager with 3 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        // get json items from server
        getJson();
    }

    public void getJson() {

        // create an instance of our news client
        NewsClient client = ServiceGenerator.createService(NewsClient.class);
        Call<NewsSourceResponse> call = client.getSources();

        call.enqueue(new Callback<NewsSourceResponse>() {
            @Override
            public void onResponse(Call<NewsSourceResponse> call, Response<NewsSourceResponse> response) {

                if(response.isSuccessful()) {
                    NewsSourceResponse sourceResponse = response.body();
                    sourcesArrayList = new ArrayList<>(sourceResponse.getSources());

                    // now add list of sources to grid layout recycler view, using adapter
                    adapter = new SourcesAdapter(getApplicationContext(), sourcesArrayList);

                    // add adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                } else {

                    Log.d("Call", call.request().body().toString());
                }

            }

            @Override
            public void onFailure(Call<NewsSourceResponse> call, Throwable t) {
                Log.d("Call", t.getMessage());
            }
        });
    }
}
