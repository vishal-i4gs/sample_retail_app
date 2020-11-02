package com.example.sampleretainapp.UI.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.ItemOfferCart;
import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.ViewModel.AppViewModel;

import java.util.Locale;

public class ItemActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView imageName;
    private TextView quantities;
    private Button addItem;
    private Button removeItem;
    private Button addButton;
    private View controlButton;
    private TextView currentNumber;
    private TextView price;
    private TextView offerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        itemName = findViewById(R.id.item_name);
        imageName = findViewById(R.id.image_item_name);
        quantities = findViewById(R.id.item_quantities);
        controlButton = findViewById(R.id.control_buttons);
        addButton = findViewById(R.id.add_button);
        addItem = findViewById(R.id.item_add);
        currentNumber = findViewById(R.id.item_current_number);
        price = findViewById(R.id.item_price);
        offerText = findViewById(R.id.item_offer_text);
        removeItem = findViewById(R.id.item_remove);

        String itemId = getIntent().getStringExtra("itemId");

        AppViewModel appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);
        appViewModel.getItemForId(itemId).observe(this, new Observer<ItemOfferCart>() {
            @Override
            public void onChanged(ItemOfferCart itemOfferCart) {
                handleItem(itemOfferCart.cart, itemOfferCart.offer, itemOfferCart.item);
                addItem.setOnClickListener(view -> {
                    appViewModel.addItem(itemOfferCart.item);
                });
                addButton.setOnClickListener(view -> {
                    appViewModel.addItem(itemOfferCart.item);
                });
                removeItem.setOnClickListener(view -> {
                    appViewModel.removeItem(itemOfferCart.item);
                });
            }
        });
    }

    private void handleItem(CartItem item, Offer offerItem, Item listItem) {
        addButton.setVisibility(View.VISIBLE);
        controlButton.setVisibility(View.GONE);
        itemName.setText(listItem.name);
        imageName.setText(String.format(Locale.ENGLISH, "%s %.1f %s", listItem.name, listItem.value, listItem.unit));
        quantities.setText(String.format(Locale.ENGLISH, "%.1f %s", listItem.value, listItem.unit));
        currentNumber.setText("0");
        String priceString = String.format(Locale.ENGLISH, "Rs %d",
                listItem.price);
        price.setText(priceString);
        if (item != null && offerItem != null) {
            if (item.quantity >= offerItem.minQuantity) {
                int discountedPrice;
                discountedPrice = (int) (listItem.price - (listItem.price * offerItem.percentageDiscount));
                priceString = String.format(Locale.ENGLISH, "Rs %d\nRs %d",
                        listItem.price, discountedPrice);
                Spannable spannable = new SpannableString(priceString);
                spannable.setSpan(
                        new StrikethroughSpan(),
                        0, priceString.lastIndexOf("\n"),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                price.setText(spannable);
            }
        }
        if (item != null) {
            currentNumber.setText(String.format(Locale.ENGLISH, "%d",
                    item.quantity));
            addButton.setVisibility(View.GONE);
            controlButton.setVisibility(View.VISIBLE);
        }
        offerText.setVisibility(View.GONE);
        if (offerItem != null) {
            offerText.setVisibility(View.VISIBLE);
            offerText.setText(String.format(Locale.ENGLISH, "Buy %d and get %d%% off",
                    offerItem.minQuantity,
                    (int) (offerItem.percentageDiscount * 100)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // use android.R.id
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}