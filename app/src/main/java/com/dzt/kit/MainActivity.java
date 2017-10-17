package com.dzt.kit;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dzt.androidkit.activity.FrameActivity;
import com.dzt.androidkit.utils.JActivityKit;
import com.dzt.androidkit.utils.JLogKit;
import com.dzt.kit.databinding.ActivityMainBinding;
import com.dzt.kit.fragment.DrawerMenuFragment;
import com.dzt.kit.fragment.MainFragment;


public class MainActivity extends FrameActivity<ActivityMainBinding> {

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidgets() {
		JLogKit.getInstance().i("initWidgets");
		showContentView();
		baseBinding.toolBar.setVisibility(View.GONE);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			//去除默认Title显示
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		toolbar.setTitle("工具类Demo");
		//隐藏左边返回按钮
		//getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, bindingView.drawerLayout, toolbar,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		bindingView.drawerLayout.addDrawerListener(toggle);
		toggle.syncState();
		MainFragment mainFragment = new MainFragment();
		JActivityKit.addFragmentToActivity(getSupportFragmentManager(),
				mainFragment, R.id.fragment_container);

		DrawerMenuFragment menuFragment = new DrawerMenuFragment();
		JActivityKit.addFragmentToActivity(getSupportFragmentManager(),
				menuFragment, R.id.fragment_container_menu);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected String[] initPermissions() {
		return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.CAMERA,
				Manifest.permission.CALL_PHONE,
				Manifest.permission.READ_PHONE_STATE};
	}

	@Override
	public void onBackPressed() {
		assert bindingView.drawerLayout != null;
		if (bindingView.drawerLayout.isDrawerOpen(GravityCompat.START)) {
			bindingView.drawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
}
