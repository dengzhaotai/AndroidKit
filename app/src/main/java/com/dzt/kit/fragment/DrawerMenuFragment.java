package com.dzt.kit.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;

import com.dzt.androidkit.adapter.RecyclerViewDividerItem;
import com.dzt.androidkit.fragment.FragmentBase;
import com.dzt.androidkit.utils.JImageKit;
import com.dzt.kit.R;
import com.dzt.kit.adapter.RecyclerViewMenuAdapter;
import com.dzt.kit.contract.DrawerContract;
import com.dzt.kit.databinding.FragmentDrawerMenuBinding;
import com.dzt.kit.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M02323 on 2017/10/17.
 */

public class DrawerMenuFragment extends FragmentBase<FragmentDrawerMenuBinding> implements DrawerContract.View {

	private RecyclerViewMenuAdapter adapter;
	private DrawerContract.Presenter presenter;
	private List<MenuItem> data = new ArrayList<>();
	private int mColumnCount = 1;

	@Override
	public int setContent() {
		return R.layout.fragment_drawer_menu;
	}

	@Override
	protected void initWidgets() {
		showContentView();
		if (mColumnCount <= 1) {
			bindingView.recyclerView.setLayoutManager(new LinearLayoutManager(context));
		} else {
			bindingView.recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
		}
		bindingView.recyclerView.addItemDecoration(new RecyclerViewDividerItem(JImageKit.dp2px(5f)));
		bindingView.recyclerView.setItemAnimator(new DefaultItemAnimator());
		adapter = new RecyclerViewMenuAdapter(context, data, R.layout.item_drawer_menu);
		bindingView.recyclerView.setAdapter(adapter);
	}

	@Override
	protected void initData() {
		presenter.loadMenu();
	}

	@Override
	public void setPresenter(DrawerContract.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showMenu(List<MenuItem> list) {
		data.clear();
		data.addAll(list);
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
}
