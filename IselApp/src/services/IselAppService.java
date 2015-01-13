package services;

import java.util.ArrayList;
import java.util.List;

import utils.RequestsToThoth;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.util.Log;
import entities.ClassItem;
import entities.NewsItem;
import entities.WorkItem;

public class IselAppService extends IntentService {

	private String TAG = "IselApp";
	private ContentResolver _cr;
	private RequestsToThoth _requests;
	private Context _context;
	private int _idx;

	public IselAppService() {
		super("IselAppService");
	}

	public IselAppService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		_cr = getContentResolver();
		_context = this.getApplicationContext();
		_requests = new RequestsToThoth();
		_idx = 0;
	}	

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();		

		Log.d(TAG , "IselAppService: action = " + action);

		if(action.equals("firstFillOfCP")){
			ClassItem[] classes = _requests.requestClasses();
			for(int i = 0 ; i < classes.length ; i++)
				insertClassesItem(classes[i]);

		}else if(action.equals("userUpdateClasses")){
			//From User Interaction - Do http request to thoth if a new class is selected
			//service starts from settingsActivity
			int[] classesIdsToRemove = intent.getIntArrayExtra("classesIdsToRemove");
			int[] classesIdsToAdd = intent.getIntArrayExtra("classesIdsToAdd");

			classesIn(classesIdsToAdd);
			classesOut(classesIdsToRemove);

		}else if(action.equals("userUpdateNews")) {
			// service starts from mainActivity to update the showNews from the news user saw
			ContentValues newsValues = new ContentValues();
			newsValues.put("_newsIsViewed", 1);
			_cr.update(
					Uri.parse("content://com.example.iselappserver/news/" + intent.getExtras().get("newId")), 
					newsValues, 
					null, 
					null);				
		}
	}

	private void classesOut(int[] classesIdsToRemove) {
		for(int id : classesIdsToRemove) {
			Cursor c = _cr.query(
					Uri.parse("content://com.example.iselappserver/classes/"+id),
					new String[]{"_classFullname", "_classShowNews"},
					null,
					null,
					null);
			c.moveToFirst();

			ContentValues removeShowFromClass = new ContentValues();
			removeShowFromClass.put("_classShowNews", 0);

			_cr.update(
					Uri.parse("content://com.example.iselappserver/classes/"+id), 
					removeShowFromClass, 
					null, 
					null);

			_cr.delete(
					Uri.parse("content://com.example.iselappserver/news"), 
					"_newsClassId = ?", 
					new String[]{""+id});

			deleteCalendarEvents(id);

			_cr.delete(
					Uri.parse("content://com.example.iselappserver/workItems"), 
					"_workItem_classId = ?", 
					new String[]{""+id});
			c.close();
		}

	}

	private void classesIn(int[] classesIdsToAdd) {
		List<ContentValues> newsItemValues = new ArrayList<ContentValues>();
		List<ContentValues> workItemsValues = new ArrayList<ContentValues>();
		for(int id : classesIdsToAdd) {
			Cursor c = _cr.query(
					Uri.parse("content://com.example.iselappserver/classes/"+id),
					new String[]{"_classFullname", "_classShowNews"},
					null,
					null,
					null);

			// se 0 vai passar para 1 para mostrar as noticias 
			// e vai fazer um request ao thoth para inserir no thothNews as noticias dessa turma
			c.moveToFirst();

			ContentValues insertShowFromClass = new ContentValues();
			insertShowFromClass.put("_classShowNews", 1);

			_cr.update(
					Uri.parse("content://com.example.iselappserver/classes/"+id), 
					insertShowFromClass, 
					null, 
					null);

			NewsItem[] result = _requests.requestNews(id, c.getString(c.getColumnIndex("_classFullname")));

			for(int i = 0; i < result.length; i++) {
				newsItemValues.add(getNewsItemContentValues(result[i], id));
			}

			WorkItem[] workItems = _requests.requestWorkItems(id, c.getString(c.getColumnIndex("_classFullname")));
			for(int i = 0; i < workItems.length; i++) {
				workItems[i].workItem_eventId = insertCalendarEvent(workItems[i], c.getString(c.getColumnIndex("_classFullname")));
				workItemsValues.add(getWorkItemContentValues(workItems[i]));
			}
			c.close();
		}
		ContentValues [] values = new ContentValues[newsItemValues.size()];
		newsItemValues.toArray(values);
		_cr.bulkInsert(Uri.parse("content://com.example.iselappserver/news"), values);
		values = new ContentValues[workItemsValues.size()];
		workItemsValues.toArray(values);
		_cr.bulkInsert(Uri.parse("content://com.example.iselappserver/workItems"), values);
	}

	private void deleteCalendarEvents(int id) {
		Cursor cursor = _cr.query(
				Uri.parse("content://com.example.iselappserver/workItems"),
				new String[]{"_workItemEventId"},
				"_workItem_classId = ?", 
				new String[]{""+id},
				null);
		while(cursor.moveToNext()){
			long eventId = cursor.getInt(cursor.getColumnIndex("_workItemEventId"));
			Uri deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventId);
			_cr.delete(deleteUri, null, null);
		}
		cursor.close();
	}

	private long insertCalendarEvent(WorkItem workItem, String classFullname) {
		return addEvent(
				workItem.workItem_title, 
				workItem.workItem_startDate.getTimeInMillis(), 
				workItem.workItem_dueDate.getTimeInMillis(),
				classFullname,
				1);		
	}

	public long addEvent(String title, long startTime,
			long endTime, String classFullname, int allDay) {
		ContentValues event = new ContentValues();
		event.put("calendar_id", ListSelectedCalendars()); // "" for insert
		event.put("title", title);
		event.put("description", "class: " + classFullname);
		event.put("eventLocation", "");
		event.put("allDay", allDay);
		event.put("eventStatus", 1);
		event.put("dtstart", endTime);
		event.put("dtend", endTime);
		event.put("hasAlarm", 1);
		event.put(Events.EVENT_TIMEZONE, "Portugal/Lisboa");

		Uri eventsUri = Uri.parse("content://com.android.calendar/events");
		Uri url = _cr.insert(eventsUri, event);

		return Long.parseLong(url.getLastPathSegment());
	}

	private int ListSelectedCalendars() {
		int result = 0;
		String[] projection = new String[] { "_id", "name" };

		Cursor managedCursor = _cr.query(
				Uri.parse("content://com.android.calendar/calendars"),
				projection,
				null,
				null,
				null);

		if (managedCursor != null && managedCursor.moveToFirst()) {

			Log.d(TAG, "Listing Selected Calendars Only");

			int idColumn = managedCursor.getColumnIndex("_id");

			do {
				String calId = managedCursor.getString(idColumn);
				result = Integer.parseInt(calId);
			} while (managedCursor.moveToNext());
		} else {
			Log.d(TAG, "No Calendars");
		}
		return result;
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


	public ContentValues getNewsItemContentValues(NewsItem item, int classId) {
		ContentValues values = new ContentValues();
		values.put("_newsId", item.news_id);
		values.put("_newsClassId", classId);
		values.put("_newsClassFullname", item.news_classFullname);
		values.put("_newsTitle", item.news_title);
		values.put("_newsWhen", Long.toString(item.news_when.getTimeInMillis()));
		values.put("_newsContent", item.news_content);
		values.put("_newsIsViewed", 0);
		return values;
	}

}
