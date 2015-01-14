package com.chinatelecom.xysq.comp.pullrefresh.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

/**
 * @author mrsimple
 */
public class RefreshLvLayout extends RefreshLayout<ListView> {

    public RefreshLvLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLvLayout(Context context) {
        this(context, null);
    }

    @Override
    public void setLoading(boolean loading) {
        super.setLoading(loading);
        if (isLoading && mAbsListView.getFooterViewsCount() == 0) {
            mAbsListView.addFooterView(mListViewFooter);
        } else {
            if (mAbsListView.getAdapter() instanceof HeaderViewListAdapter) {
                mAbsListView.removeFooterView(mListViewFooter);
            } else {
                mListViewFooter.setVisibility(View.GONE);
            }
            mYDown = 0;
            mLastY = 0;
        }
    }
}
