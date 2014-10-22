package com.example.newsclass;

import android.view.View;
import android.widget.TextView;

public class ViewModelChild {

	public TextView _content;
	
	public ViewModelChild(View view) {
		_content = (TextView) view.findViewById(R.id.item_content);
	}
}
