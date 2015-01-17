package listModels;

import handlers.ImageHandler;

import java.io.Serializable;
import java.util.List;

import entities.ParticipantItem;

public class ParticipantListModel implements Serializable {
	
	private List<ParticipantItem[]> _participants;
	private ImageHandler _ih;
	
	public ParticipantListModel(List<ParticipantItem[]> participants, ImageHandler ih){
		_participants = participants;
		_ih = ih;
	}
	
	public ParticipantItem getItem(int i, int pos){
		return _participants.get(i)[pos];
	}
	
	public ParticipantItem[] getItems(int i){
		return _participants.get(i);
	}
	
	public ImageHandler getImageHandler() {
		return _ih;
	}
}
