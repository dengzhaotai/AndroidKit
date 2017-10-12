package com.dzt.androidkit.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dzt.androidkit.R;


/**
 * Created by M02323 on 2017/9/19.
 * 加载进度条
 */

public class LoadProgressDlg extends AlertDialog {
	private ImageView progressImg;
	//旋转动画
	private Animation animation;

	public LoadProgressDlg(Context context) {
		super(context, R.style.DialogNoTitleStyleTranslucentBg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressdlg_layout);

		//点击imageview外侧区域，动画不会消失
		setCanceledOnTouchOutside(false);

		progressImg = (ImageView) findViewById(R.id.iv_load);
		//加载动画资源
		animation = AnimationUtils.loadAnimation(getContext(), R.anim.progress_rotate);
		//动画完成后，是否保留动画最后的状态，设为true
		animation.setFillAfter(true);
	}

	/**
	 * 在AlertDialog的 onStart() 生命周期里面执行开始动画
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if (animation != null) {
			progressImg.startAnimation(animation);
		}
	}

	/**
	 * 在AlertDialog的onStop()生命周期里面执行停止动画
	 */
	@Override
	protected void onStop() {
		super.onStop();
		progressImg.clearAnimation();
	}
}
