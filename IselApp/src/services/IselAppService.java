package services;

import entities.ClassItem;
import entities.NewsItem;
import entities.WorkItem;
import utils.RequestsToThoth;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;

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

			doAction(classesIdsToAdd);
			doAction(classesIdsToRemove);

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

	private void doAction(int[] classesId) {
		for(int id : classesId) {
			Cursor c = _cr.query(
					Uri.parse("content://com.example.iselappserver/classes/"+id),
					new String[]{"_classFullname", "_classShowNews"},
					null,
					null,
					null);

			// se 1 vai passar para 0 para nao mostrar as noticias 
			// e vai eliminar do thothNews as noticias dessa turma
			if (c.moveToFirst() && c.getInt(c.getColumnIndex("_classShowNews")) == 1) {

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
				_cr.delete(
						Uri.parse("content://com.example.iselappserver/workItems"), 
						"_workItem_classId = ?", 
						new String[]{""+id});
			}

			// se 0 vai passar para 1 para mostrar as noticias 
			// e vai fazer um request ao thoth para inserir no thothNews as noticias dessa turma
			if (c.moveToFirst() && c.getInt(c.getColumnIndex("_classShowNews")) == 0) {

				ContentValues insertShowFromClass = new ContentValues();
				insertShowFromClass.put("_classShowNews", 1);

				_cr.update(
						Uri.parse("content://com.example.iselappserver/classes/"+id), 
						insertShowFromClass, 
						null, 
						null);

				NewsItem[] result = _requests.requestNews(id, c.getString(c.getColumnIndex("_classFullname")));

				for(int i = 0; i < result.length; i++) {
					insertNewsItem(result[i], id);
				}

				WorkItem[] workItems = _requests.getWorkItems(id, c.getString(c.getColumnIndex("_classFullname")));
				for(int i = 0; i < workItems.length; i++) {
					insertWorkItem(workItems[i], id);
					insertCalendarEvent(workItems[i], i);
				}
			}
		}

	}

	private void insertCalendarEvent(WorkItem workItem, int id) {
		String str = addEvent(""+id, 
				workItem.workItem_title, 
				workItem.workItem_startDate.getTimeInMillis(), 
				workItem.workItem_dueDate.getTimeInMillis(), 
				1);		
	}

	public String addEvent(String calendarId, String title, long startTime,
			long endTime, int allDay) {
		ContentValues event = new ContentValues();
		event.put("calendar_id", ListSelectedCalendars()); // "" for insert
		event.put("title", title);
		event.put("description", "");
		event.put("eventLocation", "");
		event.put("allDay", allDay);
		event.put("eventStatus", 1);
		event.put("dtstart", startTime);
		event.put("dtend", endTime);
		event.put("hasAlarm", 1);
		event.put(Events.EVENT_TIMEZONE, "Portugal/Lisboa");

		Uri eventsUri = Uri.parse("content://com.android.calendar/events");
		Uri url = _cr.insert(eventsUri, event);
		String ret = url.toString();
		return ret;
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



	private void insertWorkItem(WorkItem item, int id) {
		ContentValues values = new ContentValues();
		values.put("_workItem_classId",item.workItem_classId);
		values.put("_workItem_classFullname",item.workItem_classFullname);
		values.put("_workItemId",item.workItem_id);
		values.put("_workItemAcronym",item.workItem_Acronym);
		values.put("_workItemTitle",item.workItem_title);
		values.put("_workItemReqGroupSubmission",item.workItem_reqGroupSubmission == true ? 1 : 0);
		values.put("_workItemStarDate",Long.toString(item.workItem_startDate.getTimeInMillis()));
		values.put("_workItemDueDate",Long.toString(item.workItem_dueDate.getTimeInMillis()));
		values.put("_workItemAcceptsLateSubmission",item.workItem_acceptsLateSubmission == true ? 1 : 0);
		values.put("_workItemAcceptsResubmission",item.workItem_acceptsResubmission == true ? 1 : 0);
		values.put("_workItemReportUploadInfoIsRequired",item.workItem_reportUploadInfo.reportUploadInfo_isRequired == true ? 1 : 0);
		values.put("_workItemReportUploadInfoMaxFileSizeInMB",item.workItem_reportUploadInfo.reportUploadInfo_maxFileSizeInMB);
		values.put("_workItemReportUploadInfoAcceptedExtensions",item.workItem_reportUploadInfo.reportUploadInfo_acceptedExtensions);
		values.put("_workItemAttachmentUploadInfoIsRequired",item.workItem_attachmentUploadInfo.attachmentUploadInfo_isRequired == true ? 1 : 0);
		values.put("_workItemAttachmentUploadInfoMaxFileSizeInMB",item.workItem_attachmentUploadInfo.attachmentUploadInfo_maxFileSizeInMB);
		values.put("_workItemAttachmentUploadInfoAcceptedExtensions",item.workItem_reportUploadInfo.reportUploadInfo_acceptedExtensions);
		_cr.insert(Uri.parse("content://com.example.iselappserver/workItems"), values);

	}

	private void insertClassesItem(ClassItem classItem) {
		ContentValues values = new ContentValues();
		values.put("_classId", classItem.getId());
		values.put("_classFullname", classItem.getFullname());
		values.put("_classShowNews", 0);
		_cr.insert(Uri.parse("content://com.example.iselappserver/classes"), values);

	}

	public void insertNewsItem(NewsItem item, int classId) {
		ContentValues values = new ContentValues();
		values.put("_newsId", item.news_id);
		values.put("_newsClassId", classId);
		values.put("_newsClassFullname", item.news_classFullname);
		values.put("_newsTitle", item.news_title);
		values.put("_newsWhen", Long.toString(item.news_when.getTimeInMillis()));
		values.put("_newsContent", item.news_content);
		values.put("_newsIsViewed", 0);
		_cr.insert(Uri.parse("content://com.example.iselappserver/news"), values);
	}

}
