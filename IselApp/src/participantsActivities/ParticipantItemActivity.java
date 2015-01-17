package participantsActivities;

import fragments.ParticipantItemFragment;
import handlers.ImageHandler;
import handlers.ImageHandlerThread;
import handlers.SetViewHandler;

import java.io.IOException;

import listModels.ParticipantListModel;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import classesActivities.SettingsActivity;

import com.example.iselapp.R;

public class ParticipantItemActivity extends FragmentActivity {

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
		final int participantType = i.getExtras().getInt("participantType");
		int position = i.getExtras().getInt("position",0);

		pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
			@Override
			public Fragment getItem(int pos) {
				Fragment f = ParticipantItemFragment.newInstance(_model.getItem(participantType,pos),_ih);
				return f;
			}

			@Override
			public int getCount() {
				return _model.getItems(participantType).length;
			}			
		});
		pager.setCurrentItem(position);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
