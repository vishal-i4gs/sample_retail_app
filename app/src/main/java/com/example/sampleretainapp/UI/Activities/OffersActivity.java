package com.example.sampleretainapp.UI.Activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.OfferItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.Adapters.ListAdapter;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.example.sampleretainapp.UI.ViewModel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OffersActivity extends BaseActivity implements ItemClickListener {

    private MainActivityViewModel mainActivityViewModel;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search_list, null, false);
        ll.addView(contentView, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Offers");
        }

        RecyclerView listItemView = contentView.findViewById(R.id.list_item_view);

        mainActivityViewModel = new ViewModelProvider(this).get(
                MainActivityViewModel.class);
        mainActivityViewModel.getOfferItems().observe(this,
                new Observer<List<OfferItem>>() {
                    @Override
                    public void onChanged(List<OfferItem> offerItems) {
                        List<Item> items = new ArrayList<>();
                        for (OfferItem offerItem : offerItems) {
                            items.add(offerItem.item);
                        }
                        listAdapter.setList(items);
                    }
                });

        mainActivityViewModel.getCartLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                listAdapter.notifyDataSetChanged();
            }
        });


        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(OffersActivity.this, CartActivity.class);
            startActivity(intent);
        });
        TextView cartItemCount = findViewById(R.id.cart_item_count);
        cartItemCount.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        mainActivityViewModel.getCartLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                listAdapter.notifyDataSetChanged();
                if (cartItems.size() == 0) {
                    cartItemCount.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                } else {
                    cartItemCount.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }
                cartItemCount.setText(String.format(Locale.ENGLISH, "%d", cartItems.size()));
            }
        });

        listAdapter = new ListAdapter(mainActivityViewModel, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);

    }

    @Override
    public void itemClicked(Item item) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("itemId",item.id);
        startActivity(intent);
    }
}