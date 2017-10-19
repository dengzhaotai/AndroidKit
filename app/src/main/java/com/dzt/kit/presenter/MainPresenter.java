package com.dzt.kit.presenter;

import android.content.Context;

import com.dzt.androidkit.utils.JLogKit;
import com.dzt.kit.MainActivity;
import com.dzt.kit.R;
import com.dzt.kit.UserInfoActivity;
import com.dzt.kit.contract.MainContract;
import com.dzt.kit.model.ModelMainItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M02323 on 2017/10/17.
 */

public class MainPresenter implements MainContract.Presenter{

	private MainContract.View view;

	public MainPresenter(Context context, MainContract.View view){
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
	public void loadData() {
		List<ModelMainItem> data = new ArrayList<>();
		data.add(new ModelMainItem("用户信息", R.mipmap.ic_launcher, UserInfoActivity.class));
		data.add(new ModelMainItem("二维码与条形码的扫描与生成", R.mipmap.ic_launcher, null));
		data.add(new ModelMainItem("动态生成码", R.mipmap.ic_launcher, null));

		data.add(new ModelMainItem("WebView的封装可播放视频", R.mipmap.ic_launcher,null));
		data.add(new ModelMainItem("常用的Dialog展示", R.mipmap.ic_launcher, null));
		data.add(new ModelMainItem("图片的缩放艺术", R.mipmap.ic_launcher, null));

		data.add(new ModelMainItem("RxDataTool操作Demo", R.mipmap.ic_launcher, null));
		data.add(new ModelMainItem("设备信息", R.mipmap.ic_launcher, null));
		data.add(new ModelMainItem("RxTextTool操作Demo", R.mipmap.ic_launcher, null));
		JLogKit.getInstance().e("loadData size = " + data.size());
		view.showData(data);
	}
}
