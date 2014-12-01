package com.example.newsclass;

import java.util.Set;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

public class SettingsActivity extends Activity implements LoaderCallbacks<Cursor> {

	private ListView _listView2;
	private ClassesCustomAdapter adapter;
	private String TAG = "News";
	private Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "SettingsActivity - onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		
		
		final Button btn = (Button) findViewById(R.id.button1);

		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent service = new Intent(getApplicationContext(), NewsService.class);
				service.setAction("userUpdateClasses");
		    	service.putExtra("classesId",listIdsToArray(adapter.getSetListIds()));
				getApplicationContext().startService(service);
				c.close();
				SettingsActivity.this.setResult(Activity.RESULT_OK);
				SettingsActivity.this.finish();
			}

			private int[] listIdsToArray(Set<Integer> setListIds) {
				int[] ids = new int[setListIds.size()];
				int i = 0;
				for(int id : setListIds) {
					ids[i] = id;
					i++;
				}
				return ids;				
			}
		});    		

		_listView2 = (ListView) findViewById(R.id.ListView2);
		_listView2.addFooterView(new ProgressBar(this));		

		getLoaderManager().initLoader(1, null, this);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(TAG, "SettingsActivity - onCreateLoader");
		return new CursorLoader(this, 
								Uri.parse("content://com.example.newsclassserver/thothClasses"), 
								new String[]{"_classId","fullname","showNews"}, 
								null, 
								null, 
								"_classId DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG , " SettingsActivity - onLoadFinish");
		if(data == null || data.getCount() == 0){
			Log.d(TAG, "onCreate firstFillOfCP");
			//Preencher pela primeira vez o content provider
			Intent service = new Intent(this,NewsService.class);
			service.setAction("firstFillOfCP");
			this.startService(service);
		}else{
			
		c = data;
		ClassesAsyncTask n = new ClassesAsyncTask(data){

			@Override
			protected void onPostExecute(Clazz[] result) {
				Log.d(TAG, "SettingsActivity - classesAsyncTask -> onPostExecute...");
					adapter = new ClassesCustomAdapter(SettingsActivity.this, R.layout.item_layout, result); 
					_listView2.setAdapter(adapter);
					_listView2.setOnScrollListener(adapter);
					Log.d(TAG, "SettingsActivity - classesAsyncTask -> onPostExecute finished...");
			}
		};
		Log.d(TAG, "SettingsActivity - classesAsyncTask -> execute started");
		n.execute();
		Log.d(TAG, "SettingsActivity - classesAsyncTask -> execute finished");}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "SettingsActivity - onLoaderReset");
		//_listView2.setActivated(false);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
}


