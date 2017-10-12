package com.dzt.androidkit.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.dzt.androidkit.R;


public class SideBar extends View {

	private final Context mContext;
	private final Paint mPaint = new Paint();
	// 字母表
	private char[] alphabet;
	// 列表
	private PinnedHeaderListView listView;
	private SectionIndexer sectionIndexer;
	// 提示框
	private TextView dialogText;

	public SideBar(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	/**
	 * 初始化字母表
	 */
	private void init() {
		alphabet = new char[]{'#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
				'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
				'U', 'V', 'W', 'X', 'Y', 'Z', '*'};
	}

	public void setListView(PinnedHeaderListView listView) {
		this.listView = listView;
		sectionIndexer = (SectionIndexer) listView.getAdapter();
	}

	public void setTextView(TextView dialogText) {
		this.dialogText = dialogText;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		// 得到当前触摸y值
		int i = (int) event.getY();
		int idx = i / (getMeasuredHeight() / alphabet.length);
		if (idx >= alphabet.length) {
			idx = alphabet.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			setBackgroundColor(Color.parseColor("#40000000"));
			dialogText.setVisibility(View.VISIBLE);
			dialogText.setText(String.valueOf(alphabet[idx]));
			dialogText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);

			if (alphabet[idx] == '#') {
				listView.setSelection(0);
			} else {
				int position = sectionIndexer
						.getPositionForSection(alphabet[idx]);
				if (position == -1) {
					return true;
				}
				listView.setSelection(position);
			}
		} else {
			dialogText.postDelayed(new Runnable() {

				@Override
				public void run() {
					dialogText.setVisibility(View.INVISIBLE);
				}
			}, 1000);
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.setColor(Color.parseColor("#515151"));
		mPaint.setTextSize(getResources().getInteger(R.integer.text_sidebar_size));
		mPaint.setTextAlign(Paint.Align.CENTER);
		// 绘制风格，粗体
		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
		mPaint.setTypeface(font);
		float widthCenter = getMeasuredWidth() / 2;
		if (alphabet.length > 0) {
			float height = getMeasuredHeight() / alphabet.length;
			for (int i = 0; i < alphabet.length; i++) {
				canvas.drawText(String.valueOf(alphabet[i]), widthCenter,
						(i + 1) * height, mPaint);
			}
		}
		this.invalidate();
		super.onDraw(canvas);
	}
}
