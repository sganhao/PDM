package newsActivities;

import listModels.NewsListModel;
import services.IselAppService;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.example.iselapp.R;

import fragments.NewsItemFragment;

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
}
