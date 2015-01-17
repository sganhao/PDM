package customAdapters;

import viewModels.ParticipantViewModel;
import entities.ParticipantItem;
import handlers.ImageHandler;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class ParticipantsCustomAdapter extends BaseAdapter {

	private String TAG = "IselApp";
	private int _layout;
	private LayoutInflater _layoutInflater;
	private Context _ctx;
	private ImageHandler _ih;
	private ParticipantItem[] _parts;
	
	public ParticipantsCustomAdapter(Context ctx,
			int layout, ParticipantItem[] participants, ImageHandler imageHandler) {
		
		Log.d(TAG  , "ClassesCustomAdapter -> constructor...");
		
		_ctx = ctx;
		_ih = imageHandler;
		_layout = layout;
		_layoutInflater = (LayoutInflater)_ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		_parts = participants;
	}
	
	@Override
	public int getCount() {
		return _parts.length;
	}

	@Override
	public Object getItem(int idx) {
		return getModel(idx);
	}

	private ParticipantItem getModel(int idx) {
		return _parts[idx];
	}
	
	@Override
	public long getItemId(int idx) {
		return idx;
	}

	@Override
	public View getView(int i, View view, ViewGroup group) {
		if (view == null) {
			view = _layoutInflater.inflate(_layout, null);
			view.setTag(createViewHolderFor(view));
			bindModel(getModel(i), view.getTag());
		}
		else {
			bindModel(getModel(i), view.getTag());
		}
		
		return view;
	}
	
	private void bindModel(ParticipantItem part, Object viewModelObject) {
		ParticipantViewModel partViewModel = (ParticipantViewModel) viewModelObject;
		partViewModel.participant_fullname.setText(part.participant_fullName);
		_ih.fetchImage(partViewModel.participant_image, part.participant_avatarUri,part.participant_number);		
	}

	private Object createViewHolderFor(View view) {
		return new ParticipantViewModel(view);
	}

}
