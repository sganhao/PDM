package com.example.contactsbirthdays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

public class ContactsAsyncTask extends AsyncTask<ContentResolver, Void, ContactInfo[]>{

	private ContentResolver _cr;
	private SharedPreferences _prefs;
	private String _limit;
	private List<ContactInfo> cInfo;
	
	public ContactsAsyncTask(SharedPreferences prefs){
		_prefs = prefs;
	}
	
	@Override
	protected ContactInfo[] doInBackground(ContentResolver... cr) {
		_cr = cr[0];
		
		_limit = _prefs.getString("data", null);
		
		
		Uri uri = ContactsContract.Data.CONTENT_URI;
		String [] projection = null;
		String selection = String.format("%s = 'vnd.android.cursor.item/contact_event'", ContactsContract.Data.MIMETYPE);
		
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor = _cr.query(uri, projection, selection, selectionArgs, sortOrder);		
				
		return checkDate(cursor);
	}

	private ContactInfo[] checkDate(Cursor cursor) {
		
		final Calendar actualDate = Calendar.getInstance();
		
		if (cursor.moveToFirst()){
			do {
				String date = cursor.getColumnName(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
				
				try {

					SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
					 
					Calendar contactDate = Calendar.getInstance();
					contactDate.setTime(formatoData.parse(date));
					
					Calendar limitDate = Calendar.getInstance();
					limitDate.setTime(formatoData.parse(_limit));
					
					if(checkMonth(contactDate, limitDate, actualDate) && checkDay(contactDate, limitDate, actualDate) ){
						
						int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID));	
						String name = cursor.getColumnName(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.DISPLAY_NAME));
						String image = cursor.getColumnName(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.PHOTO_THUMBNAIL_URI));
						int years = actualDate.get(Calendar.YEAR) - contactDate.get(Calendar.YEAR);
						
						String birthday = "" + contactDate.get(Calendar.DAY_OF_MONTH) + "/" + contactDate.get(Calendar.MONTH) + " (" + years + " anos)";
						
						ContactInfo newContactInfo = new ContactInfo(id, name, image, birthday);
						
						cInfo.add(newContactInfo);						
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}				
			}
			while(cursor.moveToNext());
		}	
		return (ContactInfo[]) cInfo.toArray();
	}

	private boolean checkDay(Calendar contactDate, Calendar limitDate, Calendar actualDate) {
		if (actualDate.get(Calendar.DAY_OF_MONTH) <= contactDate.get(Calendar.DAY_OF_MONTH) && 
				 contactDate.get(Calendar.DAY_OF_MONTH) <= limitDate.get(Calendar.DAY_OF_MONTH))		
			return true;
		return false;
	}

	private boolean checkMonth(Calendar contactDate, Calendar limitDate, Calendar actualDate) {
		if (actualDate.get(Calendar.MONTH)+1 <= contactDate.get(Calendar.MONTH) + 1 && 
				 contactDate.get(Calendar.MONTH) + 1 <= limitDate.get(Calendar.MONTH) + 1)		
			return true;
		return false;
		
	}
}
