package com.dzt.kit.presenter;

import android.content.Context;

import com.dzt.kit.contract.UserInfoContract;
import com.dzt.kit.model.MenuItem;

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
}
