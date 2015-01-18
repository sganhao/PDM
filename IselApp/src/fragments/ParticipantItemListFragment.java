package fragments;

import listModels.ParticipantListModel;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.example.iselapp.R;

import customAdapters.ParticipantsCustomAdapter;

public class ParticipantItemListFragment extends ListFragment {
	
	private ParticipantListModel _participantListModel;

	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		
		Bundle args = getArguments();
		_participantListModel = (ParticipantListModel) args.getSerializable("key");
		int participantType = args.getInt("participantType");
		this.setListAdapter(
				new ParticipantsCustomAdapter(getActivity(), 
						R.layout.participant_list_layout,
						_participantListModel.getItems(participantType), 
						_participantListModel.getImageHandler()));	
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Callback cb = (Callback) getActivity();
		cb.onListItemClick(position);
	}

	public static ParticipantItemListFragment newInstance(ParticipantListModel model, int participantType){
		ParticipantItemListFragment f = new ParticipantItemListFragment();
		Bundle args = new Bundle();
		args.putSerializable("key", model);
		args.putInt("participantType", participantType);
		f.setArguments(args);
		return f;
	}

	public interface Callback{
		void onListItemClick(int position);
	}
}
