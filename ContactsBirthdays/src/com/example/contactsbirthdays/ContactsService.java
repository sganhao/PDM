package com.example.contactsbirthdays;

import java.util.Calendar;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.RemoteViews;

public class ContactsService extends IntentService {

	private static final String TAG = "REC";
	private ContentResolver _cr;
	private Context _context;
	private String _action;
	private int _idx;
	
	public ContactsService() {
	    super("ContactsService");

	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "ContactsService OnCreate");
		_cr = getContentResolver();
		_context = this.getApplicationContext();
		_idx = 0;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "received intent with action = "+intent.getAction());
		_action = intent.getAction();
		Uri uri = ContactsContract.Data.CONTENT_URI;
		String [] projection = null;
		String selection = String.format("%s = 'vnd.android.cursor.item/contact_event' and %s = %s", 
				ContactsContract.Data.MIMETYPE,
				ContactsContract.CommonDataKinds.Event.TYPE,
				ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY);
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor = _cr.query(uri, projection, selection, selectionArgs, sortOrder);

		if(_action.equals("weekAlarm")){
			checkDate(cursor,7);
		}else if(_action.equals("dayAlarm")){
			checkDate(cursor,0);
		}
	}

	private void checkDate(Cursor cursor, int offset){

		Log.d(TAG, "received intent with offset = "+offset);
		while(cursor.moveToNext()){
			String[] date = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)).split("/");

			int contactDay = Integer.parseInt(date[0]);
			int contactMonth = Integer.parseInt(date[1]);

			Calendar actualDate = Calendar.getInstance();
			Calendar contactDate = Calendar.getInstance();
			Calendar limitDate = Calendar.getInstance();
			
			contactDate.set(Calendar.DAY_OF_MONTH, contactDay);
			contactDate.set(Calendar.MONTH, contactMonth);
			contactDate.set(Calendar.YEAR, actualDate.get(Calendar.YEAR));
			limitDate.add(Calendar.DAY_OF_MONTH, offset);
			
			if(offset == 7)
				actualDate.add(Calendar.DAY_OF_MONTH, 1);

			
			if(contactDate.compareTo(actualDate) >= 0 && contactDate.compareTo(limitDate) <= 0){

				int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID));	
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.DISPLAY_NAME));
				String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.PHOTO_THUMBNAIL_URI));
				int years = actualDate.get(Calendar.YEAR) - Integer.parseInt(date[2]);

				String birthday = "" + contactDate.get(Calendar.DAY_OF_MONTH) + "/" + (contactDate.get(Calendar.MONTH)+1) + " (" + years + " anos)";
				ContactInfo contactInfo = new ContactInfo(id, name, image==null?"":image, birthday);
				
				RemoteViews rl = new RemoteViews("com.example.contactsbirthdays",R.layout.notification_layout);
				if(image == null)
					rl.setImageViewResource(R.id.imgNotView, R.drawable.ic_launcher);
				else
					rl.setImageViewUri(R.id.imgNotView, contactInfo.getImage());
				if(_action.equals("weekAlarm"))
					rl.setTextViewText(R.id.textNotView, contactInfo.getName() + "'s birthday it's on "  + contactDay + "/"  + (contactMonth+1));
				else
					rl.setTextViewText(R.id.textNotView, contactInfo.getName() + "'s birthday it's today!!!");
				
				Notification.Builder builder = new Notification.Builder(_context)
				.setContentTitle("Birthday")
				.setAutoCancel(true)
				.setNumber(++_idx)
				.setSmallIcon(R.drawable.ic_launcher)
				.setOngoing(true)
				.setContent(rl);

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(People.CONTENT_URI + "/" + id));
				PendingIntent pintent = PendingIntent.getActivity(_context, 1, i, 0);

				builder.setContentIntent(pintent);
				NotificationManager manager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
				manager.notify(_idx, builder.build());
				_idx++;
			}	
		}
	}
}
