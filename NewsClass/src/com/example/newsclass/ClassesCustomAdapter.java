package com.example.newsclass;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class ClassesCustomAdapter extends BaseAdapter implements OnScrollListener {
	
	private int _layout;
	private int _count = 0;
	private LayoutInflater _layoutInflater;
	private boolean _updating;	
	private int _scrollFirst;
	private int _scrollCount;
	private Clazz [] classes;
	private Set<String> classesSelectedIds;
	public SharedPreferences _pref;
	private final String CLASSES = "ids";

	public ClassesCustomAdapter(Context ctx, int layout, Clazz [] classes, SharedPreferences pref){
		_layout = layout;
		_layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.classes = classes;
		_count = 30 ;
		classesSelectedIds = new LinkedHashSet<String>(pref.getStringSet(CLASSES, new LinkedHashSet<String>()));
		_pref = pref;
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
		return classes[idx];
		
	}

	@Override
	public long getItemId(int idx) {
		return idx;
	}

	@Override
	public View getView(int i, View view, ViewGroup parent) {
		
		if(view == null){
			view = _layoutInflater.inflate(_layout, null);
			view.setTag(createViewHolderFor(view));
			bindModel(getModel(i), view.getTag());
			
			((ViewModel)view.getTag()).selectionBox.setOnClickListener(new View.OnClickListener() {
		        
				public void onClick(View v) {
		              CheckBox cb = (CheckBox) v ;
		              Clazz c = (Clazz) cb.getTag();
		              
		              c.setShowNews(cb.isChecked());
		              
		              if(c.getShowNews()){	
		            	  classesSelectedIds.add(Integer.toString(c.getId()));
		              }else{
		            	  classesSelectedIds.remove(Integer.toString(c.getId()));
		              }
		            }  
		          });          
		}else{
			bindModel(getModel(i), view.getTag());
		}
		
		((ViewModel)view.getTag()).selectionBox.setTag(this.getModel(i));
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
	
	public Set<String> getSetListIds() {
		return classesSelectedIds;
	}
}
