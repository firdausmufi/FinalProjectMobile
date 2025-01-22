package com.daus.catering.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.daus.catering.R;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<Order> orders;
    private OnOrderClickListener onOrderClickListener;

    public HistoryAdapter(Context context, List<Order> orders, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.orders = orders;
        this.onOrderClickListener = onOrderClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvUsername.setText(order.getUsername());
        holder.tvOrderDate.setText(order.getOrderDate());

        // Display order details (items, quantity, price)
        List<OrderItem> orderItems = order.getOrderDetails();
        StringBuilder orderDetailsText = new StringBuilder();
        for (OrderItem item : orderItems) {
            orderDetailsText.append(item.getItem())
                    .append(" (Qty: ").append(item.getQuantity())
                    .append(", Price: ").append(item.getPrice())
                    .append(")\n");
        }
        holder.tvOrderDetails.setText(orderDetailsText.toString());

        holder.itemView.setOnClickListener(v -> {
            onOrderClickListener.onOrderClick(order);
        });

        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> {
            orders.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orders.size());
        });
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvOrderDate, tvOrderDetails;
        ImageView btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderDetails = itemView.findViewById(R.id.tvOrderDetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }


    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }
}
