package com.example.newsclassserver;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class NewsClassContentProvider extends ContentProvider {	

	private static final String AUTHORITY = NewsClassContentProviderContract.Authority;
	
	public static final String TAG = "News";
	
	private NewsClassOpenHelper _ds;
	
	private static final int ROOT_MATCH = 0;
	private static final int THOTHCLASSES_COLL_MATCH = 1;
	private static final int THOTHCLASSES_ITEM_MATCH = 2;
	private static final int THOTHNEWS_COLL_MATCH = 3;
	private static final int THOTHNEWS_ITEM_MATCH = 4;
	

	private static void d(String fmt, Object... args) {
		Log.d(TAG, String.format(fmt, args));
	}
	
	@Override
	public boolean onCreate() {
		Log.d(TAG, "onCreate newsClassContentProvider");
		_ds = new NewsClassOpenHelper(getContext());
		return true;
	}
	
	private static UriMatcher _matcher;
	
	static {
		_matcher = new UriMatcher(ROOT_MATCH);
		_matcher.addURI(AUTHORITY, "thothClasses", THOTHCLASSES_COLL_MATCH);
		_matcher.addURI(AUTHORITY, "thothClasses/#", THOTHCLASSES_ITEM_MATCH);
		_matcher.addURI(AUTHORITY, "thothNews", THOTHNEWS_COLL_MATCH);
		_matcher.addURI(AUTHORITY, "thothNews/#", THOTHNEWS_ITEM_MATCH);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	
		SQLiteDatabase db = _ds.getReadableDatabase();
		Cursor c = null;
		Context ctx = getContext();
		d(ctx.getPackageName());
		
		switch (_matcher.match(uri)) {
			case ROOT_MATCH:
			case THOTHCLASSES_COLL_MATCH:
				c = db.query("thothClasses", projection, selection, selectionArgs, null, null, sortOrder);
				break;
				
			case THOTHCLASSES_ITEM_MATCH:
				long idClass = ContentUris.parseId(uri);
				c = db.query("thothClasses", projection, "_classId = ?", new String[]{Long.toString(idClass)}, null, null, null);
				break;
				
			case THOTHNEWS_COLL_MATCH:
				c = db.query("thothNews", projection, selection, selectionArgs, null, null, sortOrder);
				break;
				
			case THOTHNEWS_ITEM_MATCH:
				long idNews = ContentUris.parseId(uri);
				c = db.query("thothNews", projection, "_newsId = ?", new String[]{Long.toString(idNews)}, null, null, null);
				break;
				
			default:
				return null;				
		}

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = _ds.getWritableDatabase();
		int rawsDeleted = 0;
		
		switch (_matcher.match(uri)) {
			case THOTHCLASSES_COLL_MATCH:
				rawsDeleted = db.delete("thothClasses", selection, selectionArgs);
				break;
			case THOTHNEWS_COLL_MATCH:
				rawsDeleted = db.delete("thothNews", selection, selectionArgs);
				break;
			default:
				return rawsDeleted;			
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rawsDeleted;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = _ds.getWritableDatabase();
		
		switch (_matcher.match(uri)) {
			case THOTHCLASSES_COLL_MATCH:
				db.insert("thothClasses", null, values);
				break;
			case THOTHNEWS_COLL_MATCH:
				Log.d(TAG,"Insert New");
				db.insert("thothNews", null, values);
				Log.d(TAG,"New inserted completed");
				break;
			default:
				return null;
			
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		SQLiteDatabase db = _ds.getWritableDatabase();
		int rawsThothClassesUpdated = 0, rawsThothNewsUpdated = 0;
		long id = 0;
		
		// se faz check, actualiza thothClasses e insere as noticias dessa turma na thothNews
		// se faz uncheck, actualiza thothclasses, removendo as noticias dessa turma da thothNews
		switch (_matcher.match(uri)) {
			case THOTHCLASSES_ITEM_MATCH:
				id = ContentUris.parseId(uri);
				
				rawsThothClassesUpdated = db.update("thothClasses", values, "_classId = ? ", new String[]{""+id});				
				break;
				
			case THOTHNEWS_ITEM_MATCH:
				id = ContentUris.parseId(uri);
				rawsThothClassesUpdated = db.update("thothNews", values, "_newsId = ? ", new String[]{""+id});
				break;
			default:
				return rawsThothClassesUpdated;
			
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rawsThothClassesUpdated + rawsThothNewsUpdated;
	}
}
