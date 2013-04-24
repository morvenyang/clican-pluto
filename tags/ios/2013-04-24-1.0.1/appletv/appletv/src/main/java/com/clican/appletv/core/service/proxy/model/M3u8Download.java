package com.clican.appletv.core.service.proxy.model;

import java.util.List;

public class M3u8Download {

	private String m3u8Url;

	private List<M3u8DownloadLine> m3u8DownloadLines;

	private int downloadIndex = 0;

	public String getM3u8Url() {
		return m3u8Url;
	}

	public void setM3u8Url(String m3u8Url) {
		this.m3u8Url = m3u8Url;
	}

	public List<M3u8DownloadLine> getM3u8DownloadLines() {
		return m3u8DownloadLines;
	}

	public void setM3u8DownloadLines(List<M3u8DownloadLine> m3u8DownloadLines) {
		this.m3u8DownloadLines = m3u8DownloadLines;
	}

	public void seekDownloadLine(String localPath) {
		synchronized (this) {
			for (int i = 0; i < m3u8DownloadLines.size(); i++) {
				M3u8DownloadLine downloadLine = m3u8DownloadLines.get(i);
				if (downloadLine.getLocalPath().equals(localPath)) {
					downloadIndex = i;
					break;
				}
			}
		}
	}

	public M3u8DownloadLine getNextDownloadLine() {
		synchronized (this) {
			if (downloadIndex < m3u8DownloadLines.size()) {
				M3u8DownloadLine next = m3u8DownloadLines.get(downloadIndex);
				downloadIndex++;
				return next;
			} else {
				return null;
			}
		}
	}

}
