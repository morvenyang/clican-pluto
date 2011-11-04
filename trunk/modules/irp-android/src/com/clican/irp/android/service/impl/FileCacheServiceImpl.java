package com.clican.irp.android.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;

import com.clican.irp.android.enumeration.ApplicationUrl;
import com.clican.irp.android.http.HttpGateway;
import com.clican.irp.android.service.FileCacheService;
import com.google.inject.Inject;

public class FileCacheServiceImpl implements FileCacheService {

	@Inject
	private HttpGateway httpGateway;

	@Inject
	private Context context;

	// 50M
	private long MAX_FILE_CACHE_SIZE = 1024 * 1024 * 50;

	@Override
	public File getFile(Long reportId) {
		File file = context.getFileStreamPath(reportId.toString()+".pdf");
		if (file.exists()) {
			return file;
		}
		
		String url = ApplicationUrl.DOWNLOAD_REPORT.getUrl() + "?reportId="
				+ reportId;
		FileOutputStream fos = null;
		try {
			byte[] data = httpGateway.downloadConentBySession(url);
			if (data == null || data.length == 0) {
				return null;
			}
			clearHistoryCache();
			fos = context.openFileOutput(reportId.toString()+".pdf",
					Context.MODE_WORLD_READABLE);
			fos.write(data);
			fos.flush();
			return context.getFileStreamPath(reportId.toString()+".pdf");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private void clearHistoryCache() {
		File file = context.getFileStreamPath("");
		File[] files = file.listFiles();
		long totalSize = 0;
		for (File f : files) {
			totalSize += f.length();
		}
		if (totalSize < MAX_FILE_CACHE_SIZE) {
			return;
		}
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File arg0, File arg1) {
				long result = arg0.lastModified() - arg1.lastModified();
				if (result == 0) {
					return 0;
				} else if (result > 0) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			totalSize = totalSize - f.length();
			f.delete();
			if (totalSize < MAX_FILE_CACHE_SIZE) {
				return;
			}
		}
	}

	@Override
	public void clearCache() {
		File file = context.getFileStreamPath("");
		File[] files = file.listFiles();
		for (File f : files) {
			f.delete();
		}
	}

}
