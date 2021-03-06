package com.example.newsclass;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class ClassesCustomAdapter extends BaseAdapter implements OnScrollListener {

	private String TAG = "News";
	private int _layout;
	private int _count;
	private int _scrollFirst;
	private int _scrollCount;
	private boolean _updating;
	private Clazz [] _classes;	
	private Context _context;
	private LayoutInflater _layoutInflater;
	private Set<Integer> _classesIdsToAdd;
	private Set<Integer> _classesIdsToRemove;


	public ClassesCustomAdapter(Context ctx, int layout, Clazz [] classes){
		Log.d(TAG , "ClassesCustomAdapter -> constructor...");
		_layout = layout;
		_layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_classes = classes;
		_count = 30 ;
		_classesIdsToAdd = new LinkedHashSet<Integer>();
		_classesIdsToRemove = new LinkedHashSet<Integer>();
		_context = ctx;
	}

	@Override
	public int getCount() {
		return _count;
	}

	@Override
	public Object getItem(int idx) {
		return getModel(idx);
	}

	public Clazz getModel(int idx) {
		return _classes[idx];

	}

	@Override
	public long getItemId(int idx) {
		return idx;
	}

	@Override
	public View getView(int idx, View view, ViewGroup parent) {

		final int pos = idx;
		ViewModel model;
		if(view == null){
			view = _layoutInflater.inflate(_layout, null);
			view.setTag(createViewHolderFor(view));
			bindModel(getModel(pos), view.getTag());

			model = (ViewModel) view.getTag();
			model.selectionBox.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					CheckBox cb = (CheckBox) v ;
					Clazz c = (Clazz) cb.getTag();

					c.setShowNews(cb.isChecked());

					if(c.getShowNews()){
						if(_classesIdsToRemove.contains(c.getId()))
							_classesIdsToRemove.remove(c.getId());
						else
							_classesIdsToAdd.add(c.getId());
					}else{
						if(_classesIdsToAdd.contains(c.getId()))
							_classesIdsToAdd.remove(c.getId());
						else
							_classesIdsToRemove.add(c.getId());
					}
				}  
			});   

			model.btnParticipants.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					if(ni.isConnected()){
						Button btn = (Button) v;
						Clazz c = (Clazz) btn.getTag();
						Intent i = new Intent(_context, ParticipantsActivity.class);
						i.putExtra("classId", c.getId());
						_context.startActivity(i);
					}else{
						Toast.makeText(_context, "WIFI CONNECTION REQUIRED", Toast.LENGTH_SHORT).show();
					}
				}  
			});  

		}else{
			bindModel(getModel(pos), view.getTag());
		}
		model = (ViewModel) view.getTag();
		model.selectionBox.setTag(this.getModel(pos));
		model.btnParticipants.setTag(this.getModel(pos));
		return view;
	}

	private void bindModel(Clazz clazz, Object viewModelObject){
		ViewModel viewModel = (ViewModel) viewModelObject;
		viewModel.fullNameClass.setText(clazz.getFullname());
		viewModel.selectionBox.setChecked(clazz.getShowNews());
	}

	private ViewModel createViewHolderFor(View newView) {
		return new ViewModel(newView);
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
					_count += 15;
					ClassesCustomAdapter.this.notifyDataSetChanged();
					_updating = false;
				}
			}.execute();
		}		
	}

	public Set<Integer> getClassesIdsToAdd() {
		return _classesIdsToAdd;
	}
	public Set<Integer> getClassesIdsToRemove() {
		return _classesIdsToRemove;
	}
}
