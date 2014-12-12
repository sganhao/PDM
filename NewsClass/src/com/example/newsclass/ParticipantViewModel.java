package com.example.newsclass;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantViewModel {
	
	public TextView number;
	public TextView fullname;
	public TextView email;
	public TextView isTeacher;
	public ImageView image;
	
	public ParticipantViewModel(View view) {
		number = (TextView) view.findViewById(R.id.item_number_Participant);
		fullname = (TextView) view.findViewById(R.id.item_fullname_Participant);
		email = (TextView) view.findViewById(R.id.item_email_Participant);
		isTeacher = (TextView) view.findViewById(R.id.item_isTeacher_Participant);
		image = (ImageView) view.findViewById(R.id.item_avatar_Participant);
	}	
}
