package asyncTasks;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import entities.WorkItem;

public class WorkItemsAsyncTask extends AsyncTask<Void, Void, WorkItem[]>{

	private String TAG = "IselApp";
	private WorkItem[] _workItems;
	private Cursor _cursor;
	private int idx;
	public WorkItemsAsyncTask (Cursor c) {
		_cursor = c;
		idx = 0;
	}

	@Override
	protected WorkItem[] doInBackground(Void... arg0) {
		Log.d(TAG , "WorkItemsAsyncTask - doInBackground");
		_workItems = new WorkItem[_cursor.getCount()];
		

		Log.d(TAG, "WorkItemsAsyncTask - doInBackground - starting to go through cursor...");
		_cursor.moveToFirst();
		do{
			InsertInArray();
			idx++;
		}while (_cursor.moveToNext());

		return _workItems;
	}

	private void InsertInArray() {
		long timeInMillisStartDate = Long.parseLong(_cursor.getString(_cursor.getColumnIndex("_workItemStartDate")));
		long timeInMillisDueDate = Long.parseLong(_cursor.getString(_cursor.getColumnIndex("_workItemDueDate")));
		_workItems[idx] = new WorkItem(
				_cursor.getInt(_cursor.getColumnIndex("_workItem_classId")),
				_cursor.getString(_cursor.getColumnIndex("_workItem_classFullname")),
				_cursor.getInt(_cursor.getColumnIndex("_workItemId")),
				_cursor.getString(_cursor.getColumnIndex("_workItemAcronym")), 
				_cursor.getString(_cursor.getColumnIndex("_workItemTitle")),
				timeInMillisStartDate,
				timeInMillisDueDate,
				_cursor.getInt(_cursor.getColumnIndex("_workItemEventId"))
				);
	}

}
