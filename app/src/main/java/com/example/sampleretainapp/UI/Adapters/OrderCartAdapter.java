package com.example.sampleretainapp.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ViewHolder.OrderCartViewHolder;
import com.example.sampleretainapp.UI.ViewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderCartAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CartItem> list = new ArrayList<>();

    private MainActivityViewModel mainActivityViewModel;

    public OrderCartAdapter(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }

    public void setList(List<CartItem> list) {
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
        viewHolder.setData(list.get(position),list.get(position).offerItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
