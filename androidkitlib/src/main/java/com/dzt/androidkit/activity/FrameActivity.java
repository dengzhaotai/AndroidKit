package com.dzt.androidkit.activity;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dzt.androidkit.R;
import com.dzt.androidkit.databinding.ActivityFrameBinding;
import com.dzt.androidkit.interfaces.PerfectClickListener;
import com.dzt.androidkit.utils.DialogMaker;
import com.dzt.androidkit.utils.JLogKit;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 对提示框的操作
 * Created by dzt on 2017/10/11.
 */

public abstract class FrameActivity<SV extends ViewDataBinding> extends SuperActivity implements DialogMaker.DialogCallBack {
	protected Dialog dialog;
	protected SV bindingView;
	private LinearLayout llProgressBar;
	private View refresh;
	protected ActivityFrameBinding baseBinding;
	private AnimationDrawable animationDrawable;

	protected <T extends View> T getView(int id) {
		return (T) findViewById(id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		initWidgets();
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		//super.setContentView();

		baseBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
				R.layout.activity_frame, null, false);
		bindingView = DataBindingUtil.inflate(getLayoutInflater(),
				layoutResID, null, false);
		if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
			baseBinding.getRoot().setFitsSystemWindows(true);
			SystemBarTintManager tintManager = new  SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.color_primary_dark);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		}
		// content
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		bindingView.getRoot().setLayoutParams(params);
		RelativeLayout mContainer = baseBinding.getRoot().findViewById(R.id.container);
		mContainer.addView(bindingView.getRoot());
		getWindow().setContentView(baseBinding.getRoot());

		// 设置透明状态栏
		//StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme), 0);
		llProgressBar = getView(R.id.ll_progress_bar);
		refresh = getView(R.id.ll_error_refresh);
		ImageView img = getView(R.id.img_progress);

		// 加载动画
		animationDrawable = (AnimationDrawable) img.getDrawable();
		// 默认进入页面就开启动画
		if (!animationDrawable.isRunning()) {
			animationDrawable.start();
		}

		setToolBar();
		// 点击加载失败布局
		refresh.setOnClickListener(new PerfectClickListener() {
			@Override
			protected void onNoDoubleClick(View v) {
				showLoading();
				onRefresh();
			}
		});
		bindingView.getRoot().setVisibility(View.GONE);
	}

	/**
	 * 设置titlebar
	 */
	protected void setToolBar() {
		baseBinding.toolBar.setTitle("");
		setSupportActionBar(baseBinding.toolBar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			//去除默认Title显示
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
		}
		baseBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	public void setTitle(CharSequence text) {
		//baseBinding.toolBar.setTitle(text);
		baseBinding.tvTitle.setText(text);
	}

	protected void showLoading() {
		if (llProgressBar.getVisibility() != View.VISIBLE) {
			llProgressBar.setVisibility(View.VISIBLE);
		}
		// 开始动画
		if (!animationDrawable.isRunning()) {
			animationDrawable.start();
		}
		if (bindingView.getRoot().getVisibility() != View.GONE) {
			bindingView.getRoot().setVisibility(View.GONE);
		}
		if (refresh.getVisibility() != View.GONE) {
			refresh.setVisibility(View.GONE);
		}
	}

	protected void showContentView() {
		if (llProgressBar.getVisibility() != View.GONE) {
			llProgressBar.setVisibility(View.GONE);
		}
		// 停止动画
		if (animationDrawable.isRunning()) {
			animationDrawable.stop();
		}
		if (refresh.getVisibility() != View.GONE) {
			refresh.setVisibility(View.GONE);
		}
		if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
			bindingView.getRoot().setVisibility(View.VISIBLE);
		}
	}

	protected void showError() {
		if (llProgressBar.getVisibility() != View.GONE) {
			llProgressBar.setVisibility(View.GONE);
		}
		// 停止动画
		if (animationDrawable.isRunning()) {
			animationDrawable.stop();
		}
		if (refresh.getVisibility() != View.VISIBLE) {
			refresh.setVisibility(View.VISIBLE);
		}
		if (bindingView.getRoot().getVisibility() != View.GONE) {
			bindingView.getRoot().setVisibility(View.GONE);
		}
	}

	/**
	 * 失败后点击刷新
	 */
	protected void onRefresh() {

	}

	protected abstract int getLayoutId();
	protected abstract void initWidgets();

	@Override
	public void onBtnClicked(Dialog dialog, int position, Object tag) {
		JLogKit.getInstance().i("position = " + position);
	}

	@Override
	public void onCancelDialog(Dialog dialog, Object tag) {

	}

	/**
	 * Pop-up dialog
	 *
	 * @param title                  标题
	 * @param msg                    内容
	 * @param btns                   按钮数组
	 * @param isCanCancel            是否可以取消
	 * @param isDismissAfterClickBtn 点击后是否关闭提示框
	 * @return
	 */
	public Dialog showAlertDialog(String title, String msg, String[] btns,
								  boolean isCanCancel, final boolean isDismissAfterClickBtn,
								  Object tag) {
		if (null == dialog || !dialog.isShowing()) {
			dialog = DialogMaker.showCommonAlertDialog(this, title, msg, btns, this,
					isCanCancel, isDismissAfterClickBtn, tag);
		}
		return dialog;
	}

	public Dialog showWaitDialog(String msg, boolean isCanCancel, Object tag) {
		if (null == dialog || !dialog.isShowing()) {
			dialog = DialogMaker.showCommonWaitDialog(this, msg, this,
					isCanCancel, tag);
		}
		return dialog;
	}

	/**
	 * close dialog
	 */
	public void dismissDialog() {
		if (null != dialog && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		dismissDialog();
		super.onDestroy();
	}
}
