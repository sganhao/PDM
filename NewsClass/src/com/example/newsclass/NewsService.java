package com.example.newsclass;

import java.util.Set;

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
import android.widget.RemoteViews;

public class NewsService extends IntentService  {

	ContentResolver cr = getContentResolver();
	HttpRequestsToThoth requests = new HttpRequestsToThoth();

	//	Uri uri = Uri.parse(intent.getExtras().getString("uri"));
	//	String [] projection = intent.getStringArrayExtra("projection");
	//	String selection = intent.getStringExtra("selection");
	//	String [] selectionArgs = intent.getStringArrayExtra("selectionArgs");
	//	String sortOrder = intent.getStringExtra("sortOrder");
	//	ContentValues values = (ContentValues) intent.getBundleExtra("values").get("values");
	//	String where = intent.getStringExtra("where");
	//	cr.update(uri, values, where, selectionArgs);



	public NewsService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Context context = this.getApplicationContext();
		String action = intent.getAction();		
		if(action.equals(Intent.ACTION_EDIT)){
			//From User Interaction - Do http request to thoth if a new class is selected

			// service starts from mainActivity to update the showNews from the news user saw
			if(intent.getExtras().get("from") == MainActivity.class) {

				ContentValues newsValues = new ContentValues();
				newsValues.put("showNews", 1);

				int rows = cr.update(
						Uri.parse("content://com.example.newsclassserver/thothNews/" + intent.getExtras().get("newId")), 
						newsValues, 
						null, 
						null);				
			}
			else {
				//service starts from settingsActivity
				if(intent.getExtras().get("from") == SettingsActivity.class) {
					int[] classesId = intent.getIntArrayExtra("classesId");

					for(int id : classesId) {
						Cursor c = cr.query(
								Uri.parse("content://com.example.newsclassserver/thothClasses/"+id),
								new String[]{"showNews"},
								null,
								null,
								null);

						// se 1 vai passar para 0 para nao mostrar as noticias 
						// e vai eliminar do thothNews as noticias dessa turma
						if (c.moveToFirst() && c.getInt(c.getColumnIndex("showNews")) == 1) {

							ContentValues removeShowFromClass = new ContentValues();
							removeShowFromClass.put("showNews", 0);

							cr.update(
									Uri.parse("content://com.example.newsclassserver/thothClasses/"+id), 
									removeShowFromClass, 
									null, 
									null);

							cr.delete(
									Uri.parse("content://com.example.newsclassserver/thothNews"), 
									"_classId = ?", 
									new String[]{""+id});
						}

						// se 0 vai passar para 1 para mostrar as noticias 
						// e vai fazer um request ao thoth para inserir no thothNews as noticias dessa turma
						if (c.moveToFirst() && c.getInt(c.getColumnIndex("showNews")) == 0) {

							ContentValues insertShowFromClass = new ContentValues();
							insertShowFromClass.put("showNews", 1);

							cr.update(
									Uri.parse("content://com.example.newsclassserver/thothClasses/"+id), 
									insertShowFromClass, 
									null, 
									null);

							NewItem[] result = requests.requestNews(id);

							for(int i = 0; i < result.length; i++) {
								insertNewsItem(result[i], id);
							}
						}
					}
				}
			}
		}
		if(action.equals(Intent.ACTION_INSERT_OR_EDIT)){
			//From Broadcast Receiver - Do the http request to thoth;
			//Send a notification warning about new news

			Cursor c = cr.query(
					Uri.parse("content://com.example.newsclassserver/thothClasses"), 
					new String[]{"_classId"}, 
					"showNews = ? ", 
					new String[]{""+1}, 
					null);

			while(c.moveToNext()){
				int classId = c.getInt(c.getColumnIndex("_classId"));
				NewItem[] result = requests.requestNews(classId);
				int countNews = 0;
				for(int i = 0; i < result.length; i++) {

					Cursor checkId = cr.query(
							Uri.parse("content://com.example.newsclassserver/thothNews/" + result[i].id), 
							new String[]{"_newsId"}, 
							null, 
							null, 
							null);

					if(checkId.getCount() == 0) {
						insertNewsItem(result[i], classId);
						countNews++;
					
					}
				}
				
				Notification.Builder builder = new Notification.Builder(context)
						.setContentTitle("New News")
						.setContentText("text: Class" + classId + ": " + countNews + "News added")
						.setAutoCancel(true)
						.setSmallIcon(R.drawable.ic_launcher)
						.setOngoing(true)
						.setContent(new RemoteViews("com.example.newsclass",R.layout.layout_notification_br));
				
				Intent i = new Intent(context,MainActivity.class);
				PendingIntent pintent = PendingIntent.getActivity(context, 1, i, 0);
				builder.setContentIntent(pintent);
				NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
				manager.notify(0, builder.build());
			}
		}
	}

	public void insertNewsItem(NewItem item, int classId) {
		ContentValues values = new ContentValues();
		values.put("_newsId", item.id);
		values.put("_classId", classId);
		values.put("title", item.title);
		values.put("when", item.when.toString());
		values.put("content", item.content);
		values.put("isViewed", 0);
		cr.insert(Uri.parse("content://com.example.newsclassserver/thothNews"), values);
	}
}
