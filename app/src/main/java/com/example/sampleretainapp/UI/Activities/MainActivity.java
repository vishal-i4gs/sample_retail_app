package com.example.sampleretainapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.Adapters.CategoryAdapter;
import com.example.sampleretainapp.UI.Adapters.OfferAdapter;
import com.example.sampleretainapp.UI.Misc.CirclePagerIndicatorDecoration;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CategoryAdapter listAdapter;
    private OfferAdapter offerAdapter;
    private TextView cartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.content_main, null, false);
        ll.addView(contentView, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));

        RecyclerView listItemView = contentView.findViewById(R.id.list_item_view);
        RecyclerView listItemView1 = contentView.findViewById(R.id.list_item_view2);

        mainActivityViewModel.getCategories().observe(this,
                listItems -> {
                    Log.d(TAG, String.valueOf(listItems));
                    listAdapter.setList(listItems);
                });

        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
        cartItemCount = findViewById(R.id.cart_item_count);
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

        mainActivityViewModel.getOfferItems().observe(this, strings -> offerAdapter.setList(strings));
        mainActivityViewModel.getCartLiveData().observe(this,
                cartItems -> {
                    listAdapter.notifyDataSetChanged();
                });

        listAdapter = new CategoryAdapter();
        int numberOfColumns = 2;
        listItemView.setItemAnimator(null);
        listItemView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        listItemView.setAdapter(listAdapter);

        offerAdapter = new OfferAdapter(new ItemClickListener() {
            @Override
            public void itemClicked(int position) {
                Intent intent = new Intent(MainActivity.this, OffersActivity.class);
                startActivity(intent);
            }
        });
        SnapHelper snapHelper = new PagerSnapHelper();
        listItemView1.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        snapHelper.attachToRecyclerView(listItemView1);
        listItemView1.setAdapter(offerAdapter);
        listItemView1.addItemDecoration(new CirclePagerIndicatorDecoration());
    }


}
