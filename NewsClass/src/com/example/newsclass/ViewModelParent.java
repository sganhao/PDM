package com.example.newsclass;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class ViewModelParent {

	public TextView _title;
	public TextView _date;
	
	public ViewModelParent(View view) {
		_title = (TextView) view.findViewById(R.id.item_title);
		_date = (TextView) view.findViewById(R.id.item_date);
		
		_title.setTypeface(null, Typeface.BOLD_ITALIC);
		_date.setTypeface(null, Typeface.BOLD_ITALIC);
	}
}
