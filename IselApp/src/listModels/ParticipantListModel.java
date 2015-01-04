package listModels;

import handlers.ImageHandler;

import java.io.Serializable;

import entities.ParticipantItem;

public class ParticipantListModel implements Serializable {
	
	private ParticipantItem[] _participants;
	private ImageHandler _ih;
	
	public ParticipantListModel(ParticipantItem[] participants, ImageHandler ih){
		_participants = participants;
		_ih = ih;
	}
	
	public ParticipantItem getItem(int pos){
		return _participants[pos];
	}
	
	public ParticipantItem[] getItems(){
		return _participants;
	}
	
	public ImageHandler getImageHandler() {
		return _ih;
	}
}
