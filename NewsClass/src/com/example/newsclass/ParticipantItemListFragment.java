package com.example.newsclass;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class ParticipantItemListFragment extends ListFragment {

	private ParticipantListModel _participantListModel;

	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		Bundle args = getArguments();
		_participantListModel = (ParticipantListModel) args.getSerializable("key");

		this.setListAdapter(
				new ParticipantsCustomAdapter(getActivity(), _participantListModel.getItems()));	
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Callback cb = (Callback) getActivity();
		cb.onListItemClick(position);
	}

	public static ParticipantItemListFragment newInstance(ParticipantListModel model){
		ParticipantItemListFragment f = new ParticipantItemListFragment();
		Bundle args = new Bundle();
		args.putSerializable("key", model);
		f.setArguments(args);
		return f;
	}

	public interface Callback{
		void onListItemClick(int position);
	}
}
