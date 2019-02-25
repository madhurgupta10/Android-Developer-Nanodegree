package com.example.project3.popularmoviesstage2;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    final private listItemClickListener onClickListener;
    private int numberItems;
    private List<String> urls;
    private Context context;
    private ProgressBar progressBar;

    public interface listItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MyAdapter(int numberOfItems, List<String> posterUrls, listItemClickListener listener, Context context, ProgressBar progressBar) {
        this.context = context;
        this.urls = posterUrls;
        this.onClickListener = listener;
        this.numberItems = numberOfItems;
        this.progressBar = progressBar;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForList = R.layout.grid_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForList, viewGroup, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            if (urls != null) {
                Picasso.with(context)
                        .load("https://image.tmdb.org/t/p/w185/"+urls.get(position))
                        .into(imageView);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(clickedPosition);
        }
    }
}
