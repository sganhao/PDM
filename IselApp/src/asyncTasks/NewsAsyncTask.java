package asyncTasks;

import java.text.ParseException;
import java.util.Calendar;
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
			long timeInMillis = Long.parseLong(_cursor.getString(_cursor.getColumnIndex("_newsWhen")));
			_newsItem[idx] = new NewsItem(
					_cursor.getString(_cursor.getColumnIndex("_newsClassFullname")),
					_cursor.getInt(_cursor.getColumnIndex("_newsClassId")),
					_cursor.getInt(_cursor.getColumnIndex("_newsId")), 
					_cursor.getString(_cursor.getColumnIndex("_newsTitle")),
					timeInMillis,
					_cursor.getString(_cursor.getColumnIndex("_newsContent")),
					_cursor.getInt(_cursor.getColumnIndex("_newsIsViewed")) == 1 ? true : false
					);

			idx++;
		}while (_cursor.moveToNext());

		return _newsItem;
	}
}
