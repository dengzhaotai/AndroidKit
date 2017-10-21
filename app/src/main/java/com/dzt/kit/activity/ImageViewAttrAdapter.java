package com.dzt.kit.activity;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageButton;

/**
 * Created by dzt on 2017/10/21.
 * 自定义setter，在这里自定义加载方式，也可以选择网络加载
 */

public class ImageViewAttrAdapter {
	@BindingAdapter("android:src")
	public static void setSrc(ImageButton view, Bitmap bitmap){
		view.setImageBitmap(bitmap);
	}
	@BindingAdapter("android:src")
	public static void setSrc(ImageButton view, int resId){
		view.setImageResource(resId);
	}
}
