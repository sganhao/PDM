package com.example.newsclass;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewModel {

	public final TextView fullNameClass;
	public final CheckBox selectionBox;
	
	public ViewModel(View view){
		fullNameClass = (TextView) view.findViewById(R.id.item_fullname);
		selectionBox = (CheckBox) view.findViewById(R.id.item_checkBox);
	}
}
