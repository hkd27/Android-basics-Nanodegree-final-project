package com.hemantdave.inventoryappfinalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by INDIA on 6/29/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    ArrayList<AppInventorPOJO> inventory;
    Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_INVENTORY = "Inventory";
    // Inventory.. Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SALES = "sales";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGEPATH = "imagepath";
    private static final String TABLE_NAME = "inventorydata";

    public DbHelper(Context context) {
        super(context, DATABASE_INVENTORY, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE inventorydata ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " +
                "quantity TEXT," + "price TEXT," + "sales TEXT," + " email TEXT," + "imagepath TEXT  )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS inventorydata");

        // Create tables again
        onCreate(db);
    }

    /*
    Here CRUD starts
     */
    // Adding new inventory row..
    void addInventoryData(AppInventorPOJO appInventorPOJO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, appInventorPOJO.getName());
        values.put(KEY_QUANTITY, appInventorPOJO.getQuantity());
        values.put(KEY_PRICE, appInventorPOJO.getPrice());
        values.put(KEY_SALES, appInventorPOJO.getSales());
        values.put(KEY_EMAIL, appInventorPOJO.getEmail());
        values.put(KEY_IMAGEPATH, appInventorPOJO.getImagePath());
        // inventory row dataFromPOJO
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting All inventory row..
    public ArrayList<AppInventorPOJO> getInventory() {
        ArrayList<AppInventorPOJO> getAllData = new ArrayList<AppInventorPOJO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AppInventorPOJO content = new AppInventorPOJO();
                content.setId(Integer.parseInt(cursor.getString(0)));
                content.setName(cursor.getString(1));
                content.setQuantity(cursor.getString(2));
                content.setPrice(cursor.getString(3));
                content.setSales(cursor.getString(4));
                content.setEmail(cursor.getString(5));
                content.setImagePath(cursor.getString(6));
                // Adding Result row.. to list
                getAllData.add(content);
            } while (cursor.moveToNext());
        }

        // return inventory row list
        return getAllData;
    }

    // Getting All inventory row..
    public ArrayList<AppInventorPOJO> getrowInventory(int id) {
        ArrayList<AppInventorPOJO> getAllData = new ArrayList<AppInventorPOJO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE id=" + id + " LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AppInventorPOJO content = new AppInventorPOJO();
                content.setId(Integer.parseInt(cursor.getString(0)));
                content.setName(cursor.getString(1));
                content.setQuantity(cursor.getString(2));
                content.setPrice(cursor.getString(3));
                content.setSales(cursor.getString(4));
                content.setEmail(cursor.getString(5));
                content.setImagePath(cursor.getString(6));
                // Adding Inventory row.. to list
                getAllData.add(content);
            } while (cursor.moveToNext());
        }

        // return inventory row list
        return getAllData;
    }


    // Updating single inventory row..
    public int updateInventory(AppInventorPOJO content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, content.getName());
        values.put(KEY_QUANTITY, content.getQuantity());
        values.put(KEY_PRICE, content.getPrice());
        values.put(KEY_SALES, content.getSales());
        values.put(KEY_EMAIL, content.getEmail());
        values.put(KEY_IMAGEPATH, content.getImagePath());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(content.getId())});
    }


    // Updating single inventory row..
    public int updateSales(AppInventorPOJO content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SALES, content.getSales());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(content.getId())});
    }

    // Updating single inventory row..
    public int updateQuantity(AppInventorPOJO content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, content.getQuantity());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(content.getId())});
    }

    public int modifyitemsSalesStats(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int previousQunatity = getQuantity(id);
        int previousSales = gettingSales(id);
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, (previousQunatity - 1));
        values.put(KEY_SALES, (previousSales + 1));

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = " + id,
                null);
    }

    public int addQuantity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int previousQunatity = getQuantity(id);
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, (previousQunatity + 1));
        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = " + id,
                null);
    }

    public int subtarctQuantity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int previousQunatity = getQuantity(id);
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, (previousQunatity - 1));
        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = " + id,
                null);
    }

    // Getting Quantity
    public int getQuantity(int id) {

        // Select All Query
        String selectQuery = "SELECT  quantity FROM " + TABLE_NAME + " WHERE id=" + id + " LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int Quantity = -1;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Quantity = Integer.parseInt(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return Quantity;
    }

    // Getting Quantity
    public int gettingSales(int id) {

        // Select All Query
        String selectQuery = "SELECT  sales FROM " + TABLE_NAME + " WHERE id=" + id + " LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int sales = -1;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                sales = Integer.parseInt(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return sales;
    }

    // Deleting single inventory row
    public void deleteInventory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = " + id,
                null);
        db.close();
    }

    /*//deleting database
    public void deleteDatabase() {

        context.deleteDatabase(DATABASE_INVENTORY);
    }*/

    /*//for deleting all entries
    public void deleteAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }*/

    public Cursor getId(String Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(
                "SELECT id  FROM " + TABLE_NAME + " WHERE name= '" + Name + "'", null);

    }

    // Getting contacts Count
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int value = cursor.getCount();
        cursor.close();

        // return count
        return value;
    }
}
