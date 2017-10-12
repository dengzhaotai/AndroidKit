package com.dzt.androidkit.mvp;

/**
 * UI响应
 * @param <T>
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}
