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
	private Cursor c;
	private String TAG = "News";
	
	public NewsAsyncTask (Cursor c) {
		this.c = c;
	}
	
	@Override
	protected NewItem[] doInBackground(Void... args) {
		Log.d(TAG , "NewsAsyncTask - doInBackground");
		newsarray = new NewItem[c.getCount()];
		int idx = 0;
		
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
		OrderedArray();
		return newsarray;
	}
	
	private void OrderedArray(){
		NewItem[] a = newsarray;
		for(NewItem item : a){
			insertInArray(item);
		}
	}
	
	
	private void insertInArray(NewItem item){
		if(item.isViewed){
			for(int i = firstViewedItemIdx ; i < numElems ; i++){
				if(item.when.compareTo(newsarray[i].when) >= 0){
					for(int j = numElems ; j > i ; j--){
						newsarray[j] = newsarray[j-1];
					}
					newsarray[i] = item;
					numElems++;
					return;
				}				
			}
			newsarray[numElems] = item;
			numElems++;
		}else{
			for(int i = 0 ; i < firstViewedItemIdx ; i++){
				if(item.when.compareTo(newsarray[i].when) >= 0){
					for(int j = numElems ; j > i ; j--){
						newsarray[j] = newsarray[j-1];
					}
					newsarray[i] = item;
					firstViewedItemIdx++;
					numElems++;
					return;
				}
			}
			for(int j = numElems ; j > firstViewedItemIdx ; j--){
				newsarray[j] = newsarray[j-1];
			}
			newsarray[firstViewedItemIdx] = item;
			firstViewedItemIdx++;
			numElems++;
		}
	}

}
