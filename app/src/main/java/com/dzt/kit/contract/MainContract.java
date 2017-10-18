package com.dzt.kit.contract;

import com.dzt.androidkit.mvp.BasePresenter;
import com.dzt.androidkit.mvp.BaseView;
import com.dzt.kit.model.ModelMainItem;

import java.util.List;

/**
 * Created by M02323 on 2017/10/17.
 */

public interface MainContract {

	interface View extends BaseView<Presenter>{
		void showData(List<ModelMainItem> list);
	}

	interface Presenter extends BasePresenter{
		void loadData();
	}
}
