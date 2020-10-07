package com.example.sampleretainapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.SubItem;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Repository {

    private static final String TAG = Repository.class.getSimpleName();
    private Context context;
    private static Repository shared;

    private MutableLiveData<List<Item>> listMutableLiveData =
            new MutableLiveData<>();

    private MutableLiveData<List<CartItem>> cartItemsLiveData =
            new MutableLiveData<>();

    private MutableLiveData<Integer> listItemUpdate =
            new MutableLiveData<>();

    public HashMap<String, Item> listItemHashmap = new HashMap<>();

    public List<String> searchTerms = new ArrayList<>();


    private Repository(Context context) {
        this.context = context.getApplicationContext();
        String json = loadJSONFromAsset();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Item.class);
        JsonAdapter<List<Item>> adapter = moshi.adapter(type);
        try {
            List<Item> list;
            if (json != null) {
                list = adapter.fromJson(json);
                if (list != null) {
                    for (Item item : list) {
                        listItemHashmap.put(item.brand + " " + item.name, item);
                        searchTerms.add(item.brand + " " + item.name);
                        if (!searchTerms.contains(item.brand)) {
                            searchTerms.add(item.brand);
                        }
                        searchTerms.add(item.name);
                        if (!searchTerms.contains(item.type)) {
                            searchTerms.add(item.type);
                        }
                    }
                }
                listMutableLiveData.postValue(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        new Handler().postDelayed(() -> {
//            processMessage("Ashirwad Atta-Whole Wheat", "5kgs");
//        }, 5000);
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

    static Repository getInstance(final Context context) {
        if (shared == null) {
            synchronized (Repository.class) {
                if (shared == null) {
                    shared = new Repository(context);
                }
            }
        }
        return shared;
    }

    public void addItemToCart(int subItemIndex, Item parentItem) {
        if (cartItemsLiveData.getValue() == null) {
            List<CartItem> cartItemsNew = new ArrayList<>();
            cartItemsLiveData.setValue(cartItemsNew);
        }
        CartItem cartItemC = new CartItem();
        cartItemC.subItemIndex = subItemIndex;
        cartItemC.parentItem = parentItem;
        if (cartItemsLiveData.getValue().contains(cartItemC)) {
            int index = cartItemsLiveData.getValue().indexOf(cartItemC);
            CartItem cartItem = cartItemsLiveData.getValue().get(index);
            cartItem.quantity += 1;
        } else {
            CartItem cartItem = new CartItem();
            cartItem.subItemIndex = subItemIndex;
            cartItem.parentItem = parentItem;
            cartItem.quantity = 1;
            List<CartItem> cartItemsNew = cartItemsLiveData.getValue();
            cartItemsNew.add(cartItem);
            cartItemsLiveData.postValue(cartItemsNew);
        }
    }

    public void removeItemFromCart(int subItemIndex, Item parentItem) {
        if (cartItemsLiveData.getValue() != null) {
            CartItem cartItemC = new CartItem();
            cartItemC.subItemIndex = subItemIndex;
            cartItemC.parentItem = parentItem;
            if (cartItemsLiveData.getValue().contains(cartItemC)) {
                int index = cartItemsLiveData.getValue().indexOf(cartItemC);
                CartItem cartItem = cartItemsLiveData.getValue().get(index);
                cartItem.quantity -= 1;
                if (cartItem.quantity == 0) {
                    List<CartItem> cartItemsNew = cartItemsLiveData.getValue();
                    cartItemsNew.remove(index);
                    cartItemsLiveData.postValue(cartItemsNew);
                }
            }
        }
    }

    public CartItem getCartItem(int subItemIndex, Item parentItem) {
        if (cartItemsLiveData.getValue() != null) {
            CartItem cartItemC = new CartItem();
            cartItemC.subItemIndex = subItemIndex;
            cartItemC.parentItem = parentItem;
            if (cartItemsLiveData.getValue().contains(cartItemC)) {
                int index = cartItemsLiveData.getValue().indexOf(cartItemC);
                return cartItemsLiveData.getValue().get(index);
            }
        }
        return null;
    }

    //Getters
    public LiveData<List<Item>> getList() {
        return listMutableLiveData;
    }

    public LiveData<List<CartItem>> getCartList() {
        return cartItemsLiveData;
    }

    public LiveData<Integer> getListItemUpdate() {
        return listItemUpdate;
    }

    public void processMessage(String itemName, String subItemName) {
        Item item = listItemHashmap.get(itemName);
        if (item != null) {
            for (SubItem subItem : item.subItems) {
                if (subItem.name.equalsIgnoreCase(subItemName)) {
                    addItemToCart(item.subItems.indexOf(subItem), item);
                }
            }
        }
    }

    public List<Item> getItemsForName(String name) {
        name = name.toLowerCase();
        String text;
        ArrayList<String> strings
                = new ArrayList<>(listItemHashmap.keySet());
        ArrayList<Item> filteredDataList = new ArrayList<>();
        for (String dataFromDataList : strings) {
            if (dataFromDataList.toLowerCase().contains(name.toLowerCase())) {
                filteredDataList.add(listItemHashmap.get(dataFromDataList));
            }
        }
        return filteredDataList;
    }

    //Helpers
    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = context.getAssets().open("list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}