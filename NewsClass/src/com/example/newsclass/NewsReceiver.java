package com.example.newsclass;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;

public class NewsReceiver extends BroadcastReceiver {

	private AlarmManager newsAlarm;
	private PendingIntent newsIntent;
	private AlarmManager classesAlarm;
	private PendingIntent classesIntent;
	private Context _context;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		_context = context;
		
		if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
			ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(ni.isConnected()){
				// Set the Alarms
				setNewsAlarm();
				setClassesAlarm();
				
				//Update das noticias das turmas seleccionadas
				Intent service = new Intent(_context,NewsService.class);
				service.setAction("wifi_connected");
				_context.startService(service);
			}
			else{
				// Unset the Alarms
				newsAlarm.cancel(newsIntent);
				classesAlarm.cancel(classesIntent);
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
		classesAlarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(_context,NewsReceiver.class);
		intent.setAction("classesAlarm");
		classesIntent = PendingIntent.getBroadcast(_context, 0, intent, 0);
		classesAlarm.setRepeating(
				AlarmManager.ELAPSED_REALTIME, 
				SystemClock.elapsedRealtime() + 1000, 
				AlarmManager.INTERVAL_DAY, 
				classesIntent
		);
		
		
	}

	private void setNewsAlarm(){
		newsAlarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(_context,NewsReceiver.class);
		intent.setAction("newsAlarm");
		newsIntent = PendingIntent.getBroadcast(_context, 0, intent, 0);
		newsAlarm.setInexactRepeating(
				AlarmManager.ELAPSED_REALTIME, 
				SystemClock.elapsedRealtime() + 1000, 
				AlarmManager.INTERVAL_HALF_HOUR, 
				newsIntent
		);
	}
	
}



// Automatic restart the alarm when wifi is activated
//ComponentName receiver = new ComponentName(context, NewsReceiver.class);
//PackageManager pm = context.getPackageManager();
//pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
