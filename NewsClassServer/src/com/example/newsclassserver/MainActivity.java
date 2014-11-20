package com.example.newsclassserver;

import com.example.newsclass.NewsService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //Update das noticias das turmas seleccionadas
		Intent service = new Intent(this,NewsService.class);
		service.setAction(Intent.ACTION_INSERT_OR_EDIT);
		this.startService(service);
    }
}
