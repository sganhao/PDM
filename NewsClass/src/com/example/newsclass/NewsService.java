package com.example.newsclass;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class NewsService extends IntentService  {

	private ContentResolver _cr;
	private HttpRequestsToThoth _requests;
	private Context _context;
	private int _idx;
	private String TAG = "NewsService";

	public NewsService() {
		super("NewsService");
	}

	public NewsService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		_cr = getContentResolver();
		_context = this.getApplicationContext();
		_requests = new HttpRequestsToThoth();
		_idx = 0;
	}	

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();		

		Log.d(TAG , "action = " + action);

		if(action.equals("userUpdateClasses")){
			//From User Interaction - Do http request to thoth if a new class is selected

			//service starts from settingsActivity

			int[] classesId = intent.getIntArrayExtra("classesId");

			for(int id : classesId) {
				Cursor c = _cr.query(
						Uri.parse("content://com.example.newsclassserver/thothClasses/"+id),
						new String[]{"fullname", "showNews"},
						null,
						null,
						null);

				// se 1 vai passar para 0 para nao mostrar as noticias 
				// e vai eliminar do thothNews as noticias dessa turma
				if (c.moveToFirst() && c.getInt(c.getColumnIndex("showNews")) == 1) {

					ContentValues removeShowFromClass = new ContentValues();
					removeShowFromClass.put("showNews", 0);

					_cr.update(
							Uri.parse("content://com.example.newsclassserver/thothClasses/"+id), 
							removeShowFromClass, 
							null, 
							null);

					_cr.delete(
							Uri.parse("content://com.example.newsclassserver/thothNews"), 
							"_classId = ?", 
							new String[]{""+id});
				}

				// se 0 vai passar para 1 para mostrar as noticias 
				// e vai fazer um request ao thoth para inserir no thothNews as noticias dessa turma
				if (c.moveToFirst() && c.getInt(c.getColumnIndex("showNews")) == 0) {

					ContentValues insertShowFromClass = new ContentValues();
					insertShowFromClass.put("showNews", 1);

					_cr.update(
							Uri.parse("content://com.example.newsclassserver/thothClasses/"+id), 
							insertShowFromClass, 
							null, 
							null);

					NewItem[] result = _requests.requestNews(id, c.getString(c.getColumnIndex("fullname")));

					for(int i = 0; i < result.length; i++) {
						insertNewsItem(result[i], id);
					}
				}
			}
		} else

			// service starts from mainActivity to update the showNews from the news user saw
			if(action.equals("userUpdateNews")) {

				ContentValues newsValues = new ContentValues();
				newsValues.put("isViewed", 1);

				_cr.update(
						Uri.parse("content://com.example.newsclassserver/thothNews/" + intent.getExtras().get("newId")), 
						newsValues, 
						null, 
						null);				
			}
			else 
				if(action.equals("wifi_connected")){
					//From Broadcast Receiver - Do the http request to thoth;
					//Send a notification warning about new news

					Cursor c = _cr.query(
							Uri.parse("content://com.example.newsclassserver/thothClasses"), 
							new String[]{"_classId","fullname"}, 
							"showNews = ? ", 
							new String[]{""+1}, 
							null);

					while(c.moveToNext()){
						int classId = c.getInt(c.getColumnIndex("_classId"));
						NewItem[] result = _requests.requestNews(classId,c.getString(c.getColumnIndex("fullname")));
						int countNews = 0;
						for(int i = 0; i < result.length; i++) {

							Cursor checkId = _cr.query(
									Uri.parse("content://com.example.newsclassserver/thothNews/" + result[i].newsId), 
									new String[]{"_newsId"}, 
									null, 
									null, 
									null);

							if(checkId.getCount() == 0) {
								insertNewsItem(result[i], classId);
								countNews++;
							}
						}

						RemoteViews rv = new RemoteViews("com.example.newsclass",R.layout.layout_notification_br);
						rv.setTextViewText(R.id.textView1, "Class" + classId + ": " + countNews + "News added");

						Notification.Builder builder = new Notification.Builder(_context)
						.setContentTitle("New News")
						.setAutoCancel(true)
						.setSmallIcon(R.drawable.ic_launcher)
						.setOngoing(true)
						.setContent(rv);

						Intent i = new Intent(_context,MainActivity.class);
						PendingIntent pintent = PendingIntent.getActivity(_context, 1, i, 0);
						builder.setContentIntent(pintent);
						NotificationManager manager = (NotificationManager) _context.getSystemService(_context.NOTIFICATION_SERVICE);
						manager.notify(_idx, builder.build());
						_idx++;
					}
				}else 
					if(action.equals("firstFillOfCP")){
						// Insert data in the content provider for the first time
						Clazz[] result = _requests.requestClasses();

						for (int i = 0; i < result.length; i++) {
							insertClassesItem(result[i]);
						}
					}
	}

	public void insertNewsItem(NewItem item, int classId) {
		ContentValues values = new ContentValues();
		values.put("_newsId", item.newsId);
		values.put("_classId", classId);
		values.put("classFullname", item.classFullname);
		values.put("title", item.title);
		values.put("_when", item.when.toString());
		values.put("content", item.content);
		values.put("isViewed", 0);
		_cr.insert(Uri.parse("content://com.example.newsclassserver/thothNews"), values);
	}

	public void insertClassesItem(Clazz item) {
		ContentValues values = new ContentValues();
		values.put("_classId", item.getId());
		values.put("fullname", item.getFullname());
		values.put("showNews", 0);
		_cr.insert(Uri.parse("content://com.example.newsclassserver/thothClasses"), values);
	}
}
