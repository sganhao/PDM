package com.example.newsclass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class NewsService extends IntentService  {
ContentResolver cr = getContentResolver();

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
		
		String action = intent.getAction();		
		if(action.equals(Intent.ACTION_EDIT)){
			//From User Interaction - Do http request to thoth if a new class is selected
			
			int rows = cr.update(
					intent.getData(), 
					(ContentValues)intent.getExtras().get("values"), 
					intent.getStringExtra("where"), 
					intent.getStringArrayExtra("selectionArgs")
					);

		}
		if(action.equals(Intent.ACTION_INSERT_OR_EDIT)){
			//From Broadcast Receiver - Do the http request to thoth;
			//cr.insert(intent.getData(), values);
			//Send a notification warning about new news
		}
	}

}
