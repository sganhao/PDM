package com.example.newsclass;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class NewsCustomAdapter extends BaseExpandableListAdapter implements OnGroupClickListener{
	
	private LayoutInflater _layoutInflater;
    private NewItem[] news;
    private Set<Integer> viewedNewsIds;
    private Context _context;
  
    public NewsCustomAdapter(Context context, NewItem[] news, Set<Integer> viewedIds) {
    	_context = context;
        _layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.news = news;
        viewedNewsIds = new LinkedHashSet<Integer>(viewedIds);
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
//    	if(!viewedNewsIds.contains(item.id)){
//    		viewedNewsIds.add(item.id);
//    		item.isViewed = true;
//    		
//    	}
    	
    	Intent service = new Intent(_context, NewsService.class);
    	service.putExtra("newId", item.id);
    	service.putExtra("from", MainActivity.class);
		service.setAction(Intent.ACTION_EDIT);
		_context.startService(service);
		return false;
	}
	
	public Set<Integer> getSetListViewedNewsIds() {
		return viewedNewsIds;
	}
}
