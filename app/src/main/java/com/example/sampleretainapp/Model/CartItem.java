package com.example.sampleretainapp.Model;

import java.util.Objects;

public class CartItem {
    public Item item;
    public int quantity;
    public OfferItem offerItem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return item.equals(cartItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), item, quantity, offerItem);
    }
}
