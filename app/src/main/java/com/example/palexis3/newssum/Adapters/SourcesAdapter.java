package com.example.palexis3.newssum.Adapters;


import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.example.palexis3.newssum.Activities.SourceArticleListActivity;
import com.example.palexis3.newssum.Models.Sources;
import com.example.palexis3.newssum.R;
import com.example.palexis3.newssum.Utilities.Utility;

import org.parceler.Parcels;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private ArrayList<Sources> sources;
    private Context context;
    private static String PHOTO_URL = "https://icons.better-idea.org/icon?url=%s&size=40..50..60";

    // store a reference to the ListItemClickListener interface
    private final ListItemClickListener mOnClickListener ;

    public SourcesAdapter(Context context, ArrayList<Sources> sources, ListItemClickListener listener) {
        this.context = context;
        this.sources = sources;
        mOnClickListener = listener;
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

        String url = String.format(PHOTO_URL, source.getUrl());
        Glide.with(context).load(url)
                .asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(90, 90)
                .placeholder(R.drawable.playstore_icon)
                .override(80, 80)
                .dontAnimate()
                .error(R.drawable.playstore_icon)
                .into(imageView);
    }

    // get the list of sources list
    @Override
    public int getItemCount() {
        return (sources != null) ? sources.size() : 0;
    }

    // create an interface for clicking items
    public interface ListItemClickListener {
        void onListItemClick(int clickedItem);
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
            v.setOnClickListener(this);
        }

        // launch an intent to launch SourceArticleList activity
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if(position != RecyclerView.NO_POSITION && Utility.isNetworkAvailable(context) && Utility.isOnline() ) {
                final Sources source = sources.get(position);
                mOnClickListener.onListItemClick(position);
                Intent i = new Intent(context, SourceArticleListActivity.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("source", Parcels.wrap(source));
                i.putExtra("title", source.getName());
                context.startActivity(i); // <-- PROBABLY SHOULD BE USING DAGGER
            } else {
                /*Place a snackbar notifying the user, request cannot be made */
                Snackbar.make(v, R.string.error_message, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

}
