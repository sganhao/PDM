package utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.util.Log;
import entities.WorkItem;

public class CalendarEvents {

	private static final String TAG = null;
	private ContentResolver _cr;

	public CalendarEvents(ContentResolver cr){
		_cr = cr;
	}

	public long addNewEvent(WorkItem workItem, String className){
		return addEvent(
				workItem.workItem_title, 
				workItem.workItem_startDate.getTimeInMillis(), 
				workItem.workItem_dueDate.getTimeInMillis(),
				className,
				1);
	}

	private long addEvent(String title, long startTime,
			long endTime, String classFullname, int allDay) {
		ContentValues event = new ContentValues();
		event.put("calendar_id", getCalendarId());
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

	public void deleteEvents(int id) {
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

	private int getCalendarId(){
		int result = 0;
		String[] projection = new String[] { "_id", "name" };

		Cursor cursor = _cr.query(
				Uri.parse("content://com.android.calendar/calendars"),
				projection,
				null,
				null,
				null);

		if (cursor != null && cursor.moveToFirst()) {
			int idColumn = cursor.getColumnIndex("_id");

			do {
				String calId = cursor.getString(idColumn);
				result = Integer.parseInt(calId);
			} while (cursor.moveToNext());
		} else {
			Log.d(TAG, "No Calendars");
		}
		return result;
	}
}
