package com.example.newsclass;

import java.io.Serializable;

public class ParticipantListModel implements Serializable{
	
	private Participant[] _participants;
	private ImageHandler _ih;
	
	public ParticipantListModel(Participant[] participants, ImageHandler ih){
		_participants = participants;
		_ih = ih;
	}
	
	public Participant getItem(int pos){
		return _participants[pos];
	}
	
	public Participant[] getItems(){
		return _participants;
	}
	
	public ImageHandler getImageHandler() {
		return _ih;
	}
}
