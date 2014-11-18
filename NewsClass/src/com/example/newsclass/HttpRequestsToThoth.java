package com.example.newsclass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.Html;

public class HttpRequestsToThoth {

	private NewItem[] newsarray;
	
	public Clazz[] requestClasses(){
		HttpURLConnection urlcon = null;
		try{
			URL url = new URL("http://thoth.cc.e.ipl.pt/api/v1/classes/");
			urlcon = (HttpURLConnection)url.openConnection();
			InputStream is = urlcon.getInputStream();
			String data = readAllFrom(is);
			return classesParseFrom(data);
		}catch (JSONException e) {
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			urlcon.disconnect();
		}
		return null;	            
	}

	private Clazz[] classesParseFrom(String data) throws JSONException {
		JSONObject root = new JSONObject(data);
		JSONArray jclasses = root.getJSONArray("classes");
		Clazz[] classes = new Clazz[jclasses.length()];

		for (int i = 0; i < jclasses.length(); ++i) {
			JSONObject jclass = jclasses.getJSONObject(i);
			Clazz item = new Clazz(
					jclass.getInt("id"), 
					jclass.getString("fullName"), 
					false
					);
			classes[i] = item;
		}
		return classes;
	}

	

	public NewItem[] requestNews(int id){
		HttpURLConnection urlCon = null;
		String uri = "http://thoth.cc.e.ipl.pt/api/v1/classes/{newsId}/newsitems";

		try {
			uri.replace("{newsId}", ""+id);
			URL url = new URL(uri);
			urlCon = (HttpURLConnection)url.openConnection();
			InputStream is = urlCon.getInputStream();
			String data = readAllFrom(is);
			newsarray = newsParseFrom(data);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			urlCon.disconnect();
		}

		return newsarray;
	}

	private NewItem[] newsParseFrom(String data) throws JSONException {
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
				e.printStackTrace();
			}
			newsarray[i] = item;
		}
		return newsarray;
	}
		
	private String getContent(int id) {
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
}
