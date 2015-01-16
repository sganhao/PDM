package receivers;

import java.util.List;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

public class IselAppReceiver extends BroadcastReceiver {

	private static final long OTHER_UPDATE = 70;
	private final String TAG = "IselApp";
	private final String AUTHORITY = "com.example.iselappserver";
	private static final String ACCOUNT_TYPE = "iselapp.com";
	private static final String ACCOUNT = "dummy_account";
	private final long UPDATE_CLASSES = 120;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG,intent.getAction());
		if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			Account account = new Account(ACCOUNT, ACCOUNT_TYPE);
			if(ni.isConnected()){
				//Lançar os syncadapters
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
				ContentResolver.cancelSync(account, AUTHORITY);
			}
		}
	}

}
