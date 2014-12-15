package com.example.newsclass;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantViewModel {
	
	public TextView fullname;

	public ImageView image;
	
	public ParticipantViewModel(View view) {
		fullname = (TextView) view.findViewById(R.id.item_Participant_fullname);
		image = (ImageView) view.findViewById(R.id.item_Participant_avatar);
	}	
}
