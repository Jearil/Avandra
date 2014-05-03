package com.linkedin.avandra;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by Colin on 4/18/2014.
 */
public class TaskListViewHolder
{

  public EditText textEntry;
  public Button add;

  public TaskListViewHolder(View view)
  {
    textEntry = (EditText) view.findViewById(R.id.new_task_entry);
    add = (Button) view.findViewById(R.id.add_task);
  }
}
