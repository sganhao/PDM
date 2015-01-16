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
	private final String AUTHORITY = "com.example.iselapp";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG,intent.getAction());
		if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(ni.isConnected()){
				//Lançar os syncadapters
				Account account = new Account("default_account", AUTHORITY);
		        ContentResolver.requestSync(account, AUTHORITY, Bundle.EMPTY);

			}
		}
	}

}
