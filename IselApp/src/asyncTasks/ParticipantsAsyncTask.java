package asyncTasks;

import utils.RequestsToThoth;
import entities.ParticipantItem;
import android.os.AsyncTask;

public class ParticipantsAsyncTask extends AsyncTask<Void, Void, ParticipantItem[]>{

	private int _classId;
	private RequestsToThoth _requests;

	public ParticipantsAsyncTask(int classId) {
		_classId = classId;
		_requests = new RequestsToThoth();
	}

	@Override
	protected ParticipantItem[] doInBackground(Void... params) {
		ParticipantItem[] participants = _requests.requestParticipants(_classId);
		return participants;
	}
}
