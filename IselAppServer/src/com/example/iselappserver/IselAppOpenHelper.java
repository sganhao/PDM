package com.example.iselappserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IselAppOpenHelper extends SQLiteOpenHelper {	

	private static final String TAG = "IselApp";

	public IselAppOpenHelper(Context context) {
		super(context, "iselApp.db", null, 1);
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
				"_newsTitle text, " +
				"_newsWhen Date, " +
				"_newsContent text, " +
				"_newsIsViewed integer)");
		
		db.execSQL("create table workItems (" +
				"_workItemId integer primary key," +
				"_workItemAcronym text," +
				"_workItemTitle text," +
				"_workItemReqGroupSubmission integer," +
				"_workItemStarDate Date," +
				"_workItemDueDate Date," +
				"_workItemAcceptsLateSubmission integer," +
				"_workItemAcceptsResubmission integer," +
				"_workItemInfoDocDocId integer" +
				"_workItemInfoDocFilename text," +
				"_workItemAttachmentDocDocId integer," +
				"_workItemAttachmentDocFilename text" +
				"_workItemReportUploadInfoIsRequired integer," +
				"_workItemReportUploadInfoMaxFileSizeInMB integer," +
				"_workItemReportUploadInfoAcceptedExtensions text," +
				"_workItemAttachmentUploadInfoIsRequired integer," +
				"_workItemAttachmentUploadInfoMaxFileSizeInMB integer," +
				"_workItemAttachmentUploadInfoAcceptedExtensions text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {}	
}
