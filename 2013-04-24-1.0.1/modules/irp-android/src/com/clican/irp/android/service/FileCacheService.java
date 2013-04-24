package com.clican.irp.android.service;

import java.io.File;

public interface FileCacheService {

	public File getFile(Long reportId);

	public void clearCache();
	

}
