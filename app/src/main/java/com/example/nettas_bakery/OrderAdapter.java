package com.example.nettas_bakery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView orderKeyTextView;
        private TextView statusTextView;
        private TextView totalTextView;
        private TextView userPhoneTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderKeyTextView = itemView.findViewById(R.id.orderKeyTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            totalTextView = itemView.findViewById(R.id.totalPriceTextView);
            userPhoneTextView = itemView.findViewById(R.id.phoneNumberTextView);
        }

        public void bind(Order order) {
            orderKeyTextView.setText("Order : " + order.getOrderkey());
            statusTextView.setText("Status: " + order.getStatus());
            totalTextView.setText("Total: " + order.getTotalPrice());
            userPhoneTextView.setText("User Phone: " + order.getUserPhone());
        }
    }
}