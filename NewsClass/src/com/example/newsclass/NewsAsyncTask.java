package com.example.newsclass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
			try {
				newsarray[idx] = new NewItem(
									c.getInt(c.getColumnIndex("_newsId")), 
									c.getString(c.getColumnIndex("title")),
									format.parse(c.getString(c.getColumnIndex("_when"))),
									c.getString(c.getColumnIndex("content")),
									c.getInt(c.getColumnIndex("isViewed")) == 1 ? true : false
								);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			idx++;
		}
		listToOrderedArray();
		return newsarray;
	}
	
	private void listToOrderedArray(){
		newsarray = new NewItem[newsList.size()];
		for(NewItem item : newsList){
			insertInArray(item);
		}
	}


	// TODO ver getContent
	/*private String getContent(int id) {
		try {
			URL url = new URL("http://thoth.cc.e.ipl.pt/api/v1/newsitems/"+ id);
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();

			try {
				InputStream is = urlCon.getInputStream();
				String data = readAllFrom(is);

				JSONObject root = new JSONObject(data);
				return Html.fromHtml(root.getString("content")).toString();				

			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				urlCon.disconnect();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/

	private void insertInArray(NewItem item){
		if(viewedNewsIds.contains(Integer.toString(item.id))){
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
