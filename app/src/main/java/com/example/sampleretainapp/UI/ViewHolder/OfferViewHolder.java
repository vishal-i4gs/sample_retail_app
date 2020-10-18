package com.example.sampleretainapp.UI.ViewHolder;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ItemClickListener;

public class OfferViewHolder extends RecyclerView.ViewHolder {

    private ImageView offerImage;
    private TextView offerName;

    public OfferViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        offerImage = itemView.findViewById(R.id.category_image);
        offerName = itemView.findViewById(R.id.category_name);
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.itemClicked(getAdapterPosition());
            }
        });
    }

    public void setData(Offer categoryItem) {
        offerImage.getBackground().setColorFilter(categoryItem.color, PorterDuff.Mode.SRC_OVER);
        offerName.setText(categoryItem.offerName);
    }
}
