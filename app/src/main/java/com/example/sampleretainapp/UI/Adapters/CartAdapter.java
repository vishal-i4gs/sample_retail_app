package com.example.sampleretainapp.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CartItemOffer;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.example.sampleretainapp.UI.ViewHolder.ItemView;
import com.example.sampleretainapp.UI.ViewModel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {

    private AppViewModel appViewModel;
    private ItemClickListener itemClickListener;
    private List<CartItemOffer> list = new ArrayList<>();

    public CartAdapter(AppViewModel appViewModel, ItemClickListener itemClickListener) {
        this.appViewModel = appViewModel;
        this.itemClickListener = itemClickListener;
    }

    public void setList(List<CartItemOffer> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View retailItem = LayoutInflater
                .from(parent.getContext()).inflate(
                        R.layout.retail_item,
                        parent, false);
        return new ItemView(retailItem, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemView viewHolder = (ItemView) holder;
        viewHolder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void addItem(int position) {
        appViewModel.addItem(list.get(position).item);
    }

    @Override
    public void removeItem(int position) {
        appViewModel.removeItem(list.get(position).item);
    }

    @Override
    public void itemClicked(int position) {
        itemClickListener.itemClicked(list.get(position).item);
    }

}

