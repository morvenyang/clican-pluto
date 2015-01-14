package com.chinatelecom.xysq.comp.pullrefresh.scroller.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.chinatelecom.xysq.comp.pullrefresh.scroller.RefreshLayoutBase;

public class RefreshTextView extends RefreshLayoutBase<TextView> {

    public RefreshTextView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public RefreshTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void setupContentView(Context context) {
        mContentView = new TextView(context);
        // 开启可点击,使得onTouchEvent在处理触摸事件时返回true
        mContentView.setClickable(true);
    }

    @Override
    protected boolean isTop() {
        return true;
    }

    @Override
    protected boolean isBottom() {
        return false;
    }

}
