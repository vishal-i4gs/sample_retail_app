package com.example.sampleretainapp.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.example.sampleretainapp.UI.ViewHolder.OrderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ItemClickListener itemClickListener;

    public OrderAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private List<OrderItem> list = new ArrayList<>();

    public void setList(List<OrderItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderItem = LayoutInflater
                .from(parent.getContext()).inflate(
                        R.layout.order_item,
                        parent, false);
        return new OrderViewHolder(orderItem,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderViewHolder viewHolder = (OrderViewHolder) holder;
        viewHolder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
