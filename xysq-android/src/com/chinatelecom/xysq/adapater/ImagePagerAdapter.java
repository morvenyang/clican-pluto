package com.chinatelecom.xysq.adapater;

import java.util.List;

import com.chinatelecom.xysq.activity.AnnouncementActivity;
import com.chinatelecom.xysq.activity.BroadbandRemindActivity;
import com.chinatelecom.xysq.activity.ForumActivity;
import com.chinatelecom.xysq.activity.NoticeActivity;
import com.chinatelecom.xysq.bean.Poster;
import com.chinatelecom.xysq.http.ImageRequest;
import com.chinatelecom.xysq.listener.HtmlLinkOnClickListener;
import com.chinatelecom.xysq.listener.IndexOnClickListener;
import com.chinatelecom.xysq.util.AlertUtil;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImagePagerAdapter extends PagerAdapter {

	private List<String> images;

	private Activity activity;


	public ImagePagerAdapter(List<String> images, Activity activity) {
		this.images = images;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return images.size();
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
		String imagePath = images.get(position);
		ImageRequest.requestImage(imageView, imagePath, 720, 320);
		((ViewPager) container).addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}

}
