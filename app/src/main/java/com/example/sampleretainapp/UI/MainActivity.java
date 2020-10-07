package com.example.sampleretainapp.UI;

import android.content.Intent;
import android.os.Bundle;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.R;
import com.example.sampleretainapp.ViewModel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainActivityViewModel mainActivityViewModel;
    private ListAdapter listAdapter;
    private AutoCompleteTextView autoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
        RecyclerView listItemView = findViewById(R.id.list_item_view);
        autoTextView = findViewById(R.id.search_item);
        autoTextView.clearFocus();
        mainActivityViewModel = new ViewModelProvider(this).get(
                MainActivityViewModel.class);
        mainActivityViewModel.getListLiveData().observe(this,
                listItems -> {
                    Log.d(TAG, String.valueOf(listItems));
                    listAdapter.setList(listItems);
                    ArrayList<String> strings
                            = new ArrayList<>(
                            mainActivityViewModel.getSearchTerms());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>
                            (this, android.R.layout.select_dialog_item, strings);
                    autoTextView.setThreshold(1); //will start working from first character
                    autoTextView.setAdapter(adapter);
                });
        autoTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String selected = (String) adapterView.getItemAtPosition(i);
            Log.d(TAG, selected);
            Intent intent = new Intent(MainActivity.this,
                    SearchListActivity.class);
            intent.putExtra("search_term", selected);
            startActivity(intent);
            autoTextView.setText("");
        });
//        ImageButton searchButton = findViewById(R.id.search_button);
//        searchButton.setOnClickListener(view -> {
//            if (!TextUtils.isEmpty(autoTextView.getText().toString())) {
//                Intent intent = new Intent(MainActivity.this,
//                        SearchListActivity.class);
//                intent.putExtra("search_term", autoTextView.getText().toString());
//                startActivity(intent);
//            }
//            autoTextView.setText("");
//        });
        mainActivityViewModel.getCartLiveData().observe(this,
                cartItems -> {
                    listAdapter.notifyDataSetChanged();
                });
        listAdapter = new ListAdapter(mainActivityViewModel);
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
