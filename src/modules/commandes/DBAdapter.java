package com.pauline.dm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_TABLE = "numTable";
    static final String KEY_NBCONVIVES = "nbConvives";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "tables";
    static final int DATABASE_VERSION = 1;


    static final String DATABASE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE + " ("
                    + KEY_TABLE + " INTEGER PRIMARY KEY, "
                    + KEY_NBCONVIVES + " INTEGER);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertTable(Integer table, Integer nbConvives) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TABLE, table);
        initialValues.put(KEY_NBCONVIVES, nbConvives);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteTable(Integer nbTable) {
        return db.delete(DATABASE_TABLE, KEY_TABLE + "=" + nbTable, null) > 0;
    }

    public Cursor getAllTables() {
        return db.query(DATABASE_TABLE, new String[] {KEY_TABLE, KEY_NBCONVIVES},
                null, null, null, null, null);
    }

    public Cursor getTable(Integer nbTable) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_TABLE, KEY_NBCONVIVES},
                KEY_TABLE + "=" + nbTable, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateTable(Integer nbTable, Integer nbConvives) {
        ContentValues args = new ContentValues();
        args.put(KEY_NBCONVIVES, nbConvives);
        return db.update(DATABASE_TABLE, args, KEY_TABLE + "=" + nbTable, null) > 0;
    }
}
