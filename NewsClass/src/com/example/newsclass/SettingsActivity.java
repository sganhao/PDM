package com.example.newsclass;

import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SettingsActivity extends Activity{

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
				// TODO passar para asyncTask para ficar a correr numa worker thread e deixar a UI realizar outro trabalho
				Set<Integer> classesSelected = adapter.getSetListIds();
				for(int id : classesSelected) {
					ContentValues values = new ContentValues();
					values.put("showNews", 1);
					_cr.update(Uri.parse("content://com.example.newsclassserver/thothClasses/#"), values , "_classId = ?", new String[] { Integer.toString(id)});
				}
				setResult(Activity.RESULT_OK);
				finish();
			}
		});    		

		_tv2 = (TextView) findViewById(R.id.tv2);
		_listView2 = (ListView) findViewById(R.id.ListView2);
		_listView2.addFooterView(new ProgressBar(this));		
		

		ClassesAsyncTask n = new ClassesAsyncTask(_cr){

			@Override
			protected void onPostExecute(Clazz[] result) {
				if(result == null) {
					_tv2.setText("error");
				}else {
					adapter = new ClassesCustomAdapter(SettingsActivity.this, R.layout.item_layout, result, getClassesSelected(result)); 
					_listView2.setAdapter(adapter);
					_listView2.setOnScrollListener(adapter);			
				}
			}

			private Set<Integer> getClassesSelected(Clazz[] result) {
				Set<Integer> classesSelected = new LinkedHashSet<Integer>();
				for(int i = 0; i < result.length; i++) {
					if (result[i].getShowNews())
						classesSelected.add(result[i].getId());
				}
				return classesSelected;
			}
		};
		n.execute();
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}
}


