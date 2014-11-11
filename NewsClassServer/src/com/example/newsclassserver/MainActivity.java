package com.example.newsclassserver;

import java.util.Set;

import com.example.newsclass.Clazz;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Set<String> classesIds;
        
        ThothRequestAsyncTask asyncTask = new ThothRequestAsyncTask() {
        	@Override
			protected void onPostExecute(Clazz[] result) {
        	
        	}
        };
    }
}
