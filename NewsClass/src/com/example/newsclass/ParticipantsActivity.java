package com.example.newsclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class ParticipantsActivity extends FragmentActivity implements ParticipantItemListFragment.Callback {

	private ParticipantListModel _model;
	
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
					_model = new ParticipantListModel(result);
					FragmentManager fm = getSupportFragmentManager();
					ParticipantItemListFragment f;
					if(fm.findFragmentById(R.id.participantFragmentPlaceholder) == null){
						f = ParticipantItemListFragment.newInstance(_model);			
						fm.beginTransaction()
							.add(R.id.participantFragmentPlaceholder, f)
							.commit();
					}else{
						f = ParticipantItemListFragment.newInstance(_model);
						fm.beginTransaction().replace(R.id.participantFragmentPlaceholder, f).commit();
					}
				}
			}
		};
		n.execute();
	}
	
	@Override
	public void onListItemClick(int position) {
		if (findViewById() != null) {
			FragmentManager fm = getSupportFragmentManager();
			ParticipantItemFragment newFrag = ParticipantItemFragment.newInstance(_model.getItem(position));
			fm.beginTransaction().replace(R.id.ParticipantDetailFragmentPlaceholder, newFrag).commit();
		} else {
			Intent i = new Intent(this, ParticipantItemActivity.class);
			i.putExtra("participantlist", _model);
			i.putExtra("ix", position);
			startActivity(i);
		}
		
	}
}
