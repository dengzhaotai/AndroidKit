package com.dzt.androidkit.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.dzt.androidkit.eventbus.Event;
import com.dzt.androidkit.eventbus.EventBusUtil;
import com.dzt.androidkit.utils.ActivityStackManager;
import com.dzt.androidkit.utils.JPreferenceKit;
import com.dzt.androidkit.utils.JToastKit;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 请求Android6.0以上的危险权限，这里不处理UI相关代码
 */
public abstract class SuperActivity extends AppCompatActivity {
	public static final int REQUEST_PERMISSION_CODE = 1;
	protected Context context;
	protected String[] permissions;
	protected Bundle savedInstanceState;
	protected JPreferenceKit sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		context = this;
		sharedPreferences = JPreferenceKit.getInstance();
		ActivityStackManager.getInstance().pushActivity(this);

		permissions = initPermissions();
		if (checkPermissionIsGranted()) {
			initData(savedInstanceState);
		} else {
			ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE);
		}
		EventBusUtil.register(this);
	}

	/**
	 * 接收消息函数在主线程，且为粘性事件
	 * From FramgmentOne
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(Event response) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityStackManager.getInstance().popActivity(this);
		JToastKit.getInstance().cancelCurrentToast();
		EventBusUtil.unregister(this);
	}

	protected abstract void initData(Bundle savedInstanceState);
	protected abstract String[] initPermissions();

	protected void showToast(String msg) {
		JToastKit.getInstance().showMessage(msg);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == REQUEST_PERMISSION_CODE) {
			if (permissionIsGranted(grantResults)) {
				initData(savedInstanceState);
			} else {
				showToast("权限被拒绝，关闭程序");
				finish();
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	private boolean permissionIsGranted(int[] grantResults) {
		for (int grant : grantResults) {
			if (grant != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	private boolean checkPermissionIsGranted() {
		if(permissions == null || permissions.length == 0)
			return true;
		for (String permission : permissions) {
			if(ContextCompat.checkSelfPermission(context, permission)
					!= PackageManager.PERMISSION_GRANTED){
				return false;
			}
		}
		return true;
	}

	protected void startActivity(Class<?> cls, Intent intent) {
		if (intent == null) {
			intent = new Intent(context, cls);
		}else{
			intent.setClass(context, cls);
		}
		startActivity(intent);
	}

	protected void startActivityForResult(Class<?> cls, Intent intent, int requestCode) {
		if (intent == null) {
			intent = new Intent(context, cls);
		}else{
			intent.setClass(context, cls);
		}
		startActivityForResult(intent, requestCode);
	}
}
