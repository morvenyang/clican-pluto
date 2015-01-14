package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chinatelecom.xysq.activity.IndexActivity;
import com.chinatelecom.xysq.bean.Poster;
import com.chinatelecom.xysq.http.ImageRequest;

public class PosterPagerAdapter extends PagerAdapter {

	private List<Poster> posterList;

	private IndexActivity activity;

	private LayoutInflater inflater;

	public PosterPagerAdapter(List<Poster> posterList, IndexActivity activity,
			LayoutInflater inflater) {
		this.posterList = posterList;
		this.activity = activity;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return posterList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(activity);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(params);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		final Poster poster = posterList.get(position);
		ImageRequest.requestImage(imageView, poster.getImagePath());
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("XYSQ", "click poster:"+poster.getId());
			}
		});
		((ViewPager) container).addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}

}