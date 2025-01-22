package com.daus.catering.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daus.catering.R;
import com.daus.catering.order.OrderActivity;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    List<ModelCategories> modelCategoriesList;
    Context ctx;

    public CategoriesAdapter(Context context, List<ModelCategories> items) {
        this.ctx = context;
        this.modelCategoriesList = items;
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_categories, parent, false);
        return new CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.ViewHolder holder, int position) {
        final ModelCategories data = modelCategoriesList.get(position);
        holder.imageIcon.setImageResource(data.getiIcon());
        holder.tvName.setText(data.getStrName());

        holder.cvCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(ctx, OrderActivity.class));
                intent.putExtra(OrderActivity.DATA_TITLE, data.getStrName());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelCategoriesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cvCategories;
        public TextView tvName;
        public ImageView imageIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            cvCategories = itemView.findViewById(R.id.cvCategories);
            tvName = itemView.findViewById(R.id.tvName);
            imageIcon = itemView.findViewById(R.id.imageIcon);
        }
    }

}
