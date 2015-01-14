package com.chinatelecom.xysq.comp.pullrefresh.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * @author mrsimple
 * @param <T>
 */
public abstract class RefreshAdaterView<T extends AbsListView> extends RefreshLayoutBase<T> {

    public RefreshAdaterView(Context context) {
        this(context, null);
    }

    public RefreshAdaterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshAdaterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 
     */
    public void setAdapter(ListAdapter adapter) {
        mContentView.setAdapter(adapter);
    }

    public ListAdapter getAdapter() {
        return mContentView.getAdapter();
    }

}
