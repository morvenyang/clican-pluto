package com.clican.appletv.core.service.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.BaseClient;
import com.clican.appletv.core.service.proxy.model.M3u8Download;
import com.clican.appletv.core.service.proxy.model.M3u8DownloadLine;

public class ProxyClientImpl extends BaseClient implements ProxyClient {

	private final static Log log = LogFactory.getLog(ProxyClientImpl.class);

	private String m3u8Url;

	private String m3u8String;

	private M3u8Download m3u8Download;

	private String m3u8RelativeUrl;

	private SpringProperty springProperty;

	private LinkedBlockingQueue<Runnable> senderQueue = new LinkedBlockingQueue<Runnable>();
	private ThreadPoolExecutor sendExecutor = new ThreadPoolExecutor(5, 5, 60,
			TimeUnit.SECONDS, senderQueue);

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@Override
	public M3u8Download getM3u8Download() {
		return m3u8Download;
	}

	@Override
	public String doSyncRequestByM3U8Url(String url, boolean start) {
		if (this.m3u8Url == null || !this.m3u8Url.equals(url)) {
			this.m3u8Url = url;
			int lastSlashIndex = url.lastIndexOf("/");
			if (lastSlashIndex != 1) {
				this.m3u8RelativeUrl = url.substring(lastSlashIndex);
			} else {
				this.m3u8RelativeUrl = url;
			}
			m3u8Download = new M3u8Download();
			m3u8Download.setM3u8Url(this.m3u8Url);
			try {
				File m3u8Path = new File(springProperty.getSystemTempPath()
						+ "/m3u8");
				if (!m3u8Path.exists()) {
					m3u8Path.mkdirs();
				} else {
					FileUtils.deleteDirectory(m3u8Path);
					m3u8Path.mkdirs();
				}
				this.m3u8String = this.httpGet(m3u8Url);
				String[] lines = this.m3u8String.split("\n");
				int j = 0;
				List<M3u8DownloadLine> m3u8DownloadLines = new ArrayList<M3u8DownloadLine>();
				m3u8Download.setM3u8DownloadLines(m3u8DownloadLines);
				for (int i = 0; i < lines.length; i++) {
					String line = lines[i];
					if (StringUtils.isNotEmpty(line) && !line.startsWith("#")) {
						M3u8DownloadLine m3u8DownloadLine = new M3u8DownloadLine();
						m3u8DownloadLine.setOriginalUrl(line);
						m3u8DownloadLine.setLocalUrl(springProperty
								.getSystemServerUrl()
								+ "/ctl/proxy/temp/m3u8"
								+ i
								+ ".ts");
						m3u8DownloadLine.setLocalPath(m3u8Path
								.getAbsolutePath() + "/" + i + ".ts");
						m3u8DownloadLines.add(m3u8DownloadLine);
						this.m3u8String.replaceFirst(
								m3u8DownloadLine.getOriginalUrl(),
								m3u8DownloadLine.getLocalUrl()+"?m3u8Url="+m3u8Url);
						j++;
					}
				}
				sendExecutor.shutdownNow();
				senderQueue.clear();
				sendExecutor = new ThreadPoolExecutor(5, 5, 60,
						TimeUnit.SECONDS, senderQueue);
				if (start) {
					this.startM3u8();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return m3u8String;
	}

	@Override
	public void seekDownloadLine(String localPath) {
		this.m3u8Download.seekDownloadLine(localPath);
		
	}

	private Runnable getM3u8Task(final M3u8DownloadLine m3u8DownloadLine,
			final String m3u8Url) {
		Runnable m3u8Runnable = new Runnable() {
			@Override
			public void run() {
				try {
					String reqUrl = m3u8DownloadLine.getOriginalUrl();
					if (!reqUrl.startsWith("http")) {
						reqUrl = m3u8RelativeUrl + "/" + reqUrl;
					}
					byte[] data = httpGetByData(reqUrl, null, null);
					File fileDownload = new File(m3u8DownloadLine.getLocalPath()+".download");
					File file = new File(m3u8DownloadLine.getLocalPath());
					if(!fileDownload.getParentFile().exists()){
						fileDownload.getParentFile().mkdirs();
					}
					FileUtils.writeByteArrayToFile(fileDownload, data);
					FileUtils.moveFile(fileDownload, file);
					m3u8DownloadLine.setFinished(true);
					if (ProxyClientImpl.this.m3u8Url.equals(m3u8Url)) {
						addAsyncM3u8TSRequest();
					}
				} catch (Exception e) {
					log.error(e.getMessage());
					if (ProxyClientImpl.this.m3u8Url.equals(m3u8Url)) {
						Runnable task = getM3u8Task(m3u8DownloadLine, m3u8Url);
						sendExecutor.submit(task);
					}
				}
			}
		};
		return m3u8Runnable;
	}

	private void addAsyncM3u8TSRequest() {
		M3u8DownloadLine m3u8DownloadLine = this.m3u8Download
				.getNextDownloadLine();
		if (m3u8DownloadLine == null) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("downloading m3u8 ts " + m3u8DownloadLine.getLocalPath());
		}
		Runnable task = this.getM3u8Task(m3u8DownloadLine,this.m3u8Download.getM3u8Url());
		sendExecutor.submit(task);
	}

	public void startM3u8() {
		for (int i = 0; i < 5 && i < m3u8Download.getM3u8DownloadLines().size(); i++) {
			addAsyncM3u8TSRequest();
		}
	}
}
