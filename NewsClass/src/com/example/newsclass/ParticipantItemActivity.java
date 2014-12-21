package com.example.newsclass;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;


public class ParticipantItemActivity extends FragmentActivity{

	private static String TAG = "News";
	private static SetViewHandler _svh = new SetViewHandler(Looper.getMainLooper());
	private static ImageHandlerThread _th = new ImageHandlerThread();
	private static ImageHandler _ih;
	
	static {
		_th.start();
		try {
			_ih = new ImageHandler(_svh, _th.getLooper());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,e.toString());
		} 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewPager pager = new ViewPager(this);	
		pager.setId(R.id.viewPager);
		setContentView(pager);

		Intent i = getIntent();
		final ParticipantListModel participantlist = (ParticipantListModel)i.getExtras().getSerializable("participantlist");
		int ix = i.getExtras().getInt("ix",0);

		Log.d(TAG ,"ParticipantItemActivity.oncreate "+ix);

		pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
			@Override
			public Fragment getItem(int pos) {
				Fragment f = ParticipantItemFragment.newInstance(participantlist.getItem(pos),_ih);
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
