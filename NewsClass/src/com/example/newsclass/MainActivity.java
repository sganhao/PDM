package com.example.newsclass;

import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;


public class MainActivity extends Activity {
	
	private SharedPreferences _pref;
	private ExpandableListView _exList;
	private NewsCustomAdapter newsAdapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        _pref = getSharedPreferences("classesIds",0);
        _exList = (ExpandableListView) findViewById(R.id.expandableListView1);
        
        Set<String> ids = _pref.getStringSet("ids", null);
        
        if(ids != null) {
        	NewsAsyncTask newsAsync = new NewsAsyncTask() {
        		
        		@Override
        		protected void onPostExecute(NewItem[] result) {
        			if (result != null) {
        				newsAdapter = new NewsCustomAdapter(MainActivity.this, result);
        				_exList.setAdapter(newsAdapter);
        			}
        		}
        	};
        	newsAsync.execute(ids);
        }
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
