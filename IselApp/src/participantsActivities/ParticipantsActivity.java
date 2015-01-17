package participantsActivities;

import handlers.ImageHandler;
import handlers.ImageHandlerThread;
import handlers.SetViewHandler;

import java.io.IOException;
import java.util.List;

import listModels.ParticipantListModel;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
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
	private int tabIdx;
	private int classId;
	private SharedPreferences _pref;
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
		_pref = getSharedPreferences("classId",0);
		classId = _pref.getInt("id",0);
		_pref.edit().putInt("id", 0).commit();
		if(classId == 0)
			classId = getIntent().getIntExtra("classId", 0);
		final ViewPager pager = new ViewPager(this);	
		pager.setId(R.id.viewPager);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				tabIdx = tab.getPosition();
				if(_model == null){
					
					ParticipantsAsyncTask n = new ParticipantsAsyncTask(classId){

						@Override
						protected void onPostExecute(List<ParticipantItem[]> result) {
							if (result != null) {
								_model = new ParticipantListModel(result, _ih);
								pager.setCurrentItem(tabIdx);

								FragmentManager fm = getSupportFragmentManager();
								ParticipantItemListFragment f;
								if(fm.findFragmentById(R.id.participant_list_fragmentPlaceholder) == null){
									f = ParticipantItemListFragment.newInstance(_model,tabIdx);			
									fm.beginTransaction()
									.add(R.id.participant_list_fragmentPlaceholder, f)
									.commit();
								}else{
									f = ParticipantItemListFragment.newInstance(_model,tabIdx);
									fm.beginTransaction().replace(R.id.participant_list_fragmentPlaceholder, f).commit();
								}
							}	

						}			
					};
					n.execute();
				}else{
					pager.setCurrentItem(tabIdx);

					FragmentManager fm = getSupportFragmentManager();
					ParticipantItemListFragment f;
					if(fm.findFragmentById(R.id.participant_list_fragmentPlaceholder) == null){
						f = ParticipantItemListFragment.newInstance(_model,tabIdx);			
						fm.beginTransaction()
						.add(R.id.participant_list_fragmentPlaceholder, f)
						.commit();
					}else{
						f = ParticipantItemListFragment.newInstance(_model,tabIdx);
						fm.beginTransaction().replace(R.id.participant_list_fragmentPlaceholder, f).commit();
					}
				}
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}
		};

		pager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						getActionBar().setSelectedNavigationItem(position);
					}
				});

		actionBar.addTab(actionBar.newTab().setText("Teachers")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setText("Students")
				.setTabListener(tabListener));
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
			ParticipantItemFragment newFrag = ParticipantItemFragment.newInstance(_model.getItem(tabIdx,position),_ih);
			fm.beginTransaction().replace(R.id.participant_Detail_fragmentPlaceholder, newFrag).commit();
		} else {
			_pref.edit().putInt("id", classId).commit();
			Intent i = new Intent(this, ParticipantItemActivity.class);
			i.putExtra("participantlistmodel", _model);
			i.putExtra("position", position);
			i.putExtra("participantType", tabIdx);
			startActivity(i);
		}
	}
}


