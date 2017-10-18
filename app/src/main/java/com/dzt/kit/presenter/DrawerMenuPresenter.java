package com.dzt.kit.presenter;

import android.content.Context;

import com.dzt.kit.contract.DrawerContract;
import com.dzt.kit.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M02323 on 2017/10/17.
 */

public class DrawerMenuPresenter implements DrawerContract.Presenter {

	private DrawerContract.View view;

	public DrawerMenuPresenter(Context context, DrawerContract.View view) {
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
	public void loadMenu() {
		List<MenuItem> list = new ArrayList<>();
		list.add(new MenuItem(null, "设置"));
		list.add(new MenuItem(null, "关于"));
		view.showMenu(list);
	}
}
