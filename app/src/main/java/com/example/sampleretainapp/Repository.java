package com.example.sampleretainapp;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.CartItemOffer;
import com.example.sampleretainapp.Model.CategoryItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.ItemOfferCart;
import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.Model.OfferItemCart;
import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.db.AppDatabase;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class Repository {

    public interface RepositoryInterface {
        default void getSearchResults(List<ItemOfferCart> items) {
        }
    }


    private static final String TAG = Repository.class.getSimpleName();
    private static Repository shared;

    private MutableLiveData<List<CategoryItem>> categories =
            new MutableLiveData<>();

    private LiveData<List<ItemOfferCart>> searchForNameFuzzyLiveData =
            new MutableLiveData<>();
    private MediatorLiveData<List<ItemOfferCart>> searchForNameFuzzyMediator =
            new MediatorLiveData<>();

    private LiveData<List<ItemOfferCart>> searchForNameFtsLiveData =
            new MutableLiveData<>();
    private MediatorLiveData<List<ItemOfferCart>> searchForNameFtsMediator =
            new MediatorLiveData<>();

    List<String> itemNames = new ArrayList<>();
    HashMap<String,String> itemNamesIdHashmap = new HashMap<>();

    private AppDatabase mDatabase;
    private final AppExecutors appExecutors;

    private Repository(Context context, final AppDatabase database, final AppExecutors appExecutors) {
        this.mDatabase = database;
        this.appExecutors = appExecutors;

        //Random Color Selector and Assigning it to category items.
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


        //Parsing the json file and adding the items to items table.
        String json = loadJSONFromAsset(context);
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, Item.class);
        JsonAdapter<List<Item>> adapter = moshi.adapter(type);
        List<Item> listItems;
        try {
            if (json != null) {
                listItems = adapter.fromJson(json);
                List<Item> finalListItems = listItems;
                appExecutors.diskIO().execute(() -> {
                    database.runInTransaction(() -> {
                        for (Item item : finalListItems) {
                            DecimalFormat format = new DecimalFormat("0.#");
                            item.name = String.format(Locale.ENGLISH, "%s %s %s",
                                    item.name, format.format(item.value), item.unit);
                            itemNames.add(item.name);
                            itemNamesIdHashmap.put(item.name,item.id);
                        }
                        database.itemDao().insert(finalListItems);
                    });
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Observing changes to items table and randomly applying offers to certain items.
        database.itemDao().getItems().observeForever(new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> itemAndOffers) {
                List<Offer> offerItems = new ArrayList<>();
                List<Integer> generated = new ArrayList<Integer>();
                int totalNumberOfOffers = (int) (itemAndOffers.size() * 0.30);
                for (int i = 0; i < totalNumberOfOffers; i++) {
                    while (true) {
                        Integer next = rng.nextInt(itemAndOffers.size());
                        if (!generated.contains(next)) {
                            generated.add(next);
                            break;
                        }
                    }
                }
                int counter;
                for (counter = 0; counter < generated.size(); counter++) {
                    int randomNumber = generated.get(counter);
                    Offer offerItem = new Offer();
                    offerItem.offerName = "Offer " + (counter + 1);
                    offerItem.itemId = itemAndOffers.get(randomNumber).id;
                    offerItem.minQuantity = 2 + new Random().nextInt(3);
                    offerItem.percentageDiscount = randFloat(0.1f, 0.3f);
                    offerItem.color = generatedColors.get(counter % generatedColors.size());
                    offerItems.add(offerItem);
                }

                appExecutors.diskIO().execute(() -> {
                    mDatabase.offerDao().removeAllOffers();
                    mDatabase.offerDao().insert(offerItems);
                });
                mDatabase.itemDao().getItems().removeObserver(this);
            }
        });
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

    //Cart Related Opertions/Methods
    public void addItemToCart(Item item) {
        appExecutors.diskIO().execute(() -> {
            mDatabase.runInTransaction(() -> {
                int currentNumber = 1;
                CartItem cartItem = mDatabase.cartDao().getCartItemForIdSync(item.id);
                if (cartItem != null) {
                    int number = cartItem.quantity;
                    number = number + currentNumber;
                    cartItem.quantity = number;
                    mDatabase.cartDao().update(cartItem);
                } else {
                    CartItem cartItemFinal = new CartItem();
                    cartItemFinal.itemId = item.id;
                    cartItemFinal.quantity = currentNumber;
                    mDatabase.cartDao().insert(cartItemFinal);
                }

            });
        });
    }

    public void removeItemFromCart(Item item) {
        appExecutors.diskIO().execute(() -> {
            mDatabase.runInTransaction(() -> {
                CartItem cartItem = mDatabase.cartDao().getCartItemForIdSync(item.id);
                if (cartItem == null) {
                    return;
                }
                int number = cartItem.quantity;
                number = number - 1;
                if (number == 0) {
                    mDatabase.cartDao().remove(cartItem);
                    return;
                }
                cartItem.quantity = number;
                mDatabase.cartDao().update(cartItem);
            });
        });
    }

    public void clearCart() {
        appExecutors.diskIO().execute(() -> {
            mDatabase.cartDao().removeAllItems();
        });
    }

    //Order Related Opertions/Methods
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
    public LiveData<List<ItemOfferCart>> getItems() {
        return mDatabase.itemDao().getItemsOffersCart();
    }

    public LiveData<ItemOfferCart> getItemForId(String id) {
        return mDatabase.itemDao().getItemForId(id);
    }

    public LiveData<List<OfferItemCart>> getOfferItems() {
        return mDatabase.offerDao().getOffersAndItems();
    }

    public LiveData<List<CartItemOffer>> getCartItems() {
        return mDatabase.cartDao().getCartItems();
    }

    public LiveData<List<OrderItem>> getOrderItems() {
        return mDatabase.orderDao().loadAllOrders();
    }

    public LiveData<List<CategoryItem>> getCategories() {
        return categories;
    }

    public MediatorLiveData<List<ItemOfferCart>> getSearchForNameFuzzyMediator() {
        return searchForNameFuzzyMediator;
    }

    public MediatorLiveData<List<ItemOfferCart>> getSearchForNameFtsMediator() {
        return searchForNameFtsMediator;
    }

    public void getItemsViaFtsSearch(String name) {
        if(searchForNameFtsLiveData != null) {
            searchForNameFtsMediator.removeSource(searchForNameFtsLiveData);
        }
        if(TextUtils.isEmpty(name)) {
            searchForNameFtsLiveData = mDatabase.itemDao().getItemsOffersCart();
        }
        else {
            searchForNameFtsLiveData = mDatabase.itemDao().getItemsAndOffersBasedOnSearchFts(fixQuery(name));
        }
        searchForNameFtsMediator.addSource(searchForNameFtsLiveData, itemOfferCarts
                -> searchForNameFtsMediator.postValue(itemOfferCarts));
    }

    public void getItemsViaFuzzySearch(String name) {
        if (searchForNameFuzzyLiveData != null) {
            searchForNameFuzzyMediator.removeSource(searchForNameFuzzyLiveData);
        }
        searchForNameFuzzyLiveData = mDatabase.itemDao().getItemsAndOffersBasedOnSearchFts(fixQuery(name));
        List<ExtractedResult> results = FuzzySearch.extractTop(name,itemNames,20);
        appExecutors.diskIO().execute(() -> {
            List<ItemOfferCart> itemOfferCarts = new ArrayList<>();
            for(ExtractedResult result: results) {
                if(result.getScore() > 40) {
                    String id = itemNamesIdHashmap.get(result.getString());
                    ItemOfferCart itemOfferCart = mDatabase.itemDao().getItemForIdSync(id);
                    itemOfferCarts.add(itemOfferCart);
                }
            }
            appExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    searchForNameFuzzyMediator.postValue(itemOfferCarts);
                }
            });
        });
    }

    public LiveData<List<ItemOfferCart>> getItemsViaFuzzySearchLiveData(String name) {
        MutableLiveData<List<ItemOfferCart>> listLiveData = new MutableLiveData<>();
        List<ExtractedResult> results = FuzzySearch.extractTop(name,itemNames,20);
        appExecutors.diskIO().execute(() -> {
            List<ItemOfferCart> itemOfferCarts = new ArrayList<>();
            for(ExtractedResult result: results) {
                if(result.getScore() > 40) {
                    String id = itemNamesIdHashmap.get(result.getString());
                    ItemOfferCart itemOfferCart = mDatabase.itemDao().getItemForIdSync(id);
                    itemOfferCarts.add(itemOfferCart);
                }
            }
            appExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    listLiveData.postValue(itemOfferCarts);
                }
            });
        });
        return listLiveData;
    }

    //    public void getItemsForName(String name) {
