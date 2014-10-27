package com.example.contactsbirthdays;

import java.util.Calendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class SettingsActivity extends Activity{
	
	private CalendarView _c1;
	private SharedPreferences _pref; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		initializeCalendar();
		_pref = getSharedPreferences("dateSelected", 0);
	}
	
	private void initializeCalendar() {

		_c1 = (CalendarView) findViewById(R.id.cv1);
		
		Calendar calendar = Calendar.getInstance();
		long date = calendar.getActualMinimum(Calendar.DATE);
		
		_c1.setMinDate(date);
		_c1.setShowWeekNumber(false);
		_c1.setFirstDayOfWeek(2);
		
		_c1.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				String data = "" + dayOfMonth +"/" +  month + "/" + year;
				
				Calendar c = Calendar.getInstance();
				c.set(year, month, dayOfMonth);
				if(c.compareTo(Calendar.getInstance()) >= 0){
					_pref.edit().putString("data", data).commit();
					finish();
				}else
					Toast.makeText(SettingsActivity.this, "Data inválida", Toast.LENGTH_LONG).show();							
			}
		});
	}
}
