package com.linkedin.avandra;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;


/**
 * Our main activity for launching all of our fragments to be displayed.
 *
 * Created by Colin on 4/18/2014.
 */
public class MainActivity extends Activity implements TaskListFragment.OnListSelectedListener
{
  private static final String STORED_LAST_LIST = "last_list";

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.avandra);

    if (savedInstanceState != null) {
      return;
    }
    // If we just have a container we're on a small screen and need to populate it
    if (findViewById(R.id.fragment_container) != null) {

      //TODO: This should change to the list once we have it, or default to last opened state.
      TaskListFragment fragment = new TaskListFragment();

      getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, TaskListFragment.TAG).commit();

    } else if (findViewById(R.id.tablet_container) != null) {
      // We're on a large screen, we should add both fragments
      TaskListFragment taskListFragment = new TaskListFragment();
      taskListFragment.setArguments(getIntent().getExtras());

      TaskFragment taskFragment = createDefaultTaskFragment();

      getFragmentManager().beginTransaction().add(R.id.tasklist_fragment, taskListFragment, TaskListFragment.TAG)
          .add(R.id.task_fragment, taskFragment, TaskFragment.TAG).commit();
    }
  }

  public TaskFragment createDefaultTaskFragment() {
    return createTaskFragment(getLastTaskList());
  }

  public TaskFragment createTaskFragment(int listId) {
    TaskFragment fragment = new TaskFragment();
    Bundle args = getIntent().getExtras() == null ? new Bundle() : getIntent().getExtras();
    args.putInt(TaskFragment.LIST_TO_SHOW, listId);
    fragment.setArguments(args);

    return fragment;
  }

  public int getLastTaskList() {
    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
      return preferences.getInt(STORED_LAST_LIST, 1);
  }

  @Override
  public void onListSelected(int listId)
  {
    TaskFragment taskFragment = (TaskFragment) getFragmentManager().findFragmentByTag(TaskFragment.TAG);
    // If we don't have the tasks fragment up, start one
    if (taskFragment != null) {
        taskFragment.updateListId(listId);
    } else {
      TaskFragment newTaskFragment = createTaskFragment(listId);

      FragmentTransaction transaction = getFragmentManager().beginTransaction();

      transaction.replace(R.id.fragment_container, newTaskFragment);
      transaction.addToBackStack(null);
      transaction.commit();
    }

    // any in event, let's store this for later loading
    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putInt(STORED_LAST_LIST, listId).commit();
  }
}