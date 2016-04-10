package edu.craw.e_learner.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.craw.e_learner.dao.History;

/**
 * Created by farid on 3/22/2016.
 */
public class LeanerDBSQLiteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "spider_e_learner.db";

    private static final String TEXT_TYPE = " TEXT ";
    private static final String DATE_TYPE = " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LeanerDBContract.HistoryEntry.TABLE_NAME + " (" +
                    LeanerDBContract.HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    LeanerDBContract.HistoryEntry.COLUMN_NAME_LOG + TEXT_TYPE + COMMA_SEP +
                    LeanerDBContract.HistoryEntry.COLUMN_NAME_CREATEDAT + DATE_TYPE + COMMA_SEP +
                    LeanerDBContract.HistoryEntry.COLUMN_NAME_UPDATEDAT + DATE_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LeanerDBContract.HistoryEntry.TABLE_NAME;
    private static final String TAG = "LEARNERDBSQLITELOG";

    public LeanerDBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //History

    public long createHistoryEntry(History history) throws SQLiteException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LeanerDBContract.HistoryEntry.COLUMN_NAME_LOG, history.getLog());
        // insert row
        long history_id = db.insert(LeanerDBContract.HistoryEntry.TABLE_NAME, null, values);
        return history_id;
    }


    public History getHistoryById(long _id) throws SQLiteException {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + LeanerDBContract.HistoryEntry.TABLE_NAME + " WHERE "
                + LeanerDBContract.HistoryEntry._ID + " = " + _id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        return new History(c.getString(c.getColumnIndex(LeanerDBContract.HistoryEntry.COLUMN_NAME_LOG)));
    }

    public List<History> getHistory() throws SQLiteException
    {

        List<History> all = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + LeanerDBContract.HistoryEntry.TABLE_NAME+ " ORDER BY "+ LeanerDBContract.HistoryEntry.COLUMN_NAME_UPDATEDAT+ " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {

            do {
                all.add(new
                        History(
                        Integer.parseInt(c.getLong(c.getColumnIndex(LeanerDBContract.HistoryEntry._ID))+""),
                        c.getString(c.getColumnIndex(LeanerDBContract.HistoryEntry.COLUMN_NAME_LOG)),
                        Timestamp.valueOf(c.getString(c.getColumnIndex(LeanerDBContract.HistoryEntry.COLUMN_NAME_CREATEDAT))),
                        Timestamp.valueOf(c.getString(c.getColumnIndex(LeanerDBContract.HistoryEntry.COLUMN_NAME_UPDATEDAT)))
                ));
            } while (c.moveToNext());
        }
        return all;
    }
    public int updateHistoryEntry(History history) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LeanerDBContract.HistoryEntry.COLUMN_NAME_LOG, history.getLog());
        values.put(LeanerDBContract.HistoryEntry.COLUMN_NAME_UPDATEDAT, this.getDateTime());

        // updating row
        return db.update(LeanerDBContract.HistoryEntry.TABLE_NAME, values, LeanerDBContract.HistoryEntry._ID + " = ?",
                new String[] { String.valueOf(history.getId()) });
    }
    public void deleteHistoryEntry(long _id) throws SQLiteException{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LeanerDBContract.HistoryEntry.TABLE_NAME, LeanerDBContract.HistoryEntry._ID + " = ?",
                new String[] { String.valueOf(_id) });
    }

    private String getDateTime() {//http://tips.androidhive.info/2013/10/android-insert-datetime-value-in-sqlite-database/
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
