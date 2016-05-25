package com.example.switchfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class BlankFragment5 extends Fragment {
	Button mButton;
	TextView mTextView;
	int mCount=100;
	public BlankFragment5() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView= inflater.inflate(R.layout.fragment_blank5, container, false);
		mButton=(Button) rootView.findViewById(R.id.button1);
		mTextView=(TextView) rootView.findViewById(R.id.textView1);
		mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mTextView.setText("Fragment1的计数为：" + mCount++);
			}
		});
		mTextView.setText("Fragment1的计数为：" + mCount++);
		return rootView;
	}
}
