package services;

import utils.RequestsToThoth;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

public class IselAppService extends IntentService {

	private String TAG = "IselApp";
	private ContentResolver _cr;
	private RequestsToThoth _requests;
	private Context _context;
	private int _idx;
	
	public IselAppService() {
		super("IselAppService");
	}
	
	public IselAppService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		_cr = getContentResolver();
		_context = this.getApplicationContext();
		_requests = new RequestsToThoth();
		_idx = 0;
	}	
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}

}
