package com.example.switchfragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FragmentActivity extends Activity {

	//4个用于切换的Fragment
	//Fragment  mFagments[]=new Fragment[4];
	Fragment  mFagments[]=new Fragment[]{
			new BlankFragment1(),
			new BlankFragment2(),
			new BlankFragment3(),
			new BlankFragment4()
	};
	//记录当前的Fragment的索引序号
	int mFragmentCurrentIndex=0;
	//Replace、AddBug、Add哪种方式演示
	int mDemoMode=1;
	
	Button mButton1,mButton2,mButton3,mButton4;
	TextView mTextView;
	String mTitle[]={"replace方式","add无修复模式","add标准模式"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		mDemoMode=getIntent().getIntExtra("demoMode", 1);
	
		initButton();
		mTextView.setText(mTitle[mDemoMode -1 ]);	
		//如果是首次则切换到第0个Fragment，
		//否则（恢复），在onRestoreInstanceState内处理，恢复上次显示的Fragment
		if(savedInstanceState ==null){
			//把4个Fragment都依附到组件上
			// ....
			//切换第1个Fragment
			switchFragment(0);
		}
	}

	private void initButton() {
		// TODO Auto-generated method stub
		mButton1=(Button) findViewById(R.id.button1);
		mButton2=(Button) findViewById(R.id.button2);
		mButton3=(Button) findViewById(R.id.button3);
		mButton4=(Button) findViewById(R.id.button4);
		mTextView=(TextView) findViewById(R.id.textView1);
		
		View.OnClickListener listener=new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.button1:
					switchFragment(0);
					break;
				case R.id.button2:
					switchFragment(1);
					break;
				case R.id.button3:
					switchFragment(2);
					break;
				case R.id.button4:
					switchFragment(3);
					break;	
				}
			}
		};
		mButton1.setOnClickListener(listener);
		mButton2.setOnClickListener(listener);
		mButton3.setOnClickListener(listener);
		mButton4.setOnClickListener(listener);	
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		//核心知识点4
		//如何在屏幕切换和内存不足后，让组件显示原来指定类型或序号的Fragment
		//记录当前显示的序号，在onSaveInstanceState保存，
		// 在onRestoreInstanceState恢复,恢复时切换显示
		
		
		//数据保存时保存当前的Fragment的序号
		outState.putInt("mFragmentCurrentIndex", mFragmentCurrentIndex);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// TODO Auto-generated method stub
		//从恢复中获取保存的当前Fragment的序号
		mFragmentCurrentIndex=savedInstanceState.getInt("mFragmentCurrentIndex");
		
		//高级知识点1
		//从事务中恢复原来的Fragment
		
		//通过TAG从FragmentManager中找到Fragment实例
		if(getFragmentManager().findFragmentByTag("Fragment0") != null){
			mFagments[0]=getFragmentManager().findFragmentByTag("Fragment0");
		}
		if(getFragmentManager().findFragmentByTag("Fragment1") != null){
			mFagments[1]=getFragmentManager().findFragmentByTag("Fragment1");
		}
		if(getFragmentManager().findFragmentByTag("Fragment2") != null){
			mFagments[2]=getFragmentManager().findFragmentByTag("Fragment2");
		}
		if(getFragmentManager().findFragmentByTag("Fragment3") != null){
			mFagments[3]=getFragmentManager().findFragmentByTag("Fragment3");
		}		
		
		switchFragment(mFragmentCurrentIndex);
	}

	private void switchFragment(int index) {
		//如果对应的Fragment没有实例化，先实例化
		if(mFagments[index]==null){
			switch (index) {
			case 0:
				mFagments[0]=new BlankFragment1();
				//mFagments[0].setArguments(args);
				break;
			case 1:
				mFagments[1]=new BlankFragment2();
				//mFagments[1].setArguments(args);
				break;
			case 2:
				mFagments[2]=new BlankFragment3();
				//mFagments[2].setArguments(args);
				break;
			case 3:
				mFagments[3]=new BlankFragment4();
				//mFagments[3].setArguments(args);
				break;
			}
		}		
		
		//开始一个事务
		FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
		
		switch (mDemoMode) {
		case 1:
			//以replace方式依附Fragment
			replaceFragment(fragmentTransaction,index);			
			break;
		case 2:
			//以add方式带bug依附Fragment
			addFragment_bug(fragmentTransaction,index);
			break;
		case 3:
			//以add方式健壮依附Fragment
			addFragment(fragmentTransaction,index);
			break;
		}
		//提交一个事务
		fragmentTransaction.commit();
		//记录当前的Fragment序号
		mFragmentCurrentIndex=index;
	}
	
	private void replaceFragment(FragmentTransaction fragmentTransaction,int index){
		//核心知识点1
		//只管依附即可。
		//不足点：每次切换后，组件R.id.fragment1上依附的Fragment都被清除，
		//  然后把指定的mFagments[index]再依附到组件上；
		
		//通过本例子可以看出，切换时，原mFagments[index]界面的东西依旧保存并重现
		fragmentTransaction.replace(R.id.fragment1,mFagments[index]);
	}
	
	private void addFragment_bug(FragmentTransaction fragmentTransaction,int index){
		//核心知识点2
		//Add=》把Fragment实例依附到组件的顶部；
		//不足：用于依附的组件上，可能同时依附有很多Fragment，
		//  没有被隐藏的Fragment都处于显示状态(标准的Activity则只有一个可以显示)，
		//  因此处于显示的Fragment叠加显示出现重影；
		
		
		// add方式时，如何解决同一个Fragment重复依附的问题：
		// 先判断是否依附，已经依附直接显示，没有依附先依附然后显示 
		
		//如果mFagments[index]已经依附则只显示，否则先以add方式依附然后显示
		//不足： 切换时，Fragment显示会重影。
		if(mFagments[index].isAdded()){
			fragmentTransaction.show(mFagments[index]);
		}else{
			fragmentTransaction.
				add(R.id.fragment1,mFagments[index]).show(mFagments[index]);
		}
	}
	
	private void addFragment(FragmentTransaction fragmentTransaction,int index){
		//核心知识点3
		//add方式，如何解决重影
		//把所有已经依附到组件上的Fragment，先隐藏起来，然后显示需要显示的Fragment
		//  后遗症，屏幕切换和内存不足后重新恢复时，会重新创建Fragment的实例，此时
		//  新的实例跟原来的实例不是同一个实例，因此单纯下面处理还会重印（
		//  目前只要求掌握固定Activity的方向，更复杂如何处理不要求掌握）
		
		
		
		//先隐藏全部的fragment；这种方式，在屏幕切换时依然有问题
		for(Fragment fragment : mFagments){
			//如果实例已经存在，并且依附并没有隐藏，则隐藏
			if(fragment!=null &&  fragment.isAdded() &&  !fragment.isHidden()){
				fragmentTransaction.hide(fragment);
			}
		}
		
			
		
		//如果mFagments[index]已经依附则只显示，否则先以add方式依附然后显示
		if(mFagments[index].isAdded()){
			fragmentTransaction.show(mFagments[index]);
		}else{
			fragmentTransaction.
				add(R.id.fragment1,mFagments[index],"Fragment"+index).show(mFagments[index]);
		}
	}	
}
