package com.example.newsclass;

import android.os.AsyncTask;

public class ParticipantsAsyncTask extends AsyncTask<Void,Void,Participant[]> {

	private int _classId;
	private HttpRequestsToThoth _requests;

	public ParticipantsAsyncTask(int classId) {
		_classId = classId;
		_requests = new HttpRequestsToThoth();
	}

	@Override
	protected Participant[] doInBackground(Void... arg0) {
		Participant [] participants = _requests.requestParticipants(_classId);
		return participants;
	}

}
