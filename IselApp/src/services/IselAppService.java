package services;

import java.util.ArrayList;
import java.util.List;

import utils.CalendarEvents;
import utils.RequestsToThoth;
import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;
import entities.NewsItem;
import entities.WorkItem;

public class IselAppService extends IntentService {

	private String TAG = "IselApp";
	private ContentResolver _cr;
	private RequestsToThoth _requests;
	private CalendarEvents _calendarEvents;

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
		_requests = new RequestsToThoth();
		_calendarEvents = new CalendarEvents(_cr);
	}	

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();		

		Log.d(TAG , "IselAppService: action = " + action);

		if(action.equals("userUpdateClasses")){
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
		if(classesIdsToRemove.length == 0)
			return;
		for(int id : classesIdsToRemove) {
			Cursor c = _cr.query(
					Uri.parse("content://com.example.iselappserver/classes/"+id),
					new String[]{"_classFullname", "_classShowNews"},
					null,
					null,
					null);
			c.moveToFirst();

			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

			ops.add(ContentProviderOperation.newUpdate(Uri.parse("content://com.example.iselappserver/classes/"+id)).withValue("_classShowNews", 0).build());
			
			ops.add(ContentProviderOperation.newDelete(Uri.parse("content://com.example.iselappserver/news"))
					.withSelection("_newsClassId = ?", new String[]{""+id}).build());

			ops.add(ContentProviderOperation.newDelete(Uri.parse("content://com.example.iselappserver/workItems"))
					.withSelection("_workItem_classId = ?", new String[]{""+id}).build());

			try {
				getContentResolver().applyBatch("com.example.iselappserver", ops);
				_calendarEvents.deleteEvents(id);
			} catch (RemoteException e) {
				Log.d(TAG,e.toString());
			} catch (OperationApplicationException e) {
				Log.d(TAG,e.toString());
			}finally{
				c.close();
			}			
		}
	}

	private void classesIn(int[] classesIdsToAdd) {
		if(classesIdsToAdd.length == 0)
			return;
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
				newsItemValues.add(getNewsItemContentValues(result[i]));
			}

			WorkItem[] workItems = _requests.requestWorkItems(id, c.getString(c.getColumnIndex("_classFullname")));
			for(int i = 0; i < workItems.length; i++) {
				workItems[i].workItem_eventId = _calendarEvents.addNewEvent(workItems[i], c.getString(c.getColumnIndex("_classFullname")));
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
