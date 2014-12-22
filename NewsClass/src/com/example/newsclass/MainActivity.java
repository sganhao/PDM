package com.example.newsclass;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor>, NewsItemListFragment.Callback {

	private String TAG = "News";
	private final Uri _thothNews = Uri.parse("content://com.example.newsclassserver/thothNews");
	private NewsListModel _model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate MainActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_masterdetail);
		Log.d(TAG, "onCreate loadManagerInit");
		getLoaderManager().initLoader(1, null, this);
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
			getLoaderManager().initLoader(1, null, this);
		}
	}

	private void callAsyncTask(Cursor c){
		Log.d(TAG,"MainActivity - callAsyncTask");
		NewsAsyncTask newsAsync = new NewsAsyncTask(c) {

			@Override
			protected void onPostExecute(NewItem[] result) {
				if (result != null) {	
					Log.d(TAG,"MainActivity - onPostExecute - result :! null");
					
					_model = new NewsListModel(result);
					FragmentManager fm = getSupportFragmentManager();
					NewsItemListFragment f;
					
					if(fm.findFragmentById(R.id.mainFragmentPlaceholder) == null){
						f = NewsItemListFragment.newInstance(_model);			
						fm.beginTransaction()
							.add(R.id.mainFragmentPlaceholder, f)
							.commit();
					}else{
						f = NewsItemListFragment.newInstance(_model);
						fm.beginTransaction().replace(R.id.mainFragmentPlaceholder, f).commit();
					}
				}
			}
		};
		newsAsync.execute();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Log.d(TAG, "onCreateLoader");
		return new CursorLoader(this, 
				_thothNews, 
				new String[]{"_newsId", "classFullname", "_classId", "title", "_when", "content", "isViewed"}, 
				null,
				null, 
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loaders, Cursor data) {
		Log.d(TAG, "onLoadFinish");
		if(data == null || data.getCount() == 0){
			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
			startActivityForResult(i,0);
		}
		else
			callAsyncTask(data);	
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		Log.d(TAG, "onLoaderReset");
	}

	@Override
	public void onListItemClick(int position) {
		if (findViewById(R.id.detailFragmentPlaceholder) != null) {
			FragmentManager fm = getSupportFragmentManager();
			NewsItemFragment newFrag = NewsItemFragment.newInstance(_model.getItem(position));
			fm.beginTransaction().replace(R.id.detailFragmentPlaceholder, newFrag).commit();
			
			if(!_model.getItem(position).isViewed){
				_model.getItem(position).isViewed = true;
				Intent service = new Intent(this, NewsService.class);
				service.putExtra("newId", _model.getItem(position).newsId);
				service.setAction("userUpdateNews");
				startService(service);				
			}

		} else {
			Intent i = new Intent(this, NewsItemActivity.class);
			i.putExtra("newslistmodel", _model);
			i.putExtra("position", position);
			startActivity(i);
		}		
	}
}
