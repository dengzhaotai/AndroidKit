package com.dzt.androidkit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.dzt.androidkit.R;
import com.dzt.androidkit.widgets.slide.SlideHelper;

import java.util.List;

/**
 * Created by M02323 on 2017/9/16.
 * 侧滑选中删除动画
 */

public abstract class SlideRecyclerAdapter<T> extends RecyclerView.Adapter<OrderSlideViewHolder> {
	protected Context context;
	protected List<T> datas;
	protected int layoutId;
	protected LayoutInflater inflater;

	private OnItemClickListener onItemClickListener;
	private OnItemLongClickListener onItemLongClickListener;
	protected SlideHelper slideHelper;

	/**
	 * 自定义RecyclerView item的点击事件的点击事件
	 */
	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	public interface OnItemLongClickListener {
		void onItemLongClick(View view, int position);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
		this.onItemLongClickListener = onItemLongClickListener;
	}

	public SlideRecyclerAdapter(Context context, List<T> datas, int layoutId) {
		this.context = context;
		this.datas = datas;
		this.layoutId = layoutId;
		inflater = LayoutInflater.from(context);
		slideHelper = new SlideHelper();
	}

	@Override
	public OrderSlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//这里是创建ViewHolder的地方，RecyclerAdapter内部已经实现了ViewHolder的重用
		//这里我们直接new就好了
		OrderSlideViewHolder holder = new OrderSlideViewHolder(context, inflater.inflate(layoutId, parent, false));
		slideHelper.add(holder);
		return holder;
	}

	@Override
	public void onBindViewHolder(final OrderSlideViewHolder holder, int position) {
		convert(holder, datas.get(position), position);
		if (onItemClickListener != null) {
			holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int pos = holder.getLayoutPosition();
					onItemClickListener.onItemClick(holder.itemView, pos);
				}
			});
		}
		if (onItemLongClickListener != null) {
			holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View view) {
					int pos = holder.getLayoutPosition();
					onItemLongClickListener.onItemLongClick(holder.itemView, pos);
					return false;
				}
			});
		}
	}

	public abstract void convert(OrderSlideViewHolder holder, T data, int position);

	@Override
	public int getItemCount() {
		return datas.size();
	}
}
