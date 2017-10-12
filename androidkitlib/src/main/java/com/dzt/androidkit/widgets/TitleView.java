/**
 * Copyright (C) 2015, all rights reserved.
 * Since	2015.08.20
 */
package com.dzt.androidkit.widgets;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dzt.androidkit.R;


/**
 * @Since 2015.08.20
 */
public class TitleView extends RelativeLayout {

	private Context context;
	private TextView mLeftTextView;
	private TextView mTitleTextView;
	private TextView mRightTextView;
	private ImageView mLeftImageView;
	private ImageView mRightImageView;

	public TitleView(Context context) {
		this(context, null);
	}

	public TitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initViews(context);
	}

	private void initViews(Context context) {
		this.context = context;
		View view = inflate(context, R.layout.title_view, this);
		view.setBackgroundColor(getResources().getColor(R.color.color_primary));
		mLeftTextView = view.findViewById(R.id.tv_head_left);
		mTitleTextView = view.findViewById(R.id.tv_head_title);
		mRightTextView = view.findViewById(R.id.tv_head_right);
		mLeftImageView = view.findViewById(R.id.iv_head_left);
		mRightImageView = view.findViewById(R.id.iv_head_right);
	}

	public void setResource(int resIdLeft, int resIdCenter) {
		setLeftDetail(resIdLeft);
		setCenterDetail(resIdCenter);
	}

	public void setResource(int resIdLeft, int resIdCenter, int resIdRight) {
		setLeftDetail(resIdLeft);
		setCenterDetail(resIdCenter);
		setRightDetail(resIdRight);
	}

	public void setLeftClickListener(OnClickListener listener) {
		mLeftTextView.setOnClickListener(listener);
		mLeftImageView.setOnClickListener(listener);
	}

	public void setRightClickListener(OnClickListener listener) {
		mRightTextView.setOnClickListener(listener);
		mRightImageView.setOnClickListener(listener);
	}

	public void setLeftDetail(int resId) {
		if (resId > 0 && !setImgDetail(mLeftImageView, resId)) {
			setTxvDetail(mLeftTextView, resId);
		}
	}

	public void setLeftDetail(String text) {
		mLeftTextView.setText(text);
		mLeftTextView.setVisibility(View.VISIBLE);
	}

	public void setCenterDetail(int resId) {
		setCenterDetail(getResources().getString(resId));
	}

	public void setCenterDetail(String title) {
		mTitleTextView.setText(title);
	}

	public void setRightDetail(int resId) {
		if (resId > 0 && !setImgDetail(mRightImageView, resId)) {
			setTxvDetail(mRightTextView, resId);
		}
	}

	public void setRightDetail(String text) {
		mRightTextView.setText(text);
		mRightTextView.setVisibility(View.VISIBLE);
	}

	public void setRightDetailInvisible() {
		mRightTextView.setVisibility(View.GONE);
		mRightImageView.setVisibility(View.GONE);
	}

	private boolean setTxvDetail(TextView txv, int resId) {
		boolean result = false;
		if (resId <= 0) {
			txv.setVisibility(View.INVISIBLE);
		} else {
			txv.setVisibility(View.VISIBLE);
			String text = getStringFromResId(resId);
			if (text == null) {
				txv.setVisibility(View.INVISIBLE);
			} else {
				txv.setText(text);
				result = true;
			}
		}
		return result;
	}

	private boolean setImgDetail(ImageView img, int resId) {
		boolean result = false;
		if (resId <= 0) {
			img.setVisibility(View.INVISIBLE);
		} else {
			img.setVisibility(View.VISIBLE);
			Drawable drawable = getDrawableFromResId(resId);
			if (drawable == null) {
				img.setVisibility(View.INVISIBLE);
			} else {
				img.setImageDrawable(drawable);
				result = true;
			}
		}
		return result;
	}

	private String getStringFromResId(int resId) {
		String result = null;
		try {
			result = getResources().getString(resId);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Drawable getDrawableFromResId(int resId) {
		return ContextCompat.getDrawable(context, resId);
	}

}
