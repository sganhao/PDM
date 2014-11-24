package com.example.newsclassserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsClassOpenHelper extends SQLiteOpenHelper {	

	private static final String TAG = "News";

	public NewsClassOpenHelper(Context context) {
		super(context, "thoth.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "NewsClassOpenHelper.onCreate");
		db.execSQL("create table thothClasses (_classId integer primary key, fullname text, showNews integer)");
		db.execSQL("create table thothNews (" +
						"_newsId integer primary key, " +
						"_classId integer, " +
						"title text, " +
						"_when text, " +
						"content text, " +
						"isViewed integer, " +
						"FOREIGN KEY(_classId) REFERENCES thothClasses(_classId)) ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {}	
}
