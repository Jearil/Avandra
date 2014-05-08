package com.linkedin.avandra.model;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;


/**
 * An AsyncTaskLoader that's used to get Task data from the database and load it into the adapter for display. Instances
 * of this class should be created with {@link com.linkedin.avandra.model.AsyncDBQuery.Builder}.
 * <p/>
 * Created by Colin on 4/18/2014.
 */
public class AsyncDBQuery extends AsyncTaskLoader<Cursor> {
    private static final String LIST_ID = "list_id";
    private static final String TAG = "AsyncDB";

    private TaskDBHelper.query option;
    private Bundle extras;

    private AsyncDBQuery(Context context, TaskDBHelper.query option, Bundle extras) {
        super(context);
        this.option = option;
        this.extras = extras;
        Log.i(TAG, "New AsyncDBQuery Created");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cursor loadInBackground() {
        TaskDBHelper helper = new TaskDBHelper(getContext());
        switch (option) {
            case ALLTASKLISTS:
                Log.i(TAG, "Getting task list");
                return helper.getTaskList();
            case TASKSOFLIST:
                Log.i(TAG, "Getting tasks of a list");
                int listId = extras.getInt(LIST_ID);
                return helper.getTasks(listId);
            default:
                Log.e(TAG, "Unknown task to get!");
                return null;
        }
    }

    /**
     * A builder for querying the DB for Task or TaskList data.
     */
    public static class Builder {
        private TaskDBHelper.query option;
        private Bundle extras;

        /**
         * The default builder will retrieve the list of tasklists. Columns will include
         * {@link com.linkedin.avandra.model.TaskDBHelper#LIST_NAME} and {@link android.provider.BaseColumns#_ID}
         */
        public Builder() {
            option = TaskDBHelper.query.TASKSOFLIST;
            extras = new Bundle();
        }

        /**
         * Retrieves all tasks for a specified list. Columns will include
         * {@link com.linkedin.avandra.model.TaskDBHelper#TASK_NAME} and {@link android.provider.BaseColumns#_ID}. Caller
         * should keep track of what list the results are associated with.
         * @param listId to retrieve all tasks for.
         * @return a builder to get all of the tasks for a specified list.
         */
        public Builder allTasksForList(int listId) {
            extras.clear();
            option = TaskDBHelper.query.TASKSOFLIST;
            extras.putInt(LIST_ID, listId);
            return this;
        }

        /**
         * Retrieves all of the TaskLists. Columns will include
         * {@link com.linkedin.avandra.model.TaskDBHelper#LIST_NAME} and {@link android.provider.BaseColumns#_ID}
         * @return a Builder to get all taskLists.
         */
        public Builder allTaskLists() {
            extras.clear();
            option = TaskDBHelper.query.ALLTASKLISTS;
            return this;
        }

        /**
         * Build this {@link com.linkedin.avandra.model.AsyncDBQuery}.
         * @param context for accessing resources
         * @return an {@link com.linkedin.avandra.model.AsyncDBQuery} from the properties of this builder.
         */
        public AsyncDBQuery build(Context context) {
            return new AsyncDBQuery(context, option, extras);
        }
    }
}

