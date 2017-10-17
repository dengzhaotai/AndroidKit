package com.dzt.androidkit.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dzt.androidkit.R;
import com.dzt.androidkit.eventbus.BaseEvent;
import com.dzt.androidkit.eventbus.EventBusUtil;
import com.dzt.androidkit.interfaces.PerfectClickListener;
import com.dzt.androidkit.utils.JLogKit;
import com.dzt.androidkit.utils.JToastKit;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by dzt on 2017/10/10.
 * Fragment基类，无标题，标题都在Activity中显示
 */

public abstract class FragmentBase<SV extends ViewDataBinding> extends Fragment {
	/**
	 * 是否可见状态
	 */
	protected boolean isVisible = false;
	/**
	 * 标志位，View已经初始化完成。
	 */
	protected boolean isPrepared = false;
	/**
	 * 是否第一次加载
	 */
	protected boolean isFirstLoad = true;
	protected FragmentActivity context;
	protected View view;
	// 布局view
	protected SV bindingView;
	// 加载中
	private LinearLayout mLlProgressBar;
	// 加载失败
	private LinearLayout mRefresh;
	// 内容布局
	protected RelativeLayout mContainer;
	// 动画
	private AnimationDrawable mAnimationDrawable;

	protected <T extends View> T getView(int id) {
		return (T) mContainer.findViewById(id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
		// 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
		// 导致initData反复执行,所以这里注释掉
		// isFirstLoad = true;

		// 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
		// onCreateView执行 证明被移出过FragmentManager initData确实要执行.
		// 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
		context = getActivity();
		isFirstLoad = true;
		view = inflater.inflate(R.layout.fragment_base, null);
		bindingView = DataBindingUtil.inflate(context.getLayoutInflater(),
				setContent(), null, false);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		bindingView.getRoot().setLayoutParams(params);
		mContainer = view.findViewById(R.id.container);
		mContainer.addView(bindingView.getRoot());

		mLlProgressBar = getView(R.id.ll_progress_bar);
		ImageView img = getView(R.id.img_progress);

		// 加载动画
		mAnimationDrawable = (AnimationDrawable) img.getDrawable();
		// 默认进入页面就开启动画
		if (!mAnimationDrawable.isRunning()) {
			mAnimationDrawable.start();
		}
		mRefresh = getView(R.id.ll_error_refresh);
		// 点击加载失败布局
		mRefresh.setOnClickListener(new PerfectClickListener() {
			@Override
			protected void onNoDoubleClick(View v) {
				showLoading();
				onRefresh();
			}
		});
		bindingView.getRoot().setVisibility(View.GONE);
		initWidgets();
		isPrepared = true;
		lazyLoad();
		EventBusUtil.register(this);
		return view;
	}

	/**
	 * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
	 *
	 * @param isVisibleToUser 是否显示出来了
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		JLogKit.getInstance().e("setUserVisibleHint isVisibleToUser = " + getUserVisibleHint());
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	/**
	 * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
	 * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
	 *
	 * @param hidden hidden True if the fragment is now hidden, false if it is not
	 *               visible.
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		JLogKit.getInstance().e("onHiddenChanged hidden = " + hidden);
		if (!hidden) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	protected void onVisible() {
		lazyLoad();
	}

	protected void onInvisible() {
	}

	/**
	 * 布局
	 */
	public abstract int setContent();

	/**
	 * 加载失败后点击后的操作
	 */
	protected void onRefresh() {
		JLogKit.getInstance().e("onRefresh");
	}

	/**
	 * 显示加载中状态
	 */
	protected void showLoading() {
		if (mLlProgressBar.getVisibility() != View.VISIBLE) {
			mLlProgressBar.setVisibility(View.VISIBLE);
		}
		// 开始动画
		if (!mAnimationDrawable.isRunning()) {
			mAnimationDrawable.start();
		}
		if (bindingView.getRoot().getVisibility() != View.GONE) {
			bindingView.getRoot().setVisibility(View.GONE);
		}
		if (mRefresh.getVisibility() != View.GONE) {
			mRefresh.setVisibility(View.GONE);
		}
	}

	/**
	 * 加载完成的状态
	 */
	protected void showContentView() {
		if (mLlProgressBar.getVisibility() != View.GONE) {
			mLlProgressBar.setVisibility(View.GONE);
		}
		// 停止动画
		if (mAnimationDrawable.isRunning()) {
			mAnimationDrawable.stop();
		}
		if (mRefresh.getVisibility() != View.GONE) {
			mRefresh.setVisibility(View.GONE);
		}
		if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
			bindingView.getRoot().setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 加载失败点击重新加载的状态
	 */
	protected void showError() {
		if (mLlProgressBar.getVisibility() != View.GONE) {
			mLlProgressBar.setVisibility(View.GONE);
		}
		// 停止动画
		if (mAnimationDrawable.isRunning()) {
			mAnimationDrawable.stop();
		}
		if (mRefresh.getVisibility() != View.VISIBLE) {
			mRefresh.setVisibility(View.VISIBLE);
		}
		if (bindingView.getRoot().getVisibility() != View.GONE) {
			bindingView.getRoot().setVisibility(View.GONE);
		}
	}

	/**
	 * 要实现延迟加载Fragment内容,需要在 onCreateView
	 * isPrepared = true;
	 */
	protected void lazyLoad() {
		JLogKit.getInstance().i("lazyLoad isPrepared = " + isPrepared
				+ " isVisible = " + isVisible
				+ " isFirstLoad = " + isFirstLoad);
		//if (!isPrepared || !isVisible || !isFirstLoad) {
		if (!isPrepared || !isFirstLoad) {
			return;
		}
		isFirstLoad = false;
		initData();
	}

	protected abstract void initWidgets();

	protected abstract void initData();

	/**
	 * 接收消息函数在主线程
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(BaseEvent response) {

	}

	protected void showToast(String msg) {
		JToastKit.getInstance().showMessage(msg);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBusUtil.unregister(this);
	}

	protected void startActivity(Class<?> cls, Intent intent) {
		if (null == intent) {
			intent = new Intent(context, cls);
		} else {
			intent.setClass(context, cls);
		}
		startActivity(intent);
	}
}
