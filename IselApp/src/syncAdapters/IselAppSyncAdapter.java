package syncAdapters;

import com.example.iselapp.R;

import newsActivities.*;
import entities.NewsItem;
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
import android.widget.RemoteViews;

public class IselAppSyncAdapter extends AbstractThreadedSyncAdapter {

	private ContentResolver _cr;
	private RequestsToThoth _requests;
	private Context _context;
	private int _notificationsId;
	
	
	public IselAppSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		_context = context;
		_cr = context.getContentResolver();
		_requests = new RequestsToThoth();
		_notificationsId = 0;
	}

	public IselAppSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        _context = context;
        _cr = context.getContentResolver();
        _requests = new RequestsToThoth();
        _notificationsId = 0;
    }
	
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
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
			int countNews = 0;
			
			for(int i = 0; i < result.length; i++) {

				Cursor checkId = _cr.query(
						Uri.parse("content://com.example.iselappserver/news/" + result[i].news_id), 
						new String[]{"_newsId"}, 
						null, 
						null, 
						null);

				if(checkId.getCount() == 0) {
					insertNewsItem(result[i], classId);
					countNews++;
				}
			}

			launchNotification(className, countNews);;
			
			_notificationsId++;
		}
		
	}
	
	private void launchNotification(String className, int countNews) {
		RemoteViews rv = new RemoteViews("com.example.iselapp",R.layout.notification_layout);
		rv.setTextViewText(R.id.notificationTextView, "Class" + className + ": " + countNews + "News added");

		Notification.Builder builder = new Notification.Builder(_context)
		.setContentTitle("Notification")
		.setAutoCancel(true)
		.setSmallIcon(R.drawable.ic_launcher)
		.setOngoing(true)
		.setContent(rv);

		Intent i = new Intent(_context,MainActivity.class);
		PendingIntent pintent = PendingIntent.getActivity(_context, 1, i, 0);
		builder.setContentIntent(pintent);
		NotificationManager manager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(_notificationsId, builder.build());
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
