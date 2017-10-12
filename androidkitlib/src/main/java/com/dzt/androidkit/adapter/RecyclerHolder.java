package com.dzt.androidkit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dzt.androidkit.widgets.TimeLineMarkerView;

/**
 * Created by M02323 on 2017/9/16.
 */

public class RecyclerHolder extends RecyclerView.ViewHolder {
	/**
	 * 用于存储当前item当中的View
	 */
	private SparseArray<View> mViews;
	private Context context;

	public RecyclerHolder(Context context, View itemView) {
		super(itemView);
		this.context = context;
		mViews = new SparseArray<>();
	}

	public <T extends View> T findView(int ViewId) {
		View view = mViews.get(ViewId);
		//集合中没有，则从item当中获取，并存入集合当中
		if (view == null) {
			view = itemView.findViewById(ViewId);
			mViews.put(ViewId, view);
		}
		return (T) view;
	}

	public RecyclerHolder setChecked(int viewId, boolean checked) {
		View view = findView(viewId);
		// View unable cast to Checkable
		if (view instanceof CompoundButton) {
			((CompoundButton) view).setChecked(checked);
		} else if (view instanceof CheckedTextView) {
			((CheckedTextView) view).setChecked(checked);
		}
		return this;
	}

	public RecyclerHolder setOnCheckedChangeListener(int viewId,
													 CompoundButton.OnCheckedChangeListener listener){
		CompoundButton view = findView(viewId);
		view.setOnCheckedChangeListener(listener);
		return this;
	}

	public RecyclerHolder setText(int viewId, String text) {
		TextView tv = findView(viewId);
		tv.setText(text);
		return this;
	}

	public RecyclerHolder setText(int viewId, int text) {
		TextView tv = findView(viewId);
		tv.setText(text);
		return this;
	}

	public RecyclerHolder setTextColor(int viewId, int textColor) {
		TextView view = findView(viewId);
		view.setTextColor(textColor);
		return this;
	}

	public RecyclerHolder setImageResource(int viewId, int ImageId) {
		ImageView image = findView(viewId);
		image.setImageResource(ImageId);
		return this;
	}

	public RecyclerHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView image = findView(viewId);
		image.setImageBitmap(bitmap);
		return this;
	}

	public RecyclerHolder setImageGlide(int viewId, String url) {
		ImageView image = findView(viewId);
		//使用你所用的网络框架等
//		Glide.with(context).load(url)
//				.placeholder(R.mipmap.ic_launcher)
//				.crossFade()
//				.error(R.drawable.pic_loading)
//				.into(image);
		return this;
	}

	public RecyclerHolder setImagePicasso(int viewId, String url) {
		ImageView image = findView(viewId);
		//使用你所用的网络框架等
//		Picasso.with(context).load(url)
//				.placeholder(R.mipmap.ic_launcher)
//				.resize(60, 60)
//				.centerCrop()
//				.error(R.drawable.pic_loading)
//				.into(image);
		return this;
	}

	public RecyclerHolder setImageFresco(int viewId, String url) {
//		SimpleDraweeView image = findView(viewId);
//		//使用你所用的网络框架等
//		Uri uri = Uri.parse(url);
//		image.setImageURI(uri);
		return this;
	}

	public RecyclerHolder setMarkerDrawable(int viewId, Drawable drawable) {
		TimeLineMarkerView timelineView = findView(viewId);
		timelineView.setBeginLine(null);
		timelineView.setMarkerDrawable(drawable);
		return this;
	}
}
