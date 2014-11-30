package com.example.newsclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class NewsCustomAdapter extends BaseAdapter implements OnItemClickListener{

	private LayoutInflater _layoutInflater;
	private NewItem[] news;
	private Context _context;
	private int[] count;

	public NewsCustomAdapter(Context context, NewItem[] news) {
		_context = context;
		_layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.news = news;
		count = new int[news.length];
	}
	//
	//	@Override
	//	public Object getChild(int parentPosition, int childPosititon) {
	//		return news[parentPosition];
	//	}
	//
	//	@Override
	//	public long getChildId(int parentPosition, int childPosition) {
	//		return childPosition;
	//	}
	//
	//	@Override
	//	public View getChildView(int parentPosition, final int childPosition,
	//			boolean isLastChild, View convertView, ViewGroup parent) {
	//
	//		final NewItem item = (NewItem) getChild(parentPosition, childPosition);
	//
	//		if (convertView == null) {
	//			convertView = _layoutInflater.inflate(R.layout.item2_explist_layout, null);
	//			convertView.setTag(new ViewModelChild(convertView));
	//		}
	//
	//		bindModelChild(item, convertView.getTag());
	//		return convertView;
	//	}
	//
	//	@Override
	//	public int getChildrenCount(int parentPosition) {
	//		return 1;
	//	}
	//
	//	@Override
	//	public Object getGroup(int parentPosition) {
	//		return news[parentPosition];
	//	}
	//
	//	@Override
	//	public int getGroupCount() {
	//		return news.length;
	//	}
	//
	//	@Override
	//	public long getGroupId(int parentPosition) {
	//		return parentPosition;
	//	}
	//
	//	@Override
	//	public View getGroupView(int parentPosition, boolean isExpanded,
	//			View convertView, ViewGroup parent) {
	//
	//		final NewItem item = (NewItem) getGroup(parentPosition);
	//
	//		if (convertView == null) {
	//			convertView = _layoutInflater.inflate(R.layout.item1_explist_layout, null);
	//			convertView.setTag(new ViewModelParent(convertView));
	//		}
	//
	//		bindModelParent(item, convertView.getTag());
	//
	//		return convertView;
	//	}
	//
	//	@Override
	//	public boolean hasStableIds() {
	//		return false;
	//	}
	//
	//	@Override
	//	public boolean isChildSelectable(int groupPosition, int childPosition) {
	//		return false;
	//	}

	private void bindModelParent(NewItem newItem, Object viewModelObject){
		ViewModelParent viewModel = (ViewModelParent) viewModelObject;
		viewModel._classFullname.setText(newItem.classFullname);
		viewModel._title.setText(newItem.title);
		viewModel._date.setText(newItem.when.toString());
		if(!newItem.isViewed) {
			viewModel._classFullname.setTextColor(Color.BLUE);
			viewModel._title.setTextColor(Color.BLUE);
		}else{
			viewModel._classFullname.setTextColor(Color.BLACK);
			viewModel._title.setTextColor(Color.BLACK);
		}
	}

//	private void bindModelChild(NewItem newItem, Object viewModelObject) {
//		ViewModelChild viewModel = (ViewModelChild) viewModelObject;
//		viewModel._content.setText(newItem.content);
//	}

//
//	@Override
//	public boolean onGroupClick(ExpandableListView parent, View v,
//			int groupPosition, long id) {
//		NewItem item = (NewItem) getItem(groupPosition);
//		if(item.isViewed)
//			return false;
//		if(count[groupPosition] == 1){
//
//			Intent service = new Intent(_context, NewsService.class);
//			service.putExtra("newId", item.newsId);
//			service.setAction("userUpdateNews");
//			_context.startService(service);
//			count[groupPosition]++;
//		}else if (count[groupPosition] == 0)
//			count[groupPosition]++;
//		return false;
//	}
//
//	public Set<Integer> getSetListViewedNewsIds() {
//		return viewedNewsIds;
//	}



	@Override
	public int getCount() {
		return news.length;
	}

	@Override
	public Object getItem(int id) {
		return news[id];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		final NewItem item = (NewItem) getItem(pos);

		if (view == null) {
			view = _layoutInflater.inflate(R.layout.news_item_list_layout, null);
			view.setTag(new ViewModelParent(view));
		}

		bindModelParent(item, view.getTag());

		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int groupPosition, long id) {
		
	}
}
