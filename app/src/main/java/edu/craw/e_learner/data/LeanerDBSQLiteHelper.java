package edu.craw.e_learner.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.craw.e_learner.dao.Course;
import edu.craw.e_learner.dao.History;

/**
 * Created by farid on 3/22/2016.
 */
public class LeanerDBSQLiteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "spider_e_learner.db";

    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INT(11)  ";
    private static final String STRING_TYPE = " VARCHAR(255) ";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String DATE_TYPE = " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_HISTORY_ENTRY =
            "CREATE TABLE " + LeanerDBContracts.HistoryEntry.TABLE_NAME + " (" +
                    LeanerDBContracts.HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LeanerDBContracts.HistoryEntry.COLUMN_NAME_LOG + TEXT_TYPE + COMMA_SEP +
                    LeanerDBContracts.HistoryEntry.COLUMN_NAME_CREATEDAT + DATE_TYPE + COMMA_SEP +
                    LeanerDBContracts.HistoryEntry.COLUMN_NAME_UPDATEDAT + DATE_TYPE +
                    " )";
    private static final String SQL_CREATE_COURSE_ENTRY = "CREATE TABLE " + LeanerDBContracts.CourseEntry.TABLE_NAME + " (" +
    LeanerDBContracts.CourseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_NAME + STRING_TYPE + NOT_NULL + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_ABBREVIATION + STRING_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_NETWORK_ID + INT_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_CODE + STRING_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_SCHOOLID + INT_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_SCHOOLNAME + STRING_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDID + INT_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDUSERNAME + STRING_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_ACTIVE + INT_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_CREATEDAT + DATE_TYPE + COMMA_SEP +
    LeanerDBContracts.CourseEntry.COLUMN_NAME_UPDATEDAT + DATE_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LeanerDBContracts.HistoryEntry.TABLE_NAME;
    private static final String TAG = "LEARNERDBSQLITELOG";

    public LeanerDBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_HISTORY_ENTRY);
        db.execSQL(SQL_CREATE_COURSE_ENTRY);
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

    public long createHistoryEntry(History history) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LeanerDBContracts.HistoryEntry.COLUMN_NAME_LOG, history.getLog());
        // insert row
        long history_id = db.insert(LeanerDBContracts.HistoryEntry.TABLE_NAME, null, values);
        return history_id;
    }

    public long createCourseEntry(Course entry) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_NAME, entry.getName());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_CODE, entry.getCode());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_NETWORK_ID, entry.getNetworkId());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_DESCRIPTION, entry.getDescription());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_ABBREVIATION, entry.getAbbrev());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDID, entry.getAddedId());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDUSERNAME, entry.getAddedUsername());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_SCHOOLID, entry.getSchoolId());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_SCHOOLNAME, entry.getSchoolName());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_UPDATEDAT, entry.getUpdatedAt().toString());
        values.put(LeanerDBContracts.CourseEntry.COLUMN_NAME_CREATEDAT, entry.getCreatedAt().toString());
        return db.insert(LeanerDBContracts.HistoryEntry.TABLE_NAME, null, values);
    }


    public History getHistoryById(long _id) throws SQLiteException {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + LeanerDBContracts.HistoryEntry.TABLE_NAME + " WHERE "
                + LeanerDBContracts.HistoryEntry._ID + " = " + _id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        return new History(c.getString(c.getColumnIndex(LeanerDBContracts.HistoryEntry.COLUMN_NAME_LOG)));
    }

    public Course getCourseById(long _id) throws SQLiteException {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + LeanerDBContracts.HistoryEntry.TABLE_NAME + " WHERE "
                + LeanerDBContracts.HistoryEntry._ID + " = " + _id;

        Cursor c = db.rawQuery(selectQuery, null);


        if (c != null)
            c.moveToFirst();
        return new Course(_id,
                c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_NAME)),
                c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_DESCRIPTION)),
                c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_ABBREVIATION)),
                c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_CODE)),
                Timestamp.valueOf(c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_UPDATEDAT))),
                Timestamp.valueOf(c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_CREATEDAT))),
                c.getLong(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDID)),
                c.getLong(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_SCHOOLID)),
                c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDUSERNAME)),
                c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDUSERNAME))
        );
    }

    public List<History> getHistory() throws SQLiteException {

        List<History> all = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + LeanerDBContracts.HistoryEntry.TABLE_NAME + " ORDER BY " + LeanerDBContracts.HistoryEntry.COLUMN_NAME_UPDATEDAT + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {

            do {
                all.add(new
                        History(
                        Integer.parseInt(c.getLong(c.getColumnIndex(LeanerDBContracts.HistoryEntry._ID)) + ""),
                        c.getString(c.getColumnIndex(LeanerDBContracts.HistoryEntry.COLUMN_NAME_LOG)),
                        Timestamp.valueOf(c.getString(c.getColumnIndex(LeanerDBContracts.HistoryEntry.COLUMN_NAME_CREATEDAT))),
                        Timestamp.valueOf(c.getString(c.getColumnIndex(LeanerDBContracts.HistoryEntry.COLUMN_NAME_UPDATEDAT)))
                ));
            } while (c.moveToNext());
        }
        return all;
    }
    public List<Course> getCourses() throws SQLiteException {

        List<Course> all = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + LeanerDBContracts.CourseEntry.TABLE_NAME + " ORDER BY " + LeanerDBContracts.CourseEntry.COLUMN_NAME_UPDATEDAT + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {

            do {
                all.add(new Course(c.getLong(c.getColumnIndex(LeanerDBContracts.HistoryEntry._ID)),
                        c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_NAME)),
                        c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_DESCRIPTION)),
                        c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_ABBREVIATION)),
                        c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_CODE)),
                        Timestamp.valueOf(c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_UPDATEDAT))),
                        Timestamp.valueOf(c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_CREATEDAT))),
                        c.getLong(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDID)),
                        c.getLong(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_SCHOOLID)),
                        c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDUSERNAME)),
                        c.getString(c.getColumnIndex(LeanerDBContracts.CourseEntry.COLUMN_NAME_ADDEDUSERNAME))
                ));
            } while (c.moveToNext());
        }
        return all;
    }
    public int updateHistoryEntry(History history) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LeanerDBContracts.HistoryEntry.COLUMN_NAME_LOG, history.getLog());
        values.put(LeanerDBContracts.HistoryEntry.COLUMN_NAME_UPDATEDAT, this.getDateTime());

        // updating row
        return db.update(LeanerDBContracts.HistoryEntry.TABLE_NAME, values, LeanerDBContracts.HistoryEntry._ID + " = ?",
                new String[]{String.valueOf(history.getId())});
    }

    public void deleteHistoryEntry(long _id) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LeanerDBContracts.HistoryEntry.TABLE_NAME, LeanerDBContracts.HistoryEntry._ID + " = ?",
                new String[]{String.valueOf(_id)});
    }

    private String getDateTime() {//http://tips.androidhive.info/2013/10/android-insert-datetime-value-in-sqlite-database/
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
