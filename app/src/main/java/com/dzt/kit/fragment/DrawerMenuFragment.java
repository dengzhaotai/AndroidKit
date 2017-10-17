package com.dzt.kit.fragment;

import com.dzt.androidkit.fragment.FragmentBase;
import com.dzt.kit.R;
import com.dzt.kit.databinding.FragmentDrawerMenuBinding;

/**
 * Created by M02323 on 2017/10/17.
 */

public class DrawerMenuFragment extends FragmentBase<FragmentDrawerMenuBinding> {
	@Override
	public int setContent() {
		return R.layout.fragment_drawer_menu;
	}

	@Override
	protected void initWidgets() {
		showContentView();
	}

	@Override
	protected void initData() {

	}
}