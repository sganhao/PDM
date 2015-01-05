package participantsActivities;

import fragments.ParticipantItemFragment;
import handlers.ImageHandler;
import handlers.ImageHandlerThread;
import handlers.SetViewHandler;

import java.io.IOException;

import classesActivities.SettingsActivity;

import com.example.iselapp.R;
import com.example.iselapp.R.id;

import listModels.ParticipantListModel;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

public class ParticipantItemActivity extends FragmentActivity{

	private static String TAG = "IselApp";
	private static SetViewHandler _svh = new SetViewHandler(Looper.getMainLooper());
	private static ImageHandlerThread _th = new ImageHandlerThread();
	private static ImageHandler _ih;
	private ParticipantListModel _model;
	
	static {
		_th.start();
		try {
			_ih = new ImageHandler(_svh, _th.getLooper());
		} catch (IOException e) {
			Log.d(TAG,e.toString());
		} 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG ,"ParticipantItemActivity.oncreate ");
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		ViewPager pager = new ViewPager(this);	
		pager.setId(R.id.viewPager);
		setContentView(pager);

		Intent i = getIntent();
		_model = (ParticipantListModel)i.getExtras().getSerializable("participantlistmodel");
		int position = i.getExtras().getInt("position",0);

		pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
			@Override
			public Fragment getItem(int pos) {
				Fragment f = ParticipantItemFragment.newInstance(_model.getItem(pos),_ih);
				return f;
			}

			@Override
			public int getCount() {
				return _model.getItems().length;
			}			
		});
		pager.setCurrentItem(position);
	}
	/*
	public boolean onOptionsItemSelected(MenuItem item){
		int position = item.getItemId();
		if (findViewById(R.id.participant_Detail_fragmentPlaceholder) != null) {
			FragmentManager fm = getSupportFragmentManager();
			ParticipantItemFragment newFrag = ParticipantItemFragment.newInstance(_model.getItem(position),_ih);
			fm.beginTransaction().replace(R.id.participant_Detail_fragmentPlaceholder, newFrag).commit();
			return true;
		} else {
			Intent i = new Intent(this, ParticipantItemActivity.class);
			i.putExtra("participantlistmodel", _model);
			i.putExtra("position", position);
			startActivity(i);
			return true;
		}
	}*/
}
