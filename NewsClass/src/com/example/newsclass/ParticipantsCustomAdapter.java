package com.example.newsclass;

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

public class ParticipantsCustomAdapter extends BaseAdapter implements OnItemClickListener, OnScrollListener{

	private String TAG = "News";
	private int _layout;
	private int _count = 0;
	private int _scrollFirst;
	private int _scrollCount;
	private boolean _updating;
	private LayoutInflater _layoutInflater;
	private Context _ctx;
	private ImageHandler _ih;
	private Participant[] _parts;
	
	
	
	public ParticipantsCustomAdapter(Context ctx,
			int layout, Participant[] participants, ImageHandler imageHandler) {
		
		Log.d(TAG  , "ClassesCustomAdapter -> constructor...");
		
		_ctx = ctx;
		_ih = imageHandler;
		_layout = layout;
		_layoutInflater = (LayoutInflater)_ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		_parts = participants;
		_count = _parts.length / 2;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {}

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
		_ih.fetchImage(partViewModel.image, part.avatarUri,part.number);		
	}

	private Object createViewHolderFor(View view) {
		return new ParticipantViewModel(view);
	}
	
	@Override
	public void onScroll(AbsListView arg0, int first, int count, int total) {
		_scrollFirst = first;
		_scrollCount = count;
	}

	@Override
	public void onScrollStateChanged(AbsListView list, int state) {
		if(state != 0) return;		
		if(_scrollFirst+_scrollCount >= _count - 2){			
			if(_updating) return;
			_updating = true;

			new AsyncTask<Void,Void,Void>(){

				@Override
				protected Void doInBackground(Void... arg0) {

					return null;
				}

				@Override
				protected void onPostExecute(Void arg){
					_count += 10;
					ParticipantsCustomAdapter.this.notifyDataSetChanged();
					_updating = false;
				}
			}.execute();
		}		
	}	
}
