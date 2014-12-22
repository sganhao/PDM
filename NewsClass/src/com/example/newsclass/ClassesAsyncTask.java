package com.example.newsclass;

import android.database.Cursor;
import android.os.AsyncTask;

public class ClassesAsyncTask extends AsyncTask<Void,Void,Clazz[]>{

	private Cursor _cursor;
	
	public ClassesAsyncTask(Cursor c) {
		_cursor = c;
	}

	@Override
	protected Clazz[] doInBackground(Void... args) {
		Clazz [] classes = null;
		try{
		 classes = new Clazz[_cursor.getCount()];
		int idx = 0;
		_cursor.moveToFirst();
		do{
		
			classes[idx] = new Clazz(
					_cursor.getInt(_cursor.getColumnIndex("_classId")), 
					_cursor.getString(_cursor.getColumnIndex("fullname")), 
					_cursor.getInt(_cursor.getColumnIndex("showNews")) == 1 ? true : false
							);
			idx++;
		}while (_cursor.moveToNext());
		}catch(NullPointerException e){
			return null;
		}finally{
			_cursor.close();
		}
		return classes;
	}
}
