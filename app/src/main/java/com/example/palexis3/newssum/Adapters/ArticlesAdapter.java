package com.example.palexis3.newssum.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.browser.customtabs.CustomTabsIntent;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.palexis3.newssum.Models.Articles;
import com.example.palexis3.newssum.R;
import com.example.palexis3.newssum.Utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int PIC = 0, NO_PIC = 1;

    private Context context;
    private ArrayList<Articles> articlesArrayList;

    public ArticlesAdapter(Context context, ArrayList<Articles> articlesArrayList) {
        this.context = context;
        this.articlesArrayList = articlesArrayList;
    }

    // initialize view holder for appropriate cardview
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       RecyclerView.ViewHolder viewHolder = null;
       LayoutInflater inflater = LayoutInflater.from(parent.getContext());

       switch (viewType) {

           case PIC:
               View v = inflater.inflate(R.layout.cardview_list_articles_item, parent, false);
               viewHolder = new ViewHolderPic(v);
               break;
           case NO_PIC:
               View v2 = inflater.inflate(R.layout.cardview_list_articles_item_no_pic, parent, false);
               viewHolder = new ViewHolderNoPic(v2);
               break;

       }

        return viewHolder;
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {

        final Articles article = articlesArrayList.get(position);

        // return the ArticleNoPic cardview
        if(article.getUrlToImage() == null || article.getUrlToImage().length() == 0) {
            return NO_PIC;
        } else {
            // this article has all of the necessary items
            return PIC;
        }
    }

    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the
     * RecyclerView.ViewHolder contents with the item at the given position
     * and also sets up some private fields to be used by RecyclerView.
     *
     * @param 'viewHolder' The type of RecyclerView.ViewHolder to populate
     * @param position Item position in the viewgroup.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case PIC:
                ViewHolderPic vh1 = (ViewHolderPic) holder;
                configureCardviewWithPic(vh1, position);
                break;

            case NO_PIC:
                ViewHolderNoPic vh2 = (ViewHolderNoPic) holder;
                configureCardviewWithNoPic(vh2, position);
                break;
        }

    }


    private void configureCardviewWithNoPic(ViewHolderNoPic holder, int position) {

        final Articles article = articlesArrayList.get(position);

        TextView title = holder.title;
        title.setText(article.getTitle());


        TextView author = holder.author;
        if(article.getAuthor() != null) {
            String str = String.format("Written by %s", article.getAuthor());
            author.setText(str);
        }

        // set up sharing listener
        ImageView share = holder.share;
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUrl(article.getUrl());
            }
        });
    }


    private void configureCardviewWithPic(ViewHolderPic holder, int position) {

        final Articles article = articlesArrayList.get(position);

        TextView title = holder.title;
        title.setText(article.getTitle());

        // parsing date
        TextView date = holder.date;
        if(article.getPublishedAt() != null) {
            String[] arr = article.getPublishedAt().split("T");
            date.setText(Utility.parseDate(arr[0]));
        }

        ImageView poster = holder.poster;
        poster.setImageResource(0);

        // load poster image
        Glide.with(context).load(article.getUrlToImage())
                .placeholder(R.drawable.playstore_icon)
                .error(R.drawable.playstore_icon)
                .into(poster);

        // set up sharing listener
        ImageView share = holder.share;
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUrl(article.getUrl());
            }
        });
    }

    // implicit intent for sharing article
    private void shareUrl(String url) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(shareIntent, "Share this article!"));
    }

    @Override
    public int getItemCount() {
        return (articlesArrayList != null) ? articlesArrayList.size() : 0;
    }


    public class ViewHolderPic extends RecyclerView.ViewHolder {


        @BindView(R.id.cvArticles) CardView cv;
        @BindView(R.id.ivArticleImage) ImageView poster;
        @BindView(R.id.tvArticleTitle) TextView title;
        @BindView(R.id.tvArticleDate) TextView date;
        @BindView(R.id.iv_share) ImageView share;

        public ViewHolderPic(View v) {
            super(v);

            // bind views via butterknife
            ButterKnife.bind(this, v);

            // attach an on click listener for this article
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION && Utility.isNetworkAvailable(context) && Utility.isOnline()) {

                        // launch article chrome function
                        final Articles article = articlesArrayList.get(position);
                        getArticle(article.getUrl());

                    } else {
                         /*  place a snack bar that request cannot be made */
                        Snackbar.make(v, R.string.error_message, Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
    }

    public class ViewHolderNoPic extends RecyclerView.ViewHolder {

        @BindView(R.id.tvArticleTitle) TextView title;
        @BindView(R.id.cvArticlesNoPic) CardView cv;
        @BindView(R.id.tvArticleAuthor) TextView author;
        @BindView(R.id.iv_share) ImageView share;

        public ViewHolderNoPic(View v) {
            super(v);

            // bind views via butterknife
            ButterKnife.bind(this, v);

            // attach an on click listener for this article
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION && Utility.isNetworkAvailable(context) && Utility.isOnline()) {

                        // launch article chrome function
                        final Articles article = articlesArrayList.get(position);
                        getArticle(article.getUrl());
                    } else {
                        /*  place a snack bar that request cannot be made */
                        Snackbar.make(v, R.string.error_message, Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            });

        }
    }

    // method launches a chrome tab
    public void getArticle(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        // set toolbar color and/or setting custom actions before invoking build()
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();

        // set toolbar color
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.red));

        // add share action to menu list
        builder.addDefaultShareMenuItem();

        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
