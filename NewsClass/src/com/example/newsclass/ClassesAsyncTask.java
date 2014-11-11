package com.example.newsclass;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

public class ClassesAsyncTask extends AsyncTask<Void,Void,Clazz[]>{

	private ContentResolver _cr;
	private Uri _thothClasses;
	private Clazz[] _classes;
	
	public ClassesAsyncTask(ContentResolver cr) {
		_cr = cr;
	}

	@Override
	protected Clazz[] doInBackground(Void... args) {
		_thothClasses = Uri.parse("content://com.example.newsclassserver/thothClasses");
		Cursor c = _cr.query(_thothClasses, new String[] {"_classId", "fullname", "showNews"}, null, null, null);
		_classes = new Clazz[c.getCount()];
		int idx = 0;
		
		while (c.moveToNext()) {
			_classes[idx] = new Clazz(
								c.getInt(c.getColumnIndex("id")), 
								c.getString(c.getColumnIndex("fullname")), 
								c.getInt(c.getColumnIndex("showNews")) == 1 ? true : false
							);
			idx++;
		}
		return _classes;
	}
}
