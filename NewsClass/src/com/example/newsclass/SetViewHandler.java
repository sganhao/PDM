package com.example.newsclass;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class SetViewHandler extends Handler {

	private String TAG = "News";

	public SetViewHandler(Looper mainLooper) {
		super(mainLooper);
	}

	public void handleMessage (Message msg){
		Log.d(TAG , "SetViewHandler -> Processing publish message");
		Data data = (Data)msg.obj;
		data.im.setImageBitmap(data.bm);
	}
	
	public void publishImage(ImageView im, Bitmap bm){
		Message m = obtainMessage();
		m.obj = new Data(im,bm);
		Log.d(TAG, "SetViewHandler -> Sending publish message");
		sendMessage(m);
	}
	
	public final class Data {
		public final ImageView im;
		public final Bitmap bm;
		public Data(ImageView im, Bitmap bm) {
			this.im = im;
			this.bm = bm;
		}
	}
}
