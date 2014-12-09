package com.example.newsclass;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.example.newsclass.R;


public class ParticipantItemActivity extends FragmentActivity{

	private String TAG = "News";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewPager pager = new ViewPager(this);	
		pager.setId(R.id.viewPager);
		setContentView(pager);
		
		Intent i = getIntent();
		final ParticipantListModel participantlist = (ParticipantListModel)i.getExtras().getSerializable("participantlist");
		int ix = i.getExtras().getInt("ix",0);
		
		Log.d(TAG ,"NewsItemActivity.oncreate "+ix);
		
		pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
			@Override
			public Fragment getItem(int pos) {
				Fragment f = ParticipantItemFragment.newInstance(participantlist.getItem(pos));
				return f;
			}

			@Override
			public int getCount() {
				return participantlist.getItems().length;
			}			
		});
		pager.setCurrentItem(ix);
	}
	
}
