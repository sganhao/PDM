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
	private NewItem[] _news;
	private Context _context;
	private int _layout;

	public NewsCustomAdapter(Context context,int layout, NewItem[] news) {
		_context = context;
		_layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		_news = news;
		_layout = layout;
	}
	
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

	@Override
	public int getCount() {
		return _news.length;
	}

	@Override
	public Object getItem(int id) {
		return _news[id];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		final NewItem item = (NewItem) getItem(pos);

		if (view == null) {
			view = _layoutInflater.inflate(_layout, null);
			view.setTag(new ViewModelParent(view));
		}

		bindModelParent(item, view.getTag());

		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int groupPosition, long id) {
		NewItem item = (NewItem) getItem(groupPosition);
		if(item.isViewed)
			return;
		else{
			item.isViewed = true;
			Intent service = new Intent(_context, NewsService.class);
			service.putExtra("newId", item.newsId);
			service.setAction("userUpdateNews");
			_context.startService(service);
		}
		return;
	}
}
