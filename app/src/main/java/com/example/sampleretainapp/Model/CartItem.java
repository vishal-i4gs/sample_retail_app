package com.example.sampleretainapp.Model;

import java.util.Objects;

public class CartItem {
    public int subItemIndex;
    public Item parentItem;
    public int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return subItemIndex == cartItem.subItemIndex && parentItem.equals(cartItem.parentItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subItemIndex);
    }
}
