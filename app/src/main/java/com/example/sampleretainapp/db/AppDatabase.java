package com.example.sampleretainapp.db;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.sampleretainapp.AppExecutors;
import com.example.sampleretainapp.Model.CartItem;
import com.example.sampleretainapp.Model.Item;
import com.example.sampleretainapp.Model.ItemsFts;
import com.example.sampleretainapp.Model.Offer;
import com.example.sampleretainapp.Model.OrderItem;
import com.example.sampleretainapp.db.Convertors.CartItemsConvertor;
import com.example.sampleretainapp.db.Convertors.DateConverter;
import com.example.sampleretainapp.db.dao.CartDao;
import com.example.sampleretainapp.db.dao.ItemDao;
import com.example.sampleretainapp.db.dao.OfferDao;
import com.example.sampleretainapp.db.dao.OrderDao;

@Database(entities = {OrderItem.class, CartItem.class, Offer.class, Item.class, ItemsFts.class}, version = 6)
@TypeConverters({CartItemsConvertor.class, DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "basic-sample-db";

    public abstract OrderDao orderDao();

    public abstract OfferDao offerDao();

    public abstract CartDao cartDao();

    public abstract ItemDao itemDao();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
//                .addMigrations(MIGRATION_5_6)
                .build();
    }

//    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
//
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `itemsFts` USING FTS4("
//                    + "`id` TEXT, `name` TEXT, content=`items`)");
//            database.execSQL("INSERT INTO itemsFts (`id`, `name`) "
//                    + "SELECT `id`, `name`, `description` FROM items");
//
//        }
//    };

}