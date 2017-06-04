package com.example.palexis3.newsup.Adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.palexis3.newsup.Models.Sources;
import com.example.palexis3.newsup.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private ArrayList<Sources> sources;
    private Context context;

    public SourcesAdapter(Context context, ArrayList<Sources> sources) {
        this.context = context;
        this.sources = sources;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_news_sources, parent, false);
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

        ImageView imageView = holder.imageView;
        imageView.setImageResource(0);

        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);

    }

    // get the list of sources list
    @Override
    public int getItemCount() {
        return (sources != null) ? sources.size() : 0;
    }


    // viewholder caching class for performance
    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView imageView;
        TextView name;
        TextView category;

        public ViewHolder(View v) {
            super(v);

            cv = (CardView) v.findViewById(R.id.cv);
            imageView = (ImageView) v.findViewById(R.id.sourceImage);
            name = (TextView) v.findViewById(R.id.tvName);
            category = (TextView) v.findViewById(R.id.tvCategory);
        }

    }

}
