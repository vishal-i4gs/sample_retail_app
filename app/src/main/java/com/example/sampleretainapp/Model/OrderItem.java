package com.example.sampleretainapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "orders")
public class OrderItem implements Serializable {

    @PrimaryKey
    @NonNull
    public String orderId;
    public Date orderTime;
    public int orderPrice;
    public int numberOfItems;
    public List<CartItemOffer> orderItems;
    public boolean active;
}
