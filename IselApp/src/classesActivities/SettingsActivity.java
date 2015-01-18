package classesActivities;

import java.util.Set;

import services.IselAppService;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.iselapp.R;

import customAdapters.ClassesCursorAdapter;

public class SettingsActivity extends Activity implements LoaderCallbacks<Cursor>{

	private final String TAG = "IselApp";
	private static final String AUTHORITY = "com.example.iselappserver";
	private static final String ACCOUNT_TYPE = "iselapp.com";
	private static final String ACCOUNT = "dummy_account";
	private ListView _listView;
	private ClassesCursorAdapter _adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "SettingsActivity - onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);

		final Button btn = (Button) findViewById(R.id.settings_button);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent service = new Intent(getApplicationContext(), IselAppService.class);
				service.setAction("userUpdateClasses");

				service.putExtra("classesIdsToAdd",listIdsToArray(_adapter.getClassesIdsToAdd()));
				service.putExtra("classesIdsToRemove",listIdsToArray(_adapter.getClassesIdsToRemove()));

				getApplicationContext().startService(service);

				SettingsActivity.this.finish();
			}

			private int[] listIdsToArray(Set<Integer> setListIds) {
				int[] ids = new int[setListIds.size()];
				int i = 0;
				for(int id : setListIds) {
					ids[i] = id;
					i++;
				}
				return ids;				
			}
		}); 

		_listView = (ListView) findViewById(R.id.settings_listView);
		_listView.addFooterView(new ProgressBar(this));		

		getLoaderManager().initLoader(1, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Log.d(TAG, "SettingsActivity - onCreateLoader");
		return new CursorLoader(this, 
				Uri.parse("content://com.example.iselappserver/classes"), 
				new String[]{"_classId as _id","_classFullname","_classShowNews"}, 
				null, 
				null, 
				"_classId DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG , " SettingsActivity - onLoadFinish");
		if(data == null || data.getCount() == 0){
			Log.d(TAG, "onCreate firstFillOfCP");
			//Preencher pela primeira vez o content provider
			CreateSyncAccount(this);
		}else{
			_adapter = new ClassesCursorAdapter(SettingsActivity.this, data, 0); 
			_listView.setAdapter(_adapter);
			_listView.setOnScrollListener(_adapter);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {}

	public static void CreateSyncAccount(Context ctx){

		final Account account = new Account(ACCOUNT, ACCOUNT_TYPE);
		AccountManager am = (AccountManager) ctx.getSystemService(Activity.ACCOUNT_SERVICE);
		am.addAccountExplicitly(account, null, null);
		TriggerRefresh(account);
	}

	private static void TriggerRefresh(Account account) {
		Bundle b = new Bundle();

		b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		ContentResolver.requestSync(account,AUTHORITY,b);
	}
}
