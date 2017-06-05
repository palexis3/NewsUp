package com.example.palexis3.newsup.Utilities;

import java.util.HashMap;
import java.util.Map;

public class Utility {

    private static final String NEWS_API_KEY = "77b5d1cccfc04ba0a312d832ee46b4cf";

    public static String getNewsApiKey() {
        return NEWS_API_KEY;
    }

    public static String getLanguage(String key) {

        if(key == null || key.length() == 0) return "N/A";

        Map<String, String> languageMap = new HashMap<>();

        languageMap.put("en", "English");
        languageMap.put("de", "German");

        return languageMap.containsKey(key) ? languageMap.get(key) : key;
    }

    public static String getCountry(String key) {

        if(key == null || key.length() == 0) return "N/A";

        Map<String, String> countryMap = new HashMap<>();

        countryMap.put("in", "India");
        countryMap.put("us", "USA");
        countryMap.put("gb", "United Kingdom");
        countryMap.put("de", "Germany");
        countryMap.put("au", "Australia");
        countryMap.put("it", "Italy");


        return countryMap.containsKey(key) ? countryMap.get(key) : key;
    }

    // converting numerical date to string based date
    public static String parseDate(String inputDate) {

        if(inputDate == null || inputDate.length() == 0) return "N/A";

        String[] arr = inputDate.split("-");
        if(arr.length != 3) return "N/A";

        HashMap<String, String> map = new HashMap<>();

        map.put("1", "January");
        map.put("2", "February");
        map.put("3", "March");
        map.put("4", "April");
        map.put("5", "May");
        map.put("6", "June");
        map.put("7", "July");
        map.put("8", "August");
        map.put("9", "September");
        map.put("10", "October");
        map.put("11", "November");
        map.put("12", "December");

        String month = arr[1].charAt(0) == '0' ? arr[1].substring(1) : arr[1];
        String res = String.format("%s %s, %s", map.get(month), arr[2], arr[0]);
        return res;
    }

}
