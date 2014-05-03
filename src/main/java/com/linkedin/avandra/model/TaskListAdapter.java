package com.linkedin.avandra.model;


import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;


/**
 * Created by Colin on 4/18/2014.
 */
public class TaskListAdapter extends CursorAdapter
{
  public TaskListAdapter(Context context, Cursor c, boolean autoRequery)
  {
    super(context, c, autoRequery);
  }

  public TaskListAdapter(Context context, Cursor c, int flags)
  {
    super(context, c, flags);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent)
  {
    return null;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor)
  {

  }
}
