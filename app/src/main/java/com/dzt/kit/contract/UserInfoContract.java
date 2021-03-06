package com.dzt.kit.contract;

import android.content.Intent;

import com.dzt.androidkit.mvp.BasePresenter;
import com.dzt.androidkit.mvp.BaseView;
import com.dzt.kit.model.MenuItem;
import com.dzt.kit.model.ModelMainItem;

import java.util.List;

/**
 * Created by M02323 on 2017/10/17.
 */

public interface UserInfoContract {

	interface View extends BaseView<Presenter> {
		void showPopupWindow();
		void returnCropRequest(Intent data);
		void returnCamera();
		void returnSelectImage(Intent data);
	}

	interface Presenter extends BasePresenter {
		void initPopupWindow();
		void onResult(int requestCode, Intent data);
		void setCropRequest(Intent data);
	}
}
