package com.example.newsclass;

import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;


public class MainActivity extends Activity implements LoaderCallbacks<Cursor>, ServiceConnection {

	private final String CLASSES = "ids";
	private Set<Integer> viewedNewsIds;
	private ExpandableListView _exList;
	private NewsCustomAdapter newsAdapter;
	private ContentResolver _cr;
	private Uri _thothClasses;
	private Uri _thothNews;
	private boolean mBound = false;

	private String TAG = "News";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d(TAG, "onCreate MainActivity");
		_cr = getContentResolver();

		_thothClasses = Uri.parse("content://com.example.newsclassserver/thothClasses");
		Cursor c = _cr.query(_thothClasses, new String[]{"_classId"}, null, null, null);
		if(c == null || c.getCount() == 0) {

			Log.d(TAG, "onCreate firstFillOfCP");
			//Preencher pela primeira vez o content provider
			Intent service = new Intent(this, NewsBoundedService.class);
			service.setAction("firstFillOfCP");

			Log.d(TAG, "MainActivity -> entering bindService...");
			bindService(service, this, Context.BIND_AUTO_CREATE);
			mBound = true;
			Log.d(TAG, "MainActivity -> bindService complete...");
		}

		_exList = (ExpandableListView) findViewById(R.id.expandableListView1);

		//viewedNewsIds = new LinkedHashSet<Integer>(newsAdapter.getSetListViewedNewsIds());        
		/*
		if(classesIds.size() == 0) {
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
		}else*/

		Log.d(TAG, "onCreate loadManagerInit");
		getLoaderManager().initLoader(1, null, this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mBound) {
			unbindService(this);
			mBound = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data){
		if(reqCode == 0 && resCode == RESULT_OK){
			viewedNewsIds =  new LinkedHashSet<Integer>(newsAdapter.getSetListViewedNewsIds());
			callAsyncTask();
		}
	}

	private void callAsyncTask(){
		NewsAsyncTask newsAsync = new NewsAsyncTask(_cr) {

			@Override
			protected void onPostExecute(NewItem[] result) {
				if (result != null) {
					newsAdapter = new NewsCustomAdapter(MainActivity.this, result, viewedNewsIds);
					_exList.setAdapter(newsAdapter);
					_exList.setOnGroupClickListener(newsAdapter);
				}
			}
		};
		newsAsync.execute();
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}


	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		Log.d(TAG, "onCreateLoader");
		return new CursorLoader(this, 
				Uri.parse("content://com.example.newsclassserver/thothNews"), 
				new String[]{"_newsId","_classId", "title", "_when", "content", "isViewed"}, 
				null,// noticias das classes q est�o selecionadas 
				null, 
				null);
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loaders, Cursor data) {

		Log.d(TAG, "onLoadFinish");
		if(data == null){
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, 0);
		}
		else
			callAsyncTask();		
	}


	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO check this thing here
		Log.d(TAG, "onLoaderReset");
		_exList.clearChoices();		
	}


	@Override
	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		Log.d(TAG, "MainActivity -> onServiceConnected...");
		getLoaderManager().initLoader(1, null, this);		
	}


	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		Log.d(TAG, "MainActivity -> onServiceDisconnected...");
		mBound = false;
	}
}
