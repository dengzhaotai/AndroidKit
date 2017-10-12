package com.dzt.androidkit.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dzt.androidkit.R;


public class DialogMaker {

	public interface DialogCallBack {
		/**
		 * Dialog button click callback
		 *
		 * @param dialog
		 * @param position Click on the BTN index
		 * @param tag
		 */
		void onBtnClicked(Dialog dialog, int position, Object tag);

		/**
		 * Callback when the dialog box disappears
		 *
		 * @param dialog
		 * @param tag
		 */
		void onCancelDialog(Dialog dialog, Object tag);
	}

	public static final String BIRTHDAY_FORMAT = "%04d-%02d-%02d";

	/**
	 * Create a common alert dialog box
	 *
	 * @param context
	 * @param title                  Dialog box title, for null or "" no title
	 * @param msg                    Dialog box displays the contents of the
	 * @param btns                   The title of the display button, no button for null
	 * @param callBack               Click on the button callback
	 * @param isCanCancel            Whether you can click the back button dialog disappears
	 * @param isDismissAfterClickBtn Click on any button to disappear after the dialog box
	 * @param tag
	 * @return Display dialog box and return
	 */
	public static Dialog showCommonAlertDialog(Context context, String title,
											   String msg, String[] btns, final DialogCallBack
													   callBack,
											   boolean isCanCancel, final boolean
													   isDismissAfterClickBtn,
											   final Object tag) {
		final Dialog dialog = new Dialog(context,
				R.style.DialogNoTitleStyleTranslucentBg);
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.dlg_alert_common_layout, new LinearLayout(context), false);
		TextView tvContent = (TextView) contentView
				.findViewById(R.id.tv_dlg_content);
		TextView tvTitle = (TextView) contentView
				.findViewById(R.id.tv_dlg_title);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != callBack) {
					callBack.onBtnClicked(dialog, (int) v.getTag(), tag);
				}
				if (isDismissAfterClickBtn) {
					dialog.dismiss();
				}
			}
		};
		if (null != btns) {
			int len = btns.length;
			if (len > 0) {
				LinearLayout btnLayout = (LinearLayout) contentView
						.findViewById(R.id.ll_btn);
				Button btn;
				View lineView;
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						0, JImageKit.dp2px(44));
				params.weight = 1.0f;
				for (int i = 0; i < len; i++) {
					btn = new Button(context);
					btn.setText(btns[i]);
					btn.setTag(i);
					btn.setSingleLine(true);
					btn.setEllipsize(TruncateAt.END);
					btn.setOnClickListener(listener);
					btn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							JImageKit.dp2px(16));
					btn.setTextColor(Color.parseColor("#007cff"));
					btn.setGravity(Gravity.CENTER);
					if (0 == i && 1 == len) {
						btn.setBackgroundResource(R.drawable.selector_alert_single_btn); // single
					} else if (0 == i) {
						btn.setBackgroundResource(R.drawable.selector_alert_left_btn); // left
					} else if (i > 0 && i < len - 1) {
						btn.setBackgroundResource(R.drawable.selector_alert_mid_btn); // mid
					} else {
						btn.setBackgroundResource(R.drawable.selector_alert_right_btn); // right
					}
					btn.setPadding(10, 10, 10, 10);
					btnLayout.addView(btn, params);
					lineView = new View(context);
					lineView.setBackgroundColor(Color.parseColor("#b2b22b"));
					if (i < len - 1 && len > 1) {
						btnLayout
								.addView(
										lineView,
										new LinearLayout.LayoutParams(
												1,
												LinearLayout.LayoutParams.MATCH_PARENT));
					}
				}
			}
		}
		if (!TextUtils.isEmpty(title)) {
			setDialogTextViewContent(title, tvTitle);
			tvTitle.setVisibility(View.VISIBLE);
		} else if (!TextUtils.isEmpty(msg)) {
			title = msg;
			msg = null;
			setDialogTextViewContent(title, tvTitle);
			tvTitle.setVisibility(View.VISIBLE);
		} else {
			tvTitle.setVisibility(View.GONE);
			tvContent.setVisibility(View.GONE);
		}
		MarginLayoutParams mParams = (MarginLayoutParams) tvTitle
				.getLayoutParams();
		if (!TextUtils.isEmpty(msg)) {
			final int margin = JImageKit.dp2px(21.33f);
			mParams.topMargin = margin;
			mParams.bottomMargin = 0;
			tvTitle.setLayoutParams(mParams);
			setDialogTextViewContent(msg, tvContent);
			tvContent.setVisibility(View.VISIBLE);
		} else if (!TextUtils.isEmpty(title)) {
			final int margin = JImageKit.dp2px(38.67f);
			mParams.topMargin = margin;
			mParams.bottomMargin = margin;
			tvTitle.setLayoutParams(mParams);
			tvContent.setVisibility(View.GONE);
		}
		contentView.requestLayout();
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (null != callBack) {
					callBack.onCancelDialog((Dialog) dialog, tag);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(isCanCancel);
		dialog.setContentView(contentView);
		// 设置对话框宽度
		Window window = dialog.getWindow();
		WindowManager.LayoutParams aWmLp = window.getAttributes();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		aWmLp.width = dm.widthPixels - 100;
		aWmLp.gravity = Gravity.CENTER;
		window.setAttributes(aWmLp);

		dialog.show();
		return dialog;
	}

	/**
	 * Set the text information
	 *
	 * @param content
	 * @param tView
	 */
	private static void setDialogTextViewContent(String content, TextView tView) {
		if (null == tView || TextUtils.isEmpty(content)) {
			return;
		}

		String NEW_LINE = System.getProperty("line.separator");
		if (content.contains(NEW_LINE) || content.contains("\n")) {
			tView.setGravity(Gravity.CENTER);
		} else {
			float destWidth = getContentWidth(content, tView);
			float maxWidth = JImageKit.dp2px(235.33f);
			if (destWidth >= Math.round(maxWidth - 0.5f)) {
				tView.setGravity(Gravity.LEFT);
			} else {
				tView.setGravity(Gravity.CENTER);
			}
		}

		tView.setText(content);
	}

	private static float getContentWidth(String content, TextView view) {
		if (null == view && TextUtils.isEmpty(content)) {
			return 0f;
		}
		return getContentWidthWithSize(content,
				view.getTextSize() * view.getTextScaleX());
	}

	private static float getContentWidthWithSize(String content, float textSize) {
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		float width = paint.measureText(content);
		return width;
	}

	/**
	 * Show the uniform style for the dialog box, no title
	 *
	 * @param context
	 * @param msg         dialog context
	 * @param callBack    dialog callback
	 * @param isCanCancel If you can cancel
	 * @param tag
	 * @return
	 */
	public static Dialog showCommonWaitDialog(Context context, String msg,
											  final DialogCallBack callBack, boolean
													  isCanCancel,
											  final Object tag) {
		final Dialog dialog = new Dialog(context,
				R.style.DialogNoTitleRoundCornerStyle);
		dialog.setOwnerActivity((Activity) context);
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.dlg_wait_common_layout, new LinearLayout(context), false);
		TextView tvContent = (TextView) contentView
				.findViewById(R.id.tv_dlg_content);
		if (!TextUtils.isEmpty(msg)) {
			tvContent.setText(msg);
		}
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (null != callBack) {
					callBack.onCancelDialog((Dialog) dialog, tag);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(isCanCancel);
		dialog.setContentView(contentView);

		// Set the width of the dialog
		Window window = dialog.getWindow();
		WindowManager.LayoutParams aWmLp = window.getAttributes();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		aWmLp.width = dm.widthPixels / 2;
		aWmLp.gravity = Gravity.CENTER;
		window.setAttributes(aWmLp);

		dialog.show();
		return dialog;
	}
}
