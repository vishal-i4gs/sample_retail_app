package com.example.sampleretainapp.UI.ViewHolder;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.R;

import java.util.Locale;

public class OrderCartViewHolder extends RecyclerView.ViewHolder {

    private TextView itemName;
    private TextView quantities;
    private TextView currentNumber;
    private TextView price;

    public OrderCartViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.item_name);
        quantities = itemView.findViewById(R.id.item_quantities);
        currentNumber = itemView.findViewById(R.id.item_total_quantity);
        price = itemView.findViewById(R.id.item_price);
    }

    public void setData(Item item, CartItem cartItem, Offer offerItem) {
        itemName.setText(item.name);
        quantities.setText(String.format(Locale.ENGLISH, "%.1f %s",
                item.value, item.unit));
        currentNumber.setText("0");
        currentNumber.setText(String.format(Locale.ENGLISH, "Quantity : %d", cartItem.quantity));

        int totalPrice = cartItem.quantity * item.price;
        String priceString = String.format(Locale.ENGLISH, "Rs %d",
                totalPrice);
        price.setText(priceString);

        if (offerItem == null) {
            return;
        }
        if (cartItem.quantity >= offerItem.minQuantity) {
            int discountedPrice;
            discountedPrice = (int) (totalPrice - (totalPrice * offerItem.percentageDiscount));
            priceString = String.format(Locale.ENGLISH, "Rs %d\nRs %d",
                    totalPrice, discountedPrice);
        }
        Spannable spannable = new SpannableString(priceString);
        if (cartItem.quantity >= offerItem.minQuantity) {
            spannable.setSpan(
                    new StrikethroughSpan(),
                    0, priceString.lastIndexOf("\n"),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        price.setText(spannable);
    }
}
