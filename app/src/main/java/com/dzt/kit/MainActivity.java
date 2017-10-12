package com.dzt.kit;

import android.os.Bundle;

import com.dzt.androidkit.activity.FrameActivity;
import com.dzt.androidkit.utils.JLogKit;
import com.dzt.kit.databinding.ActivityMainBinding;


public class MainActivity extends FrameActivity<ActivityMainBinding> {

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidgets() {
		showContentView();
		setTitle("测试");
		bindingView.tvText.setText("欢迎来到Android世界");
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		JLogKit.getInstance().i("欢迎来到Android世界");
	}

	@Override
	protected String[] initPermissions() {
		return new String[0];
	}
}
