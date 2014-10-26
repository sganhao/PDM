package com.example.contactsbirthdays;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

public class ContactsAsyncTask extends AsyncTask<ContentResolver, Void, Cursor>{

	private ContentResolver _cr;
	private SharedPreferences _prefs;
	
	public ContactsAsyncTask(SharedPreferences prefs){
		_prefs = prefs;
	}
	
	@Override
	protected Cursor doInBackground(ContentResolver... cr) {
		_cr = cr[0];
		
		String limit = _prefs.getString("data", null);
		
		
		Uri uri = ContactsContract.Data.CONTENT_URI;
		String [] projection = null;
		String selection = String.format("%s = 'vnd.android.cursor.item/contact_event'", ContactsContract.Data.MIMETYPE);
		
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor = _cr.query(uri, projection, selection, selectionArgs, sortOrder);		
		
		return cursor;
	}
}
