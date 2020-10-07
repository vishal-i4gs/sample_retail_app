package com.example.sampleretainapp.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ViewHolder.CartItemView;
import com.example.sampleretainapp.UI.ViewHolder.ItemView;
import com.example.sampleretainapp.ViewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {

    private MainActivityViewModel mainActivityViewModel;
    private List<CartItem> list = new ArrayList<>();
    ArrayList<CartItemView> myViewHolders = new ArrayList<>();
    ArrayList<Integer> currentListItemSelection = new ArrayList<>();

    CartAdapter(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }

    void setList(List<CartItem> list) {
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
                        R.layout.retail_cart_item,
                        parent, false);
        return new CartItemView(retailItem, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartItemView viewHolder = (CartItemView) holder;
        viewHolder.setData(list.get(position).parentItem, list.get(position),
                list.get(position).subItemIndex);
        if (myViewHolders.size() > position) {
            myViewHolders.remove(position);
        }
        myViewHolders.add(position, viewHolder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void addItem(int position) {
        mainActivityViewModel.addItem(list.get(position).subItemIndex,
                list.get(position).parentItem);
        notifyItemChanged(position);
    }

    @Override
    public void removeItem(int position) {
        mainActivityViewModel.removeItem(list.get(position).subItemIndex,
                list.get(position).parentItem);
        notifyItemChanged(position);
    }

    @Override
    public void subItemIndexChanged(int subIndex, int position) {
        currentListItemSelection.set(position, subIndex);
        notifyItemChanged(position);
    }

}