//        if (searchForName != null) {
//            searchForNameMediator.removeSource(searchForName);
//        }
//        searchForName = mDatabase.itemDao().getItemsAndOffersBasedOnSearchFts(fixQuery(name));
//        searchForNameMediator.addSource(searchForName, itemOfferCarts
//                -> searchForNameMediator.postValue(itemOfferCarts));
//    }


//    public void getItemsForName(String name, RepositoryInterface repositoryInterface) {
//        if (searchForName != null) {
//            searchForNameMediator.removeSource(searchForName);
//        }
//        searchForName = mDatabase.itemDao().getItemsAndOffersBasedOnSearchFts(fixQuery(name));
//        List<ExtractedResult> results = FuzzySearch.extractAll(name,itemNames);
//        appExecutors.diskIO().execute(() -> {
//            List<ItemOfferCart> itemOfferCarts = new ArrayList<>();
//            for(ExtractedResult result: results) {
//                if(result.getScore() > 70) {
//                    String id = itemNamesIdHashmap.get(result.getString());
//                    ItemOfferCart itemOfferCart = mDatabase.itemDao().getItemForIdSync(id);
//                    itemOfferCarts.add(itemOfferCart);
//                }
//            }
//            appExecutors.mainThread().execute(new Runnable() {
//                @Override
//                public void run() {
//                    repositoryInterface.getSearchResults(itemOfferCarts);
//                }
//            });
//        });
////        searchForNameMediator.addSource(searchForName, itemOfferCarts
////                -> searchForNameMediator.postValue(itemOfferCarts));
//    }


    //Helpers
    public static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    private static String loadJSONFromAsset(Context context) {
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

    private static String fixQuery(String query) {
        return query.trim().replaceAll("\\s+", "*") + "*";
    }

}
