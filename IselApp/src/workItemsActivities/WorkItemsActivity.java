package workItemsActivities;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import asyncTasks.WorkItemsAsyncTask;
import classesActivities.SettingsActivity;

import com.example.iselapp.R;

import customAdapters.WorkItemCustomAdapter;
import entities.WorkItem;

public class WorkItemsActivity extends Activity implements LoaderCallbacks<Cursor>{

	private static final String TAG = "IselApp";
	private final Uri _workItems = Uri.parse("content://com.example.iselappserver/workItems");
	private ListView _listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate MainActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workitems_activity_layout);
		Log.d(TAG, "onCreate loadManagerInit");
		getLoaderManager().initLoader(1, null, this);
		_listView = (ListView) findViewById(R.id.workItems_list);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(TAG, "onCreateLoader");
		return new CursorLoader(this, 
				_workItems, 
				new String[]{
						"_workItemId",
						"_workItem_classId",
						"_workItem_classFullname",
						"_workItemAcronym",
						"_workItemTitle",
						"_workItemStartDate",
						"_workItemDueDate",
						"_workItemEventId"}, 
				null,
				null, 
				"_workItemDueDate asc");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG, "onLoadFinish");
		if(data == null || data.getCount() == 0){
			Intent i = new Intent(WorkItemsActivity.this, SettingsActivity.class);
			startActivityForResult(i,0);
		}
		else
			callAsyncTask(data);
	}

	private void callAsyncTask(Cursor c){
		Log.d(TAG,"MainActivity - callAsyncTask");
		WorkItemsAsyncTask newsAsync = new WorkItemsAsyncTask(c) {

			@Override
			protected void onPostExecute(WorkItem[] result) {
				if (result != null) {	
					Log.d(TAG,"MainActivity - onPostExecute - result :! null");
					WorkItemCustomAdapter adapter = new WorkItemCustomAdapter(
							WorkItemsActivity.this, 
							R.layout.workitem_list_layout, 
							result);
					_listView.setAdapter(adapter);
				}
			}
		};
		newsAsync.execute();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}
