package com.example.newsclass;

import java.io.Serializable;

public class ParticipantListModel implements Serializable{
	
	private Participant[] _participants;
	
	public ParticipantListModel(Participant[] participants){
		_participants = participants;
	}
	
	public Participant getItem(int pos){
		return _participants[pos];
	}
	
	public Participant[] getItems(){
		return _participants;
	}

}
