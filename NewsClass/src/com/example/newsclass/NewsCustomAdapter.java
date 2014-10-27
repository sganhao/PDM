package com.example.newsclass;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class NewsCustomAdapter extends BaseExpandableListAdapter implements OnGroupClickListener{
	
	private final String NEWS = "viewedNewsIds";
	private LayoutInflater _layoutInflater;
    private NewItem[] news;
    private Set<String> viewedNewsIds;
    private SharedPreferences _pref;
  
    public NewsCustomAdapter(Context context, NewItem[] news, SharedPreferences pref) {
        _layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.news = news;
        viewedNewsIds = new LinkedHashSet<String>(pref.getStringSet("viewedNewsIds", new LinkedHashSet<String>()));
        _pref = pref;
    }
 
    @Override
    public Object getChild(int parentPosition, int childPosititon) {
        return news[parentPosition];
    }
 
    @Override
    public long getChildId(int parentPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int parentPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final NewItem item = (NewItem) getChild(parentPosition, childPosition);
 
        if (convertView == null) {
            convertView = _layoutInflater.inflate(R.layout.item2_explist_layout, null);
            convertView.setTag(new ViewModelChild(convertView));
        }
 
        bindModelChild(item, convertView.getTag());
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int parentPosition) {
        return 1;
    }
 
    @Override
    public Object getGroup(int parentPosition) {
        return news[parentPosition];
    }
 
    @Override
    public int getGroupCount() {
        return news.length;
    }
 
    @Override
    public long getGroupId(int parentPosition) {
        return parentPosition;
    }
 
    @Override
    public View getGroupView(int parentPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	
    	final NewItem item = (NewItem) getGroup(parentPosition);
    	 
        if (convertView == null) {
        	convertView = _layoutInflater.inflate(R.layout.item1_explist_layout, null);
            convertView.setTag(new ViewModelParent(convertView));
        }
         
        bindModelParent(item, convertView.getTag());
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
    
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
 
   
    
	private void bindModelParent(NewItem newItem, Object viewModelObject){
		ViewModelParent viewModel = (ViewModelParent) viewModelObject;
		viewModel._title.setText(newItem.title);
		viewModel._date.setText(newItem.when.toString());
		if(!newItem.isViewed) {
			viewModel._title.setTextColor(Color.BLUE);
		}else{
			viewModel._title.setTextColor(Color.BLACK);
		}
	}
	
	private void bindModelChild(NewItem newItem, Object viewModelObject) {
		ViewModelChild viewModel = (ViewModelChild) viewModelObject;
		viewModel._content.setText(newItem.content);
	}


	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		
    	NewItem item = (NewItem) getGroup(groupPosition);
    	if(!viewedNewsIds.contains(Integer.toString(item.id))){
    		viewedNewsIds.add(Integer.toString(item.id));
    		_pref.edit().putStringSet(NEWS, new LinkedHashSet<String>(viewedNewsIds)).commit();
    		item.isViewed = true;
    	}
		return false;
	}
	
	public Set<String> getSetListViewedNewsIds() {
		return viewedNewsIds;
	}
}
