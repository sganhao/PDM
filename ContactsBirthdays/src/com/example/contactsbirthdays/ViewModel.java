package com.example.contactsbirthdays;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewModel {	
	
	public final ImageView photo;
	public final TextView contactName;
	public final TextView contactBirthday;
	
	public ViewModel(View view){
		photo = (ImageView) view.findViewById(R.id.imageView1);
		contactName = (TextView) view.findViewById(R.id.textView1);
		contactBirthday = (TextView) view.findViewById(R.id.textView2);
	}
}
