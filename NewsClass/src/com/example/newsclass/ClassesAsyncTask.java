package com.example.newsclass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;

public class ClassesAsyncTask extends AsyncTask<SharedPreferences,Void,Clazz[]>{

	List<Clazz> clazz;	
	SharedPreferences spref;

	public ClassesAsyncTask(SharedPreferences _pref) {
		spref = _pref;
	}

	@Override
	protected Clazz[] doInBackground(SharedPreferences... pref0) {
		try {
			URL url = new URL("http://thoth.cc.e.ipl.pt/api/v1/classes/");
			HttpURLConnection urlcon = (HttpURLConnection)url.openConnection();
			try{
				InputStream is = urlcon.getInputStream();
				String data = readAllFrom(is);
				return parseFrom(data);
			}catch (JSONException e) {
				return null;
			}finally {
				urlcon.disconnect();
			}	            
		}catch(IOException e) {
			return null;
		}
	}


	private Clazz[] parseFrom(String data) throws JSONException {
		JSONObject root = new JSONObject(data);
		JSONArray jclasses = root.getJSONArray("classes");
		Clazz[] classes = new Clazz[jclasses.length()];


		for (int i = 0; i < jclasses.length(); ++i) {
			JSONObject jclass = jclasses.getJSONObject(i);
			Clazz item = new Clazz();
			item.fullname = jclass.getString("fullName");
			item.id = jclass.getInt("id");
			classes[i] = item;
			spref.edit()
				.putBoolean(Integer.toString(item.id), false);
		}
		spref.edit().commit();
		return classes;
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
