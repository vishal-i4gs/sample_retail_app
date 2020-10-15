package com.example.sampleretainapp.UI.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sampleretainapp.App;
import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.CategoryItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.OfferItem;
import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.Repository;

import java.util.HashMap;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private Context context;

    private Repository mRepository;

    private String currentSearchTerm;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application;
        mRepository = ((App) application).getRepository();
    }

    public String getCurrentSearchTerm() {
        return currentSearchTerm;
    }

    public void setCurrentSearchTerm(String searchTerm) {
        this.currentSearchTerm = searchTerm;
    }

    public LiveData<List<Item>> getListLiveData() {
        return mRepository.getList();
    }

    public LiveData<List<CategoryItem>> getCategories() {
        return mRepository.getCategories();
    }

    public LiveData<List<OfferItem>> getOfferItems() {
        return mRepository.getOfferItemsLiveData();
    }

    public LiveData<List<OrderItem>> getOrderItems() {
        return mRepository.getOrderItemsLiveData();
    }


    public HashMap<String, Item> getItemsHashmap() {
        return mRepository.listItemHashmap;
    }

    public LiveData<List<CartItem>> getCartLiveData() {
        return mRepository.getCartList();
    }

    public void clearCart() {
        mRepository.clearCart();
    }

    public void addItem(Item item, OfferItem offerItem) {
        mRepository.addItemToCart(item, offerItem);
    }

    public void removeItem(Item item) {
        mRepository.removeItemFromCart(item);
    }

    public void addOrderItem(OrderItem orderItem) {
        mRepository.addOrderItem(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem) {mRepository.removeOrderItem(orderItem);}

    public CartItem getCartItem(Item item) {
        return mRepository.getCartItem(item);
    }

    public OfferItem getOfferItem(Item item) {
        return mRepository.getOfferItem(item);
    }

    public LiveData<OrderItem> getOrderItem(String orderItemId) {
        return mRepository.getOrderItem(orderItemId);
    }

    public Item getItem(String itemId) {
        return mRepository.getItem(itemId);
    }

    public List<Item> getSearchItem(String searchItem) {
        return mRepository.getItemsForName(searchItem);
    }

    public List<String> getSearchTerms() {
        return mRepository.getSearchTerms();
    }

}
