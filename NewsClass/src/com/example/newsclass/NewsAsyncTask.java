package com.example.newsclass;

import java.util.Date;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class NewsAsyncTask extends AsyncTask<Void, Void, NewItem[]>{
	
	private String TAG = "News";
	private int _numItems;
	private int _firstViewedItemIdx;
	private NewItem[] _newsarray;
	private Cursor _cursor;
	

	public NewsAsyncTask (Cursor c) {
		_cursor = c;
		_numItems = 0;
		_firstViewedItemIdx = 0;
	}

	@Override
	protected NewItem[] doInBackground(Void... args) {
		Log.d(TAG , "NewsAsyncTask - doInBackground");
		_newsarray = new NewItem[_cursor.getCount()];
		int idx = 0;

		Log.d(TAG, "News AsyncTask - doInBackground - starting to go through cursor...");
		_cursor.moveToFirst();
		do{
			_newsarray[idx] = new NewItem(
					_cursor.getString(_cursor.getColumnIndex("classFullname")),
					_cursor.getInt(_cursor.getColumnIndex("_newsId")), 
					_cursor.getInt(_cursor.getColumnIndex("_classId")), 
					_cursor.getString(_cursor.getColumnIndex("title")),
					new Date(_cursor.getString(_cursor.getColumnIndex("_when"))),
					_cursor.getString(_cursor.getColumnIndex("content")),
					_cursor.getInt(_cursor.getColumnIndex("isViewed")) == 1 ? true : false
					);
			idx++;
		}while (_cursor.moveToNext());

		OrderedArray();
		return _newsarray;
	}

	private void OrderedArray(){
		NewItem[] a = _newsarray;
		for(NewItem item : a){
			insertInArray(item);
		}
	}	

	private void insertInArray(NewItem item){
		if(item.isViewed){
			for(int i = _firstViewedItemIdx ; i < _numItems ; i++){
				if(item.when.compareTo(_newsarray[i].when) >= 0){
					for(int j = _numItems ; j > i ; j--){
						_newsarray[j] = _newsarray[j-1];
					}
					_newsarray[i] = item;
					_numItems++;
					return;
				}				
			}
			_newsarray[_numItems] = item;
			_numItems++;
		}else{
			for(int i = 0 ; i < _firstViewedItemIdx ; i++){
				if(item.when.compareTo(_newsarray[i].when) >= 0){
					for(int j = _numItems ; j > i ; j--){
						_newsarray[j] = _newsarray[j-1];
					}
					_newsarray[i] = item;
					_firstViewedItemIdx++;
					_numItems++;
					return;
				}
			}
			for(int j = _numItems ; j > _firstViewedItemIdx ; j--){
				_newsarray[j] = _newsarray[j-1];
			}
			_newsarray[_firstViewedItemIdx] = item;
			_firstViewedItemIdx++;
			_numItems++;
		}
	}
}
