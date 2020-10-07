package com.example.sampleretainapp.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sampleretainapp.App;
import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.SubItem;
import com.example.sampleretainapp.Repository;

import java.util.HashMap;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private Context context;

    private Repository mRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application;
        mRepository = ((App) application).getRepository();
    }

    public LiveData<List<Item>> getListLiveData() {
        return mRepository.getList();
    }

    public HashMap<String, Item> getItemsHashmap() {
        return mRepository.listItemHashmap;
    }

    public LiveData<List<CartItem>> getCartLiveData() {
        return mRepository.getCartList();
    }

    public void addItem(int subItemIndex, Item parentItem) {
        mRepository.addItemToCart(subItemIndex, parentItem);
    }

    public void removeItem(int subItemIndex, Item parentItem) {
        mRepository.removeItemFromCart(subItemIndex, parentItem);
    }

    public CartItem getCartItem(int subItemIndex, Item parentItem) {
        return mRepository.getCartItem(subItemIndex, parentItem);
    }

    public List<Item> getSearchItem(String searchItem) {
        return mRepository.getItemsForName(searchItem);
    }

    public List<String> getSearchTerms() {
        return mRepository.getSearchTerms();
    }

}
