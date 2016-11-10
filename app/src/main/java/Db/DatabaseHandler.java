package Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Models.PendingPojo;

/**
 * Created by mraokorni on 11/7/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Todos";

    // PendingPojos table name
    private static final String TABLE_PENDING = "pedingTodos";
    // DonePojos table name
    private static final String TABLE_DONE = "doneTodos";
    // PendingPojos Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STATE = "state";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PendingPojoS_TABLE = "CREATE TABLE " + TABLE_PENDING + "(" + KEY_NAME + " TEXT,"
                + KEY_STATE + " INTEGER" + ")";
        String CREATE_DonePojo_TABLE = "CREATE TABLE " + TABLE_DONE + "(" + KEY_NAME + " TEXT,"
                + KEY_STATE + " INTEGER" + ")";
        db.execSQL(CREATE_PendingPojoS_TABLE);
        db.execSQL(CREATE_DonePojo_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONE);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new PendingPojo
    public void addPendingPojo(PendingPojo pendingPojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //  values.put(KEY_ID, pendingPojo.getId());
        values.put(KEY_NAME, pendingPojo.getName()); // PendingPojo Name
        values.put(KEY_STATE, pendingPojo.getState()); // PendingPojo Phone
        // Inserting Row
        db.insert(TABLE_PENDING, null, values);
        db.close(); // Closing database connection
    }

    // Getting All PendingPojos
    public List<PendingPojo> getAllPendingPojos() {
        List<PendingPojo> PendingPojoList = new ArrayList<PendingPojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PENDING;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PendingPojo PendingPojo = new PendingPojo();
                //    PendingPojo.setId(Integer.parseInt(cursor.getString(0)));
                PendingPojo.setName(cursor.getString(0));
                PendingPojo.setState(cursor.getInt(1));
                // Adding PendingPojo to list
                PendingPojoList.add(PendingPojo);
            } while (cursor.moveToNext());
        }
        // return PendingPojo list
        return PendingPojoList;
    }

    // Deleting single PendingPojo
    public void deletePendingPojo(PendingPojo PendingPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PENDING, KEY_NAME + " = ?",
                new String[]{(PendingPojo.getName())});
        db.close();
    }

    //Done Todos CRUD operations
    // Adding new PendingPojo
    public void addDonePojo(PendingPojo pendingPojo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //  values.put(KEY_ID, pendingPojo.getId());
        values.put(KEY_NAME, pendingPojo.getName()); // PendingPojo Name
        values.put(KEY_STATE, pendingPojo.getState()); // PendingPojo Phone
        // Inserting Row
        db.insert(TABLE_DONE, null, values);
        db.close(); // Closing database connection
    }

    // Getting All PendingPojos
    public List<PendingPojo> getAllDonePojos() {
        List<PendingPojo> PendingPojoList = new ArrayList<PendingPojo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DONE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PendingPojo PendingPojo = new PendingPojo();
                //    PendingPojo.setId(Integer.parseInt(cursor.getString(0)));
                PendingPojo.setName(cursor.getString(0));
                PendingPojo.setState(cursor.getInt(1));
                // Adding PendingPojo to list
                PendingPojoList.add(PendingPojo);
            } while (cursor.moveToNext());
        }
        // return PendingPojo list
        return PendingPojoList;
    }

    // Deleting single PendingPojo
    public void deleteDonePojo(PendingPojo PendingPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DONE, KEY_NAME + " = ?",
                new String[]{(PendingPojo.getName())});
        db.close();
    }
}