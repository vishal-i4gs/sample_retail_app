package com.example.sampleretainapp;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.CategoryItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.OfferItem;
import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.db.AppDatabase;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Repository {

    private static final String TAG = Repository.class.getSimpleName();
    private Context context;
    private static Repository shared;

    private MutableLiveData<List<Item>> listMutableLiveData =
            new MutableLiveData<>();

    private MutableLiveData<List<CategoryItem>> categories =
            new MutableLiveData<>();

    private MutableLiveData<List<CartItem>> cartItemsLiveData =
            new MutableLiveData<>();

    private MutableLiveData<List<OfferItem>> offerItemsLiveData =
            new MutableLiveData<>();

    private MutableLiveData<List<OrderItem>> orderItemsLiveData =
            new MutableLiveData<>();

    private MutableLiveData<Integer> listItemUpdate =
            new MutableLiveData<>();

    public HashMap<String, Item> listItemHashmap = new HashMap<>();

    public List<String> searchTerms = new ArrayList<>();

    private MediatorLiveData<List<OrderItem>> mObservableOrders;
    private final AppDatabase mDatabase;
    private final AppExecutors appExecutors;

    private Repository(Context context, final AppDatabase database, final AppExecutors appExecutors) {
        this.context = context.getApplicationContext();
        this.mDatabase = database;
        this.appExecutors = appExecutors;
        String json = loadJSONFromAsset();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Item.class);
        JsonAdapter<List<Item>> adapter = moshi.adapter(type);
        List<Item> listItems = new ArrayList<>();

        try {
            if (json != null) {
                listItems = adapter.fromJson(json);
                if (listItems != null) {
                    for (Item item : listItems) {
                        listItemHashmap.put(item.name + " " + item.value + item.unit, item);
                        if (!searchTerms.contains(item.name)) {
                            searchTerms.add(item.name);
                        }
                    }
                }
                listMutableLiveData.postValue(listItems);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Random rng = new Random();
        List<Integer> generatedColors = new ArrayList<Integer>() {
            {
                add(Color.parseColor("#1E63A9"));
                add(Color.parseColor("#F96948"));
                add(Color.parseColor("#DA5D8D"));
                add(Color.parseColor("#18763C"));
                add(Color.parseColor("#F4A200"));
                add(Color.parseColor("#71A1C1"));
                add(Color.parseColor("#7FACD9"));
                add(Color.parseColor("#C09060"));
                add(Color.parseColor("#888D42"));
                add(Color.parseColor("#182C5C"));
                add(Color.parseColor("#31BEA0"));
            }
        };

        List<CategoryItem> categoryItems = new ArrayList();
        for (int i = 0; i < 4; i++) {
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.categoryName = "Category " + (i + 1);
            categoryItem.color = generatedColors.get(i % generatedColors.size());
            categoryItems.add(categoryItem);
        }
        categories.postValue(categoryItems);


        List<OfferItem> offerItems = new ArrayList<>();
        List<Integer> generated = new ArrayList<Integer>();
        int totalNumberOfOffers = (int) (listItems.size() * 0.30);
        for (int i = 0; i < totalNumberOfOffers; i++) {
            while (true) {
                Integer next = rng.nextInt(totalNumberOfOffers) + 1;
                if (!generated.contains(next)) {
                    generated.add(next);
                    break;
                }
            }
        }
        int counter;
        for (counter = 0; counter < generated.size(); counter++) {
            int randomNumber = generated.get(counter);
            OfferItem offerItem = new OfferItem();
            offerItem.offerName = "Offer " + (counter + 1);
            offerItem.item = listItems.get(randomNumber);
            offerItem.minQuantity = 2 + new Random().nextInt(3);
            offerItem.percentageDiscount = randFloat(0.1f, 0.3f);
            offerItem.color = generatedColors.get(counter % generatedColors.size());
            offerItems.add(offerItem);
        }
        offerItemsLiveData.postValue(offerItems);

        mObservableOrders = new MediatorLiveData<>();
        mObservableOrders.addSource(mDatabase.orderDao().loadAllOrders(),
                productEntities -> {
                    mObservableOrders.postValue(productEntities);
                });

    }

    public static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

    static Repository getInstance(final Context context, final AppDatabase mDatabase, final AppExecutors appExecutors) {
        if (shared == null) {
            synchronized (Repository.class) {
                if (shared == null) {
                    shared = new Repository(context, mDatabase, appExecutors);
                }
            }
        }
        return shared;
    }

    public void addItemToCart(Item item, OfferItem offerItem) {
        if (cartItemsLiveData.getValue() == null) {
            List<CartItem> cartItemsNew = new ArrayList<>();
            cartItemsLiveData.setValue(cartItemsNew);
        }
        CartItem cartItemC = new CartItem();
        cartItemC.item = item;
        if (cartItemsLiveData.getValue().contains(cartItemC)) {
            int index = cartItemsLiveData.getValue().indexOf(cartItemC);
            CartItem cartItem = cartItemsLiveData.getValue().get(index);
            cartItem.quantity += 1;
            cartItem.offerItem = offerItem;
            cartItemsLiveData.postValue(cartItemsLiveData.getValue());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.item = item;
            cartItem.quantity = 1;
            cartItem.offerItem = offerItem;
            List<CartItem> cartItemsNew = cartItemsLiveData.getValue();
            cartItemsNew.add(cartItem);
            cartItemsLiveData.postValue(cartItemsNew);
        }
    }

    public void removeItemFromCart(Item item) {
        if (cartItemsLiveData.getValue() != null) {
            CartItem cartItemC = new CartItem();
            cartItemC.item = item;
            if (cartItemsLiveData.getValue().contains(cartItemC)) {
                int index = cartItemsLiveData.getValue().indexOf(cartItemC);
                CartItem cartItem = cartItemsLiveData.getValue().get(index);
                cartItem.quantity -= 1;
                if (cartItem.quantity == 0) {
                    List<CartItem> cartItemsNew = cartItemsLiveData.getValue();
                    cartItemsNew.remove(index);
                    cartItemsLiveData.postValue(cartItemsNew);
                } else {
                    cartItemsLiveData.postValue(cartItemsLiveData.getValue());
                }
            }
        }
    }

    public CartItem getCartItem(Item item) {
        if (cartItemsLiveData.getValue() != null) {
            CartItem cartItemC = new CartItem();
            cartItemC.item = item;
            if (cartItemsLiveData.getValue().contains(cartItemC)) {
                int index = cartItemsLiveData.getValue().indexOf(cartItemC);
                return cartItemsLiveData.getValue().get(index);
            }
        }
        return null;
    }

    public OfferItem getOfferItem(Item item) {
        if (offerItemsLiveData.getValue() != null) {
            OfferItem offerItem = new OfferItem();
            offerItem.item = item;
            if (offerItemsLiveData.getValue().contains(offerItem)) {
                int index = offerItemsLiveData.getValue().indexOf(offerItem);
                return offerItemsLiveData.getValue().get(index);
            }
        }
        return null;
    }

    public Item getItem(String itemId) {
        if (listMutableLiveData.getValue() != null) {
            Item item = new Item();
            item.id = itemId;
            if (listMutableLiveData.getValue().contains(item)) {
                int index = listMutableLiveData.getValue().indexOf(item);
                return listMutableLiveData.getValue().get(index);
            }
        }
        return null;
    }

    public LiveData<OrderItem> getOrderItem(String orderItemId) {
        return mDatabase.orderDao().loadOrder(orderItemId);
    }

    public void addOrderItem(OrderItem item) {
        appExecutors.diskIO().execute(() -> {
            mDatabase.orderDao().insert(item);
        });
    }

    public void removeOrderItem(OrderItem item) {
        appExecutors.diskIO().execute(() -> {
            mDatabase.orderDao().update(false, item.orderId);
        });
    }

    //Getters
    public LiveData<List<Item>> getList() {
        return listMutableLiveData;
    }

    public LiveData<List<CategoryItem>> getCategories() {
        return categories;
    }

    public LiveData<List<CartItem>> getCartList() {
        return cartItemsLiveData;
    }

    public void clearCart() {
        cartItemsLiveData.postValue(new ArrayList<>());
    }

    public LiveData<Integer> getListItemUpdate() {
        return listItemUpdate;
    }

    public MutableLiveData<List<OfferItem>> getOfferItemsLiveData() {
        return offerItemsLiveData;
    }

    public LiveData<List<OrderItem>> getOrderItemsLiveData() {
        return mDatabase.orderDao().loadAllOrders();
//        return mObservableOrders;
    }


    public void processMessage(String itemName, String subItemName) {
        Item item = listItemHashmap.get(itemName);
//        if (item != null) {
//            for (SubItem subItem : item.subItems) {
//                if (subItem.name.equalsIgnoreCase(subItemName)) {
//                    addItemToCart(item.subItems.indexOf(subItem), item);
//                }
//            }
//        }
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