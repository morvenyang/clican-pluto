package com.chinatelecom.xysq.adapater;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.chinatelecom.xysq.bean.PhotoItem;
import com.chinatelecom.xysq.layout.PhotoGridItem;

public class PhotoAdapter extends BaseAdapter   {
	private Context context;
	private List<PhotoItem> bitList;
	public PhotoAdapter(Context context, List<PhotoItem> bitList) {
		this.context = context;
		this.bitList = bitList;
	}

	@Override
	public int getCount() {
		return bitList.size();
	}

	@Override
	public Object getItem(int position) {
		return bitList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoGridItem item;
		if(convertView == null){
			item = new PhotoGridItem(context);
			 item.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,  
                     LayoutParams.MATCH_PARENT));
		}else{
			item = (PhotoGridItem)convertView;
		}
		item.SetBitmap(bitList.get(position).getBitmap());
        boolean flag = bitList.get(position).isSelect();
		item.setChecked(flag);
		return item;
	}
}
