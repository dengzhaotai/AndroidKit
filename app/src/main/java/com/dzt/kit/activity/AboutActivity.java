package com.dzt.kit.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.dzt.androidkit.activity.FrameActivity;
import com.dzt.kit.R;
import com.dzt.kit.databinding.ActivityAboutBinding;
import com.dzt.kit.model.MenuItem;

public class AboutActivity extends FrameActivity<ActivityAboutBinding> {


	@Override
	protected int getLayoutId() {
		return R.layout.activity_about;
	}

	@Override
	protected void initWidgets() {
		showContentView();
		setTitle("关于");
		//Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		bindingView.setMenu(new MenuItem(R.mipmap.ic_launcher, "图标"));
		bindingView.setResId(R.drawable.selector_round_btn_red);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected String[] initPermissions() {
		return new String[0];
	}
}
