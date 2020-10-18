package com.example.sampleretainapp.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart",
        foreignKeys = @ForeignKey(entity = Item.class,
                parentColumns = "id",
                childColumns = "itemId",
                onDelete = ForeignKey.CASCADE))
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    public int cartId;
    public String itemId;
    public int quantity;
}
