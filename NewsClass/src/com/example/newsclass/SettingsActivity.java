package com.example.newsclass;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SettingsActivity extends Activity{

	private TextView _tv2;
	private ListView _listView2;
	private ClassesCustomAdapter adapter;
	public SharedPreferences _pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				_pref.edit()
				.putStringSet("ids", adapter.getSetListIds())
				.commit();
				SettingsActivity.this.finish();
			}
			}
		);          
		
		
		_tv2 = (TextView) findViewById(R.id.tv2);
		_listView2 = (ListView) findViewById(R.id.ListView2);
		_listView2.addFooterView(new ProgressBar(this));
		_pref = getSharedPreferences("classesIds", 0);
		
		ClassesAsyncTask n = new ClassesAsyncTask(){
			
			@Override
			protected void onPostExecute(Clazz[] result) {
				if(result == null) {
					_tv2.setText("error");
				}else {
					adapter = new ClassesCustomAdapter(SettingsActivity.this, R.layout.item_layout, result); 
					_listView2.setAdapter(adapter);
					_listView2.setOnScrollListener(adapter);			
				}
			}
		};
		n.execute();
	}
}


