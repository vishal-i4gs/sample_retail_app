package com.example.sampleretainapp.Model;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "offers",
        foreignKeys = @ForeignKey(entity = Item.class,
                parentColumns = "id",
                childColumns = "itemId",
                onDelete = ForeignKey.CASCADE))
public class Offer {

    @PrimaryKey(autoGenerate = true)
    public int offerId;
    public String offerName;
    public String itemId;
    public int minQuantity;
    public float percentageDiscount;
    public @ColorInt
    int color;

}
