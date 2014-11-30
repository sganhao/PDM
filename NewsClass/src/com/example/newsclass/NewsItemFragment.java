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

	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance){
		Log.d(TAG , "ItemFragment.onCreateView");
		View v = inflater.inflate(R.layout.news_item_fragment_layout, parent, false);
		TextView tv = (TextView) v.findViewById(R.id.item_content);
		Bundle b = getArguments();
		NewItem model = (NewItem) b.getSerializable("item");
		tv.setText(model.toString());
		return v;		
	}
	
	public static NewsItemFragment newInstance(NewItem model){
		NewsItemFragment f = new NewsItemFragment();
		Bundle b = new Bundle();
		b.putSerializable("item", model);
		f.setArguments(b);
		Log.d(TAG, "ItemFragment.newInstance "+model);
		return f;
	}

}
