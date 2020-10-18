package com.example.sampleretainapp.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.sampleretainapp.Model.CartItemOffer;
import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ViewModel.AppViewModel;

import java.util.Date;
import java.util.UUID;

public class CheckOutActivity extends AppCompatActivity {

    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);
        appViewModel.getCartItems().observe(this,
                cartItems -> {
                    if (cartItems.size() == 0) {
                        onBackPressed();
                        return;
                    }
                    int sum = 0;
                    for (CartItemOffer cartItem : cartItems) {
                        float price = cartItem.cart.quantity * cartItem.item.price;
                        Offer offerItem =  cartItem.offer;
                        if (offerItem != null) {
                            if (cartItem.cart.quantity >= offerItem.minQuantity) {
                                price = (float) (price - (offerItem.percentageDiscount * price));
                            }
                        }
                        sum += price;
                    }
                    OrderItem orderItem = new OrderItem();
                    orderItem.orderId = UUID.randomUUID().toString();
                    orderItem.numberOfItems = cartItems.size();
                    orderItem.orderPrice = sum;
                    orderItem.active = true;
                    orderItem.orderTime = new Date();
                    orderItem.orderItems = cartItems;
                    appViewModel.addOrderItem(orderItem);
                    appViewModel.clearCart();
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
                        startActivity(intent);
                    }, 2000);

                });
    }

    @Override
    public void onBackPressed() {
    }
}