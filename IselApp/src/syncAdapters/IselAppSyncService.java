package syncAdapters;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class IselAppSyncService extends Service{

	private static IselAppSyncAdapter sSyncAdapter = null;
	
	private static final Object sSyncAdapterLock = new Object();

	private String TAG = "IselApp";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG , "IselAppSyncService created...");
		synchronized (sSyncAdapterLock) {
			if(sSyncAdapter == null) {
				sSyncAdapter = new IselAppSyncAdapter(getApplicationContext(), true);
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}
}
