package com.example.palexis3.newsup.Responses;


import com.example.palexis3.newsup.Models.Sources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class NewsSourceResponse {

    private List<Sources> sources;

    public NewsSourceResponse() {
        sources = new ArrayList<>();
    }

    public List<Sources> getSources() {
        return sources;
    }

    public static NewsSourceResponse parseJson(String response) {
        Gson gson = new GsonBuilder().create();
        NewsSourceResponse sourceResponse = gson.fromJson(response, NewsSourceResponse.class);
        return sourceResponse;
    }

}
