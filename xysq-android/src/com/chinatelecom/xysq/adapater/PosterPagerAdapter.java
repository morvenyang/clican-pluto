package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chinatelecom.xysq.activity.IndexActivity;
import com.chinatelecom.xysq.bean.Poster;

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
		imageView.getLayoutParams().height = 100;
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		Poster poster = posterList.get(position);
		imageView.setImageURI(Uri.parse(poster.getImagePath()));
		((ViewPager) container).addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}

}
