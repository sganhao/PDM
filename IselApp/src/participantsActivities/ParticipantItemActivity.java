package participantsActivities;

import fragments.ParticipantItemFragment;
import handlers.ImageHandler;
import handlers.ImageHandlerThread;
import handlers.SetViewHandler;

import java.io.IOException;

import com.example.iselapp.R;
import com.example.iselapp.R.id;

import listModels.ParticipantListModel;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ParticipantItemActivity extends FragmentActivity{

	private static String TAG = "IselApp";
	private static SetViewHandler _svh = new SetViewHandler(Looper.getMainLooper());
	private static ImageHandlerThread _th = new ImageHandlerThread();
	private static ImageHandler _ih;
	
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
		
		ViewPager pager = new ViewPager(this);	
		pager.setId(R.id.viewPager);
		setContentView(pager);

		Intent i = getIntent();
		final ParticipantListModel model = (ParticipantListModel)i.getExtras().getSerializable("participantlistmodel");
		int position = i.getExtras().getInt("position",0);

		pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()){
			@Override
			public Fragment getItem(int pos) {
				Fragment f = ParticipantItemFragment.newInstance(model.getItem(pos),_ih);
				return f;
			}

			@Override
			public int getCount() {
				return model.getItems().length;
			}			
		});
		pager.setCurrentItem(position);
	}
}
