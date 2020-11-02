package com.example.sampleretainapp.UI.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.sampleretainapp.App;
import com.example.sampleretainapp.Model.CartItemOffer;
import com.example.sampleretainapp.Model.CategoryItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.ItemOfferCart;
import com.example.sampleretainapp.Model.OfferItemCart;
import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.Repository;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private static final String TAG = AppViewModel.class.getSimpleName();

    private Repository mRepository;
    private String currentSearchTerm;

    public AppViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((App) application).getRepository();
    }

    //Functions/Methods related to items.
    public LiveData<List<ItemOfferCart>> getItems() {
        return mRepository.getItems();
    }
    public LiveData<ItemOfferCart> getItemForId(String id) {
        return mRepository.getItemForId(id);
    }


    //Functions/Methods related to offers.
    public LiveData<List<OfferItemCart>> getOfferItems() {
        return mRepository.getOfferItems();
    }


    //Functions/Methods related to cart.
    public LiveData<List<CartItemOffer>> getCartItems() {
        return mRepository.getCartItems();
    }
    public void clearCart() {
        mRepository.clearCart();
    }
    public void addItem(Item item) {
        mRepository.addItemToCart(item);
    }
    public void removeItem(Item item) {
        mRepository.removeItemFromCart(item);
    }


    //Functions/Methods related to orders.
    public LiveData<List<OrderItem>> getOrderItems() {
        return mRepository.getOrderItems();
    }
    public LiveData<OrderItem> getOrderItem(String orderItemId) {
        return mRepository.getOrderItem(orderItemId);
    }
    public void addOrderItem(OrderItem orderItem) {
        mRepository.addOrderItem(orderItem);
    }
    public void removeOrderItem(OrderItem orderItem) {mRepository.removeOrderItem(orderItem);}


    //Functions/Methods related to categories.
    public LiveData<List<CategoryItem>> getCategories() {
        return mRepository.getCategories();
    }


    //Functions/Methods related to search.
    public String getCurrentSearchTerm() {
        return currentSearchTerm;
    }
    public void setCurrentSearchTerm(String searchTerm) {
        this.currentSearchTerm = searchTerm;
    }

    public LiveData<List<ItemOfferCart>> getSearchForNameFuzzyMediator() {
        return mRepository.getSearchForNameFuzzyMediator();
    }
    public LiveData<List<ItemOfferCart>> getSearchForNameFtsMediator() {
        return mRepository.getSearchForNameFtsMediator();
    }

    public void getItemsViaFuzzySearch(String searchItem) {
        mRepository.getItemsViaFuzzySearch(searchItem);
    }
    public void getItemsViaFtsSearch(String searchItem) {
        mRepository.getItemsViaFtsSearch(searchItem);
    }

    public LiveData<List<ItemOfferCart>> getFuzzySearchMediatorLive(String name) {
        return mRepository.getItemsViaFuzzySearchLiveData(name);
    }



}
