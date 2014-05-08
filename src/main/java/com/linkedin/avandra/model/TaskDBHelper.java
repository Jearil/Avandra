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
  public static final int DATABASE_VERSION = 1;

  // TaskList table for containing our list of lists
  public static final String TASK_LIST_TABLE_NAME = "tasklist";
  public static final String LIST_NAME = "list";
  public static final String TASK_LIST_TABLE_CREATE =
      "CREATE TABLE " + TASK_LIST_TABLE_NAME + " (" +
          BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
          LIST_NAME + " TEXT)";

  // Task table contains all tasks from every list
  public static final String TASK_TABLE_NAME = "tasks";
  public static final String TASK_NAME = "task";
  public static final String TASK_COMPLETED = "completed";
  public static final String TASK_LIST_ID = "tasklistId";
  public static final String TASK_TABLE_CREATE =
      "CREATE TABLE " + TASK_TABLE_NAME + " (" +
          BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
          TASK_NAME + " TEXT, " +
          TASK_COMPLETED + " BOOLEAN DEFAULT FALSE," +
          TASK_LIST_ID + " INTEGER NOT NULL," +
          "FOREIGN KEY(" + TASK_LIST_ID + ") REFERENCES " + TASK_LIST_TABLE_NAME + "(" + BaseColumns._ID + "))";


  public TaskDBHelper(Context context)
  {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db)
  {
    db.execSQL(TASK_LIST_TABLE_CREATE);
    db.execSQL(TASK_TABLE_CREATE);
    db.execSQL("INSERT INTO " + TASK_LIST_TABLE_NAME + "(" + LIST_NAME + ") VALUES ('ToDo')");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
    // do nothing currently
    for (int i = oldVersion; i < newVersion; i++) {
      switch (i) {
        default:
          break;
      }
    }
  }

  /**
   * Queries the database for a list of all tasks.
   *
   * @return a cursor with the data for all tasks.
   */
  public Cursor getTaskList() {
    return query(TASK_LIST_TABLE_NAME, new String[]{BaseColumns._ID, LIST_NAME});
  }

  public Cursor getTasks(int listId) {
    String where = TASK_LIST_ID + "=?";
    return query(TASK_TABLE_NAME,
                 new String[]{BaseColumns._ID, TASK_NAME},
                 where,
                 new String[]{Integer.toString(listId)});
  }

  /**
   * Insert a new task into the database.
   *
   * @param task text to insert.
   */
  public void addNewTask(String task, int taskList) {
    ContentValues insertValue = new ContentValues();
    insertValue.put(TASK_NAME, task);
    insertValue.put(TASK_LIST_ID, taskList);
    long id = getWritableDatabase().insert(TASK_TABLE_NAME, null, insertValue);
    Log.i("TASK_DB", "Inserted record " + id);
  }

    /**
     * removes the specified task from the database. One should change the display to reflect this change afterwards.
     * @param taskId to remove.
     */
  public void removeTask(int taskId) {
    String where = BaseColumns._ID + "=?";
    SQLiteDatabase db = getWritableDatabase();
    db.delete(TASK_TABLE_NAME, where, new String[]{Integer.toString(taskId)});
  }

    /**
     * Add a new list that can contain tasks.
     * @param taskList is the name to call the new list.
     */
  public void addNewTaskList(String taskList) {
    ContentValues insertValue = new ContentValues();
    insertValue.put(LIST_NAME, taskList);
    long id = getWritableDatabase().insert(TASK_LIST_TABLE_NAME, null, insertValue);
    Log.i(TAG, "Inserted list " + id);
  }

  private Cursor query(String DBTable, String[] columns) {
    return query(DBTable, columns, null, null);
  }

  private Cursor query(String DBTable, String[] columns, String selection, String[] selectionArgs) {
    SQLiteDatabase db = getReadableDatabase();
    return db.query(DBTable, columns, selection, selectionArgs, null, null, null, null);
  }
}
