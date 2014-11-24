package com.example.newsclass;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class NewsBoundedService extends Service {

	private ContentResolver _cr;
	private HttpRequestsToThoth _requests;
	private String TAG = "News";

	@Override
	public IBinder onBind(Intent intent) {
		String action = intent.getAction();		

		Log.d(TAG  , "action = " + action);

		if(action.equals("userUpdateClasses")){
			//From User Interaction - Do http request to thoth if a new class is selected

			//service starts from settingsActivity

			int[] classesId = intent.getIntArrayExtra("classesId");

			for(int id : classesId) {
				Cursor c = _cr.query(
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

					NewItem[] result = _requests.requestNews(id);

					for(int i = 0; i < result.length; i++) {
						insertNewsItem(result[i], id);
					}
				}
			}
		} else

			// service starts from mainActivity to update the showNews from the news user saw
			if(action.equals("userUpdateNews")) {

				ContentValues newsValues = new ContentValues();
				newsValues.put("showNews", 1);

				_cr.update(
						Uri.parse("content://com.example.newsclassserver/thothNews/" + intent.getExtras().get("newId")), 
						newsValues, 
						null, 
						null);				
			}
			else 
				if(action.equals("firstFillOfCP")){
					// Insert data in the content provider for the first time
					Clazz[] result = _requests.requestClasses();

					for (int i = 0; i < result.length; i++) {
						insertClassesItem(result[i]);
					}
				}
		return null;
	}

	public void insertNewsItem(NewItem item, int classId) {
		ContentValues values = new ContentValues();
		values.put("_newsId", item.id);
		values.put("_classId", classId);
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
