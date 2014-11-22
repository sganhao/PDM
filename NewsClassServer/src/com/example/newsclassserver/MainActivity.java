package com.example.newsclassserver;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    private String TAG = "News";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		Log.d(TAG , "\nonCreate MainActivity newsClassServer");
    }
}
