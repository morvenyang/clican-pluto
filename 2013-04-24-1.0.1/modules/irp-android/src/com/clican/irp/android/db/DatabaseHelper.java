package com.clican.irp.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DatabaseHelper";
	
	private static final String DATABASE_CREATE =
        "create table irp_property (id integer primary key autoincrement, "
        + "name text not null, value text);";
	
	private static final String DATABASE_DROP = "DROP TABLE IF EXISTS irp_property;";

    private static final String DATABASE_NAME = "irp";
    private static final int DATABASE_VERSION = 2;
    
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL(DATABASE_DROP);
        onCreate(db);
    }
}
