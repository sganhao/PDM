package com.example.iselappserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IselAppOpenHelper extends SQLiteOpenHelper {	

	private static final String TAG = "IselApp";

	public IselAppOpenHelper(Context context) {
		super(context, "IselApp.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "IselAppOpenHelper.onCreate");
		db.execSQL("create table classes (" +
				"_classId integer primary key, " +
				"_classFullname text, " +
				"_classShowNews integer)");
		
		db.execSQL("create table news (" +
				"_newsId integer primary key, " +
				"_newsClassId integer," +
				"_newsClassFullname text," + 
				"_newsTitle text, " +
				"_newsWhen text, " +
				"_newsContent text, " +
				"_newsIsViewed integer," +
				"FOREIGN KEY(_newsClassId) REFERENCES classes(_classId)) "
				);
		
		db.execSQL("create table workItems (" +
				"_workItemId integer primary key," +
				"_workItem_classId integer," + 
				"_workItem_classFullname text," +
				"_workItemAcronym text," +
				"_workItemTitle text," +
				"_workItemStartDate text," +
				"_workItemDueDate text," +
				"_workItemEventId integer," +
				"FOREIGN KEY(_workItem_classId) REFERENCES classes(_classId))"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {}	
}

