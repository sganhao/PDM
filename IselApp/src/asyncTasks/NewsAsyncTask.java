package asyncTasks;

import java.text.ParseException;
import java.util.Date;

import entities.NewsItem;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class NewsAsyncTask extends AsyncTask<Void, Void, NewsItem[]>{

	private String TAG = "IselApp";
	private int _numItems;
	private int _firstViewedItemIdx;
	private NewsItem[] _newsItem;
	private Cursor _cursor;
	

	public NewsAsyncTask (Cursor c) {
		_cursor = c;
		_numItems = 0;
		_firstViewedItemIdx = 0;
	}
	
	@Override
	protected NewsItem[] doInBackground(Void... params) {
		Log.d(TAG , "NewsAsyncTask - doInBackground");
		_newsItem = new NewsItem[_cursor.getCount()];
		int idx = 0;

		Log.d(TAG, "News AsyncTask - doInBackground - starting to go through cursor...");
		_cursor.moveToFirst();
		do{
			try {
				_newsItem[idx] = new NewsItem(
						_cursor.getString(_cursor.getColumnIndex("_newsClassFullname")),
						_cursor.getInt(_cursor.getColumnIndex("_newsId")), 
						_cursor.getInt(_cursor.getColumnIndex("_newsClassId")), 
						_cursor.getString(_cursor.getColumnIndex("_newsTitle")),
						_cursor.getString(_cursor.getColumnIndex("_newsWhen")),
						_cursor.getString(_cursor.getColumnIndex("_newsContent")),
						_cursor.getInt(_cursor.getColumnIndex("_newsIsViewed")) == 1 ? true : false
						);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			idx++;
		}while (_cursor.moveToNext());

		OrderedArray();
		return _newsItem;
	}
	
	private void OrderedArray(){
		NewsItem[] a = _newsItem;
		for(NewsItem item : a){
			insertInArray(item);
		}
	}
	
	private void insertInArray(NewsItem item){
		if(item.news_isViewed){
			for(int i = _firstViewedItemIdx ; i < _numItems ; i++){
				if(item.news_when.compareTo(_newsItem[i].news_when) >= 0){
					for(int j = _numItems ; j > i ; j--){
						_newsItem[j] = _newsItem[j-1];
					}
					_newsItem[i] = item;
					_numItems++;
					return;
				}				
			}
			_newsItem[_numItems] = item;
			_numItems++;
		}else{
			for(int i = 0 ; i < _firstViewedItemIdx ; i++){
				if(item.news_when.compareTo(_newsItem[i].news_when) >= 0){
					for(int j = _numItems ; j > i ; j--){
						_newsItem[j] = _newsItem[j-1];
					}
					_newsItem[i] = item;
					_firstViewedItemIdx++;
					_numItems++;
					return;
				}
			}
			for(int j = _numItems ; j > _firstViewedItemIdx ; j--){
				_newsItem[j] = _newsItem[j-1];
			}
			_newsItem[_firstViewedItemIdx] = item;
			_firstViewedItemIdx++;
			_numItems++;
		}
	}
}
