package com.example.newsclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageHandler extends Handler implements Serializable{

	private SetViewHandler _h;
	private String TAG = "News";
	private final String _storageDirectoryPath;

	public ImageHandler(SetViewHandler h, Looper l) throws IOException{
		super(l);
		_h = h;
		_storageDirectoryPath = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES)
				+ File.separator
				+ "ImagesCached";
		File outputDir = new File(_storageDirectoryPath);
		outputDir.mkdirs();
	}

	public void handleMessage (Message msg){
		Data data = (Data)msg.obj; 
		URL url;
		try {
			Log.d(TAG , "Processing fecth message");
			if(hasExternalStoragePublicPicture(data.id) && isExternalStorageReadable()){
				File f = createFileInStorageDir(data.id);
				FileInputStream fis = new FileInputStream(f);
				Bitmap bitmap = BitmapFactory.decodeStream(fis);
				_h.publishImage(data.im,bitmap);
				fis.close();
			}else{
				url = new URL(data.uri);
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				InputStream s = c.getInputStream();
				Bitmap bm = BitmapFactory.decodeStream(s);
				cacheImage(data.id,bm);
				_h.publishImage(data.im, bm);
				s.close();
			}
		} catch (MalformedURLException e) {
			Log.d(TAG, "Exception: %s" + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Exception: %s" + e.getMessage());
		}
	}

	private void cacheImage(int id, Bitmap bm) throws IOException {
		if(isExternalStorageWritable()){
			File f = createFileInStorageDir(id);
			FileOutputStream fos = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
			fos.close();
		}
	}

	public void fetchImage(ImageView im, String uri, int id){
		Message m = obtainMessage();
		m.obj = new Data(uri,im, id);
		Log.d(TAG, "Sending fetch message");
		sendMessage(m);
	}

	static class Data{
		public final String uri;
		public final ImageView im;
		public final int id;
		public Data(String uri, ImageView im, int id){
			this.uri = uri;
			this.im = im;
			this.id = id;
		}
	}

	boolean hasExternalStoragePublicPicture(int id) {;
	File file = new File(_storageDirectoryPath, Integer.toString(id) + ".png");
	return file.exists();
	}

	public File createFileInStorageDir(int id) {
		return new File(_storageDirectoryPath, Integer.toString(id) + ".png");

	}

	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

}
