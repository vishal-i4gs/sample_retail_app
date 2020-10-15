package com.example.sampleretainapp.Model;

import java.util.List;
import java.util.Objects;

public class Item {
    public String name;
    public String id;
    public Float value;
    public String unit;
    public Integer price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, id, value, unit, price);
    }
}
