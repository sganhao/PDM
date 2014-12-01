package com.example.newsclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class NewsItemListFragment extends ListFragment {

	private static final String TAG = "News";
	private NewsListModel _newsListModel;
	
	@Override
	public void onCreate(Bundle state){
		Log.d(TAG,"NewsItemListFragment - onCreate");
		super.onCreate(state);
		Bundle args = getArguments();
		_newsListModel = (NewsListModel) args.getSerializable("key");
		
		this.setListAdapter(
				new NewsCustomAdapter(getActivity(), _newsListModel.getItems()));	
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Intent i = new Intent(getActivity(), NewsItemActivity.class);
		i.putExtra("newslist", _newsListModel);
		i.putExtra("ix", position);
		startActivity(i);
	}
	
	public static NewsItemListFragment newInstance(NewsListModel model){
		NewsItemListFragment f = new NewsItemListFragment();
		Bundle args = new Bundle();
		args.putSerializable("key", model);
		f.setArguments(args);
		return f;
	}
}
