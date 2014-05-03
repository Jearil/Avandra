package com.linkedin.avandra.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


/**
 * DB Commands for the Task Lists.
 */
public class TaskDBHelper extends SQLiteOpenHelper
{
  public enum query {
    ALLTASKLISTS,
    TASKSOFLIST
  }

  public static final String TAG = "TaskDBHelper";

  public static final String DATABASE_NAME = "avandra";
  public static final int DATABASE_VERSION = 2;
  public static final String TASK_LIST_TABLE_NAME = "tasklists";
  public static final String TASK_LIST_NAME = "tasklist";
  public static final String TASK_LIST_COMPLETED = "completed";
  public static final String TASK_LIST_CREATE =
      "CREATE TABLE " + TASK_LIST_TABLE_NAME + " (" +
          BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
          TASK_LIST_NAME + " TEXT, " +
          TASK_LIST_COMPLETED + " BOOLEAN DEFAULT FALSE)";


  public TaskDBHelper(Context context)
  {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db)
  {
    db.execSQL(TASK_LIST_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    // do nothing currently
    for (int i = oldVersion; i < newVersion; i++) {
      switch (i) {
        case 1:
          upgradeTo2(db);
          break;
        default:
          break;
      }
    }
  }

  public void upgradeTo2(SQLiteDatabase db) {
    Log.i(TAG, "Upgrading to version 2");
    String upgradeText = "ALTER TABLE " + TASK_LIST_TABLE_NAME + " ADD COLUMN " +
        TASK_LIST_COMPLETED + " BOOLEAN DEFAULT FALSE";
    db.execSQL(upgradeText);
  }

  public Cursor getTaskList() {
    return query(TASK_LIST_TABLE_NAME, new String[]{BaseColumns._ID, TASK_LIST_NAME});
  }

  public void addNewTask(String task) {
    ContentValues insertValue = new ContentValues();
    insertValue.put(TASK_LIST_NAME, task);
    long id = getWritableDatabase().insert(TASK_LIST_TABLE_NAME, null, insertValue);
    Log.i("TASK_DB", "Inserted record " + id);
  }

  public Cursor query(String DBTable, String[] columns) {
    SQLiteDatabase db = getReadableDatabase();
    return db.query(DBTable, columns, null, null, null, null, null, null);
  }
}
