package com.example.sampleretainapp.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.example.sampleretainapp.R;
import com.example.sampleretainapp.ViewModel.MainActivityViewModel;

public class SearchListActivity extends AppCompatActivity {

    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        RecyclerView listItemView = findViewById(R.id.list_item_view);
        String searchTerm = getIntent().getStringExtra("search_term");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Items similar to " + searchTerm);
        }

        MainActivityViewModel mainActivityViewModel =
                new ViewModelProvider(this).get(
                        MainActivityViewModel.class);
        listAdapter = new ListAdapter(mainActivityViewModel);
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        listItemView.setLayoutManager(layoutManager);
        listItemView.setItemAnimator(null);
        listItemView.setAdapter(listAdapter);
        listAdapter.setList(mainActivityViewModel.getSearchItem(searchTerm));
    }
}
