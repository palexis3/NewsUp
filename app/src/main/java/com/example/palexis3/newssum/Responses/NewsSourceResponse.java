package com.example.palexis3.newssum.Responses;


import com.example.palexis3.newssum.Models.Sources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NewsSourceResponse {

    @SerializedName("sources")
    private List<Sources> sources;

    public NewsSourceResponse() {
        sources = new ArrayList<>();
    }

    public List<Sources> getSources() {
        return sources;
    }

    public static NewsSourceResponse parseJson(String response) throws JSONException {
        Gson gson = new GsonBuilder().create();
        NewsSourceResponse sourceResponse = gson.fromJson(response, NewsSourceResponse.class);
        return sourceResponse;
    }

}
