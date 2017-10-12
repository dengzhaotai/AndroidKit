package com.dzt.androidkit.adapter;

import android.content.Context;
import android.view.View;

import com.dzt.androidkit.R;
import com.dzt.androidkit.widgets.slide.SlideViewHolder;


/**
 * Created by M02323 on 2017/9/20.
 */

public class OrderSlideViewHolder extends SlideViewHolder {

	//TODO 暂时注释掉这部分代码
	private View contentRl;

	public OrderSlideViewHolder(Context context, View itemView) {
		super(context, itemView);
		//contentRl = itemView.findViewById(R.id.rl_fragment_order_signed);
	}

	public void bind() {
		//slide offset
		setOffset(50);
		//slide must call,param is slide view
		//onBindSlide(contentRl);
	}

	@Override
	public void doAnimationSet(int offset, float fraction) {
		//contentRl.scrollTo(offset, 0);
	}

	@Override
	public void onBindSlideClose(int state) {

	}

	@Override
	public void doAnimationSetOpen(int state) {

	}
}
