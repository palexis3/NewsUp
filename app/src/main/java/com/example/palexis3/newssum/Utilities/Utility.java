package com.example.palexis3.newssum.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

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

        // not a valid date, so just return the current date
        if(arr[0].equals("0001")) {
            DateFormat dateFormat = new SimpleDateFormat("MM dd yyyy");
            Date date = new Date();

            String format = dateFormat.format(date);
            String[] temp_arr = format.split(" ");

            String ret = String.format("%s %s, %s", map.get(getMonth(temp_arr[0])), temp_arr[1], temp_arr[2]);

            return ret;
        }

        String res = String.format("%s %s, %s", map.get(getMonth(arr[1])), arr[2], arr[0]);
        return res;
    }

    public static String getMonth(String str) {
        return str.charAt(0) == '0' ? str.substring(1) : str;
    }

    public static String getLangSources(String lang) {

        if(lang == null || lang.length() == 0) return "";

        HashMap<String, String> map = new HashMap<>();

        map.put("English", "en");
        map.put("German", "de");
        map.put("French", "fr");

        return map.get(lang);
    }

    public static String getCountrySources(String country) {

        if(country == null || country.length() == 0) return "";

        HashMap<String, String> map = new HashMap<>();

        map.put("Australia", "au");
        map.put("Germany", "de");
        map.put("India", "in");
        map.put("Italy", "it");
        map.put("USA", "us");

        return map.get(country);
    }

    public static String getCategorySources(String category) {

        if(category == null ||category.length() == 0) return "";

        HashMap<String, String> map = new HashMap<>();

        map.put("Business", "business");
        map.put("Entertainment", "entertainment");
        map.put("Gaming", "gaming");
        map.put("General", "general");
        map.put("Music", "music");
        map.put("Politics", "politics");
        map.put("Science and Nature", "science-and-nature");
        map.put("Sport", "sport");
        map.put("Technology", "technology");

        return map.get(category);
    }

    // this method checks if there is internet connectivity on the device
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch(IOException e) {e.printStackTrace();}
          catch (InterruptedException e) {e.printStackTrace();}
        return false;
    }

    // additional method to check connectivity
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
