package com.example.contactsbirthdays;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class ContactsBReceiver extends BroadcastReceiver {
	private static final String TAG = "REC";
	private AlarmManager weekAlarm;
	private AlarmManager dayAlarm;
	private Context _context;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "received intent with action = "+intent.getAction());
		_context = context;
				
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			setDayAlarm();
			setWeekAlarm();
		}
		if(intent.getAction().equals("weekAlarm") || intent.getAction().equals("dayAlarm")){
			Intent i = new Intent(context, ContactsService.class);
			i.setAction(intent.getAction());
		    context.startService(i);
		}
	}

	private void setWeekAlarm(){
		Intent i = new Intent(_context,ContactsBReceiver.class);
		i.setAction("weekAlarm");
		PendingIntent pi = PendingIntent.getBroadcast(_context, 0, i, 0);
		weekAlarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
		weekAlarm.setRepeating(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + 1000,
				AlarmManager.INTERVAL_DAY, 
				pi);
		Log.d(TAG,"inside setWeekAlarm");
	}
	
	private void setDayAlarm(){
		Intent i = new Intent(_context,ContactsBReceiver.class);
		i.setAction("dayAlarm");
		PendingIntent pi = PendingIntent.getBroadcast(_context, 0, i, 0);
		dayAlarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
		dayAlarm.setRepeating(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + 1000,
				AlarmManager.INTERVAL_DAY, 
				pi);
		Log.d(TAG,"inside setDayAlarm");
	}


}
