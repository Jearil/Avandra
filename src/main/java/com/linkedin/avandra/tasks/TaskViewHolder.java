package com.linkedin.avandra.tasks;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.linkedin.avandra.R;


/**
 * Holds the inner view for the task layout. Contains a reference to the text entry for adding a new task along with
 * the button.
 */
public class TaskViewHolder
{

  public EditText textEntry;
  public Button add;

  public TaskViewHolder(View view)
  {
    textEntry = (EditText) view.findViewById(R.id.new_task_entry);
    add = (Button) view.findViewById(R.id.add_task);
  }
}
