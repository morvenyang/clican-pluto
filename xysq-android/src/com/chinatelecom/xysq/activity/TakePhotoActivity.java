package com.chinatelecom.xysq.activity;

import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.chinatelecom.xysq.bean.PhotoItem;

public class TakePhotoActivity extends BaseActivity implements
		android.view.SurfaceHolder.Callback, PictureCallback {

	public static final int MAX_WIDTH = 200;
	public static final int MAX_HEIGHT = 200;

	private SurfaceView surfaceView;
	private SurfaceHolder holder;

	private Camera camera; // 这个是hardare的Camera对象

	private Button takePhotoButton;
	private Button cancelButton;
	private Button submitButton;
	private Button switchButton;
	private Button autoManualButton;

	private Uri currentUri;

	private boolean front = false;
	private boolean auto = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_photo);
		takePhotoButton = (Button) findViewById(R.id.take_photo_button);
		cancelButton = (Button) findViewById(R.id.take_photo_cancel_button);
		submitButton = (Button) findViewById(R.id.take_photo_submit_button);
		switchButton = (Button) findViewById(R.id.take_photo_switch_button);
		autoManualButton = (Button) findViewById(R.id.take_photo_auto_manual_button);

		submitButton.setVisibility(View.INVISIBLE);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent(TakePhotoActivity.this,
						TopicAndPostActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("selectPhoto", true);
				String filePath = currentUri.getPath();
				String fileName = StringUtils.substringAfterLast(filePath, "/");
				PhotoItem pi = new PhotoItem(filePath, fileName);
				intent.putExtra("takePhotoBit", pi);
				startActivity(intent);
			}
		});
		switchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopCamera();
				front = !front;
				startCamera(front, holder);
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		autoManualButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (auto) {
					autoManualButton
							.setBackgroundResource(R.drawable.photo_icon_manual);
				} else {
					autoManualButton
							.setBackgroundResource(R.drawable.photo_icon_auto);
				}
				auto = !auto;
				surfaceView.setFocusable(auto);
			}
		});
		surfaceView = (SurfaceView) findViewById(R.id.take_photo_surfaceView);
		surfaceView.setFocusable(auto);
		surfaceView.setFocusableInTouchMode(true);
		surfaceView.setClickable(true);
		holder = surfaceView.getHolder();
		holder.addCallback(this);

		// 点击"Photo Button"按钮照相
		takePhotoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				camera.takePicture(null, null, null, TakePhotoActivity.this);
			}
		});
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Uri imageUri = this.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new ContentValues());
		currentUri = imageUri;
		try {
			OutputStream os = this.getContentResolver().openOutputStream(
					imageUri);
			os.write(data);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.e("XYSQ", "拍照失败", e);
		}
		camera.startPreview();
		submitButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	private void startCamera(boolean front, SurfaceHolder holder) {
		// 当Surface被创建的时候，该方法被调用，可以在这里实例化Camera对象
		// 同时可以对Camera进行定制
		if (front) {
			camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT); // 获取Camera实例
		} else {
			camera = Camera.open(CameraInfo.CAMERA_FACING_BACK); // 获取Camera实例
		}
		try {

			if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				// 在2.2以上可以使用
				camera.setDisplayOrientation(90);
			} else {
				// 在2.2以上可以使用
				camera.setDisplayOrientation(0);
			}
			Parameters parameters = camera.getParameters();
			List<Size> sizes = parameters.getSupportedPictureSizes();
			Camera.Size size = sizes.get(0);
			for (int i = 0; i < sizes.size(); i++) {
				if (sizes.get(i).width > size.width)
					size = sizes.get(i);
			}
			Log.d("XYSQ", "camera " + size.width + "*" + size.height);
			parameters.setPictureSize(size.width, size.height);
			parameters.setPictureFormat(ImageFormat.JPEG);// 设置照片的输出格式
			parameters.setJpegQuality(100);
			camera.setParameters(parameters);
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
			// 如果出现异常，则释放Camera对象
			camera.release();
		}

		// 启动预览功能
		camera.startPreview();
		camera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				
			}
		});
	}

	private void stopCamera() {
		camera.stopPreview();
		camera.release();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		this.startCamera(front, holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 当Surface被销毁的时候，该方法被调用
		// 在这里需要释放Camera资源
		stopCamera();

	}

	@Override
	protected String getPageName() {
		return "拍照";
	}

}
