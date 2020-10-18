package com.example.sampleretainapp.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ItemOfferCart {
    @Embedded
    public Item item;
    @Relation(
            parentColumn = "id",
            entityColumn = "itemId",
            entity = Offer.class
    )
    public Offer offer;
    @Relation(
            parentColumn = "id",
            entityColumn = "itemId"
    )
    public CartItem cart;
}
