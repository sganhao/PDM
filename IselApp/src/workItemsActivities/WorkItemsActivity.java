package workItemsActivities;

import listModels.WorkItemListModel;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import asyncTasks.WorkItemsAsyncTask;
import classesActivities.SettingsActivity;

import com.example.iselapp.R;

import entities.WorkItem;
import fragments.WorkItemFragment;
import fragments.WorkItemListFragment;

public class WorkItemsActivity extends FragmentActivity implements LoaderCallbacks<Cursor>, WorkItemListFragment.Callback {

	private static final String TAG = "IselApp";
	private final Uri _workItems = Uri.parse("content://com.example.iselappserver/workItems");
	private WorkItemListModel _model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate MainActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workitem_masterdetail);
		Log.d(TAG, "onCreate loadManagerInit");
		getLoaderManager().initLoader(1, null, this);
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
						"_workItemStarDate",
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
					
					_model = new WorkItemListModel(result);
					FragmentManager fm = getSupportFragmentManager();
					WorkItemListFragment f;
					
					if(fm.findFragmentById(R.id.workitem_Detail_fragmentPlaceholder) == null){
						f = WorkItemListFragment.newInstance(_model);			
						fm.beginTransaction()
							.add(R.id.workitem_list_fragmentPlaceholder, f)
							.commit();
					}else{
						f = WorkItemListFragment.newInstance(_model);
						fm.beginTransaction().replace(R.id.workitem_list_fragmentPlaceholder, f).commit();
					}
				}
			}
		};
		newsAsync.execute();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onListItemClick(int position) {
		if (findViewById(R.id.workitem_Detail_fragmentPlaceholder) != null) {
			FragmentManager fm = getSupportFragmentManager();
			WorkItemFragment newFrag = WorkItemFragment.newInstance(_model.getItem(position));
			fm.beginTransaction().replace(R.id.workitem_Detail_fragmentPlaceholder, newFrag).commit();
			
//			if(!_model.getItem(position).news_isViewed){
//				_model.getItem(position).news_isViewed = true;
//				Intent service = new Intent(this, IselAppService.class);
//				service.putExtra("newId", _model.getItem(position).news_id);
//				service.setAction("userUpdateNews");
//				startService(service);				
//			}
		} else {
			Intent i = new Intent(this, WorkItemActivity.class);
			i.putExtra("workitemlistmodel", _model);
			i.putExtra("position", position);
			startActivity(i);
		}		
	}
}
