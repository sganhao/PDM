package com.example.newsclass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsItemFragment extends Fragment{

	private static String TAG = "News";
	private View _view;
	private NewItem _item;

	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance){
		Log.d(TAG , "ItemFragment.onCreateView");
		_view = inflater.inflate(R.layout.news_item_fragment_layout, parent, false);
		Bundle b = getArguments();
		_item = (NewItem) b.getSerializable("item");
		bindModel();
		return _view;		
	}
	
	public static NewsItemFragment newInstance(NewItem model){
		NewsItemFragment f = new NewsItemFragment();
		Bundle b = new Bundle();
		b.putSerializable("item", model);
		f.setArguments(b);
		Log.d(TAG, "ItemFragment.newInstance "+model);
		return f;
	}
	
	private void bindModel(){
		TextView className = (TextView) _view.findViewById(R.id.item_classFullname_NewItem);
		className.setText("" + _item.classFullname);
		
		TextView title = (TextView) _view.findViewById(R.id.item_title_NewItem);
		title.setText(_item.title);
		
		TextView date = (TextView) _view.findViewById(R.id.item_date_NewItem);
		date.setText(_item.when.toString());
		
		TextView content = (TextView) _view.findViewById(R.id.item_content_NewItem);
		content.setText("\n" + _item.content);
	}

}
