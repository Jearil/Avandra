package com.linkedin.avandra.model;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


/**
 * Created by Colin on 4/18/2014.
 */
public class AsyncDBQuery extends AsyncTaskLoader<Cursor>
{
  private TaskDBHelper.query option;

  private AsyncDBQuery(Context context, TaskDBHelper.query option)
  {
    super(context);
    this.option = option;
    Log.i("ASYNC_DB", "New AsyncDBQuery Created");
  }

  @Override
  public Cursor loadInBackground()
  {
    TaskDBHelper helper = new TaskDBHelper(getContext());
    switch(option) {
      case ALLTASKLISTS:
        Log.i("ASYNC", "Getting task list");
        return helper.getTaskList();
      default:
        Log.e("ASYNC", "Unknown task to get!");
        return null;
    }
  }

  public static class Builder {
    private TaskDBHelper.query option;

    public Builder()
    {
      option = TaskDBHelper.query.ALLTASKLISTS;
    }

    public Builder allTaskLists() {
      option = TaskDBHelper.query.ALLTASKLISTS;
      return this;
    }

    public AsyncDBQuery build(Context context) {
      return new AsyncDBQuery(context, option);
    }
  }
}

