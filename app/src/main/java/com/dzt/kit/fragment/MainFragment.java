package com.dzt.kit.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.dzt.androidkit.adapter.RecyclerViewDividerItem;
import com.dzt.androidkit.fragment.FragmentBase;
import com.dzt.androidkit.utils.JImageKit;
import com.dzt.androidkit.utils.JLogKit;
import com.dzt.kit.R;
import com.dzt.kit.adapter.RecyclerViewMainAdapter;
import com.dzt.kit.contract.MainContract;
import com.dzt.kit.databinding.FragmentMainBinding;
import com.dzt.kit.model.ModelMainItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M02323 on 2017/10/17.
 */

public class MainFragment extends FragmentBase<FragmentMainBinding> implements MainContract.View {
	private MainContract.Presenter presenter;
	private List<ModelMainItem> mData = new ArrayList<>();
	private RecyclerViewMainAdapter mainAdapter;
	private int mColumnCount = 3;

	@Override
	public int setContent() {
		return R.layout.fragment_main;
	}

	@Override
	protected void initWidgets() {
		JLogKit.getInstance().e("initWidgets");
		showContentView();
		if (mColumnCount <= 1) {
			bindingView.recyclerView.setLayoutManager(new LinearLayoutManager(context));
		} else {
			bindingView.recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
		}
		bindingView.recyclerView.addItemDecoration(new RecyclerViewDividerItem(JImageKit.dp2px(5f)));
		mainAdapter = new RecyclerViewMainAdapter(context,
				mData, R.layout.item_recyclerview_main);
		mainAdapter.setOnClickItemListener(new RecyclerViewMainAdapter.OnClickItemListener() {
			@Override
			public void onClick(ModelMainItem data) {
				JLogKit.getInstance().e("onClick name = " + data.getName());
				startActivity(data.getActivity(), null);
			}
		});
		bindingView.recyclerView.setAdapter(mainAdapter);
	}

	@Override
	protected void initData() {
		presenter.loadData();
	}

	@Override
	public void setPresenter(MainContract.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showData(List<ModelMainItem> list) {
		mData.clear();
		mData.addAll(list);
		if (mainAdapter != null)
			mainAdapter.notifyDataSetChanged();
	}
}
