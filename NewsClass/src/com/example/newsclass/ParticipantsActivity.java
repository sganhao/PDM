package com.example.newsclass;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class ParticipantsActivity extends FragmentActivity implements ParticipantItemListFragment.Callback {

	private ParticipantListModel _model;
	private static SetViewHandler _svh = new SetViewHandler(Looper.getMainLooper());
	private static ImageHandlerThread _th = new ImageHandlerThread();
	private static ImageHandler _ih;
	
	static {
		_th.start();
		_ih = new ImageHandler(_svh, _th.getLooper()); 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.participant_masterdetail);
		
		int classId = getIntent().getIntExtra("classId", 0);
		
		ParticipantsAsyncTask n = new ParticipantsAsyncTask(classId){

			@Override
			protected void onPostExecute(Participant[] result) {
				//TODO - adpater to show information, implement fragments
				if (result != null) {		
					_model = new ParticipantListModel(result, _ih);
					FragmentManager fm = getSupportFragmentManager();
					ParticipantItemListFragment f;
					if(fm.findFragmentById(R.id.ParticipantFragmentPlaceholder) == null){
						f = ParticipantItemListFragment.newInstance(_model);			
						fm.beginTransaction()
							.add(R.id.ParticipantFragmentPlaceholder, f)
							.commit();
					}else{
						f = ParticipantItemListFragment.newInstance(_model);
						fm.beginTransaction().replace(R.id.ParticipantFragmentPlaceholder, f).commit();
					}
				}
			}
		};
		n.execute();
	}
	
	@Override
	public void onListItemClick(int position) {
		if (findViewById(R.id.ParticipantDetailFragmentPlaceholder) != null) {
			FragmentManager fm = getSupportFragmentManager();
			ParticipantItemFragment newFrag = ParticipantItemFragment.newInstance(_model.getItem(position),_ih);
			fm.beginTransaction().replace(R.id.ParticipantDetailFragmentPlaceholder, newFrag).commit();
		} else {
			Intent i = new Intent(this, ParticipantItemActivity.class);
			i.putExtra("participantlist", _model);
			i.putExtra("ix", position);
			startActivity(i);
		}
		
	}
}
