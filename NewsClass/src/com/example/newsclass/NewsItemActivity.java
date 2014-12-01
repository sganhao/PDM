package com.example.newsclass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class NewsItemActivity extends FragmentActivity{
	
	private String TAG = "News";
	private Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewPager pager = new ViewPager(this);	
		pager.setId(R.id.viewPager);
		setContentView(pager);
		_context = getApplicationContext();
		
		Intent i = getIntent();
		final NewsListModel newslist = (NewsListModel)i.getExtras().getSerializable("newslist");
		int ix = i.getExtras().getInt("ix",0);
		Log.d(TAG ,"NewsItemActivity.oncreate "+ix);
		
		pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
			@Override
			public Fragment getItem(int pos) {
				Fragment f = NewsItemFragment.newInstance(_context, newslist.getItem(pos));
				return f;
			}

			@Override
			public int getCount() {
				return newslist.getItems().length;
			}			
		});
		pager.setCurrentItem(ix);			
	}
}
