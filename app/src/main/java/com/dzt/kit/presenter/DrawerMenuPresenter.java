package com.dzt.kit.presenter;

import android.content.Context;

import com.dzt.kit.contract.DrawerContract;

/**
 * Created by M02323 on 2017/10/17.
 */

public class DrawerMenuPresenter implements DrawerContract.Presenter{

	private DrawerContract.View view;

	public DrawerMenuPresenter(Context context, DrawerContract.View view){
		this.view = view;
		view.setPresenter(this);
	}

	@Override
	public void subscribe() {

	}

	@Override
	public void unSubscribe() {

	}
}
