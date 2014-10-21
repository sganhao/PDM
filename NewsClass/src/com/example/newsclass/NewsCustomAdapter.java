package com.example.newsclass;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class NewsCustomAdapter extends BaseExpandableListAdapter{
	
	private Context _context;
    private NewItem[] news; 
  
    public NewsCustomAdapter(Context context, NewItem[] news) {
        this._context = context;
        this.news = news;
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
 
        final String childText = (String) getChild(parentPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item2_explist_layout, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.item_content);
 
        txtListChild.setText(childText);
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
    	
        String headerTitle = (String) getGroup(parentPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item1_explist_layout, null);
        }
 
        TextView labelParentTitle = (TextView) convertView
                .findViewById(R.id.item_title);
        TextView labelParentDate = (TextView) convertView
                .findViewById(R.id.item_date);
        
        labelParentTitle.setTypeface(null, Typeface.BOLD);
        labelParentTitle.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int parentPosition, int childPosition) {
        return true;
    }
}
