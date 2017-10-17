package com.dzt.androidkit.eventbus;

/**
 * Created by dzt on 2017-10-10.
 */
public class BaseEvent {
    private Object mSource;

    public BaseEvent(Object source) {
        this.mSource = source;
    }
}
