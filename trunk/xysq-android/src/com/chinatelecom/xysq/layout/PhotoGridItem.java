package com.chinatelecom.xysq.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.PhotoItem;
import com.chinatelecom.xysq.http.ImageRequest;

public class PhotoGridItem extends RelativeLayout implements Checkable {
	private Context mContext;
	private boolean mCheck;
	private ImageView mImageView;
	private ImageView mSelect;
	
	public PhotoGridItem(Context context) {
		this(context, null, 0);
	}
	
	public PhotoGridItem(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }

	public PhotoGridItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.photo_item, this);
		mImageView = (ImageView)findViewById(R.id.photo_imageView);
		mSelect = (ImageView)findViewById(R.id.photo_selectedImageView);
	}

	public void hideSelectedFlag(){
		mSelect.setVisibility(View.INVISIBLE);
	}
	@Override
	public void setChecked(boolean checked) {
		mCheck = checked;
		if(checked){
			mSelect.setVisibility(View.VISIBLE);
		}else{
			mSelect.setVisibility(View.INVISIBLE);
		}
	}   
	
	@Override
	public boolean isChecked() {
		return mCheck;
	}

	@Override
	public void toggle() {  
		setChecked(!mCheck);
	}
	
	public void setImgResID(int id){
		if(mImageView != null){
			mImageView.setBackgroundResource(id);
		}
	}
	
	public void setBitmap(Bitmap bit){
		if(mImageView != null){
			mImageView.setImageBitmap(bit);
		}
	}
	
	public void setImageUrl(String imageUrl){
		if(mImageView != null){
			DisplayMetrics dm =mContext.getResources().getDisplayMetrics();  
	        int wScreen = dm.widthPixels;  
	        
			
			ImageRequest.requestImage(mImageView, imageUrl,(wScreen-125)/4,(wScreen-125)/5);
		}
	}
	
	
}
