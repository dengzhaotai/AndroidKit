package com.dzt.kit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dzt.androidkit.activity.FrameActivity;
import com.dzt.androidkit.utils.JActivityKit;
import com.dzt.androidkit.utils.JLogKit;
import com.dzt.kit.R;
import com.dzt.kit.databinding.ActivityUserInfoBinding;
import com.dzt.kit.fragment.UserInfoFragment;
import com.dzt.kit.presenter.UserInfoPresenter;
import com.yalantis.ucrop.UCrop;

/**
 * 通过图库或照相得到图片，经过裁剪得到想要的图片
 */
public class UserInfoActivity extends FrameActivity<ActivityUserInfoBinding>{

	private UserInfoPresenter userInfoPresenter;
	private UserInfoFragment userInfoFragment;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_user_info;
	}

	@Override
	protected void initWidgets() {
		showContentView();
		setTitle("个人信息");
		userInfoFragment = new UserInfoFragment();
		JActivityKit.addFragmentToActivity(getSupportFragmentManager(),
				userInfoFragment, R.id.fragment_container);
		userInfoPresenter = new UserInfoPresenter(context, userInfoFragment);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == UCrop.REQUEST_CROP) {
				userInfoFragment.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected String[] initPermissions() {
		return new String[0];
	}
}
