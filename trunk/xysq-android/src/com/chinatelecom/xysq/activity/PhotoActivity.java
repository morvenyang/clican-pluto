package com.chinatelecom.xysq.activity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.adapater.PhotoAdappter;
import com.chinatelecom.xysq.bean.PhotoAlbum;

public class PhotoActivity extends Activity  {
	private GridView gv;
	private PhotoAlbum album;
	private PhotoAdappter adapter;
	private TextView tv;
	private int chooseNum = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		tv = (TextView)findViewById(R.id.photo_chooseNum);
		album = (PhotoAlbum)getIntent().getExtras().get("album");
		/**获取已经选择的图片**/
		for (int i = 0; i < album.getBitList().size(); i++) {
			if(album.getBitList().get(i).isSelect()){
				chooseNum++;
			}
		}
		gv =(GridView)findViewById(R.id.photo_gridView);
		adapter = new PhotoAdappter(this,album);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(gvItemClickListener);
		tv.setText(chooseNum+"");
	}
	
	
	private OnItemClickListener gvItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if( album.getBitList().get(position).isSelect()){
				album.getBitList().get(position).setSelect(false);
				chooseNum--;
			}else{
				album.getBitList().get(position).setSelect(true);
				chooseNum++;
			}
			tv.setText(chooseNum+"");
			adapter.notifyDataSetChanged();
		}
	};
}
