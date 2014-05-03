package com.linkedin.avandra;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.linkedin.avandra.model.AsyncDBQuery;
import com.linkedin.avandra.model.TaskDBHelper;


/**
 * Fragment for showing all of our Task Lists
 */
public class TaskListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
  private SimpleCursorAdapter adapter;
  private TaskListViewHolder viewHolder;

  public final static String TAG = "TaskListFragment";

  public interface OnListSelectedListener {
    public void onListSelected(int listId);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.task_lists, container, false);
    viewHolder = new TaskListViewHolder(view);
    view.setTag(viewHolder);

    final LoaderManager.LoaderCallbacks<Cursor> callback = this;
    viewHolder.addNewList.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        String text = viewHolder.newListName.getText().toString();
        if (text != null && !text.equals(""))
        {
          TaskDBHelper helper = new TaskDBHelper(getActivity());

          helper.addNewTaskList(text);
          viewHolder.newListName.setText("");
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

    String[] fromColumns = {TaskDBHelper.LIST_NAME};
    int[] toViews = {R.id.taskListEntry};
    adapter = new SimpleCursorAdapter(getActivity(), R.layout.task_item, null, fromColumns, toViews, 0);
    setListAdapter(adapter);

    getLoaderManager().initLoader(0, null, this).forceLoad();
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id)
  {
    super.onListItemClick(l, v, position, id);
    Cursor cursor = adapter.getCursor();
    cursor.moveToPosition(position);
    int listId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
    try {
      OnListSelectedListener listener = (OnListSelectedListener) getActivity();
      listener.onListSelected(listId);
    } catch (ClassCastException ex) {
      throw new ClassCastException(getActivity().toString() + " must implement OnListSelectedListener");
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args)
  {
    return new AsyncDBQuery.Builder().allTaskLists().build(getActivity());
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data)
  {
    Log.i(TAG, "Finished loading all lists");
    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader)
  {
    adapter.swapCursor(null);
  }
}
