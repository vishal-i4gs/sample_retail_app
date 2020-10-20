package com.example.sampleretainapp.Model;

import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Fts4(contentEntity = Item.class)
@Entity(tableName = "itemsFts")
public class ItemsFts {
    public String id;
    public String name;
}
