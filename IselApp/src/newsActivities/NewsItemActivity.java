package newsActivities;

import com.example.iselapp.R;
import com.example.iselapp.R.id;

import services.IselAppService;
import fragments.NewsItemFragment;
import listModels.NewsListModel;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;

public class NewsItemActivity extends FragmentActivity {
	
	private String TAG = "IselApp";
	private Context _context;
	private NewsListModel _model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG ,"NewsItemActivity.oncreate ");
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		ViewPager pager = new ViewPager(this);	
		pager.setId(R.id.viewPager);
		setContentView(pager);
		
		_context = getApplicationContext();

		Intent intent = getIntent();
		_model = (NewsListModel) intent.getExtras().getSerializable("newslistmodel");
		int position = intent.getExtras().getInt("position",0);

		if(!_model.getItem(position).news_isViewed){
			Intent service = new Intent(_context, IselAppService.class);
			service.putExtra("newId", _model.getItem(position).news_id);
			service.setAction("userUpdateNews");
			_context.startService(service);
		}
		

		pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
			@Override
			public Fragment getItem(int pos) {
				Fragment f = NewsItemFragment.newInstance(_model.getItem(pos));
				return f;
			}

			@Override
			public int getCount() {
				return _model.getItems().length;
			}			
		});
		pager.setCurrentItem(position);	
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				if(!_model.getItem(pos).news_isViewed){
					_model.getItem(pos).news_isViewed = true;
					
					Intent service = new Intent(_context, IselAppService.class);
					service.putExtra("newId", _model.getItem(pos).news_id);
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
	/*
	public boolean onOptionsItemSelected(MenuItem item){
		int position = item.getActionView().getId();
		if (findViewById(R.id.mainactivity_detailFragmentPlaceholder) != null) {
			FragmentManager fm = getSupportFragmentManager();
			NewsItemFragment newFrag = NewsItemFragment.newInstance(_model.getItem(position));
			fm.beginTransaction().replace(R.id.mainactivity_detailFragmentPlaceholder, newFrag).commit();
			
			if(!_model.getItem(position).news_isViewed){
				_model.getItem(position).news_isViewed = true;
				Intent service = new Intent(this, IselAppService.class);
				service.putExtra("newId", _model.getItem(position).news_id);
				service.setAction("userUpdateNews");
				startService(service);				
			}

		} else {
			Intent i = new Intent(this, NewsItemActivity.class);
			i.putExtra("newslistmodel", _model);
			i.putExtra("position", position);
			startActivity(i);
		}		
		return true;
	}*/
}
