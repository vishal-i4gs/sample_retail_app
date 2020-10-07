package com.example.sampleretainapp.Model;

import java.util.Objects;

public class SubItem {

    public String name;
    public float price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubItem item = (SubItem) o;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, price);
    }

}
