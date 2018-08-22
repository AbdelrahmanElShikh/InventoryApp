package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by bodiy_000 on 09-Apr-18.
 */

public class InventoryDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "inventory.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_INTEGER = "integer";

    private final static String CREATE_TABLE_ENTRY = "CREATE TABLE " + InventoryContract.ProductEntry.TABlE_NAME +
            " ( " +
            InventoryContract.ProductEntry.COLUMN_ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT ," +
            InventoryContract.ProductEntry.COLUMN_PRODUCT_NAME + " " + TYPE_TEXT + " , " +
            InventoryContract.ProductEntry.COLUMN_PRODUCT_PRICE + " " + TYPE_INTEGER + " , " +
            InventoryContract.ProductEntry.COLUMN_PRODUCT_QUANTITY + " " + TYPE_INTEGER + " , " +
            InventoryContract.ProductEntry.COLUMN_SUPPLIER_NAME + " " + TYPE_TEXT + " , " +
            InventoryContract.ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " " + TYPE_TEXT +
            " )";

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ENTRY);
        Log.v(this.getClass().getSimpleName(), "The Create Stmt is >> " + CREATE_TABLE_ENTRY + "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
