package fragments;

import com.example.iselapp.R;

import handlers.ImageHandler;
import entities.ParticipantItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantItemFragment extends Fragment {

	private static final String TAG = "IselApp";
	private View _view;
	private ParticipantItem _item;
	private ImageHandler _ih;

	public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstance){
		Log.d(TAG , "ParticipantItemFragment.onCreateView");
		_view = inflater.inflate(R.layout.participant_item_layout, group, false);
		Bundle b = getArguments();
		_item = (ParticipantItem) b.getSerializable("item");
		_ih = (ImageHandler) b.getSerializable("handler");
		bindModel();
		return _view;		
	}
	
	public static ParticipantItemFragment newInstance(ParticipantItem model, ImageHandler ih){
		ParticipantItemFragment f = new ParticipantItemFragment();
		Bundle b = new Bundle();
		b.putSerializable("item", model);
		b.putSerializable("handler", ih);
		f.setArguments(b);
		Log.d(TAG, "ParticipantItemFragment.newInstance "+model);
		return f;
	}
	
	private void bindModel(){
		TextView number = (TextView) _view.findViewById(R.id.participant_item_number);
		number.setText("Number: " + _item.participant_number);
		
		TextView fullname = (TextView) _view.findViewById(R.id.participant_item_fullname);
		fullname.setText("Name: " +_item.participant_fullName);
		
		TextView email = (TextView) _view.findViewById(R.id.participant_item_email);
		email.setText("E-mail: "+  _item.participant_email);
		
		ImageView im = (ImageView) _view.findViewById(R.id.participant_item_avatar);
		_ih.fetchImage(im, _item.participant_avatarUri,_item.participant_number);
	}
}
