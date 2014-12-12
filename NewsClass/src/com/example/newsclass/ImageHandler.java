package com.example.newsclass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageHandler extends Handler {
	
	private SetViewHandler _h;
	private String TAG = "News";
	
	public ImageHandler(SetViewHandler h, Looper l){
		super(l);
		_h = h;
	}
	
	public void handleMessage (Message msg){
		Data data = (Data)msg.obj; 
		URL url;
		try {
			Log.d(TAG , "Processing fecth message");
			url = new URL(data.uri);		
			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			InputStream s = c.getInputStream();
			Bitmap bm = BitmapFactory.decodeStream(s);
			_h.publishImage(data.im, bm);
		} catch (MalformedURLException e) {
			Log.d(TAG, "Exception: %s" + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Exception: %s" + e.getMessage());
		}
	}
	
	public void fetchImage(ImageView im, String uri){
		Message m = obtainMessage();
		m.obj = new Data(uri,im);
		Log.d(TAG, "Sending fetch message");
		sendMessage(m);
	}
	
	static class Data{
		public final String uri;
		public final ImageView im;
		public Data(String uri, ImageView im){
			this.uri = uri;
			this.im = im;
		}
	}
}
