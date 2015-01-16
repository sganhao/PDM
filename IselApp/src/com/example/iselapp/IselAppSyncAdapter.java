package com.example.iselapp;

import java.util.ArrayList;
import java.util.List;

import com.example.iselapp.R;

import newsActivities.*;
import entities.ClassItem;
import entities.NewsItem;
import entities.WorkItem;
import utils.CalendarEvents;
import utils.NotificationLaunch;
import utils.RequestsToThoth;
import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class IselAppSyncAdapter extends AbstractThreadedSyncAdapter {
	private final String TAG = "IselApp";
	private ContentResolver _cr;
	private RequestsToThoth _requests;
	private NotificationLaunch _notificationLaunch;


	public IselAppSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		_cr = context.getContentResolver();
		_requests = new RequestsToThoth();
		_notificationLaunch = new NotificationLaunch(context);
	}

	public IselAppSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		_cr = context.getContentResolver();
		_requests = new RequestsToThoth();
		_notificationLaunch = new NotificationLaunch(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Log.d(TAG,"onPerformSync");
		checkForNewClasses();
		checkForNewNewsItems();
		checkForNewWorkItems();

	}

	
	

	public void checkForNewClasses(){
		Cursor classesSelectedCursor = _cr.query(
				Uri.parse("content://com.example.iselappserver/classes"), 
				new String[]{"_classId"}, 
				null, 
				null, 
				null);
		ClassItem[] result = _requests.requestClasses();
		int countClassesAdded = 0;
		for(int i = 0; i < result.length; i++) {
			if(!findId(classesSelectedCursor,result[i].getId())){
				insertClassesItem(result[i]);
				countClassesAdded++;
			}
		}
		_notificationLaunch.launchNotification("Added " + countClassesAdded + " new classes");
		classesSelectedCursor.close();
	}

	public void checkForNewNewsItems(){
		List<ContentValues> newsItemValues = new ArrayList<ContentValues>();
		Cursor classesSelectedCursor = _cr.query(
				Uri.parse("content://com.example.iselappserver/classes"), 
				new String[]{"_classId","_classFullname"}, 
				"_classShowNews = ? ", 
				new String[]{""+1}, 
				null);

		while(classesSelectedCursor.moveToNext()){
			int classId = classesSelectedCursor.getInt(classesSelectedCursor.getColumnIndex("_classId"));
			String className = classesSelectedCursor.getString(classesSelectedCursor.getColumnIndex("_classFullname"));

			NewsItem[] result = _requests.requestNews(classId,className);
			int countNewsAdded = 0;

			for(int i = 0; i < result.length; i++) {

				Cursor checkId = _cr.query(
						Uri.parse("content://com.example.iselappserver/news/" + result[i].news_id), 
						new String[]{"_newsId"}, 
						null, 
						null, 
						null);

				if(checkId.getCount() == 0) {
					newsItemValues.add(getNewsItemContentValues(result[i]));
					countNewsAdded++;
				}
				checkId.close();
			}

			ContentValues [] values = new ContentValues[newsItemValues.size()];
			newsItemValues.toArray(values);
			_cr.bulkInsert(Uri.parse("content://com.example.iselappserver/news"), values);

			_notificationLaunch.launchNotification("Class " + className + ": Added " + countNewsAdded + " News");
		}
		classesSelectedCursor.close();
	}

	public void checkForNewWorkItems(){
		List<ContentValues> workItemsValues = new ArrayList<ContentValues>();
		CalendarEvents calendarEvents = new CalendarEvents(_cr);
		Cursor classesSelectedCursor = _cr.query(
				Uri.parse("content://com.example.iselappserver/classes"), 
				new String[]{"_classId","_classFullname"}, 
				"_classShowNews = ? ", 
				new String[]{""+1}, 
				null);

		while(classesSelectedCursor.moveToNext()){
			int classId = classesSelectedCursor.getInt(classesSelectedCursor.getColumnIndex("_classId"));
			String className = classesSelectedCursor.getString(classesSelectedCursor.getColumnIndex("_classFullname"));

			WorkItem[] result = _requests.requestWorkItems(classId,className);
			int countWorkItemsAdded = 0;

			for(int i = 0; i < result.length; i++) {

				Cursor checkId = _cr.query(
						Uri.parse("content://com.example.iselappserver/workItems/" + result[i].workItem_id), 
						new String[]{"_workItemId"}, 
						null, 
						null, 
						null);

				if(checkId.getCount() == 0) {
					result[i].workItem_eventId = calendarEvents.addNewEvent(result[i], className);
					workItemsValues.add(getWorkItemContentValues(result[i]));
					countWorkItemsAdded++;
				}
				checkId.close();
			}

			ContentValues[] values = new ContentValues[workItemsValues.size()];
			workItemsValues.toArray(values);
			_cr.bulkInsert(Uri.parse("content://com.example.iselappserver/workItems"), values);

			_notificationLaunch.launchNotification("Class " + className + ": Added " + countWorkItemsAdded + " WorkItems");;
		}
		classesSelectedCursor.close();
	}

	private boolean findId(Cursor cursor, int id){
		cursor.moveToFirst();
		do{
			if(cursor.getInt(cursor.getColumnIndex("_classId")) == id){
				cursor.close();
				return true;
			}
		}while(cursor.moveToNext());
		cursor.close();
		return false;
	}

	private ContentValues getWorkItemContentValues(WorkItem item) {
		ContentValues values = new ContentValues();
		values.put("_workItem_classId",item.workItem_classId);
		values.put("_workItem_classFullname",item.workItem_classFullname);
		values.put("_workItemId",item.workItem_id);
		values.put("_workItemAcronym",item.workItem_Acronym);
		values.put("_workItemTitle",item.workItem_title);
		values.put("_workItemStartDate",Long.toString(item.workItem_startDate.getTimeInMillis()));
		values.put("_workItemDueDate",Long.toString(item.workItem_dueDate.getTimeInMillis()));
		values.put("_workItemEventId",item.workItem_eventId);
		return values;

	}

	private void insertClassesItem(ClassItem classItem) {
		ContentValues values = new ContentValues();
		values.put("_classId", classItem.getId());
		values.put("_classFullname", classItem.getFullname());
		values.put("_classShowNews", 0);
		_cr.insert(Uri.parse("content://com.example.iselappserver/classes"), values);

	}

	public ContentValues getNewsItemContentValues(NewsItem item) {
		ContentValues values = new ContentValues();
		values.put("_newsId", item.news_id);
		values.put("_newsClassId", item.news_classId);
		values.put("_newsClassFullname", item.news_classFullname);
		values.put("_newsTitle", item.news_title);
		values.put("_newsWhen", Long.toString(item.news_when.getTimeInMillis()));
		values.put("_newsContent", item.news_content);
		values.put("_newsIsViewed", 0);
		return values;
	}

}
