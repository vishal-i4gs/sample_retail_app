package com.example.sampleretainapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.CartItemOffer;

import java.util.List;

@Dao
public interface CartDao {

//    @Transaction
//    @Query("SELECT * FROM cart ORDER BY date DESC")
//    LiveData<List<CartItemOffer>> getCartItems();

    @Transaction
    @Query("SELECT * FROM cart ORDER by cartId DESC")
    LiveData<List<CartItemOffer>> getCartItems();

    @Query("DELETE FROM cart")
    public void removeAllItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CartItem> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartItem cartItem);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(CartItem cartItem);

    @Delete
    void remove(CartItem cartItem);

    @Transaction
    @Query("SELECT * FROM cart where itemId=:id")
    LiveData<CartItem> getCartItemForId(String id);

    @Transaction
    @Query("SELECT * FROM cart where itemId=:id")
    CartItem getCartItemForIdSync(String id);
}
