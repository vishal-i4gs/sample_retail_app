package com.example.sampleretainapp.Model;

import androidx.annotation.ColorInt;

import java.util.Objects;

public class OfferItem {
    public String offerName;
    public Item item;
    public int minQuantity;
    public float percentageDiscount;
    public @ColorInt int color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfferItem offerItem = (OfferItem) o;
        return item.equals(offerItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), offerName, item, minQuantity, percentageDiscount, color);
    }
}
