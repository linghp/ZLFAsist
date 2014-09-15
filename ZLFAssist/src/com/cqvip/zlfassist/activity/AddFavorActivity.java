package com.cqvip.zlfassist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cqvip.zlfassist.R;

public class AddFavorActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcom);
	}
	public void begin(View v){
		startActivity(new Intent(AddFavorActivity.this,MainActivity.class));
		finish();
	}

	
}
