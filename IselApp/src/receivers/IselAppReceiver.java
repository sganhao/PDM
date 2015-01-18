package receivers;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

public class IselAppReceiver extends BroadcastReceiver {

	private final String TAG = "IselApp";
	private final long OTHER_UPDATE = 30 * 60;
	private final long UPDATE_CLASSES = 24 * 60 * 60;
	private final String AUTHORITY = "com.example.iselappserver";
	private final String ACCOUNT_TYPE = "iselapp.com";
	private final String ACCOUNT = "dummy_account";	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			Account account = new Account(ACCOUNT, ACCOUNT_TYPE);
			if(ni.isConnected()){
				Log.d(TAG,"Adding Periodic Syncs");
		        ContentResolver.requestSync(account, AUTHORITY, Bundle.EMPTY);
		        Bundle b = new Bundle();
		        b.putBoolean("classes", true);
		        ContentResolver.addPeriodicSync(
						account, 
						AUTHORITY, 
						b, 
						UPDATE_CLASSES);

		        ContentResolver.addPeriodicSync(
						account, 
						AUTHORITY, 
						Bundle.EMPTY, 
						OTHER_UPDATE);
			}else{
				Log.d(TAG,"Removing Periodic Syncs");
				ContentResolver.cancelSync(account, AUTHORITY);
			}
		}
	}
}
