package participantsActivities;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class MyTabListener implements TabListener {

	private Fragment mFragment;
	private ParticipantsActivity mActivity;
	private String mTag;
	private String mClass;
	
	public MyTabListener(ParticipantsActivity activity, String tag, String clz) {
		mActivity = activity;
		mTag = tag;
		mClass = clz;
	}
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (mFragment == null) {
			mFragment = Fragment.instantiate(mActivity, mClass);
			ft.add(android.R.id.content, mFragment, mTag);
		} else {
			ft.attach(mFragment);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (mFragment != null) {
			ft.detach(mFragment);
		}
	}

}
