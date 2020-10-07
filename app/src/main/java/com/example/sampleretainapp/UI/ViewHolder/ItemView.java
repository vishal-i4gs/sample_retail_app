package com.example.sampleretainapp.UI.ViewHolder;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.SubItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ItemClickListener;

import java.util.Locale;

public class ItemView extends RecyclerView.ViewHolder {

    private TextView brandName;
    private TextView itemName;
    private Button quantities;
    private Button addItem;
    private Button removeItem;
    private TextView currentNumber;
    private TextView price;

    private Item item;

    public ItemView(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        brandName = itemView.findViewById(R.id.brand_name);
        itemName = itemView.findViewById(R.id.item_name);
        quantities = itemView.findViewById(R.id.item_quantities);
        quantities.setOnClickListener(view -> {
            AlertDialog.Builder builderSingle =
                    new AlertDialog.Builder(itemView.getContext());
            builderSingle.setTitle("Select One Name:-");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    itemView.getContext(),
                    android.R.layout.select_dialog_singlechoice);
            for (SubItem subItem : item.subItems) {
                arrayAdapter.add(subItem.name);
            }
            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                itemClickListener.subItemIndexChanged(which, getAdapterPosition());
            });
            builderSingle.show();
        });
        addItem = itemView.findViewById(R.id.item_add);
        addItem.setOnClickListener(view -> itemClickListener.addItem(getAdapterPosition()));
        removeItem = itemView.findViewById(R.id.item_remove);
        removeItem.setOnClickListener(view -> itemClickListener.removeItem(getAdapterPosition()));
        currentNumber = itemView.findViewById(R.id.item_current_number);
        price = itemView.findViewById(R.id.item_price);
    }

    public void setData(Item listItem, CartItem item, int subItemIndex) {
        this.item = listItem;
        brandName.setText(listItem.brand);
        itemName.setText(listItem.name);
        quantities.setText(listItem.subItems.get(subItemIndex).name);
        currentNumber.setText("0");
        String priceString = String.format(Locale.ENGLISH, "Rs %.1f",
                listItem.subItems.get(subItemIndex).price);
        price.setText(priceString);
        if (item != null) {
            currentNumber.setText(String.format(Locale.ENGLISH, "%d",
                    item.quantity));
        }
    }

}
