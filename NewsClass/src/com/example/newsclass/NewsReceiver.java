package com.example.newsclass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;

public class NewsReceiver extends BroadcastReceiver {

	private String TAG = "News";
	private AlarmManager _newsAlarm;
	private PendingIntent _newsIntent;
	private AlarmManager _classesAlarm;
	private PendingIntent _classesIntent;
	private Context _context;


	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		_context = context;
		Log.d(TAG,action);
		if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
			ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(ni.isConnected()){
				setNewsAlarm();
				setClassesAlarm();

				//Update das noticias das turmas seleccionadas
				Intent service = new Intent(_context,NewsService.class);
				service.setAction("wifi_connected");
				_context.startService(service);
			}
			else if(_newsAlarm != null && _classesAlarm != null){
				// Unset the Alarms
				_newsAlarm.cancel(_newsIntent);
				_classesAlarm.cancel(_classesIntent);
			}
		}else if(action.equals("classesAlarm")){
			Intent i = new Intent(_context,NewsService.class);
			i.setAction(action);
			_context.startService(i);
		}else if(action.equals("newsAlarm")){
			Intent i = new Intent(_context,NewsService.class);
			i.setAction(action);
			_context.startService(i);
		}		
	}

	private void setClassesAlarm() {
		_classesAlarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(_context,NewsReceiver.class);
		intent.setAction("classesAlarm");
		_classesIntent = PendingIntent.getBroadcast(_context, 0, intent, 0);
		_classesAlarm.setRepeating(
				AlarmManager.ELAPSED_REALTIME, 
				SystemClock.elapsedRealtime() + 1000, 
				AlarmManager.INTERVAL_DAY, 
				_classesIntent
				);		
	}

	private void setNewsAlarm(){
		_newsAlarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(_context,NewsReceiver.class);
		intent.setAction("newsAlarm");
		_newsIntent = PendingIntent.getBroadcast(_context, 0, intent, 0);
		_newsAlarm.setInexactRepeating(
				AlarmManager.ELAPSED_REALTIME, 
				SystemClock.elapsedRealtime() + 1000, 
				AlarmManager.INTERVAL_HALF_HOUR, 
				_newsIntent
				);
	}	
}