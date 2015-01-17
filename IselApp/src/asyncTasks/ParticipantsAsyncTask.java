package asyncTasks;

import java.util.ArrayList;
import java.util.List;

import utils.RequestsToThoth;
import entities.ParticipantItem;
import android.content.ContentValues;
import android.os.AsyncTask;

public class ParticipantsAsyncTask extends AsyncTask<Void, Void, List<ParticipantItem[]>>{

	private int _classId;
	private RequestsToThoth _requests;
	private List<ParticipantItem[]> listparticipants;

	public ParticipantsAsyncTask(int classId) {
		_classId = classId;
		_requests = new RequestsToThoth();
		listparticipants = new ArrayList<ParticipantItem[]>();
	}

	@Override
	protected List<ParticipantItem[]> doInBackground(Void... params) {
		ParticipantItem[] participants = _requests.requestParticipants(_classId);
		
		separateTeachersFromStudents(participants);
		return listparticipants;
	}

	private void separateTeachersFromStudents(ParticipantItem[] participants) {
		List<ParticipantItem> teachers = new ArrayList<ParticipantItem>();
		List<ParticipantItem> students = new ArrayList<ParticipantItem>();
		for(int i = 0 ; i < participants.length ; i++){
			if(participants[i].participant_isTeacher)
				teachers.add(participants[i]);
			else
				students.add(participants[i]);
		}
		ParticipantItem [] p = new ParticipantItem[teachers.size()];
		teachers.toArray(p);
		listparticipants.add(p);
		p = new ParticipantItem[students.size()];
		students.toArray(p);
		listparticipants.add(p);
	}
}
