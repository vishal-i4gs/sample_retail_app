package com.example.sampleretainapp.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.ItemOfferCart;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.Adapters.ListAdapter;
import com.example.sampleretainapp.UI.Fragments.SearchDialogFragment;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.example.sampleretainapp.UI.ViewModel.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class SearchListActivity extends BaseActivity implements ItemClickListener {

    private ListAdapter listAdapter;
    private AppViewModel appViewModel;
    private TextView orderEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search_list, null, false);
        ll.addView(contentView, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));

        RecyclerView listItemView = contentView.findViewById(R.id.list_item_view);
        orderEmptyTextView = contentView.findViewById(R.id.order_empty_text_view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search");
        }

        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);
        orderEmptyTextView.setVisibility(View.GONE);
        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(SearchListActivity.this, CartActivity.class);
            startActivity(intent);
        });
        TextView cartItemCount = findViewById(R.id.cart_item_count);
        cartItemCount.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        appViewModel.getCartItems().observe(this, cartItems -> {
//            appViewModel.getItemsViaFuzzySearch(appViewModel.getCurrentSearchTerm());
            appViewModel.getFuzzySearchMediatorLive(appViewModel.getCurrentSearchTerm()).observe(
                    this, new Observer<List<ItemOfferCart>>() {
                        @Override
                        public void onChanged(List<ItemOfferCart> itemOfferCarts) {
                            listAdapter.setList(itemOfferCarts);
                        }
                    });
            if (cartItems.size() == 0) {
                cartItemCount.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
            } else {
                cartItemCount.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
            }
            cartItemCount.setText(String.format(Locale.ENGLISH, "%d", cartItems.size()));
        });
        listAdapter = new ListAdapter(appViewModel, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);

//        appViewModel.getSearchForNameFuzzyMediator().observe(this, itemOfferCarts -> {
//            if (itemOfferCarts.size() == 0) {
//                orderEmptyTextView.setVisibility(View.VISIBLE);
//            } else {
//                orderEmptyTextView.setVisibility(View.GONE);
//            }
//            listAdapter.setList(itemOfferCarts);
//        });

//        appViewModel.getSearchForNameFtsMediator().observe(this, itemOfferCarts -> {
//            if (itemOfferCarts.size() == 0) {
//                orderEmptyTextView.setVisibility(View.VISIBLE);
//            } else {
//                orderEmptyTextView.setVisibility(View.GONE);
//            }
//            listAdapter.setList(itemOfferCarts);
//        });

        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }

        editText.setText(appViewModel.getCurrentSearchTerm());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        appViewModel.setCurrentSearchTerm(intent.getStringExtra("search_term"));
        editText.setText(appViewModel.getCurrentSearchTerm());
//        appViewModel.getItemsViaFtsSearch(appViewModel.getCurrentSearchTerm());
//        appViewModel.getItemsViaFuzzySearch(appViewModel.getCurrentSearchTerm());
        appViewModel.getFuzzySearchMediatorLive(appViewModel.getCurrentSearchTerm()).observe(
                this, new Observer<List<ItemOfferCart>>() {
                    @Override
                    public void onChanged(List<ItemOfferCart> itemOfferCarts) {
                        listAdapter.setList(itemOfferCarts);
                    }
                });
    }

    @Override
    protected void showDialog(String searchString) {
        super.showDialog(appViewModel.getCurrentSearchTerm());
    }

    @Override
    public void itemClicked(Item item) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("itemId", item.id);
        startActivity(intent);
    }

}
