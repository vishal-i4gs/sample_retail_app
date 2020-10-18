package com.example.sampleretainapp.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class CartItemOffer {
    @Embedded
    public CartItem cart ;
    @Relation(
            parentColumn = "itemId",
            entityColumn = "id",
            entity = Item.class
    )
    public Item item;
    @Relation(
            parentColumn = "itemId",
            entityColumn = "itemId"
    )
    public Offer offer;
}
