package com.example.newsclass;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class ParticipantsCustomAdapter extends BaseAdapter implements OnItemClickListener{

	private int _layout;
	private int _count = 0;
	private LayoutInflater _layoutInflater;
	private Context _ctx;
	private ImageHandler _ih;
	private Participant[] _parts;
	private String TAG = "News";
	
	
	public ParticipantsCustomAdapter(Context ctx,
			int layout, Participant[] participants, ImageHandler imageHandler) {
		
		Log.d(TAG  , "ClassesCustomAdapter -> constructor...");
		
		_ctx = ctx;
		_ih = imageHandler;
		_layout = layout;
		_layoutInflater = (LayoutInflater)_ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		_parts = participants;
		_count = 100;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		return _count;
	}

	@Override
	public Object getItem(int idx) {
		return getModel(idx);
	}

	private Participant getModel(int idx) {
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

	private void bindModel(Participant part, Object viewModelObject) {
		ParticipantViewModel partViewModel = (ParticipantViewModel) viewModelObject;
		partViewModel.fullname.setText(part.fullName);
		//partViewModel.email.setText(part.email);
		_ih.fetchImage(partViewModel.image, part.avatarUri);		
	}

	private Object createViewHolderFor(View view) {
		return new ParticipantViewModel(view);
	}
}
