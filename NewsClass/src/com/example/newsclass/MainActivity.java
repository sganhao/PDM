package com.example.newsclass;

import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;


public class MainActivity extends Activity {


	private final String CLASSES = "ids";
	private final String NEWS = "viewedNewsIds";
	private SharedPreferences _pref;
	private ExpandableListView _exList;
	private NewsCustomAdapter newsAdapter;

	Set<String> classesIds;
	Set<String> newsIds;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_pref = getSharedPreferences("workprefs",0);
		_exList = (ExpandableListView) findViewById(R.id.expandableListView1);

		classesIds = new LinkedHashSet<String>(_pref.getStringSet(CLASSES, null));
		newsIds = new LinkedHashSet<String>(_pref.getStringSet(NEWS, new LinkedHashSet<String>()));        

		if(classesIds == null) {
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
		}
		
		NewsAsyncTask newsAsync = new NewsAsyncTask() {

			@Override
			protected void onPostExecute(NewItem[] result) {
				if (result != null) {
					newsAdapter = new NewsCustomAdapter(MainActivity.this, result, _pref);
					_exList.setAdapter(newsAdapter);
					_exList.setOnGroupClickListener(newsAdapter);
				}
			}
		};
		newsAsync.execute(classesIds, newsIds);
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
			classesIds = new LinkedHashSet<String>(_pref.getStringSet(CLASSES, null));
			newsIds = new LinkedHashSet<String>(_pref.getStringSet(NEWS, new LinkedHashSet<String>()));
			NewsAsyncTask newsAsync = new NewsAsyncTask() {

				@Override
				protected void onPostExecute(NewItem[] result) {
					if (result != null) {
						newsAdapter = new NewsCustomAdapter(MainActivity.this, result, _pref);
						_exList.setAdapter(newsAdapter);
						_exList.setOnGroupClickListener(newsAdapter);
					}
				}
			};
			newsAsync.execute(classesIds, newsIds);
		}
	}
}
