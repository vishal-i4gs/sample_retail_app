package com.example.sampleretainapp.Model;

import java.util.List;
import java.util.Objects;

public class Item {
    public String brand;
    public String name;
    public String id;
    public String type;
    public List<Float> quantities;
    public List<SubItem> subItems;
    public String measurement;
    public Float price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Item item = (Item) o;
        return brand.equals(item.brand) &&
                name.equals(item.name) &&
                id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), brand, name, id, type, quantities, measurement, price);
    }
}
