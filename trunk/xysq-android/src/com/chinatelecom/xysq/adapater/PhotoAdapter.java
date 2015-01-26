package com.chinatelecom.xysq.adapater;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.chinatelecom.xysq.bean.PhotoItem;
import com.chinatelecom.xysq.layout.PhotoGridItem;

public class PhotoAdapter extends BaseAdapter {
	private Context context;
	private List<PhotoItem> bitList;
	private boolean hideSelectedFlag;
	private List<String> imageList;

	public PhotoAdapter(Context context, List<PhotoItem> bitList,
			boolean hideSelectedFlag) {
		this.context = context;
		this.bitList = bitList;
		this.hideSelectedFlag = hideSelectedFlag;
	}
	
	public PhotoAdapter(Context context, List<String> imageList) {
		this.context = context;
		this.imageList = imageList;
		this.hideSelectedFlag = true;
	}

	@Override
	public int getCount() {
		if(bitList!=null){
			return bitList.size();
		}else{
			return imageList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if(bitList!=null){
			return bitList.get(position);
		}else{
			return imageList.get(position);
		}
		
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoGridItem item;
		if (convertView == null) {
			item = new PhotoGridItem(context);
			item.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
		} else {
			item = (PhotoGridItem) convertView;
		}
		if(bitList!=null){
			item.setBitmap(bitList.get(position).getBitmap());
		}else{
			if(!StringUtils.isEmpty(imageList.get(position))){
				item.setImageUrl(imageList.get(position));
			}
		}
		
		if (this.hideSelectedFlag) {
			item.hideSelectedFlag();
		} else {
			if(bitList!=null){
				boolean flag = bitList.get(position).isSelect();
				item.setChecked(flag);
			}
		}
		return item;
	}
}
