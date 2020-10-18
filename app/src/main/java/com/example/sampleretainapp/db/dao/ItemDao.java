package com.example.sampleretainapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.ItemOfferCart;

import java.util.List;

@Dao
public interface ItemDao {

    @Transaction
    @Query("SELECT * FROM items")
    LiveData<List<ItemOfferCart>> getItemsAndOffers();

    @Transaction
    @Query("SELECT * FROM items")
    LiveData<List<Item>> getItems();

    @Query("DELETE FROM items")
    public void removeAllItems();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Item> items);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Transaction
    @Query("SELECT * FROM items WHERE name LIKE '%' || :search || '%'")
    LiveData<List<ItemOfferCart>> getItemsAndOffersBasedOnSearch(String search);

    @Transaction
    @Query("SELECT * FROM items where id = :id")
    LiveData<ItemOfferCart> getItemForId(String id);
}
