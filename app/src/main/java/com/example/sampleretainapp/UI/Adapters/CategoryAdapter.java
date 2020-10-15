package com.example.sampleretainapp.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CategoryItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ViewHolder.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryItem> list = new ArrayList<>();


    public void setList(List<CategoryItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View retailItem = LayoutInflater
                .from(parent.getContext()).inflate(
                        R.layout.category_item,
                        parent, false);
        return new CategoryViewHolder(retailItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
        viewHolder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
