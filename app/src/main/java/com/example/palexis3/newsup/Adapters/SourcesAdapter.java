package com.example.palexis3.newsup.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.palexis3.newsup.Activities.SourceArticleListActivity;
import com.example.palexis3.newsup.Models.Articles;
import com.example.palexis3.newsup.Models.Sources;
import com.example.palexis3.newsup.Networking.NewsClient;
import com.example.palexis3.newsup.Networking.ServiceGenerator;
import com.example.palexis3.newsup.R;
import com.example.palexis3.newsup.Responses.NewsArticleResponse;
import com.example.palexis3.newsup.Utilities.Utility;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private ArrayList<Sources> sources;
    private Context context;
    private static String PHOTO_URL = "https://icons.better-idea.org/icon?url=%s&size=40..50..60";

    public SourcesAdapter(Context context, ArrayList<Sources> sources) {
        this.context = context;
        this.sources = sources;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_list_sources_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Sources source = sources.get(position);

        TextView name = holder.name;
        name.setText(source.getName());

        TextView category = holder.category;
        category.setText(source.getCategory());

        TextView sourceInfo = holder.sourceInfo;
        String info = String.format("%s | %s", Utility.getLanguage(source.getLanguage()), Utility.getCountry(source.getCountry()));
        sourceInfo.setText(info);

        ImageView imageView = holder.imageView;
        imageView.setImageResource(0);

        /** TODO: Using the MyAppGlideModule class, improve the quality of icons using Glide and improve alignment of textviews in cardviews */
        String url = String.format(PHOTO_URL, source.getUrl());
        Glide.with(context).load(url).into(imageView);
        //Picasso.with(context).load(url).into(imageView);
    }

    // get the list of sources list
    @Override
    public int getItemCount() {
        return (sources != null) ? sources.size() : 0;
    }


    // viewholder caching class for performance
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        ImageView imageView;
        TextView name;
        TextView category;
        TextView sourceInfo;

        public ViewHolder(View v) {
            super(v);

            cv = (CardView) v.findViewById(R.id.cv);
            imageView = (ImageView) v.findViewById(R.id.sourceImage);
            name = (TextView) v.findViewById(R.id.tvName);
            category = (TextView) v.findViewById(R.id.tvCategory);
            sourceInfo = (TextView) v.findViewById(R.id.tvSourceInfo);

            // attach click listener to this entire row
            itemView.setOnClickListener(this);
        }

        // launch an intent to get the list of articles for this source
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                final Sources source = sources.get(position);

                // create an instance of our news client
                NewsClient client = ServiceGenerator.createService(NewsClient.class);
                Call<NewsArticleResponse> call = client.getArticles(source.getId(), Utility.getNewsApiKey());

                call.enqueue(new Callback<NewsArticleResponse>() {
                    @Override
                    public void onResponse(Call<NewsArticleResponse> call, Response<NewsArticleResponse> response) {

                        if(response.isSuccessful()) {

                            NewsArticleResponse sourceResponse = response.body();
                            ArrayList<Articles> articlesArrayList = new ArrayList<>(sourceResponse.getArticles());

                            // launch an activity with the list of articles
                            Intent i = new Intent(context, SourceArticleListActivity.class); // <- SHOULD BE USING DAGGER FOR DEPENDENCY INJECTION
                            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("title", source.getName());
                            i.putExtra("sourcesList", Parcels.wrap(articlesArrayList));

                            // go to the SourceArticleListActivity
                            context.startActivity(i); // <- SHOULD BE USING DAGGER FOR DEPENDENCY INJECTION

                        } else{
                            Log.d("Source", call.request().body().toString());
                            Toast.makeText(context, "Cannot get articles at the moment!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsArticleResponse> call, Throwable t) {
                        Log.d("Source", call.request().body().toString());
                        Toast.makeText(context, "Cannot get articles at the moment!", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                /** TODO: Place a snackbar notifying the user, request cannot be made */
            }
        }
    }

}
