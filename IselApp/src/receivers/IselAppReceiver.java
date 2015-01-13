package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class IselAppReceiver extends BroadcastReceiver {

	private String TAG = "IselApp";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG,intent.getAction());
		if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(ni.isConnected()){
				//Lançar os syncadapters
			}
		}
	}

}
