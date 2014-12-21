package com.example.newsclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantItemFragment extends Fragment{

	private static final String TAG = "News";
	private View _view;
	private Participant _item;
	private ImageHandler _ih;

	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance){
		Log.d(TAG , "ParticipantItemFragment.onCreateView");
		_view = inflater.inflate(R.layout.participant_item_fragment_layout, parent, false);
		Bundle b = getArguments();
		_item = (Participant) b.getSerializable("item");
		_ih = (ImageHandler) b.getSerializable("handler");
		bindModel();
		return _view;		
	}
	
	public static ParticipantItemFragment newInstance(Participant model, ImageHandler ih){
		ParticipantItemFragment f = new ParticipantItemFragment();
		Bundle b = new Bundle();
		b.putSerializable("item", model);
		b.putSerializable("handler", ih);
		f.setArguments(b);
		Log.d(TAG, "ParticipantItemFragment.newInstance "+model);
		return f;
	}
	
	private void bindModel(){
		TextView number = (TextView) _view.findViewById(R.id.item_number_Participant);
		number.setText("Number: " + _item.number);
		
		TextView fullname = (TextView) _view.findViewById(R.id.item_fullname_Participant);
		fullname.setText("Name: " +_item.fullName);
		
		TextView email = (TextView) _view.findViewById(R.id.item_email_Participant);
		email.setText("E-mail: "+  _item.email);
		
		TextView participant = (TextView) _view.findViewById(R.id.item_isTeacher_Participant);
		participant.setText("Participation: " + (_item.isTeacher ? "Teacher" : "Student"));
		
		ImageView im = (ImageView) _view.findViewById(R.id.item_avatar_Participant);
		_ih.fetchImage(im, _item.avatarUri,_item.number);
	}
}
