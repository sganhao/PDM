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

public class NewsItemActivity extends FragmentActivity{

	private String TAG = "News";
	private Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG ,"NewsItemActivity.oncreate ");
		super.onCreate(savedInstanceState);

		ViewPager pager = new ViewPager(this);	
		pager.setId(R.id.viewPager);
		setContentView(pager);
		
		_context = getApplicationContext();

		Intent intent = getIntent();
		final NewsListModel model = (NewsListModel) intent.getExtras().getSerializable("newslistmodel");
		int position = intent.getExtras().getInt("position",0);

		if(!model.getItem(position).isViewed){
			Intent service = new Intent(_context, NewsService.class);
			service.putExtra("newId", model.getItem(position).newsId);
			service.setAction("userUpdateNews");
			_context.startService(service);
		}
		

		pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
			@Override
			public Fragment getItem(int pos) {
				Fragment f = NewsItemFragment.newInstance(model.getItem(pos));
				return f;
			}

			@Override
			public int getCount() {
				return model.getItems().length;
			}			
		});
		pager.setCurrentItem(position);	
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				if(!model.getItem(pos).isViewed){
					model.getItem(pos).isViewed = true;
					
					Intent service = new Intent(_context, NewsService.class);
					service.putExtra("newId", model.getItem(pos).newsId);
					service.setAction("userUpdateNews");
					_context.startService(service);
				}

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
		});
	}
}
