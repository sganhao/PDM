package com.example.newsclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ParticipantItemFragment extends Fragment{

	private static final String TAG = "News";
	private View _view;
	private Participant _item;

	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance){
		Log.d(TAG , "ParticipantItemFragment.onCreateView");
		_view = inflater.inflate(R.layout.news_item_fragment_layout, parent, false);
		Bundle b = getArguments();
		_item = (Participant) b.getSerializable("item");
		bindModel();
		return _view;		
	}
	
	public static ParticipantItemFragment newInstance(Participant model){
		ParticipantItemFragment f = new ParticipantItemFragment();
		Bundle b = new Bundle();
		b.putSerializable("item", model);
		f.setArguments(b);
		Log.d(TAG, "ParticipantItemFragment.newInstance "+model);
		return f;
	}
	
	private void bindModel(){
		TextView number = (TextView) _view.findViewById(R.id.item_classFullname_NewItem);
		number.setText("" + _item.number);
		
		TextView fullname = (TextView) _view.findViewById(R.id.item_title_NewItem);
		fullname.setText(_item.fullName);
		
		TextView email = (TextView) _view.findViewById(R.id.item_date_NewItem);
		email.setText(_item.email);
		
		TextView participant = (TextView) _view.findViewById(R.id.item_content_NewItem);
		participant.setText(_item.isTeacher ? "This Participant is a Teacher" : "This Participant is a Student");
	}

}
