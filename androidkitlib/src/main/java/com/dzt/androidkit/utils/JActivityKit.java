package com.dzt.androidkit.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by dzt on 2017/10/17.
 */

public class JActivityKit {
	public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameId) {
		if(!fragment.isAdded()){
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.add(frameId, fragment);
			transaction.commitAllowingStateLoss();
		}
	}
}
