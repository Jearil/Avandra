package com.linkedin.avandra;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Colin on 4/18/2014.
 */
public class Launcher extends Activity
{
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tasks_fragment);
  }
}