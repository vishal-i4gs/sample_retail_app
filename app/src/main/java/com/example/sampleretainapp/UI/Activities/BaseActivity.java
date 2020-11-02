package com.example.sampleretainapp.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.sampleretainapp.R;
import com.example.sampleretainapp.UI.Fragments.SearchDialogFragment;
import com.example.sampleretainapp.UI.ViewModel.AppViewModel;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    protected LinearLayout ll;
    protected TextView editText;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    AppViewModel appViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editText = findViewById(R.id.search_text);

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        ll = (LinearLayout) findViewById(R.id.layout_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        appViewModel = new ViewModelProvider(this).get(
                AppViewModel.class);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.home:
                        dl.closeDrawer(GravityCompat.START);
                        intent = new Intent(BaseActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.my_offers:
                        dl.closeDrawer(GravityCompat.START);
                        intent = new Intent(BaseActivity.this, OffersActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.my_orders:
                        dl.closeDrawer(GravityCompat.START);
                        intent = new Intent(BaseActivity.this, OrderActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });
        editText.setOnClickListener(view -> showDialog(""));
    }


    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // use android.R.id
            dl.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showDialog(String searchString) {
        SearchDialogFragment newFragment = SearchDialogFragment.newInstance(searchString);
        newFragment.viewItemListener = new SearchDialogFragment.ViewItemListener() {
            @Override
            public void onSearchClicked(String searchString) {
                Intent intent = new Intent(BaseActivity.this,
                        SearchListActivity.class);
                intent.putExtra("search_term", searchString);
                startActivity(intent);
            }

            @Override
            public void onItemClicked(String itemId) {
                Intent intent = new Intent(BaseActivity.this, ItemActivity.class);
                intent.putExtra("itemId", itemId);
                startActivity(intent);
            }
        };
        newFragment.show(getSupportFragmentManager(), "autoCompleteFragment");
    }
}
