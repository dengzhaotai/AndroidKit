package com.dzt.kit.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.dzt.androidkit.utils.JLogKit;
import com.dzt.kit.contract.UserInfoContract;
import com.dzt.kit.fragment.UserInfoFragment;
import com.dzt.kit.model.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by M02323 on 2017/10/17.
 */

public class UserInfoPresenter implements UserInfoContract.Presenter {

	private UserInfoContract.View view;

	public UserInfoPresenter(Context context, UserInfoContract.View view) {
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void subscribe() {

	}

	@Override
	public void unSubscribe() {

	}


	@Override
	public void initPopupWindow() {

	}

	@Override
	public void onResult(int requestCode, Intent data) {
		if (requestCode == UserInfoFragment.CALL_CAMERA) {
			if (data == null) {
				view.returnCamera();
			}
		} else if (requestCode == UserInfoFragment.SELECTIMAGE_ONE) {
			if (data != null) {
				view.returnSelectImage(data);
			}
		}
	}

	@Override
	public void setCropRequest(Intent data) {
		view.returnCropRequest(data);
	}
}
