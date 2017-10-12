package com.dzt.androidkit.utils;

import android.app.Activity;

import java.util.Stack;

public class ActivityStackManager {
	private Stack<Activity> activityStack;
	private Stack<Activity> activityBack;

	private ActivityStackManager() {
	}

	private static class SingletonHolder {
		static ActivityStackManager sInstance = new ActivityStackManager();
	}

	public static ActivityStackManager getInstance() {
		return SingletonHolder.sInstance;
	}

	public void init() {
		activityStack = new Stack<>();
		activityBack = new Stack<>();
	}

	/**
	 * 进栈
	 *
	 * @param activity activity
	 */
	public void pushActivity(Activity activity) {
		activityStack.add(activity);
	}

	/**
	 * 出栈
	 *
	 * @param activity activity
	 */
	public void popActivity(Activity activity) {
		if (activityStack != null && activityStack.size() > 0) {
			if (activity != null) {
				activityStack.remove(activity);
			}
		}
	}

	public void pushBackActivity(Activity activity) {
		activityBack.add(activity);
	}

	public void popBackActivity(Activity activity) {
		if (activityBack != null && activityBack.size() > 0) {
			if (activity != null) {
				activityBack.remove(activity);
			}
		}
	}

	public int getBackActivitySize() {
		return activityBack.size();
	}

	public int getActivitySize() {
		return activityStack.size();
	}

	public Activity getLastActivity() {
		return activityStack.lastElement();
	}

	public void finishAllActivity() {
		if (activityStack != null) {
			while (!activityStack.empty()) {
				Activity activity = activityStack.pop();
				if (activity == null)
					continue;
				activity.finish();
			}
		}
	}
}
