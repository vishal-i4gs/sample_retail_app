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

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.Adapters.ListAdapter;
import com.example.sampleretainapp.UI.Fragments.AutoCompleteDialogFragment;
import com.example.sampleretainapp.UI.ItemClickListener;
import com.example.sampleretainapp.UI.ViewModel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class SearchListActivity extends BaseActivity implements ItemClickListener {

    private ListAdapter listAdapter;
    private MainActivityViewModel mainActivityViewModel;
    private TextView orderEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_search_list, null, false);
        ll.addView(contentView, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));

        RecyclerView listItemView = contentView.findViewById(R.id.list_item_view);
        orderEmptyTextView = contentView.findViewById(R.id.order_empty_text_view);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search");
        }

        mainActivityViewModel = new ViewModelProvider(this).get(
                MainActivityViewModel.class);
        orderEmptyTextView.setVisibility(View.GONE);
        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(SearchListActivity.this, CartActivity.class);
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

        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        mainActivityViewModel.setCurrentSearchTerm(intent.getStringExtra("search_term"));
        editText.setText(mainActivityViewModel.getCurrentSearchTerm());
        if (mainActivityViewModel.getSearchItem(
                mainActivityViewModel.getCurrentSearchTerm()).size() == 0) {
            orderEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            orderEmptyTextView.setVisibility(View.GONE);
        }
        listAdapter.setList(mainActivityViewModel.getSearchItem(mainActivityViewModel.getCurrentSearchTerm()));
    }

    @Override
    protected void showDialog() {
        AutoCompleteDialogFragment newFragment = AutoCompleteDialogFragment.newInstance(
                mainActivityViewModel.getCurrentSearchTerm());
        newFragment.cityList = mainActivityViewModel.getSearchTerms();
        newFragment.viewItemListener = new AutoCompleteDialogFragment.ViewItemListener() {
            @Override
            public void onItemClicked(String item) {
                Intent intent = new Intent(SearchListActivity.this,
                        SearchListActivity.class);
                intent.putExtra("search_term", item);
                startActivity(intent);
            }
        };
        newFragment.show(getSupportFragmentManager(), "autoCompleteFragment");
    }

    @Override
    public void itemClicked(Item item) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("itemId",item.id);
        startActivity(intent);
    }

}
