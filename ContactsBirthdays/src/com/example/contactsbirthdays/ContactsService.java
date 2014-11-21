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
	
	public ContactsService() {
	    super("ContactsService");
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "ContactsService OnCreate");
		_cr = getContentResolver();
		_context = this.getApplicationContext();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "received intent with action = "+intent.getAction());
		String action = intent.getAction();
		Uri uri = ContactsContract.Data.CONTENT_URI;
		String [] projection = null;
		String selection = String.format("%s = 'vnd.android.cursor.item/contact_event' and %s = %s", 
				ContactsContract.Data.MIMETYPE,
				ContactsContract.CommonDataKinds.Event.TYPE,
				ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY);
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor = _cr.query(uri, projection, selection, selectionArgs, sortOrder);

		if(action.equals("weekAlarm")){
			Log.d(TAG, "weekAlarm");
			checkDate(cursor,7);
		}else if(action.equals("dayAlarm")){
			Log.d(TAG, "dayAlarm");
			checkDate(cursor,0);
		}
	}

	private void checkDate(Cursor cursor, int offset){
		int idx = 0;
		while(cursor.moveToNext()){
			String[] date = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)).split("/");

			int contactDay = Integer.parseInt(date[0]);
			int contactMonth = Integer.parseInt(date[1]);

			Calendar contactDate = Calendar.getInstance();
			contactDate.set(Calendar.DAY_OF_MONTH, contactDay);
			contactDate.set(Calendar.MONTH, contactMonth);

			Calendar limitDate = Calendar.getInstance();
			
			Calendar actualDate = Calendar.getInstance();
			contactDate.set(Calendar.YEAR, actualDate.get(Calendar.YEAR));

			limitDate.add(Calendar.DAY_OF_MONTH, offset);
			
			if(contactDate.compareTo(actualDate) >= 0 && contactDate.compareTo(limitDate) <= 0){

				int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID));	
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.DISPLAY_NAME));
				String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.PHOTO_THUMBNAIL_URI));
				int years = actualDate.get(Calendar.YEAR) - Integer.parseInt(date[2]);

				String birthday = "" + contactDate.get(Calendar.DAY_OF_MONTH) + "/" + (contactDate.get(Calendar.MONTH)+1) + " (" + years + " anos)";

				ContactInfo contactInfo = new ContactInfo(id, name, image==null?"":image, birthday);


				RemoteViews rl = new RemoteViews("com.example.contactsbirthdays",R.layout.notification_layout);
				rl.setImageViewUri(R.id.imgNotView, contactInfo.getImage());
				rl.setTextViewText(R.id.textNotView, contactInfo.getName() + " birthday it's on " + contactDay + "/"  + contactMonth);
				rl.getLayoutId();
				Notification.Builder builder = new Notification.Builder(_context)
				.setContentTitle("Birthday")
				.setContentText("text: " + contactInfo.getName() + " birthday it's on " + contactDay + "/"  + contactMonth)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher)
				.setOngoing(true)
				.setContent(rl);

				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(People.CONTENT_URI + "/" + contactInfo.getId()));
				PendingIntent pintent = PendingIntent.getActivity(_context, 1, i, 0);

				builder.setContentIntent(pintent);
				NotificationManager manager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
				manager.notify(idx, builder.build());
				idx++;
			}	
		}
	}
}
