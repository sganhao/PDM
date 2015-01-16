package com.example.iselapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class IselAppAuthenticatorService extends Service {

	private IselAppAuthenticator _authenticator;

	@Override
	public void onCreate(){
		_authenticator = new IselAppAuthenticator(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return _authenticator.getIBinder();
	}
}
