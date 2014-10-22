package com.example.newsclass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class NewsAsyncTask extends AsyncTask<Set<String>, Void, NewItem[]>{

	private String _link = "http://thoth.cc.e.ipl.pt/api/v1/classes/{newsId}/newsitems";
	private NewItem[] newsarray;
	private int numElems = 0;
	private int firstViewedItemIdx = 0;
	private Set<String> viewedNewsIds;
	private List<NewItem> newsList;

	// Pedido às noticias de cada turma
	@Override
	protected NewItem[] doInBackground(Set<String>... params) {
		newsList = new LinkedList<NewItem>();
		viewedNewsIds = params[1];
		for (String set : params[0]) {
			String auxUrl = _link.replace("{newsId}", set);
			try {
				URL url = new URL(auxUrl);
				HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();

				try {
					InputStream is = urlCon.getInputStream();
					String data = readAllFrom(is);
					parseFrom(data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

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


	private void parseFrom(String data) throws JSONException {
		JSONObject root = new JSONObject(data);
		JSONArray jnews = root.getJSONArray("newsItems");
		newsarray = new NewItem[jnews.length()];



		for (int i = 0; i < jnews.length(); ++i) {
			JSONObject jnew = jnews.getJSONObject(i);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			NewItem item = null;
			try {
				item = new NewItem(
						jnew.getInt("id"), 
						jnew.getString("title"), 
						format.parse(jnew.getString("when")),
						getContent(jnew.getInt("id")),
						viewedNewsIds.contains(Integer.toString(jnew.getInt("id")))
						);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newsList.add(item);
		}
	}

	private String readAllFrom(InputStream is) {
		Scanner s = new Scanner(is);
		try{
			s.useDelimiter("\\A");
			return s.hasNext() ? s.next() : null;
		}finally{
			s.close();
		}
	}

	private String getContent(int id) {
		try {
			URL url = new URL("http://thoth.cc.e.ipl.pt/api/v1/newsitems/"+ id);
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();

			try {
				InputStream is = urlCon.getInputStream();
				String data = readAllFrom(is);

				JSONObject root = new JSONObject(data);
				return root.getString("content");				

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

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
