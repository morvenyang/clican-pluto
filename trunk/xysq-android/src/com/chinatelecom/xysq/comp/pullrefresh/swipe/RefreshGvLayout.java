package com.chinatelecom.xysq.comp.pullrefresh.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author mrsimple
 */
public class RefreshGvLayout extends RefreshLayout<GridView> {

    public RefreshGvLayout(Context context) {
        this(context, null);
    }

    public RefreshGvLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
