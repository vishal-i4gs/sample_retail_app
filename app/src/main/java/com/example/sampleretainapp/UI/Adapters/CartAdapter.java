package com.example.sampleretainapp.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.example.sampleretainapp.UI.ViewHolder.ItemView;
import com.example.sampleretainapp.UI.ViewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {

    private MainActivityViewModel mainActivityViewModel;
    private ItemClickListener itemClickListener;
    private List<CartItem> list = new ArrayList<>();
    ArrayList<Integer> currentListItemSelection = new ArrayList<>();

    public CartAdapter(MainActivityViewModel mainActivityViewModel, ItemClickListener itemClickListener) {
        this.mainActivityViewModel = mainActivityViewModel;
        this.itemClickListener = itemClickListener;
    }

    public void setList(List<CartItem> list) {
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            currentListItemSelection.add(0);
        }
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
        viewHolder.setData(list.get(position), list.get(position).offerItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void addItem(int position) {
        mainActivityViewModel.addItem(list.get(position).item,mainActivityViewModel.getOfferItem(list.get(position).item));
    }

    @Override
    public void removeItem(int position) {
        mainActivityViewModel.removeItem(list.get(position).item);
    }

    @Override
    public void itemClicked(int position) {
        itemClickListener.itemClicked(list.get(position).item);
    }

}

