package services;

import entities.ClassItem;
import entities.NewsItem;
import utils.RequestsToThoth;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
			}
		}

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
