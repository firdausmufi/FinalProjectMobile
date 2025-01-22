package com.daus.catering.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.daus.catering.R;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder>{

    List<ModelTrending> modelTrendingList;
    Context ctx;

    public TrendingAdapter(Context context, List<ModelTrending> items) {
        this.ctx = context;
        this.modelTrendingList = items;
    }

    @Override
    public TrendingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trending, parent, false);
        return new TrendingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrendingAdapter.ViewHolder holder, int position) {
        final ModelTrending data = modelTrendingList.get(position);
        holder.imgThumb.setImageResource(data.getImgThumb());
        holder.tvPlaceName.setText(data.getTvPlaceName());
        holder.tvVote.setText(data.getTvVote());
    }

    @Override
    public int getItemCount() {
        return modelTrendingList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPlaceName, tvVote;
        public ImageView imgThumb;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPlaceName = itemView.findViewById(R.id.tvPlaceName);
            tvVote = itemView.findViewById(R.id.tvVote);
            imgThumb = itemView.findViewById(R.id.imgThumb);
        }
    }

}
