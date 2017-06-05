package com.example.palexis3.newsup.Adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.palexis3.newsup.Models.Articles;
import com.example.palexis3.newsup.R;
import com.example.palexis3.newsup.Utilities.Utility;

import java.util.ArrayList;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Articles> articlesArrayList;

    public ArticlesListAdapter(Context context, ArrayList<Articles> articlesArrayList) {
        this.context = context;
        this.articlesArrayList = articlesArrayList;
    }

    // initialize view holder for cardview items
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_articles, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Articles article = articlesArrayList.get(position);

        TextView title = holder.title;
        title.setText(article.getTitle());

        // parsing date
        TextView date = holder.date;
        if(article.getPublishedAt() != null && article.getPublishedAt().length() > 0) {
            String[] arr = article.getPublishedAt().split("T");
            date.setText(Utility.parseDate(arr[0]));
        } else {
            date.setText("N/A");
        }

        ImageView poster = holder.poster;
        poster.setImageResource(0);

        if(article.getUrlToImage() != null && article.getUrlToImage().length() > 0) {
            // load poster image
            Glide.with(context).load(article.getUrlToImage()).into(poster);
        } else {
            Glide.with(context).load(R.drawable.not_found).into(poster);
        }

    }

    @Override
    public int getItemCount() {
        return (articlesArrayList != null) ? articlesArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView poster;
        TextView title;
        TextView date;

        public ViewHolder(View v) {
            super(v);

            cv = (CardView) v.findViewById(R.id.cvArticles);
            poster = (ImageView) v.findViewById(R.id.ivArticleImage);
            title = (TextView) v.findViewById(R.id.tvArticleTitle);
            date = (TextView) v.findViewById(R.id.tvArticleDate);
        }

    }
}
