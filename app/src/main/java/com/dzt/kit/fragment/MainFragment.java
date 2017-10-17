package com.dzt.kit.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.dzt.androidkit.adapter.RecyclerViewDividerItem;
import com.dzt.androidkit.fragment.FragmentBase;
import com.dzt.androidkit.utils.JImageKit;
import com.dzt.androidkit.utils.JLogKit;
import com.dzt.kit.MainActivity;
import com.dzt.kit.R;
import com.dzt.kit.UserInfoActivity;
import com.dzt.kit.adapter.RecyclerViewMainAdapter;
import com.dzt.kit.databinding.FragmentMainBinding;
import com.dzt.kit.model.ModelMainItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M02323 on 2017/10/17.
 */

public class MainFragment extends FragmentBase<FragmentMainBinding> {
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
				startActivity(data.getActivity(), null);
			}
		});
		bindingView.recyclerView.setAdapter(mainAdapter);
	}

	@Override
	protected void initData() {
		mData.add(new ModelMainItem("用户信息", R.mipmap.ic_launcher, UserInfoActivity.class));
		mData.add(new ModelMainItem("二维码与条形码的扫描与生成", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("动态生成码", R.mipmap.ic_launcher, MainActivity.class));

		mData.add(new ModelMainItem("WebView的封装可播放视频", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("常用的Dialog展示", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("图片的缩放艺术", R.mipmap.ic_launcher, MainActivity.class));

		mData.add(new ModelMainItem("RxDataTool操作Demo", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("设备信息", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("RxTextTool操作Demo", R.mipmap.ic_launcher, MainActivity.class));
		JLogKit.getInstance().e("initData size = " + mData.size());
		if (mainAdapter != null)
			mainAdapter.notifyDataSetChanged();
	}
}
