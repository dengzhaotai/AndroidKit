package com.dzt.kit.activity;

import android.os.Bundle;

import com.dzt.androidkit.activity.FrameActivity;
import com.dzt.kit.R;
import com.dzt.kit.databinding.ActivityAboutBinding;

public class AboutActivity extends FrameActivity<ActivityAboutBinding> {


	@Override
	protected int getLayoutId() {
		return R.layout.activity_about;
	}

	@Override
	protected void initWidgets() {
		showContentView();
		setTitle("关于");
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected String[] initPermissions() {
		return new String[0];
	}
}
