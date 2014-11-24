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
	private List<ContactInfo> _cInfo;
	private ContactInfo [] _res;

	public ContactsAsyncTask(SharedPreferences prefs){
		_prefs = prefs;
		_cInfo = new LinkedList<ContactInfo>();
	}

	@Override
	protected ContactInfo[] doInBackground(ContentResolver... cr) {
		_cr = cr[0];

		_limit = _prefs.getString("data", null);

		Uri uri = ContactsContract.Data.CONTENT_URI;
		String [] projection = null;
		String selection = String.format("%s = 'vnd.android.cursor.item/contact_event' and %s = %s", 
				ContactsContract.Data.MIMETYPE,
				ContactsContract.CommonDataKinds.Event.TYPE,
				ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY);
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor = _cr.query(uri, projection, selection, selectionArgs, sortOrder);	

		List<ContactInfo> list = checkDate(cursor);

		listToOrderArray(list);
		cursor.close();
		return _res;
	}

	private void listToOrderArray(List<ContactInfo> contactList) {
		_res = new ContactInfo[contactList.size()];
		int numElems = 0;
		for(ContactInfo contact : contactList){

			if(numElems == 0){
				_res[numElems] = contact;
				numElems++;
			}
			else {
				String dateOfContactToInsert = contact.getBirthday();
				String[] auxDateOfContactToInsert = dateOfContactToInsert.split("/");

				Calendar dateToInsert = Calendar.getInstance();
				dateToInsert.set(Calendar.DAY_OF_MONTH, Integer.parseInt(auxDateOfContactToInsert[0]));
				dateToInsert.set(Calendar.MONTH, Integer.parseInt(getMonth(auxDateOfContactToInsert[1])));
				int i;
				for(i = 0; i < numElems; i++) {

					String dateOfContactInArray = _res[i].getBirthday();
					String[] auxDateOfContactInArray = dateOfContactInArray.split("/");

					Calendar dateInArray = Calendar.getInstance();
					dateInArray.set(Calendar.DAY_OF_MONTH, Integer.parseInt(auxDateOfContactInArray[0]));
					dateInArray.set(Calendar.MONTH, Integer.parseInt(getMonth(auxDateOfContactInArray[1])));

					if(dateToInsert.compareTo(dateInArray) <= 0) {
						for(int j = numElems; j > i; j--) {
							_res[j] = _res[j-1];
						}
						_res[i] = contact;
						numElems++;
						break;
					}
				}
				if(i == numElems){
					_res[numElems] = contact;
					numElems++;
				}
			}
		}
	}

	private String getMonth(String string) {
		String[] month = string.split(" ");

		return month[0];
	}

	private List<ContactInfo> checkDate(Cursor cursor) {		

		String[] lim = _limit.split("/");
		while(cursor.moveToNext()){
			String[] date = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)).split("/");
			Calendar actualDate = Calendar.getInstance();
			
			Calendar contactDate = Calendar.getInstance();
			contactDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[0]));
			contactDate.set(Calendar.MONTH, Integer.parseInt(date[1]));
			
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

				_cInfo.add(newContactInfo);						
			}		
		}
		return _cInfo;
	}
}
