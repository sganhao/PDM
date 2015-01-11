package participantsActivities;

import handlers.ImageHandler;
import handlers.ImageHandlerThread;
import handlers.SetViewHandler;

import java.io.IOException;

import listModels.ParticipantListModel;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import asyncTasks.ParticipantsAsyncTask;
import classesActivities.SettingsActivity;

import com.example.iselapp.R;

import entities.ParticipantItem;
import fragments.ParticipantItemFragment;
import fragments.ParticipantItemListFragment;

public class ParticipantsActivity extends FragmentActivity implements ParticipantItemListFragment.Callback {

	private static final String TAG = "IselApp";
	private ParticipantListModel _model;
	private static SetViewHandler _svh = new SetViewHandler(Looper.getMainLooper());
	private static ImageHandlerThread _th = new ImageHandlerThread();
	private static ImageHandler _ih;
//	private ViewPager _viewPager;
//	private ParticipantsPagerAdapter _participantsPageAdapter;

	static {
		_th.start();
		try {
			_ih = new ImageHandler(_svh, _th.getLooper());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		} 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.participant_masterdetail);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//		_viewPager = new ViewPager(this);
//		_viewPager.setId(R.id.viewPager);
//		
//
//		_participantsPageAdapter = new ParticipantsPagerAdapter(getSupportFragmentManager());

		int classId = getIntent().getIntExtra("classId", 0);

		ParticipantsAsyncTask n = new ParticipantsAsyncTask(classId){

			@Override
			protected void onPostExecute(ParticipantItem[] result) {
				if (result != null) {		
					_model = new ParticipantListModel(result, _ih);

					FragmentManager fm = getSupportFragmentManager();
					ParticipantItemListFragment f;
					if(fm.findFragmentById(R.id.participant_list_fragmentPlaceholder) == null){
						f = ParticipantItemListFragment.newInstance(_model);			
						fm.beginTransaction()
						.add(R.id.participant_list_fragmentPlaceholder, f)
						.commit();
					}else{
						f = ParticipantItemListFragment.newInstance(_model);
						fm.beginTransaction().replace(R.id.participant_list_fragmentPlaceholder, f).commit();
					}
				}	
				
//				_viewPager.setAdapter(_participantsPageAdapter);
//				setContentView(_viewPager);
//				
//				for (int j = 0; j < 2; j++) {
//					actionBar.addTab(
//							actionBar.newTab()
//							.setText(_model.getItem(j).participant_isTeacher ? "Teachers" : "Students")
//							.setTabListener(ParticipantsActivity.this));
//				}
			}			
		};
		n.execute();		

//		_viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
//			@Override
//			public void onPageSelected (int position) {
//				actionBar.setSelectedNavigationItem(position);
//			}
//		});


	}

	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(int position) {
		if (findViewById(R.id.participant_Detail_fragmentPlaceholder) != null) {
			FragmentManager fm = getSupportFragmentManager();
			ParticipantItemFragment newFrag = ParticipantItemFragment.newInstance(_model.getItem(position),_ih);
			fm.beginTransaction().replace(R.id.participant_Detail_fragmentPlaceholder, newFrag).commit();
		} else {
			Intent i = new Intent(this, ParticipantItemActivity.class);
			i.putExtra("participantlistmodel", _model);
			i.putExtra("position", position);
			startActivity(i);
		}
	}

//	@Override
//	public void onTabReselected(Tab tab, FragmentTransaction ft) {
//	}
//
//	@Override
//	public void onTabSelected(Tab tab, FragmentTransaction ft) {
//		_viewPager.setCurrentItem(tab.getPosition());
//	}
//
//	@Override
//	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//	}
//
//	class ParticipantsPagerAdapter extends FragmentPagerAdapter {
//
//		public ParticipantsPagerAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int i) {
//			Fragment f = ParticipantItemListFragment.newInstance(_model);
//			return f;
//		}
//
//		@Override
//		public int getCount() {
//			return 2;
//		}
//	}
}


