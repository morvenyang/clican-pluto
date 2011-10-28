package com.clican.irp.android.db;

import com.google.inject.Inject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PropertyDbAdapter {

	@Inject
	private DatabaseHelper mDbHelper;

	private SQLiteDatabase mDb;

	public PropertyDbAdapter open() throws SQLException {
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createProperty(String name, String value) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", name);
		initialValues.put("value", value);
		return mDb.insert("irp_property", null, initialValues);
	}

	public boolean deleteProperty(String name) {
		return mDb.delete("irp_property", "name='" + name+"'", null) > 0;
	}

	public String getProperty(String name) {
		Cursor mCursor =mDb.query(true, "irp_property", new String[] { "value" }, "name='"
				+ name + "'", null, null, null, null, null);
		if (mCursor != null) {
			if( mCursor.moveToFirst()){
				return mCursor.getString(0);
			}
		}
		return null;
	}

	public boolean updateProperty(String name, String value) {
		ContentValues args = new ContentValues();
		args.put("value", value);
		return mDb.update("irp_property", args, "name='" + name + "'", null) > 0;
	}

}
