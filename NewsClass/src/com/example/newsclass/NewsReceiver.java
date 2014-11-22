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

public class NewsReceiver extends BroadcastReceiver {

	AlarmManager newsAlarm;
	PendingIntent newsIntent;
	AlarmManager classesAlarm;
	PendingIntent classesIntent;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(ni.isConnected()){
				// Set the Alarms
				setNewsAlarm(context);
				setClassesAlarm(context);
				
				//Update das noticias das turmas seleccionadas
				Intent service = new Intent(context,NewsService.class);
				service.setAction(Intent.ACTION_INSERT_OR_EDIT);
				context.startService(service);
			}
			else{
				// Unset the Alarms
				newsAlarm.cancel(newsIntent);
				classesAlarm.cancel(classesIntent);
			}
		}
	}

	private void setClassesAlarm(Context context) {
		classesAlarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		Intent intent = new Intent(context,NewsService.class);
		classesIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		classesAlarm.setRepeating(
				AlarmManager.ELAPSED_REALTIME, 
				AlarmManager.INTERVAL_DAY, 
				AlarmManager.INTERVAL_DAY, 
				classesIntent
		);
		
		// Automatic restart the alarm when wifi is activated
//		ComponentName receiver = new ComponentName(context, NewsReceiver.class);
//		PackageManager pm = context.getPackageManager();
//		pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);		
	}

	private void setNewsAlarm(Context context){
		newsAlarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		Intent intent = new Intent(context,NewsService.class);
		newsIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		newsAlarm.setInexactRepeating(
				AlarmManager.ELAPSED_REALTIME, 
				AlarmManager.INTERVAL_HALF_HOUR, 
				AlarmManager.INTERVAL_HALF_HOUR, 
				newsIntent
		);
		
		// Automatic restart the alarm when wifi is activated
//		ComponentName receiver = new ComponentName(context, NewsReceiver.class);
//		PackageManager pm = context.getPackageManager();
//		pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	}
	
}
