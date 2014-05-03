package com.linkedin.avandra;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by Colin on 5/3/2014.
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
