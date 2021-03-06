package com.example.palexis3.newssum.Models;

import org.parceler.Parcel;

@Parcel
public class Sources {

    String id;
    String name;
    String description;
    String url;
    String category;
    String language;
    String country;

    // empty constructor used for parceler library
    public Sources(){};

    public String getId() { return id; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

}
