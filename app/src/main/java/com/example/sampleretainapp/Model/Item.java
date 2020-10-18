package com.example.sampleretainapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "items")
public class Item {

    @PrimaryKey
    @NonNull
    public String id;
    public String name;
    public Float value;
    public String unit;
    public Integer price;

}
