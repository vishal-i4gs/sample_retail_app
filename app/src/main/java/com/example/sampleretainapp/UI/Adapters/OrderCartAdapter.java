package com.example.sampleretainapp.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CartItemOffer;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ViewHolder.OrderCartViewHolder;
import com.example.sampleretainapp.UI.ViewModel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderCartAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CartItemOffer> list = new ArrayList<>();

    private AppViewModel appViewModel;

    public OrderCartAdapter(AppViewModel appViewModel) {
        this.appViewModel = appViewModel;
    }

    public void setList(List<CartItemOffer> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderCartItem = LayoutInflater
                .from(parent.getContext()).inflate(
                        R.layout.order_cart_item,
                        parent, false);
        return new OrderCartViewHolder(orderCartItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderCartViewHolder viewHolder = (OrderCartViewHolder) holder;
        viewHolder.setData(list.get(position).item,list.get(position).cart, list.get(position).offer);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
