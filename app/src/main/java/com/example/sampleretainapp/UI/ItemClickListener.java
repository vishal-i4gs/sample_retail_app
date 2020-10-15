package com.example.sampleretainapp.UI;

import com.example.sampleretainapp.Model.Item;

public interface ItemClickListener {
    default void addItem(int position) {
    }

    default void removeItem(int position) {
    }

    default void itemClicked(int position) {
    }

    default void itemClicked(Item item) {
    }
}
