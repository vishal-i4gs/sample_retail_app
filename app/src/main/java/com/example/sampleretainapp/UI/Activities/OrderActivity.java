package com.example.sampleretainapp.UI.Activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.Adapters.OrderAdapter;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.example.sampleretainapp.UI.ViewModel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {

    private OrderAdapter listAdapter;
    private List<OrderItem> orderItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_order, null, false);
        ll.addView(contentView, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Orders");
        }

        TextView orderEmptyTextView = contentView.findViewById(R.id.order_empty_text_view);
        orderEmptyTextView.setVisibility(View.GONE);

        RecyclerView listItemView = contentView.findViewById(R.id.list_item_view);

        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(this).get(
                MainActivityViewModel.class);

        mainActivityViewModel.getOrderItems().observe(this,
                orderItems -> {
                    Log.d("Count is ", String.valueOf(orderItems.size()));
                    if (orderItems.size() == 0) {
                        orderEmptyTextView.setVisibility(View.VISIBLE);
                    } else {
                        orderEmptyTextView.setVisibility(View.GONE);
                    }
                    this.orderItems = orderItems;
                    listAdapter.setList(orderItems);
                });

        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(OrderActivity.this, CartActivity.class);
            startActivity(intent);
        });
        TextView cartItemCount = findViewById(R.id.cart_item_count);
        cartItemCount.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
//        mainActivityViewModel.getCartLiveData().observe(this, new Observer<List<CartItem>>() {
//            @Override
//            public void onChanged(List<CartItem> cartItems) {
////                listAdapter.notifyDataSetChanged();
//                if (cartItems.size() == 0) {
//                    cartItemCount.setVisibility(View.GONE);
//                    fab.setVisibility(View.GONE);
//                } else {
//                    cartItemCount.setVisibility(View.VISIBLE);
//                    fab.setVisibility(View.VISIBLE);
//                }
//                cartItemCount.setText(String.format(Locale.ENGLISH, "%d", cartItems.size()));
//            }
//        });

        listAdapter = new OrderAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(int position) {
                List<OrderItem> orderItems =  OrderActivity.this.orderItems;
                if(orderItems.size() < position) {
                    return;
                }
                OrderItem orderItem = orderItems.get(position);
                Intent intent = new Intent(OrderActivity.this, OrderItemsActivity.class);
                intent.putExtra("orderItemId", orderItem.orderId);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Count","onDestroy");
    }
}