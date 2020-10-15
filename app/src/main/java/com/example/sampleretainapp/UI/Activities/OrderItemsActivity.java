package com.example.sampleretainapp.UI.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.OfferItem;
import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.Adapters.OrderCartAdapter;
import com.example.sampleretainapp.UI.ViewModel.MainActivityViewModel;

import java.util.Locale;

public class OrderItemsActivity extends AppCompatActivity {

    private OrderCartAdapter listAdapter;
    TextView totalCost;
    TextView totalSave;
    RecyclerView listItemView;
    Button removeButton;
    MainActivityViewModel mainActivityViewModel;
    View orderControlSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);
        removeButton = findViewById(R.id.remove_button);
        listItemView = findViewById(R.id.list_item_view);
        orderControlSection = findViewById(R.id.order_control_section);
        totalCost = findViewById(R.id.total_cost);
        totalSave = findViewById(R.id.total_save);
        mainActivityViewModel = new ViewModelProvider(this).get(
                MainActivityViewModel.class);
        mainActivityViewModel.getOrderItem(getIntent().getStringExtra("orderItemId"))
                .observe(this, new Observer<OrderItem>() {
                    @Override
                    public void onChanged(OrderItem orderItem) {
                        updateOrderItem(orderItem);
                    }
                });
    }

    private void updateOrderItem(OrderItem orderItem) {
        int sum = 0;
        int sumWithoutDiscount = 0;
        for (CartItem cartItem : orderItem.orderItems) {
            float price = cartItem.quantity * cartItem.item.price;
            sumWithoutDiscount += price;
            OfferItem offerItem = cartItem.offerItem;
            if (offerItem != null) {
                if (cartItem.quantity >= offerItem.minQuantity) {
                    price = (float) (price - (offerItem.percentageDiscount * price));
                }
            }
            sum += price;
        }
        totalCost.setText(String.format(Locale.ENGLISH, "Total: Rs %d",
                sum));
        int saved = sumWithoutDiscount - sum;
        if(saved > 0) {
            totalSave.setVisibility(View.VISIBLE);
            totalSave.setText(String.format(Locale.ENGLISH, "Saved: Rs %d",saved
            ));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Order #" + orderItem.orderId.substring(0, 10));
        }
        listAdapter = new OrderCartAdapter(mainActivityViewModel);
        listAdapter.setList(orderItem.orderItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);
        removeButton.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(OrderItemsActivity.this).create();
            alertDialog.setTitle("Cancel Order");
            alertDialog.setMessage("Are you sure, you want to cancel this order ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "YES",
                    (dialog, which) -> {
                        mainActivityViewModel.removeOrderItem(orderItem);
                        dialog.dismiss();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });
        if(orderItem.active) {
            orderControlSection.setBackgroundColor(Color.BLACK);
            removeButton.setEnabled(true);
            removeButton.setText("CANCEL ORDER");
        }
        else {
            orderControlSection.setBackgroundColor(Color.BLACK);
            removeButton.setEnabled(false);
            removeButton.setText("ORDER CANCELLED");
        }
    }

}