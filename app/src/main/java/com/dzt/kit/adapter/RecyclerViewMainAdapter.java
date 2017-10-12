package com.dzt.kit.adapter;

import android.content.Context;
import android.view.View;

import com.dzt.androidkit.adapter.RecyclerAdapter;
import com.dzt.androidkit.adapter.RecyclerHolder;
import com.dzt.kit.R;
import com.dzt.kit.model.ModelMainItem;

import java.util.List;


/**
 * Created by dzt on 2017/10/12.
 */

public class RecyclerViewMainAdapter extends RecyclerAdapter<ModelMainItem> {

	private OnClickItemListener listener;

	public interface OnClickItemListener {
		void onClick(ModelMainItem data);
	}

	public void setOnClickItemListener(OnClickItemListener listener) {
		this.listener = listener;
	}

	public RecyclerViewMainAdapter(Context context, List<ModelMainItem> items, int layoutId) {
		super(context, items, layoutId);
	}

	@Override
	public void convert(RecyclerHolder holder, final ModelMainItem data, int position) {
		holder.setText(R.id.tv_name, data.getName());
		holder.setImageGlide(R.id.imageView, data.getImage());
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
