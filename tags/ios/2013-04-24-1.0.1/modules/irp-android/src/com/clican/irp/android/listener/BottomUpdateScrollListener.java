package com.clican.irp.android.listener;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class BottomUpdateScrollListener implements OnScrollListener {

	private boolean mTopFreeDisplayFoot = false;

	public abstract void triggerBottomUpdate(AbsListView view);

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mTopFreeDisplayFoot) {
			triggerBottomUpdate(view);
		}
	}

	public void onScroll(AbsListView view, int first, int count, int total) {
		if (first + count >= total) {
			// reach bottom of the ListView
			mTopFreeDisplayFoot = true;
		} else {
			mTopFreeDisplayFoot = false;
		}
	}

}
