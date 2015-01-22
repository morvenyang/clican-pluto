package com.chinatelecom.xysq.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

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
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
			MediaStore.Images.Media.DATA
	};

	public static void loadPhtotAlbum(final Activity activity,
			final HttpCallback callback) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				List<PhotoAlbum> aibumList = new ArrayList<PhotoAlbum>();
				Cursor cursor = MediaStore.Images.Media.query(
						activity.getContentResolver(),
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						STORE_IMAGES);
				Map<String, PhotoAlbum> countMap = new HashMap<String, PhotoAlbum>();
				PhotoAlbum pa = null;
				while (cursor.moveToNext()) {
					String id = cursor.getString(3);
					String dir_id = cursor.getString(4);
					String dir = cursor.getString(5);
					String filePath = cursor.getString(6);
					if (!countMap.containsKey(dir_id)) {
						pa = new PhotoAlbum();
						pa.setName(dir);
						pa.setBitmap(Integer.parseInt(id));
						pa.setCount("1");
						pa.getBitList().add(new PhotoItem(Integer.valueOf(id),filePath));
						countMap.put(dir_id, pa);
					} else {
						pa = countMap.get(dir_id);
						pa.setCount(String.valueOf(Integer.parseInt(pa
								.getCount()) + 1));
						pa.getBitList().add(new PhotoItem(Integer.valueOf(id),filePath));
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
					callback.success("/loadPhtotAlbum", result.getResult());
				} else {
					callback.failure("/loadPhtotAlbum", result.getCode(),
							result.getMessage());
				}
			}
		};
		task.execute(new String[] {});
	}

	private static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		Log.d("XYSQ", "width:"+bitmap.getWidth());
		Log.d("XYSQ", "height:"+bitmap.getHeight());
		return bitmap;
	}

	public static void prepareThumbnail(final Activity activity,
			final HttpCallback callback, final PhotoAlbum album) {
		AsyncTask<String, Void, TaskResult> task = new AsyncTask<String, Void, TaskResult>() {
			@Override
			protected TaskResult doInBackground(String... params) {
				for (PhotoItem pi : album.getBitList()) {
					pi.setBitmap(getImageThumbnail(pi.getFilePath(),200,200));
				}
				return new TaskResult(1, "", album);
			}

			@Override
			protected void onPostExecute(TaskResult result) {
				if (result.getCode() == 1) {
					callback.success("/prepareThumbnail", result.getResult());
				} else {
					callback.failure("/prepareThumbnail", result.getCode(),
							result.getMessage());
				}
			}
		};
		task.execute(new String[] {});
	}

}
