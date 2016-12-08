package com.fall16.csc413.team12.eventbrowserfinale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;


/**
 * Created by wgalan on 12/4/16.
 */

// This class is not currently needed. Could be used as extra points

public class DatabaseTable {

	private static final String TAG = "EventBrowserDatabase";

	//The columns we'll include in the dictionary table
	public static final String COL_NAME = "NAME";

	private static final String DATABASE_NAME = "DATABASE";
	private static final String FTS_VIRTUAL_TABLE = "FTS";
	private static final int DATABASE_VERSION = 1;

	private final DatabaseOpenHelper mDatabaseOpenHelper;

	public DatabaseTable(Context context) {
		mDatabaseOpenHelper = new DatabaseOpenHelper(context);
	}

	// Following two methods searches for the query
	public Cursor getNameMatches(String query, String[] columns) {
		String selection = COL_NAME + " MATCH ?";
		String[] selectionArgs = new String[] {query+"*"};

		return query(selection, selectionArgs, columns);
	}

	// Any matching results are returned in a Cursor
	private Cursor query(String selection, String[] selectionArgs, String[] columns) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(FTS_VIRTUAL_TABLE);

		Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
				columns, selection, selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		return cursor;
	}

	private static class DatabaseOpenHelper extends SQLiteOpenHelper {

		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;

		private static final String FTS_TABLE_CREATE =
				"CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
						" USING fts3 (" +
						COL_NAME + ")";

		DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			mDatabase = db;
			mDatabase.execSQL(FTS_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
			onCreate(db);
		}

		public long addName(String name) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(COL_NAME, name);

			return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
		}
	}
}
