package com.example.newsclass;

import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;


public class MainActivity extends Activity {

	private final String CLASSES = "ids";
	private Set<Integer> viewedNewsIds;
	private ExpandableListView _exList;
	private NewsCustomAdapter newsAdapter;
	private ContentResolver _cr;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_cr = getContentResolver();

		_exList = (ExpandableListView) findViewById(R.id.expandableListView1);

		viewedNewsIds = new LinkedHashSet<Integer>(newsAdapter.getSetListViewedNewsIds());        
/*
		if(classesIds.size() == 0) {
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
		}else*/
			callAsyncTask();
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
}
