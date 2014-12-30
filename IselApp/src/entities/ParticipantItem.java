package entities;

import java.io.Serializable;

public class ParticipantItem implements Serializable{
	int participant_number;
	String participant_fullName;
	String participant_email;
	String participant_avatarUri;
	boolean participant_isTeacher;
	
	public ParticipantItem(int participant_number, String participant_fullName, String participant_email, String participant_avatarUri, boolean participant_isTeacher){
		this.participant_number = participant_number;
		this.participant_fullName = participant_fullName;
		this.participant_email = participant_email;
		this.participant_avatarUri = participant_avatarUri;
		this.participant_isTeacher = participant_isTeacher;
	}
}
