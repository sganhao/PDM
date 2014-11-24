package com.example.newsclass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class NewsAsyncTask extends AsyncTask<Void, Void, NewItem[]>{

	private NewItem[] newsarray;
	private int numElems = 0;
	private int firstViewedItemIdx = 0;
	private Set<String> viewedNewsIds;
	private List<NewItem> newsList;
	private Uri _thothNews;
	private ContentResolver _cr;
	private String TAG = "News";
	private int idx = 0;
	
	public NewsAsyncTask (ContentResolver cr) {
		_cr = cr;
	}
	
	@Override
	protected NewItem[] doInBackground(Void... args) {
		Log.d(TAG , "NewsAsyncTask - doInBackground");
		_thothNews = Uri.parse("content://com.example.newsclassserver/thothNews");
		Cursor c = _cr.query(_thothNews, new String[] {"_newsId", "title", "_when", "content", "isViewed"}, null, null, null);
		newsarray = new NewItem[c.getCount()];
		int idx = 0;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Log.d(TAG, "starting to go through cursor...");
		
		while (c.moveToNext()) {

				newsarray[idx] = new NewItem(
									c.getInt(c.getColumnIndex("_newsId")), 
									c.getString(c.getColumnIndex("title")),
									new Date(c.getString(c.getColumnIndex("_when"))),
									c.getString(c.getColumnIndex("content")),
									c.getInt(c.getColumnIndex("isViewed")) == 1 ? true : false
								);
			idx++;
		
		}
		bubbleSortViewed();
		orderArray(0,newsarray.length);
		orderArray(idx,newsarray.length);
		for(int i = 0 ; i < newsarray.length ; i++){
			if(newsarray[i].isViewed){
				idx = i;
				i = newsarray.length;
			}
		}
		invertArray(idx);
		return newsarray;
	}
	
	private void invertArray(int str) {
		NewItem [] a = newsarray;
		int i = 0;
		for(int j = str-1 ; j > 0 ; j--){
			newsarray[i] = a[j];
			i++;
		}
		
		for(int j = newsarray.length-1 ; j > str ; j--){
			newsarray[i] = a[j];
			i++;
		}
	}
	
	public void orderArray(int in, int f) {
		for (int i = in; i < f - 1; i++) {
		    for (int j = i; j < f - 1; j++) {
		    	if(newsarray[i+1].isViewed && !newsarray[i].isViewed){
		    		i = f-1;
		    		j = f-1;
		    		idx=i;
		    	}else
		        if (newsarray[i].when.compareTo(newsarray[i+1].when) > 0) {
		            NewItem temp = newsarray[j];
		            newsarray[j] = newsarray[j + 1];
		            newsarray[j + 1] = temp;
		        }
		    }
		}
	}

	public void bubbleSortViewed() {
		for (int i = 0; i < newsarray.length - 1; i++) {
		    for (int j = 0; j < newsarray.length - 1; j++) {
		    	int a = newsarray[j + 1].isViewed ? 1 : 0;
		    	int b = newsarray[j].isViewed ? 1 : 0;
		        if (a < b) {
		            NewItem temp = newsarray[j];
		            newsarray[j] = newsarray[j + 1];
		            newsarray[j + 1] = temp;
		        }
		    }
		}
	}
	
	

}
