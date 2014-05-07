package com.linkedin.avandra.tasklists;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.linkedin.avandra.R;


/**
 * Holds the inner view for the task list layout. Contains a reference to the text entry for adding a new list along
 * with the button.
 */
public class TaskListViewHolder
{
  public EditText newListName;
  public Button addNewList;

  public TaskListViewHolder(View view)
  {
    newListName = (EditText) view.findViewById(R.id.add_task_list);
    addNewList = (Button) view.findViewById(R.id.create_list);
  }
}
