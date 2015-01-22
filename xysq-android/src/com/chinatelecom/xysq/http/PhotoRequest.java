package com.chinatelecom.xysq.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.chinatelecom.xysq.bean.PhotoAlbum;
import com.chinatelecom.xysq.bean.PhotoItem;

public class PhotoRequest {

	// 设置获取图片的字段信
		private static final String[] STORE_IMAGES = {
				MediaStore.Images.Media.DISPLAY_NAME, // 显示的名字
				MediaStore.Images.Media.LATITUDE, // 维度
				MediaStore.Images.Media.LONGITUDE, // 经度
				MediaStore.Images.Media._ID, // id
				MediaStore.Images.Media.BUCKET_ID, // dir id 目录
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME // dir name 目录名字
		};
		
	public static void loadPhtotAlbum(final Activity activity,final HttpCallback callback){
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
					List<PhotoAlbum> aibumList = new ArrayList<PhotoAlbum>();
					Cursor cursor = MediaStore.Images.Media.query(activity.getContentResolver(),
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
					Map<String, PhotoAlbum> countMap = new HashMap<String, PhotoAlbum>();
					PhotoAlbum pa = null;
					while (cursor.moveToNext()) {
						String id = cursor.getString(3);
						String dir_id = cursor.getString(4);
						String dir = cursor.getString(5);
						if (!countMap.containsKey(dir_id)) {
							pa = new PhotoAlbum();
							pa.setName(dir);
							pa.setBitmap(Integer.parseInt(id));
							pa.setCount("1");
							pa.getBitList().add(new PhotoItem(Integer.valueOf(id)));
							countMap.put(dir_id, pa);
						} else {
							pa = countMap.get(dir_id);
							pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
							pa.getBitList().add(new PhotoItem(Integer.valueOf(id)));
						}
					}
					cursor.close();
					Iterable<String> it = countMap.keySet();
					for (String key : it) {
						aibumList.add(countMap.get(key));
					}
					return new TaskResult(1, "", aibumList);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/loadPhtotAlbum",
							result.getResult());
				} else {
					callback.failure("/loadPhtotAlbum",
							result.getCode(), result.getMessage());
				}
			}
		};
		task.execute(new String[] {});
	}
}
