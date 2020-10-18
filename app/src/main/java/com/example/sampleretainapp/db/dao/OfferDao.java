package com.example.sampleretainapp.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.Model.OfferItemCart;

import java.util.List;

@Dao
public interface OfferDao {

    @Transaction
    @Query("SELECT * FROM offers")
    LiveData<List<OfferItemCart>> getOffersAndItems();

    @Query("DELETE FROM offers")
    public void removeAllOffers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Offer> offers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Offer offer);
}
