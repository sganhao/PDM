package com.example.newsclass;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SettingsActivity extends Activity implements LoaderCallbacks<Cursor> {

	private TextView _tv2;
	private ListView _listView2;
	private ClassesCustomAdapter adapter;
	private ContentResolver _cr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		_cr = getContentResolver();
		
		
		Button btn = (Button) findViewById(R.id.button1);

		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO lançar service para fzr update no CP
				Intent service = new Intent(getApplicationContext(), NewsService.class);
		    	service.putExtra("classesId", adapter.getSetListIds().toArray());
		    	service.putExtra("from", MainActivity.class);
				service.setAction("userUpdateClasses");
				getApplicationContext().startService(service);
				setResult(Activity.RESULT_OK);
				finish();
			}
		});    		

		_tv2 = (TextView) findViewById(R.id.tv2);
		_listView2 = (ListView) findViewById(R.id.ListView2);
		_listView2.addFooterView(new ProgressBar(this));		

		getLoaderManager().initLoader(1, null, this);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, 
								Uri.parse("content://com.example.newsclass/thothClasses"), 
								new String[]{"_classId","fullname","showNews"}, 
								null, 
								null, 
								"_classId DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data.getCount() == 0)
			_tv2.setText("Error");
		ClassesAsyncTask n = new ClassesAsyncTask(data){

			@Override
			protected void onPostExecute(Clazz[] result) {
					adapter = new ClassesCustomAdapter(SettingsActivity.this, R.layout.item_layout, result); 
					_listView2.setAdapter(adapter);
					_listView2.setOnScrollListener(adapter);
			}
		};
		n.execute();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		_listView2.setAdapter(null);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
}


