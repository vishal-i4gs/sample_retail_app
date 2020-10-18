package com.example.sampleretainapp.UI.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sampleretainapp.Model.CartItemOffer;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.Adapters.CartAdapter;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.example.sampleretainapp.UI.ViewModel.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class CartActivity extends BaseActivity implements ItemClickListener {


    private AppViewModel appViewModel;
    private CartAdapter listAdapter;
    private Button buyButton;
    private TextView totalCost;
    private TextView totalSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_cart, null, false);
        ll.addView(contentView,
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Cart");
        }

        RecyclerView listItemView = contentView.findViewById(R.id.list_item_view);
        buyButton = contentView.findViewById(R.id.buy_button);
        buyButton.setOnClickListener(view -> {
                Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
                startActivity(intent);
        });

        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this).create();
            alertDialog.setTitle("Clear Cart");
            alertDialog.setMessage("Are you sure, you want to clear your cart ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "YES",
                    (dialog, which) -> {
                        appViewModel.clearCart();
                        dialog.dismiss();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });

        View checkoutSection = contentView.findViewById(R.id.checkout_section);
        totalCost = contentView.findViewById(R.id.total_cost);
        totalSave = contentView.findViewById(R.id.total_save);

        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);

        TextView orderEmptyTextView = contentView.findViewById(R.id.order_empty_text_view);
        orderEmptyTextView.setVisibility(View.GONE);
        if (appViewModel.getOrderItems().getValue() == null) {
            orderEmptyTextView.setVisibility(View.VISIBLE);
        }
        appViewModel.getCartItems().observe(this,
                cartItems -> {
                    listAdapter.setList(cartItems);
                    totalCost.setText("");
                    totalSave.setVisibility(View.GONE);
                    if (cartItems.size() == 0) {
                        fab.setVisibility(View.GONE);
                        checkoutSection.setVisibility(View.GONE);
                        orderEmptyTextView.setVisibility(View.VISIBLE);
                        return;
                    }
                    orderEmptyTextView.setVisibility(View.GONE);
                    checkoutSection.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    int sum = 0;
                    int sumWithoutDiscount = 0;
                    for (CartItemOffer cartItem : cartItems) {
                        float price = cartItem.cart.quantity * cartItem.item.price;
                        sumWithoutDiscount += price;
                        Offer offerItem = cartItem.offer;
                        if (offerItem != null) {
                            if (cartItem.cart.quantity >= offerItem.minQuantity) {
                                price = (float) (price - (offerItem.percentageDiscount * price));
                            }
                        }
                        sum += price;
                    }
                    totalCost.setText(String.format(Locale.ENGLISH, "Total: Rs %d",
                            sum));
                    int saved = sumWithoutDiscount - sum;
                    if (saved > 0) {
                        totalSave.setVisibility(View.VISIBLE);
                        totalSave.setText(String.format(Locale.ENGLISH, "Saved: Rs %d", saved
                        ));
                    }
                });
        listAdapter = new CartAdapter(appViewModel, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);
    }

    @Override
    public void itemClicked(Item item) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("itemId", item.id);
        startActivity(intent);
    }

}
