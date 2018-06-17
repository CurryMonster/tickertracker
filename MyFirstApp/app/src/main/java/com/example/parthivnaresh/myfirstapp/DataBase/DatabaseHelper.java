package com.example.parthivnaresh.myfirstapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Parthiv Naresh on 4/1/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "nutrition.db";
    public static final String TABLE_NAME = "nutrition_table";
    public static final String Col_1 = "ID";
    public static final String Col_2 = "NAME";
    public static final String Col_3 = "CALORIES";
    public static final String Col_4 = "FAT";
    public static final String Col_5 = "PROTEIN";
    public static final String Col_6 = "CARBOHYDRATES";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, CALORIES INTEGER, " +
                "FAT REAL, PROTEIN REAL, CARBOHYDRATES REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, Integer calories, Float fat, Float protein, Float carbs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2, name);
        contentValues.put(Col_3, calories);
        contentValues.put(Col_4, fat);
        contentValues.put(Col_5, protein);
        contentValues.put(Col_6, carbs);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, String name, Integer calories, Float fat, Float protein, Float carbs) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_1, id);
        contentValues.put(Col_2, name);
        contentValues.put(Col_3, calories);
        contentValues.put(Col_4, fat);
        contentValues.put(Col_5, protein);
        contentValues.put(Col_6, carbs);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});
        return true;
    }
}
