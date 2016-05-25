package com.example.switchfragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
	
	int mFragmentCurrentIndex=0;
	
	Button mButton1,mButton2,mButton3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initButton();		
	}

	private void initButton() {
		// TODO Auto-generated method stub
		mButton1=(Button) findViewById(R.id.button1);
		mButton2=(Button) findViewById(R.id.button2);
		mButton3=(Button) findViewById(R.id.button3);
			
		View.OnClickListener listener=new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,FragmentActivity.class);
				switch (v.getId()) {
				case R.id.button1:
					intent.putExtra("demoMode", 1);
					break;
				case R.id.button2:
					intent.putExtra("demoMode", 2);
					break;
				case R.id.button3:
					intent.putExtra("demoMode", 3);
					break;				
				}
				startActivity(intent);
			}
		};
		mButton1.setOnClickListener(listener);
		mButton2.setOnClickListener(listener);
		mButton3.setOnClickListener(listener);
	}
}
