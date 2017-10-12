package com.dzt.androidkit.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class JToastKit {
	private Handler handler = new Handler();
	private Toast toast = null;
	private Object synObj = new byte[0];
	private Context context;

	private JToastKit() {
	}

	private static class SingletonHolder {
		static JToastKit sInstance = new JToastKit();
	}

	public static JToastKit getInstance() {
		return SingletonHolder.sInstance;
	}

	public void init(Context context) {
		this.context = context;
	}

	public void showMessage(final int resId) {
		String msg = context.getString(resId);
		showMessage(msg, Toast.LENGTH_LONG);
	}

	public void showMessage(final String msg) {
		showMessage(msg, Toast.LENGTH_LONG);
	}

	public void showMessage(final String msg,
							final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							if (toast == null) {
								toast = Toast.makeText(context, msg, len);
							}
							toast.setText(msg);
							//toast.setGravity(Gravity.CENTER, 0, 0);
							toast.setDuration(len);
							toast.show();
						}
					}
				});
			}
		}).start();
	}

	public void cancelCurrentToast() {
		if (toast != null) {
			toast.cancel();
		}
	}
}
