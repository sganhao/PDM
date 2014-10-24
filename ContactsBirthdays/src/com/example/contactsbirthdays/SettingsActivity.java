package com.example.contactsbirthdays;

import java.util.Calendar;
import java.util.Date;

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
		
		//indica se mostra o número da semana ou não
		_c1.setShowWeekNumber(false);
		
		// inicia a semana à 2ªf
		_c1.setFirstDayOfWeek(2);
		
		// coloca a semana selecionada coom destaque
		//_c1.setSelectedWeekBackgroundColor(color.holo_blue_light);
		
		// cor para o mês q não está selecionado
		//_c1.setUnfocusedMonthDateColor(color.transparent);
		
		// cor para as linhas separadoras das semanas
		//_c1.setWeekSeparatorLineColor(color.black);
		
		// destacar o selecionado
		//_c1.setSelectedDateVerticalBar(color.darker_gray);
		
		_c1.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				String data = "" + dayOfMonth +"/" +  (month+1) + "/" + year;
				
				Calendar c = Calendar.getInstance();
				c.set(year, month, dayOfMonth);
				if(c.compareTo(Calendar.getInstance()) >= 0){
					_pref.edit().putString("data", data).commit();
					finish();
				}
				Toast.makeText(SettingsActivity.this, "Data inválida", Toast.LENGTH_LONG).show();
				
								
			}
		});
	}
}
