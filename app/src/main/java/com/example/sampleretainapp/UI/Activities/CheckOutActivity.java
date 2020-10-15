package com.example.sampleretainapp.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.OfferItem;
import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ViewModel.MainActivityViewModel;

import java.util.Date;
import java.util.UUID;

public class CheckOutActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        mainActivityViewModel = new ViewModelProvider(this).get(
                MainActivityViewModel.class);
        mainActivityViewModel.getCartLiveData().observe(this,
                cartItems -> {
                    if (cartItems.size() == 0) {
                        onBackPressed();
                        return;
                    }
                    int sum = 0;
                    for (CartItem cartItem : cartItems) {
                        float price = cartItem.quantity * cartItem.item.price;
                        OfferItem offerItem = mainActivityViewModel.getOfferItem(cartItem.item);
                        if (offerItem != null) {
                            if (cartItem.quantity >= offerItem.minQuantity) {
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
                    mainActivityViewModel.addOrderItem(orderItem);
                    mainActivityViewModel.clearCart();
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