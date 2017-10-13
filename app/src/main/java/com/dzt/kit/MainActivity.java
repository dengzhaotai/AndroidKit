package com.dzt.kit;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.dzt.androidkit.activity.FrameActivity;
import com.dzt.androidkit.adapter.RecyclerViewDividerItem;
import com.dzt.androidkit.utils.JImageKit;
import com.dzt.kit.adapter.RecyclerViewMainAdapter;
import com.dzt.kit.databinding.ActivityMainBinding;
import com.dzt.kit.model.ModelMainItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FrameActivity<ActivityMainBinding> {

	private List<ModelMainItem> mData = new ArrayList<>();
	private RecyclerViewMainAdapter mainAdapter;
	private int mColumnCount = 3;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initWidgets() {
		showContentView();
		setTitle("工具类Demo");
		//隐藏左边返回按钮
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
	protected void initData(Bundle savedInstanceState) {
		mData.add(new ModelMainItem("用户信息", R.mipmap.ic_launcher, UserInfoActivity.class));
		mData.add(new ModelMainItem("二维码与条形码的扫描与生成", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("动态生成码", R.mipmap.ic_launcher, MainActivity.class));

		mData.add(new ModelMainItem("WebView的封装可播放视频", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("常用的Dialog展示", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("图片的缩放艺术", R.mipmap.ic_launcher, MainActivity.class));

		mData.add(new ModelMainItem("RxDataTool操作Demo", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("设备信息", R.mipmap.ic_launcher, MainActivity.class));
		mData.add(new ModelMainItem("RxTextTool操作Demo", R.mipmap.ic_launcher, MainActivity.class));
		if (mainAdapter != null)
			mainAdapter.notifyDataSetChanged();
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
}
