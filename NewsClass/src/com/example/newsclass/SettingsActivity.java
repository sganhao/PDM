package com.example.newsclass;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SettingsActivity extends Activity{

	private TextView _tv2;
	private ListView _listView2;
	public SharedPreferences _pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		
		_tv2 = (TextView) findViewById(R.id.tv2);
		_listView2 = (ListView) findViewById(R.id.ListView2);
		_listView2.addFooterView(new ProgressBar(this));
		_pref = getSharedPreferences("classesIds1", 0);
		
		ClassesAsyncTask n = new ClassesAsyncTask(){
			
			@Override
			protected void onPostExecute(Clazz[] result) {
				if(result == null) {
					_tv2.setText("error");
				}else {
					CustomAdapter adapter = new CustomAdapter(SettingsActivity.this, R.layout.item_layout, result); 
					_listView2.setAdapter(adapter);
					_listView2.setOnScrollListener(adapter);
					
					_pref.edit()
						.putStringSet("ids", adapter.getSetListIds())
						.commit();				
				}
			}
		};
		n.execute();
	}
}


