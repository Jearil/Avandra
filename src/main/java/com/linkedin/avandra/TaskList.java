package com.linkedin.avandra;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import com.linkedin.avandra.model.AsyncDBQuery;
import com.linkedin.avandra.model.TaskDBHelper;


/**
 * Fragment for showing the lsit of tasks.
 */
public class TaskList extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
  private final static String TAG = "TaskList";
  private SimpleCursorAdapter adapter;
  private TaskListViewHolder viewHolder;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.task_list, container);
    viewHolder = new TaskListViewHolder(view);
    view.setTag(viewHolder);

    final LoaderManager.LoaderCallbacks<Cursor> callback = this;

    viewHolder.add.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        String text = viewHolder.textEntry.getText().toString();
        if (text != null && !text.equals("")) {
          TaskDBHelper helper = new TaskDBHelper(getActivity());
          helper.addNewTask(text);
          viewHolder.textEntry.setText("");
          getLoaderManager().restartLoader(0, null, callback).forceLoad();
        }
      }
    });
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState)
  {
    super.onActivityCreated(savedInstanceState);

    String[] fromColumns = {TaskDBHelper.TASK_LIST_NAME};
    int[] toViews = {R.id.taskListEntry};
    adapter = new SimpleCursorAdapter(getActivity(), R.layout.task_list_item, null, fromColumns, toViews, 0);
    setListAdapter(adapter);

    getLoaderManager().initLoader(0, null, this).forceLoad();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args)
  {
    Log.i(TAG, "Creating Adapter");
    return new AsyncDBQuery.Builder().allTaskLists().build(getActivity());
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data)
  {
    Log.i(TAG, "Finished loading");
    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader)
  {
    adapter.swapCursor(null);
  }
}