package com.example.onlinenotes;

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
                "(usn INT PRIMARY KEY, " +
                "password TEXT NOT NULL, " +
                "role NUMBER DEFAULT 1 " +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
