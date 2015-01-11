package newsActivities;

import listModels.NewsListModel;
import services.IselAppService;
import workItemsActivities.WorkItemsActivity;
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
import android.view.Menu;
import android.view.MenuItem;
import asyncTasks.NewsAsyncTask;
import classesActivities.SettingsActivity;

import com.example.iselapp.R;

import entities.NewsItem;
import fragments.NewsItemFragment;
import fragments.NewsItemListFragment;


public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor>, NewsItemListFragment.Callback {

	private String TAG = "IselApp";
	private final Uri _news = Uri.parse("content://com.example.iselappserver/news");
	private NewsListModel _model;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate MainActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity_masterdetail);
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
		}else if(id == R.id.action_workitems){
			Intent i = new Intent(this, WorkItemsActivity.class);
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
			protected void onPostExecute(NewsItem[] result) {
				if (result != null) {	
					Log.d(TAG,"MainActivity - onPostExecute - result :! null");
					
					_model = new NewsListModel(result);
					FragmentManager fm = getSupportFragmentManager();
					NewsItemListFragment f;
					
					if(fm.findFragmentById(R.id.mainactivity_mainFragmentPlaceholder) == null){
						f = NewsItemListFragment.newInstance(_model);			
						fm.beginTransaction()
							.add(R.id.mainactivity_mainFragmentPlaceholder, f)
							.commit();
					}else{
						f = NewsItemListFragment.newInstance(_model);
						fm.beginTransaction().replace(R.id.mainactivity_mainFragmentPlaceholder, f).commit();
					}
				}
			}
		};
		newsAsync.execute();
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(TAG, "onCreateLoader");
		return new CursorLoader(this, 
				_news, 
				new String[]{"_newsId", "_newsClassId", "_newsClassFullname", "_newsTitle", "_newsWhen", "_newsContent", "_newsIsViewed"}, 
				null,
				null, 
				"_newsIsViewed asc, _newsWhen desc");
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
		if (findViewById(R.id.mainactivity_detailFragmentPlaceholder) != null) {
			FragmentManager fm = getSupportFragmentManager();
			NewsItemFragment newFrag = NewsItemFragment.newInstance(_model.getItem(position));
			fm.beginTransaction().replace(R.id.mainactivity_detailFragmentPlaceholder, newFrag).commit();
			
			if(!_model.getItem(position).news_isViewed){
				_model.getItem(position).news_isViewed = true;
				Intent service = new Intent(this, IselAppService.class);
				service.putExtra("newId", _model.getItem(position).news_id);
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
