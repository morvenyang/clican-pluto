package com.chinatelecom.xysq.comp.pullrefresh.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.chinatelecom.xysq.comp.pullrefresh.base.PullRefreshBase;

/**
 * @author mrsimple
 */
public class PullRefreshTextView extends PullRefreshBase<TextView> {

    public PullRefreshTextView(Context context) {
        super(context);
    }

    public PullRefreshTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initContentView() {
        // 初始化mContentView
        mContentView = new TextView(getContext());
        mContentView.setTextSize(20f);
        mContentView.setGravity(Gravity.CENTER);
        mContentView.setBackgroundColor(Color.CYAN);
    }

    /*
     * 是否滑动到了顶端，如果返回true, 则表示到了顶端，用户继续下拉则触发下拉刷新.由于TextView默认没有滑动，因此直接返回true.
     * @see com.uit.pullrefresh.base.PullRefreshBase#isTop()
     */
    @Override
    protected boolean isTop() {
        return true;
    }
}
