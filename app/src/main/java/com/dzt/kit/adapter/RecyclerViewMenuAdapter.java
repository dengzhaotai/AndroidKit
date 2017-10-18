package com.dzt.kit.adapter;

import android.content.Context;
import android.view.View;

import com.dzt.androidkit.adapter.RecyclerAdapter;
import com.dzt.androidkit.adapter.RecyclerHolder;
import com.dzt.kit.R;
import com.dzt.kit.model.MenuItem;

import java.util.List;


/**
 * Created by dzt on 2017/10/12.
 */

public class RecyclerViewMenuAdapter extends RecyclerAdapter<MenuItem> {

	private OnClickItemListener listener;

	public interface OnClickItemListener {
		void onClick(MenuItem data);
	}

	public void setOnClickItemListener(OnClickItemListener listener) {
		this.listener = listener;
	}

	public RecyclerViewMenuAdapter(Context context, List<MenuItem> items, int layoutId) {
		super(context, items, layoutId);
	}

	@Override
	public void convert(RecyclerHolder holder, final MenuItem data, int position) {
		holder.setText(R.id.tv_title, data.title);
		holder.setImageGlide(R.id.iv_icon, R.mipmap.ic_launcher);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null) {
					listener.onClick(data);
				}
			}
		});
	}
}
