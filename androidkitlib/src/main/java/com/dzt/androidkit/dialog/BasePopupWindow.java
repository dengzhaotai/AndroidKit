package com.dzt.androidkit.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.dzt.androidkit.R;


/**
 * Created by dzt on 2016/5/30.
 */

public class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(Context context) {
        super(context);
        initBasePopupWindow();
    }

    /**
     * 初始化BasePopupWindow的一些信息
     * */
    private void initBasePopupWindow() {
        setAnimationStyle(R.style.style_top_window_animation);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(true);
        setFocusable(true);
    }
}
