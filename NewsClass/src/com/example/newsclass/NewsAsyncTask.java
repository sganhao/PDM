package com.example.newsclass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.format.DateFormat;

public class NewsAsyncTask extends AsyncTask<Set<String>, Void, NewItem[]>{

	private String _link = "http://thoth.cc.e.ipl.pt/api/v1/classes/{newsId}/newsitems";
	private NewItem[] newsarray;
	private int numElems = 0;
	
	
	// Pedido às noticias de cada turma
	@Override
	protected NewItem[] doInBackground(Set<String>... params) {

		for (String set : params[0]) {
			String auxUrl = _link.replace("{newsId}", set);
			try {
				URL url = new URL(auxUrl);
				HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
				
				try {
					InputStream is = urlCon.getInputStream();
					String data = readAllFrom(is);
					parseFrom(data);
					return newsarray;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
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
										false
										);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			insertInArray(item);
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
		if(numElems == 0){
			newsarray[numElems] = item;
			numElems++;
			return;
		}
		for(int i = 0 ; i < numElems ; i++){
			if(item.when.compareTo(newsarray[i].when) <= 0){
				for(int j = numElems ; j > i ; j--){
					newsarray[j] = newsarray[j-1];
				}
				newsarray[i] = item;
				numElems++;
				break;
			}
		}
	}
}
