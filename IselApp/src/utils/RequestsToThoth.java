package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;
import android.util.Log;

import entities.*;

public class RequestsToThoth {

	private static final String TAG = "IselApp";

	private final String CLASSES = "http://thoth.cc.e.ipl.pt/api/v1/classes/";
	private final String NEWS = "http://thoth.cc.e.ipl.pt/api/v1/classes/{classId}/newsitems";
	private final String PARTICIPANTS = "http://thoth.cc.e.ipl.pt/api/v1/classes/{classId}/participants";
	private final String CONTENT = "http://thoth.cc.e.ipl.pt/api/v1/newsitems/";
	private final String WORKITEMS = "http://thoth.cc.e.ipl.pt/api/v1/classes/{classId}/workitems";
	private final String WORKITEM = "http://thoth.cc.e.ipl.pt/api/v1/workitems/";

	private ParticipantItem[] _participants;
	private int _posParticipant;

	public RequestsToThoth(){
		_posParticipant = 0;
	}

	public ClassItem[] requestClasses(){
		HttpURLConnection urlcon = null;
		try{
			URL url = new URL(CLASSES);
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

	private ClassItem[] classesParseFrom(String data) throws JSONException {
		JSONObject root = new JSONObject(data);
		JSONArray jclasses = root.getJSONArray("classes");
		ClassItem[] classes = new ClassItem[jclasses.length()];

		for (int i = 0; i < jclasses.length(); ++i) {
			JSONObject jclass = jclasses.getJSONObject(i);
			ClassItem item = new ClassItem(
					jclass.getInt("id"), 
					jclass.getString("fullName"), 
					false
					);
			classes[i] = item;
		}
		return classes;
	}



	public NewsItem[] requestNews(int classId, String classFullname){
		Log.d(TAG, "requestNews");
		HttpURLConnection urlCon = null;
		String uri = NEWS;
		try {
			uri = uri.replace("{classId}", ""+classId);
			URL url = new URL(uri);
			urlCon = (HttpURLConnection)url.openConnection();
			InputStream is = urlCon.getInputStream();
			String data = readAllFrom(is);
			return newsParseFrom(data, classId, classFullname);
		} catch (JSONException e) {
			Log.d(TAG, e.toString());
		} catch (MalformedURLException e) {
			Log.d(TAG, e.toString());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		} catch (ParseException e) {
			Log.d(TAG, e.toString());
		}finally{
			urlCon.disconnect();
		}
		return null;
	}

	private NewsItem[] newsParseFrom(String data, int classId, String classFullname) throws JSONException, IOException, ParseException {
		JSONObject root = new JSONObject(data);
		JSONArray jnews = root.getJSONArray("newsItems");
		NewsItem[] newsarray = new NewsItem[jnews.length()];

		for (int i = 0; i < jnews.length(); ++i) {
			JSONObject jnew = jnews.getJSONObject(i);
			newsarray[i] = new NewsItem(
					classFullname,
					classId,
					jnew.getInt("id"),
					jnew.getString("title"), 
					getDate(jnew.getString("when")),
					getNewsItemContent(jnew.getInt("id")),
					false
					);
		}
		return newsarray;
	}

	private long getDate(String str) {
		String [] aux = str.split("[-T:.]+");
		Calendar c = Calendar.getInstance();
		c.set(
				Integer.parseInt(aux[0]), 
				Integer.parseInt(aux[1]) - 1, 
				Integer.parseInt(aux[2]), 
				Integer.parseInt(aux[3]), 
				Integer.parseInt(aux[4]), 
				Integer.parseInt(aux[5]));
		return c.getTimeInMillis();
	}

	private String getNewsItemContent(int id) throws IOException, JSONException {
		HttpURLConnection urlcon = null;
		try {
			URL url = new URL(CONTENT + id);
			urlcon = (HttpURLConnection)url.openConnection();
			InputStream is = urlcon.getInputStream();
			String data = readAllFrom(is);
			JSONObject root = new JSONObject(data);
			return root.getString("content").toString();
		}finally{
			urlcon.disconnect();
		}
	}	



	public ParticipantItem[] requestParticipants(int classId) {
		Log.d(TAG, "request Participant");
		HttpURLConnection urlCon = null;
		String uri = PARTICIPANTS;
		try {
			uri = uri.replace("{classId}", ""+classId);
			URL url = new URL(uri);
			urlCon = (HttpURLConnection)url.openConnection();
			InputStream is = urlCon.getInputStream();
			String data = readAllFrom(is);
			participantsParseFrom(data, classId);
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

	private ParticipantItem[] participantsParseFrom(String data, int classId) throws JSONException {
		JSONObject root = new JSONObject(data);
		JSONObject jmainTeacher = root.getJSONObject("mainTeacher");
		JSONArray jotherTeachers = root.getJSONArray("otherTeachers");
		JSONArray jstudents = root.getJSONArray("students");
		_participants = new ParticipantItem[1 + jotherTeachers.length() +  jstudents.length()];

		insertIntoParticipants(jmainTeacher, true);
		insertJSONArrayIntoParicipant(jotherTeachers, true);
		insertJSONArrayIntoParicipant(jstudents, false);
		return _participants;
	}

	private void insertJSONArrayIntoParicipant(JSONArray jArray, boolean isTeacher) throws JSONException {
		for(int i = 0; i < jArray.length(); i++) {
			insertIntoParticipants(jArray.getJSONObject(i), isTeacher);
		}		
	}

	private void insertIntoParticipants(JSONObject jmainTeacher, boolean isTeacher) throws JSONException {
		JSONObject avatar = jmainTeacher.getJSONObject("avatarUrl");
		ParticipantItem part = new ParticipantItem (
				jmainTeacher.getInt("number"),
				jmainTeacher.getString("fullName"),
				jmainTeacher.getString("academicEmail"),
				avatar.getString("size128"),
				isTeacher
				);
		_participants[_posParticipant] = part;	
		_posParticipant++;
	}



	public WorkItem[] getWorkItems(int classId, String classFullname){
		Log.d(TAG, "requestWorkItems");
		HttpURLConnection urlCon = null;
		String uri = WORKITEMS;
		try {
			uri = uri.replace("{classId}", ""+classId);
			URL url = new URL(uri);
			urlCon = (HttpURLConnection)url.openConnection();
			InputStream is = urlCon.getInputStream();
			String data = readAllFrom(is);
			return workItemsParseFrom(data, classId, classFullname);
		} catch (JSONException e) {
			Log.d(TAG, e.toString());
		} catch (MalformedURLException e) {
			Log.d(TAG, e.toString());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		} catch (ParseException e) {
			Log.d(TAG, e.toString());
		}finally{
			urlCon.disconnect();
		}
		return null;
	}

	private WorkItem[] workItemsParseFrom(String data, int classId, String classFullname) throws JSONException, IOException, ParseException {
		JSONObject root = new JSONObject(data);
		JSONArray jworkItems = root.getJSONArray("workItems");
		WorkItem[] workItems = new WorkItem[jworkItems.length()];

		for (int i = 0; i < jworkItems.length(); ++i) {
			JSONObject jworkItem = jworkItems.getJSONObject(i);
			int workItemId = jworkItem.getInt("id");
			workItems[i] = getDetailedContentWorkItem(workItemId, classId, classFullname);
		}
		return workItems;
	}

	private WorkItem getDetailedContentWorkItem(int workItemId, int classId, String classFullname) throws IOException, JSONException, ParseException {
		Log.d(TAG, "requestWorkItems");
		HttpURLConnection urlCon = null;
		try{
			URL url = new URL(WORKITEM + workItemId);
			urlCon = (HttpURLConnection)url.openConnection();
			InputStream is = urlCon.getInputStream();
			String data = readAllFrom(is);
			return detailedContentWorkItemParseFrom(data, classId, classFullname);
		}finally{
			urlCon.disconnect();
		}
	}

	private WorkItem detailedContentWorkItemParseFrom(String data, int classId, String classFullname) throws JSONException, ParseException {
		JSONObject root = new JSONObject(data);

		JSONObject jreportUploadInfo = root.getJSONObject("reportUploadInfo");
		JSONObject jattachmentUploadInfo = root.getJSONObject("attachmentUploadInfo");

		ReportUploadInfo repUpInfo = new ReportUploadInfo(
				jreportUploadInfo.getBoolean("isRequired"), 
				jreportUploadInfo.getInt("maxFileSizeInMB"), 
				getAcceptedExtensions(jreportUploadInfo.getJSONArray("acceptedExtensions"))
				);
		AttachmentUploadInfo attachUpInfo = new AttachmentUploadInfo(
				jattachmentUploadInfo.getBoolean("isRequired"), 
				jattachmentUploadInfo.getInt("maxFileSizeInMB"), 
				getAcceptedExtensions(jattachmentUploadInfo.getJSONArray("acceptedExtensions"))
				);

		return new WorkItem(
				classId,
				classFullname,
				root.getInt("id"),
				root.getString("acronym"),
				root.getString("title"),
				root.getBoolean("requiresGroupSubmission"),
				getDate(root.getString("startDate")),
				getDate(root.getString("dueDate")),
				root.getBoolean("acceptLateSubmission"),
				root.getBoolean("acceptResubmission"),
				repUpInfo,
				attachUpInfo
				);


	}

	private String getAcceptedExtensions(JSONArray jsonArray) throws JSONException {
		String extensions = "";
		for(int i = 0 ; i < jsonArray.length() ; i++){
			extensions += jsonArray.get(i);
			if(i != jsonArray.length() - 1)
				extensions += " | ";
		}
		return extensions;
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
