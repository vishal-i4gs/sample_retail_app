package com.example.sampleretainapp.UI.ViewHolder;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.CartItemOffer;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ItemClickListener;

import java.util.Locale;

public class ItemView extends RecyclerView.ViewHolder {

    private TextView itemName;
    private TextView imageName;
    private TextView quantities;
    private Button addButton;
    private View controlButton;
    private TextView currentNumber;
    private TextView price;
    private TextView offerText;

    public ItemView(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        itemName = itemView.findViewById(R.id.item_name);
        imageName = itemView.findViewById(R.id.image_item_name);
        quantities = itemView.findViewById(R.id.item_quantities);
        controlButton = itemView.findViewById(R.id.control_buttons);
        addButton = itemView.findViewById(R.id.add_button);
        Button addItem = itemView.findViewById(R.id.item_add);
        addItem.setOnClickListener(view -> itemClickListener.addItem(getAdapterPosition()));
        addButton.setOnClickListener(view -> itemClickListener.addItem(getAdapterPosition()));
        Button removeItem = itemView.findViewById(R.id.item_remove);
        removeItem.setOnClickListener(view -> itemClickListener.removeItem(getAdapterPosition()));
        currentNumber = itemView.findViewById(R.id.item_current_number);
        price = itemView.findViewById(R.id.item_price);
        offerText = itemView.findViewById(R.id.item_offer_text);
        itemView.setOnClickListener(view -> itemClickListener.itemClicked(getAdapterPosition()));
    }

    public void setData(Item listItem, CartItem item, Offer offerItem) {
        addButton.setVisibility(View.VISIBLE);
        controlButton.setVisibility(View.GONE);
        itemName.setText(listItem.name);
        imageName.setText(String.format(Locale.ENGLISH, "%s %.1f %s", listItem.name, listItem.value, listItem.unit));
        quantities.setText(String.format(Locale.ENGLISH, "%.1f %s", listItem.value, listItem.unit));
        currentNumber.setText("0");
        String priceString = String.format(Locale.ENGLISH, "Rs %d",
                listItem.price);
        price.setText(priceString);
        if (item != null && offerItem != null) {
            if (item.quantity >= offerItem.minQuantity) {
                int discountedPrice;
                discountedPrice = (int) (listItem.price - (listItem.price * offerItem.percentageDiscount));
                priceString = String.format(Locale.ENGLISH, "Rs %d\nRs %d",
                        listItem.price, discountedPrice);
                Spannable spannable = new SpannableString(priceString);
                spannable.setSpan(
                        new StrikethroughSpan(),
                        0, priceString.lastIndexOf("\n"),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                price.setText(spannable);
            }
        }
        if (item != null) {
            currentNumber.setText(String.format(Locale.ENGLISH, "%d",
                    item.quantity));
            addButton.setVisibility(View.GONE);
            controlButton.setVisibility(View.VISIBLE);
        }
        offerText.setText("");
        if (offerItem != null) {
            offerText.setText(String.format(Locale.ENGLISH, "Buy %d and get %d%% off",
                    offerItem.minQuantity,
                    (int) (offerItem.percentageDiscount * 100)));
        }
    }

    public void setData(CartItemOffer listItem) {
        addButton.setVisibility(View.GONE);
        controlButton.setVisibility(View.VISIBLE);
        itemName.setText(listItem.item.name);
        imageName.setText(String.format(Locale.ENGLISH, "%s %.1f %s", listItem.item.name, listItem.item.value, listItem.item.unit));
        quantities.setText(String.format(Locale.ENGLISH, "%.1f %s", listItem.item.value, listItem.item.unit));
        currentNumber.setText("0");
        currentNumber.setText(String.format(Locale.ENGLISH, "%d",
                listItem.cart.quantity));
        int totalPrice = listItem.cart.quantity * listItem.item.price;
        String priceString = String.format(Locale.ENGLISH, "Rs %d",
                totalPrice);
        price.setText(priceString);
        offerText.setText("");

        if (listItem.offer == null) {
            return;
        }
        offerText.setText(String.format(Locale.ENGLISH, "Buy %d and get %d%% off",
                listItem.offer.minQuantity,
                (int) (listItem.offer.percentageDiscount * 100)));
        if (listItem.cart.quantity >= listItem.offer.minQuantity) {
            int discountedPrice;
            discountedPrice = (int) (totalPrice - (totalPrice * listItem.offer.percentageDiscount));
            priceString = String.format(Locale.ENGLISH, "Rs %d\nRs %d",
                    totalPrice, discountedPrice);
        }
        Spannable spannable = new SpannableString(priceString);
        if (listItem.cart.quantity >= listItem.offer.minQuantity) {
            spannable.setSpan(
                    new StrikethroughSpan(),
                    0, priceString.lastIndexOf("\n"),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        price.setText(spannable);
    }

}
