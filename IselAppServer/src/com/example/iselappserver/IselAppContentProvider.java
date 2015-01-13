package com.example.iselappserver;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class IselAppContentProvider extends ContentProvider{

	private static final String AUTHORITY = IselAppContentProviderContract.Authority;

	public static final String TAG = "IselApp";

	private IselAppOpenHelper _ds;

	private static final int ROOT_MATCH = 0;
	private static final int CLASSES_COLL_MATCH = 1;
	private static final int CLASSES_ITEM_MATCH = 2;
	private static final int NEWS_COLL_MATCH = 3;
	private static final int NEWS_ITEM_MATCH = 4;
	private static final int WORKITEMS_COLL_MATCH = 5;
	private static final int WORKITEMS_ITEM_MATCH = 6;	

	@Override
	public boolean onCreate() {
		_ds = new IselAppOpenHelper(getContext());
		return true;
	}

	private static UriMatcher _matcher;

	static {
		_matcher = new UriMatcher(ROOT_MATCH);
		_matcher.addURI(AUTHORITY, "classes", CLASSES_COLL_MATCH);
		_matcher.addURI(AUTHORITY, "classes/#", CLASSES_ITEM_MATCH);
		_matcher.addURI(AUTHORITY, "news", NEWS_COLL_MATCH);
		_matcher.addURI(AUTHORITY, "news/#", NEWS_ITEM_MATCH);
		_matcher.addURI(AUTHORITY, "workItems", WORKITEMS_COLL_MATCH);
		_matcher.addURI(AUTHORITY, "workItems/#", WORKITEMS_ITEM_MATCH);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteDatabase db = _ds.getReadableDatabase();
		Cursor c = null;
		Context ctx = getContext();
		Log.d(TAG, ctx.getPackageName());

		switch (_matcher.match(uri)) {
		case ROOT_MATCH:
		case CLASSES_COLL_MATCH:
			c = db.query("classes", projection, selection, selectionArgs, null, null, sortOrder);
			break;

		case CLASSES_ITEM_MATCH:
			long idClass = ContentUris.parseId(uri);
			c = db.query("classes", projection, "_classId = ?", new String[]{Long.toString(idClass)}, null, null, null);
			break;

		case NEWS_COLL_MATCH:
			c = db.query("news", projection, selection, selectionArgs, null, null, sortOrder);
			break;

		case NEWS_ITEM_MATCH:
			long idNews = ContentUris.parseId(uri);
			c = db.query("news", projection, "_newsId = ?", new String[]{Long.toString(idNews)}, null, null, null);
			break;

		case WORKITEMS_COLL_MATCH:
			c = db.query("workItems", projection, selection, selectionArgs, null, null, sortOrder);
			break;

		case WORKITEMS_ITEM_MATCH:
			long idWorkItems = ContentUris.parseId(uri);
			c = db.query("workItems", projection, "_workItemId = ?", new String[]{Long.toString(idWorkItems)}, null, null, null);
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
		case CLASSES_COLL_MATCH:
			rawsDeleted = db.delete("classes", selection, selectionArgs);
			break;
		case NEWS_COLL_MATCH:
			rawsDeleted = db.delete("news", selection, selectionArgs);
			break;
		case WORKITEMS_COLL_MATCH:
			rawsDeleted = db.delete("workItems", selection, selectionArgs);
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
		case CLASSES_COLL_MATCH:
			db.insert("classes", null, values);
			break;
		case NEWS_COLL_MATCH:
			db.insert("news", null, values);
			break;
		case WORKITEMS_COLL_MATCH:
			db.insert("workItems", null, values);
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
		int rawsUpdated = 0;
		long id = 0;

		switch (_matcher.match(uri)) {
		case CLASSES_COLL_MATCH:
			rawsUpdated = db.update("classes", values, selection, selectionArgs);
			break;
		case CLASSES_ITEM_MATCH:
			id = ContentUris.parseId(uri);
			rawsUpdated = db.update("classes", values, "_classId = ? ", new String[]{""+id});				
			break;
		case NEWS_COLL_MATCH:
			rawsUpdated = db.update("news", values, selection, selectionArgs);
			break;
		case NEWS_ITEM_MATCH:
			id = ContentUris.parseId(uri);
			rawsUpdated = db.update("news", values, "_newsId = ? ", new String[]{""+id});
			break;
		case WORKITEMS_COLL_MATCH:
			rawsUpdated = db.update("workItems", values, selection, selectionArgs);
			break;
		default:
			return rawsUpdated;			
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rawsUpdated;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues [] values){
		//ToDo - Inserir várias linhas duma so vez
		Log.d(TAG, "bulkInsert :P");
		int rowsInserted = 0;
		switch (_matcher.match(uri)) {
		case CLASSES_COLL_MATCH:
			rowsInserted = insertValues("classes",values);
			break;
		case NEWS_COLL_MATCH:
			rowsInserted = insertValues("news",values);
			break;
		case WORKITEMS_COLL_MATCH:
			rowsInserted = insertValues("workItems",values);
			break;
		default:
			return 0;			
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsInserted;
	}

	private int insertValues(String table, ContentValues[] values) {
		SQLiteDatabase db = _ds.getWritableDatabase();
		long result = 0;
		int count = 0;
		db.beginTransaction();
		for(int i = 0 ; i < values.length ; i++){
			result = db.insert(table, null, values[i]);
			if(result != 0)
				count++;
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return count;
	}
}
