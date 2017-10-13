package com.dzt.androidkit.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.dzt.androidkit.R;
import com.dzt.androidkit.widgets.scaleimage.ImageSource;
import com.dzt.androidkit.widgets.scaleimage.ScaleImageView;

/**
 * Created by M02323 on 2017/10/13.
 */

public class ScaleViewDlg extends BaseDialog {
	private ScaleImageView scaleImageView;
	private String filePath;
	private Uri fileUri;
	private String fileAssetName;
	private Bitmap fileBitmap;
	private int resId;

	public ScaleViewDlg(Context context, int themeResId) {
		super(context, themeResId);
		initView();
	}

	public ScaleViewDlg(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initView();
	}

	public ScaleViewDlg(Context context) {
		super(context);
		initView();
	}

	public ScaleViewDlg(Activity context) {
		super(context);
		initView();
	}

	public ScaleViewDlg(Context context, float alpha, int gravity) {
		super(context, alpha, gravity);
		initView();
	}

	public ScaleImageView getRxScaleImageView() {
		return scaleImageView;
	}

	public void setImagePath(String filePath) {
		this.filePath = filePath;
		scaleImageView.setImage(ImageSource.uri(filePath));
	}

	public void setImageUri(Uri uri) {
		this.fileUri = uri;
		scaleImageView.setImage(ImageSource.uri(uri));
	}

	public void setImageAssets(String assetName) {
		this.fileAssetName = assetName;
		scaleImageView.setImage(ImageSource.asset(assetName));
	}

	public void setImageRes(int resId) {
		this.resId = resId;
		scaleImageView.setImage(ImageSource.resource(resId));
	}

	public void setImageBitmap(Bitmap bitmap) {
		this.fileBitmap = bitmap;
		scaleImageView.setImage(ImageSource.bitmap(fileBitmap));
	}

	private void initView() {
		View dlgView = LayoutInflater.from(context).inflate(R.layout.dlg_scaleview, null);
		scaleImageView = dlgView.findViewById(R.id.rx_scale_view);
		scaleImageView.setMaxScale(20);
		ImageView ivClose = dlgView.findViewById(R.id.iv_close);
		ivClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cancel();
			}
		});
		setFullScreen();
		setContentView(dlgView);
	}
}
