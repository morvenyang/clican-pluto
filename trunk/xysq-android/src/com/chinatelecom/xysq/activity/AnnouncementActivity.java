package com.chinatelecom.xysq.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.comp.pullrefresh.listener.OnLoadListener;
import com.chinatelecom.xysq.comp.pullrefresh.listener.OnRefreshListener;
import com.chinatelecom.xysq.comp.pullrefresh.scroller.impl.RefreshListView;

public class AnnouncementActivity extends Activity {

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		this.setListView();
	}
	
	private void setListView() {
		List<String> sList = new ArrayList<String>();
		for(int i=0;i<20;i++){
			sList.add("Item - "+i);
		}
        final RefreshListView refreshLayout = (RefreshListView)this.findViewById(R.id.announcement_listView);

        // 获取ListView, 这里的listview就是Content view
        refreshLayout.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sList));
        // 设置下拉刷新监听器
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "refreshing", Toast.LENGTH_SHORT)
                        .show();

                refreshLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshLayout.refreshComplete();
                    }
                }, 1500);
            }
        });

        // 不设置的话到底部不会自动加载
        refreshLayout.setOnLoadListener(new OnLoadListener() {

            @Override
            public void onLoadMore() {
                Toast.makeText(getApplicationContext(), "loading", Toast.LENGTH_SHORT)
                        .show();

                refreshLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshLayout.loadCompelte();
                    }
                }, 1500);
            }
        });

    }
}
