package com.example.sampleretainapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.ViewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {


    private MainActivityViewModel mainActivityViewModel;
    private CartAdapter listAdapter;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView listItemView = findViewById(R.id.list_item_view);
        buyButton = findViewById(R.id.buy_button);
        mainActivityViewModel = new ViewModelProvider(this).get(
                MainActivityViewModel.class);
        mainActivityViewModel.getCartLiveData().observe(this,
                cartItems -> {
                    listAdapter.setList(cartItems);
                    int sum = 0;
                    for (CartItem cartItem : cartItems) {
                        sum += (cartItem.quantity * cartItem.parentItem.subItems.get(cartItem.subItemIndex).price);
                    }
                    buyButton.setText(String.format(Locale.ENGLISH, "Pay Rs %d",
                            sum));
                });
        listAdapter = new CartAdapter(mainActivityViewModel);
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);
    }


}
