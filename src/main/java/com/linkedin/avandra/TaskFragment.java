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
public class TaskFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
  public final static String LIST_TO_SHOW = "list_to_show";

  public final static String TAG = "TaskFragment";
  private SimpleCursorAdapter adapter;
  private TaskViewHolder viewHolder;
  private int listId;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Bundle arguments = getArguments();

    if (arguments != null)
    {
      listId = arguments.getInt(LIST_TO_SHOW);
    } else {
      listId = 1;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.tasks, container, false);
    viewHolder = new TaskViewHolder(view);
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
          helper.addNewTask(text, listId); //todo: link up to tasklist
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

    String[] fromColumns = {TaskDBHelper.TASK_NAME};
    int[] toViews = {R.id.taskListEntry};
    adapter = new SimpleCursorAdapter(getActivity(), R.layout.task_item, null, fromColumns, toViews, 0);
    setListAdapter(adapter);

    getLoaderManager().initLoader(0, null, this).forceLoad();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args)
  {
    Log.i(TAG, "Creating Adapter");
    //TODO: Add list number
    return new AsyncDBQuery.Builder().allTasksForList(listId).build(getActivity());
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

  public void updateListId(int listId) {
    this.listId = listId;
    getLoaderManager().restartLoader(0, null, this).forceLoad();
  }
}