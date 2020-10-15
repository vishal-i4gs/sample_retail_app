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
    public List<CartItem> orderItems;
    public boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return orderId.equals(orderItem.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderId, orderTime, orderPrice, numberOfItems, orderItems, active);
    }
}
