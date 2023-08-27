package com.example.onlinenotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database1 extends SQLiteOpenHelper {
    private static final String dbname = "notes";

    public static final String register = "register";

    public Database1(@Nullable Context context) {super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS " + register +
                "(usn TEXT PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role NUMBER DEFAULT 1 " +
                ")"
        );

        sqLiteDatabase.execSQL("" +
                "INSERT INTO " + register + " " +
                "(usn,name, password, role) VALUES " +
                "('4SO22MC0091', 'harish','123456', 0)"
        );
    }
    public boolean registerData(String name,String usn, String password) {

        //open database in writable mode to insert data
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Create contentValues; a string-builder-like function
        ContentValues contentValues = new ContentValues();


        contentValues.put("name", name);
        contentValues.put("usn", usn);
        contentValues.put("password", password);


        //insert data into table
        long res = sqLiteDatabase.insert(register, null, contentValues);

        sqLiteDatabase.close();
        return res != -1;

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
