package com.example.newsclass;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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
		_pref = getSharedPreferences("classes", 0);
		ClassesAsyncTask n = new ClassesAsyncTask(_pref){
			@Override
			protected void onPostExecute(Clazz[] result) {
				if(result == null) {
					_tv2.setText("error");
				}else {
					/*List<Map<String,String>> list = new LinkedList<Map<String,String>>();

					for (int i = 0; i < result.length; ++i) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("fullname", result[i].getFullname());
						list.add(map);
					}*/
					CustomAdapter adapter = new CustomAdapter(SettingsActivity.this, R.layout.item_layout, result); 
					_listView2.setAdapter(adapter);
					_listView2.setOnScrollListener(adapter);	    			
				}
			}
		};
		n.execute();
	}

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        
        // Check which checkbox was clicked
        if(view.getId() == R.id.item_checkBox) {
                if (checked){
                	
                }
                else{
                    // Remove the meat     
                }
        }
        // TODO: Veggie sandwich
    }
}


