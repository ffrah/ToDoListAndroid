package com.ffrah.to_do_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "todolist";
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_ISDONE = "isDone";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TASK + " TEXT, " + COLUMN_ISDONE
                + " BOOLEAN DEFAULT 0)";
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task);
        db.insert(TABLE_NAME, null, values);
    }

    public void deleteTask(int taskId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(taskId)});  //db.delete(TABLE_NAME, COLUMN_ID + "=" + taskId, null); prone to sql injection
    }

    public void recreateDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteAllCompletedTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ISDONE + " = 1");
    }

    public void updateTask(int taskId, boolean isDone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISDONE, TaskModel.boolToInt(isDone));
        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(taskId)});
    }

    public ArrayList<TaskModel> getAllLabels(){
        ArrayList<TaskModel> list = new ArrayList<TaskModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ISDONE + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) { // looping through all rows and adding to list
            do {
                TaskModel row = new TaskModel(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
                list.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean isEmpty()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (mCursor.moveToFirst())
            return true;
        else
            return false;
    }
}