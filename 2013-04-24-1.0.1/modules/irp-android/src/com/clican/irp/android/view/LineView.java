package com.clican.irp.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;

public class LineView extends TextView {

	public LineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LineView(Context context) {
		super(context);
	}

	@Override
	public void draw(Canvas canvas) {
		Paint paint=new Paint();  
		Style style=Style.FILL_AND_STROKE;
		paint.setStyle(style);
		paint.setColor(Color.BLACK);
		canvas.drawLine(0, 0, this.getWidth(), 0, paint);
	}
	
	

}
