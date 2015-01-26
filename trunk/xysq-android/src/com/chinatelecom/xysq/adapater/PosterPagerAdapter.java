package com.chinatelecom.xysq.adapater;

import java.util.List;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chinatelecom.xysq.activity.AnnouncementActivity;
import com.chinatelecom.xysq.activity.BroadbandRemindActivity;
import com.chinatelecom.xysq.activity.ForumActivity;
import com.chinatelecom.xysq.activity.IndexActivity;
import com.chinatelecom.xysq.activity.NoticeActivity;
import com.chinatelecom.xysq.activity.StoreDetailActivity;
import com.chinatelecom.xysq.bean.Poster;
import com.chinatelecom.xysq.http.ImageRequest;
import com.chinatelecom.xysq.listener.HtmlLinkOnClickListener;
import com.chinatelecom.xysq.listener.IndexOnClickListener;
import com.chinatelecom.xysq.util.AlertUtil;

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
		ImageRequest.requestImage(imageView, poster.getImagePath(), 720, 320);
		if (poster.getType().equals("HTML5")) {
			imageView.setOnClickListener(new HtmlLinkOnClickListener(poster
					.getHtml5Link(), poster.getName(), activity, false));
		} else if (poster.getType().equals("INNER_MODULE")) {
			if (poster.getInnerModule().equals("ANNOUNCEMENT")) {
				imageView.setOnClickListener(new IndexOnClickListener(activity,
						AnnouncementActivity.class, false, true));
			} else if (poster.getInnerModule().equals("NOTICE")) {
				imageView.setOnClickListener(new IndexOnClickListener(activity,
						NoticeActivity.class, false, true));
			} else if (poster.getInnerModule().equals("BBS")) {
				imageView.setOnClickListener(new IndexOnClickListener(activity,
						ForumActivity.class, true, true));
			} else if (poster.getInnerModule().equals("PARKING")) {
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertUtil.alert(activity, "开发中...");
					}
				});
			} else if (poster.getInnerModule().equals("BROADBAND_REMIND")) {
				imageView.setOnClickListener(new IndexOnClickListener(activity,
						BroadbandRemindActivity.class, true, false));
			}
		} else if(poster.getType().equals("STORE_DETAIL")) {
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity, StoreDetailActivity.class);
					intent.putExtra("storeId", poster.getStoreId());
					activity.startActivity(intent);
				}
			});
		}
		((ViewPager) container).addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}

}
