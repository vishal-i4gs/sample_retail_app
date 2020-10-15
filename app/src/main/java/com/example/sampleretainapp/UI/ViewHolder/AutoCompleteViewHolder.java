package com.example.sampleretainapp.UI.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ItemClickListener;

public class AutoCompleteViewHolder extends RecyclerView.ViewHolder {

    private TextView title;

    public AutoCompleteViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.auto_complete_title);
    }

    public void setData(String title) {
        this.title.setText(title);
    }

}
