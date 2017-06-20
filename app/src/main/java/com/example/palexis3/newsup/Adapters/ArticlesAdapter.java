package com.example.palexis3.newsup.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.palexis3.newsup.Activities.ArticleWebViewActivity;
import com.example.palexis3.newsup.Models.Articles;
import com.example.palexis3.newsup.R;
import com.example.palexis3.newsup.Utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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

        /*
        TextView description = holder.description;
        description.setMovementMethod(new ScrollingMovementMethod());
        if(article.getDescription() != null) {
            description.setText(article.getDescription());
        } else {
            description.setMaxHeight(1);
            description.setTextColor(ContextCompat.getColor(context, R.color.grey));
            description.setText("Nothing.");
        }
        */


        TextView author = holder.author;
        if(article.getAuthor() != null) {
            String str = String.format("Written by %s", article.getAuthor());
            author.setText(str);
        }
    }


    private void configureCardviewWithPic(ViewHolderPic holder, int position) {

        final Articles article = articlesArrayList.get(position);

        TextView title = holder.title;
        title.setText(article.getTitle());

        // parsing date
        TextView date = holder.date;
        // && article.getPublishedAt().length() == 2
        if(article.getPublishedAt() != null) {
            String[] arr = article.getPublishedAt().split("T");
            date.setText(Utility.parseDate(arr[0]));
        }


        ImageView poster = holder.poster;
        poster.setImageResource(0);

        // load poster image
        Glide.with(context).load(article.getUrlToImage()).into(poster);
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

        public ViewHolderPic(View v) {
            super(v);

            // bind views via butterknife
            ButterKnife.bind(this, v);

            // attach an on click listenr for this article
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    /** TODO: USE DAGGER INSTEAD OF CONTEXT FOR SPAWNING INTENT */
                    if(position != RecyclerView.NO_POSITION) {

                        // launch article intent
                        final Articles article = articlesArrayList.get(position);
                        Intent i = new Intent(context, ArticleWebViewActivity.class);  // <- USE DAGGER FOR DEPENDENCY INJECTION
                        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("title", article.getTitle());
                        i.putExtra("url", article.getUrl());

                        context.startActivity(i); // <- USE DAGGER FOR DEPENDENCY INJECTION

                    } else {
                        /** TODO: place a snack bar that request cannot be made */
                    }
                }
            });
        }
    }

    public class ViewHolderNoPic extends RecyclerView.ViewHolder {

        @BindView(R.id.tvArticleTitle) TextView title;
        @BindView(R.id.cvArticlesNoPic) CardView cv;
        //@BindView(R.id.tvArticleDescription) TextView description;
        @BindView(R.id.tvArticleAuthor) TextView author;

        public ViewHolderNoPic(View v) {
            super(v);

            // bind views via butterknife
            ButterKnife.bind(this, v);

            // attach an on click listenr for this article
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    /** TODO: USE DAGGER INSTEAD OF CONTEXT FOR SPAWNING INTENT */
                    if(position != RecyclerView.NO_POSITION) {

                        // launch article intent
                        final Articles article = articlesArrayList.get(position);
                        Intent i = new Intent(context, ArticleWebViewActivity.class);  // <- USE DAGGER FOR DEPENDENCY INJECTION
                        i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("title", article.getTitle());
                        i.putExtra("url", article.getUrl());

                        context.startActivity(i); // <- USE DAGGER FOR DEPENDENCY INJECTION

                    } else {
                        /** TODO: place a snack bar that request cannot be made */
                    }
                }
            });

        }
    }
}
