package com.example.contactsbirthdays;


import java.util.Calendar;
import java.util.LinkedList;
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
		cInfo = new LinkedList<ContactInfo>();
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
		
		List<ContactInfo> list = checkDate(cursor);
		ContactInfo [] res = new ContactInfo[list.size()];
		
		list.toArray(res);
		return res;
	}

	private List<ContactInfo> checkDate(Cursor cursor) {		
		
		if (cursor.moveToFirst()){
			String[] lim = _limit.split("/");
			do {
				String[] date = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)).split("/");
				
				Calendar contactDate = Calendar.getInstance();
				contactDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
				contactDate.set(Calendar.MONTH, Integer.parseInt(date[1]));
				
				Calendar actualDate = Calendar.getInstance();
				contactDate.set(Calendar.YEAR, actualDate.get(Calendar.YEAR));
				
				Calendar limitDate = Calendar.getInstance();
				limitDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(lim[0]));
				limitDate.set(Calendar.MONTH, Integer.parseInt(lim[1]));
				limitDate.set(Calendar.YEAR, Integer.parseInt(lim[2]));

				if(contactDate.compareTo(actualDate) >= 0 && contactDate.compareTo(limitDate) <= 0){
			
					int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID));	
					String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.DISPLAY_NAME));
					String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.PHOTO_THUMBNAIL_URI));
					int years = actualDate.get(Calendar.YEAR) - Integer.parseInt(date[2]);
					
					String birthday = "" + contactDate.get(Calendar.DAY_OF_MONTH) + "/" + (contactDate.get(Calendar.MONTH)+1) + " (" + years + " anos)";
					
					ContactInfo newContactInfo = new ContactInfo(id, name, image==null?"":image, birthday);
					
					cInfo.add(newContactInfo);						
				}				
			}
			while(cursor.moveToNext());
		}
			return cInfo;
	}

	/*
	//checkMonth(contactDate, limitDate, actualDate) && checkDay(contactDate, limitDate, actualDate
	private boolean checkDay(Calendar contactDate, Calendar limitDate, Calendar actualDate) {
		if(contactDate.get(Calendar.MONTH) > actualDate.get(Calendar.MONTH) && contactDate.get(Calendar.MONTH) < limitDate.get(Calendar.MONTH))
			return true;
		if (actualDate.get(Calendar.DAY_OF_MONTH) <= contactDate.get(Calendar.DAY_OF_MONTH) && 
				 contactDate.get(Calendar.DAY_OF_MONTH) <= limitDate.get(Calendar.DAY_OF_MONTH))		
			return true;
		return false;
	}

	private boolean checkMonth(Calendar contactDate, Calendar limitDate, Calendar actualDate) {
		if (actualDate.get(Calendar.MONTH)+1 <= contactDate.get(Calendar.MONTH) + 1 && 
				 contactDate.get(Calendar.MONTH) + 1 <= limitDate.get(Calendar.MONTH))		
			return true;
		return false;
		
	}*/
}
