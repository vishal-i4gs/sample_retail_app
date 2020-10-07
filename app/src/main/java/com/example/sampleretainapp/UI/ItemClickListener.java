package com.example.sampleretainapp.UI;

import com.example.sampleretainapp.Model.Item;

public interface ItemClickListener {
    void addItem(int position);

    void removeItem(int position);

    void subItemIndexChanged(int subIndex, int index);
}
