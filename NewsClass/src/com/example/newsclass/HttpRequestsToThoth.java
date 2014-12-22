package com.example.newsclass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;

public class HttpRequestsToThoth {

	private static final String TAG = "News";
	private int _posParticipant;
	private NewItem[] _newsarray;
	private Participant[] _participants;

	public HttpRequestsToThoth(){
		_posParticipant = 0;
	}

	public Clazz[] requestClasses(){
		HttpURLConnection urlcon = null;
		try{
			URL url = new URL("http://thoth.cc.e.ipl.pt/api/v1/classes/");
			urlcon = (HttpURLConnection)url.openConnection();
			InputStream is = urlcon.getInputStream();
			String data = readAllFrom(is);
			return classesParseFrom(data);
		}catch (JSONException e) {
			Log.d(TAG, e.toString());
		} catch (MalformedURLException e) {
			Log.d(TAG, e.toString());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
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

	public NewItem[] requestNews(int classId, String classFullname){
		Log.d(TAG, "requestNews");
		HttpURLConnection urlCon = null;
		String uri = "http://thoth.cc.e.ipl.pt/api/v1/classes/{classId}/newsitems";

		try {
			uri = uri.replace("{classId}", ""+classId);
			URL url = new URL(uri);
			urlCon = (HttpURLConnection)url.openConnection();
			InputStream is = urlCon.getInputStream();
			String data = readAllFrom(is);
			_newsarray = newsParseFrom(data, classId, classFullname);
		} catch (JSONException e) {
			Log.d(TAG, e.toString());
		} catch (MalformedURLException e) {
			Log.d(TAG, e.toString());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}finally{
			urlCon.disconnect();
		}

		return _newsarray;
	}

	private NewItem[] newsParseFrom(String data, int classId, String classFullname) throws JSONException {
		JSONObject root = new JSONObject(data);
		JSONArray jnews = root.getJSONArray("newsItems");
		_newsarray = new NewItem[jnews.length()];

		for (int i = 0; i < jnews.length(); ++i) {
			JSONObject jnew = jnews.getJSONObject(i);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			NewItem item = null;
			try {
				item = new NewItem(
						classFullname,
						jnew.getInt("id"), 
						classId,
						jnew.getString("title"), 
						format.parse(jnew.getString("when")),
						getContent(jnew.getInt("id")),
						false
						);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			_newsarray[i] = item;
		}
		return _newsarray;
	}

	private String getContent(int id) {
		HttpURLConnection urlcon = null;
		try {
			URL url = new URL("http://thoth.cc.e.ipl.pt/api/v1/newsitems/"+ id);
			urlcon = (HttpURLConnection)url.openConnection();
			InputStream is = urlcon.getInputStream();
			String data = readAllFrom(is);
			JSONObject root = new JSONObject(data);
			return Html.fromHtml(root.getString("content")).toString();
		} catch (MalformedURLException e) {
			Log.d(TAG, e.toString());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		} catch (JSONException e) {
			Log.d(TAG, e.toString());
		}finally{
			urlcon.disconnect();
		}
		return null;
	}	

	public Participant[] requestParticipants(int classId) {
		Log.d(TAG, "request Participant");
		HttpURLConnection urlCon = null;
		String uri = "http://thoth.cc.e.ipl.pt/api/v1/classes/{classId}/participants";
		try {
			uri = uri.replace("{classId}", ""+classId);
			URL url = new URL(uri);
			urlCon = (HttpURLConnection)url.openConnection();
			InputStream is = urlCon.getInputStream();
			String data = readAllFrom(is);
			_participants = participantsParseFrom(data, classId);
		} catch (JSONException e) {
			Log.d(TAG, e.toString());
		} catch (MalformedURLException e) {
			Log.d(TAG, e.toString());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}finally{
			urlCon.disconnect();
		}
		return _participants;
	}	

	private Participant[] participantsParseFrom(String data, int classId) throws JSONException {
		JSONObject root = new JSONObject(data);
		JSONObject jmainTeacher = root.getJSONObject("mainTeacher");
		JSONArray jotherTeachers = root.getJSONArray("otherTeachers");
		JSONArray jstudents = root.getJSONArray("students");
		_participants = new Participant[1 + jotherTeachers.length() +  jstudents.length()];

		insertIntoParticipants(jmainTeacher, true);
		insertJSONArrayIntoParicipant(jotherTeachers, true);
		insertJSONArrayIntoParicipant(jstudents, false);
		return _participants;
	}

	private void insertJSONArrayIntoParicipant(JSONArray jArray,
			boolean isTeacher) throws JSONException {
		for(int i = 0; i < jArray.length(); i++) {
			insertIntoParticipants(jArray.getJSONObject(i), isTeacher);
		}		
	}

	private void insertIntoParticipants(JSONObject jmainTeacher, boolean isTeacher) throws JSONException {
		JSONObject avatar = jmainTeacher.getJSONObject("avatarUrl");
		Participant part = new Participant (
				jmainTeacher.getInt("number"),
				jmainTeacher.getString("fullName"),
				jmainTeacher.getString("academicEmail"),
				avatar.getString("size128"),
				isTeacher
				);
		_participants[_posParticipant] = part;	
		_posParticipant++;
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
