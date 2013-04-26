package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;
import com.clican.appletv.core.service.proxy.ProxyClient;
import com.clican.appletv.core.service.proxy.model.M3u8Download;

@Controller
public class ProxyController {

	private final static Log log = LogFactory.getLog(ProxyController.class);

	@Autowired
	private SpringProperty springProperty;

	@Autowired
	private ProxyClient proxyClient;

	@RequestMapping("/proxy/updateVersion.do")
	public void updateVersion(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "version", required = true) String version)
			throws Exception {
		springProperty.setSystemVersion(version);
	}

	@RequestMapping("/proxy/play.m3u8")
	public void playM3u8(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "url", required = false) String url)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("play m3u8 :" + url);
		}
		if (!springProperty.isSystemProxyPlay()) {
			response.sendRedirect(url);
		} else {
			String m3u8String = proxyClient.doSyncRequestByM3U8Url(url, true);
			response.getOutputStream().write(m3u8String.getBytes("utf-8"));
		}
	}

	@RequestMapping("/proxy/temp/m3u8/*.ts")
	public void playTs(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "m3u8Url", required = false) String m3u8Url)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("play m3u8 ts:" + request.getRequestURI());
		}
		String requestUrl = request.getRequestURI().toString();
		String localPath = requestUrl.replace("/appletv/noctl/proxy/temp",
				springProperty.getSystemTempPath());
		M3u8Download m3u8Download = proxyClient.getM3u8Download();
		if (StringUtils.isEmpty(m3u8Download.getM3u8Url())
				|| !m3u8Download.getM3u8Url().equals(m3u8Url)) {
			proxyClient.doSyncRequestByM3U8Url(m3u8Url, false);
			proxyClient.seekDownloadLine(localPath);
			proxyClient.startM3u8();
		}
		boolean seek = true;
		while (true) {
			File file = new File(localPath);
			if (file.exists()) {
				response.getOutputStream().write(
						FileUtils.readFileToByteArray(file));
				return;
			} else {
				if (seek) {
					seek = false;
					proxyClient.seekDownloadLine(localPath);
				}
				Thread.sleep(1000);
			}
		}
	}

	@RequestMapping("/proxy/play.mp4")
	public void playMp4(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "url", required = false) String url)
			throws Exception {
		if (!springProperty.isSystemProxyPlay()) {
			response.sendRedirect(url);
		} else {

		}
	}

	@RequestMapping("/proxy/sync.zip")
	public void sync(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("down sync version, t=" + request.getParameter("t"));
		}
		String webinf = request.getSession().getServletContext()
				.getRealPath("WEB-INF");
		File webapp = new File(webinf).getParentFile();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(os);
		this.zipFile(webapp.getAbsolutePath(), out);
		response.addHeader("version", springProperty.getSystemVersion());
		byte[] data = os.toByteArray();
		response.setContentType("application/zip");
		response.setContentLength(data.length);
		response.getOutputStream().write(data);
	}

	@RequestMapping("/proxy/sync/version.do")
	public void syncVersion(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("check sync version, t=" + request.getParameter("t"));
		}
		response.addHeader("version", springProperty.getSystemVersion());
	}

	public void zipFile(String fileToZip, ZipOutputStream zipOut)
			throws IOException {

		File srcFile = new File(fileToZip);
		if (srcFile.isDirectory()) {
			for (String fileName : srcFile.list()) {
				if (fileName.equals("WEB-INF") || fileName.equals("jsp")
						|| fileName.contains(".svn")
						|| fileName.equals("sync.zip")
						|| fileName.endsWith(".mkv")) {
					continue;
				}
				addToZip("", fileToZip + "/" + fileName, zipOut);
			}
		} else {
			addToZip("", fileToZip, zipOut);
		}
		zipOut.flush();
		zipOut.close();
	}

	private void addToZip(String path, String srcFile, ZipOutputStream zipOut)
			throws IOException {
		File file = new File(srcFile);
		String filePath = "".equals(path) ? file.getName() : path + "/"
				+ file.getName();
		if (file.isDirectory()) {
			for (String fileName : file.list()) {
				if (fileName.contains(".svn")) {
					continue;
				}
				addToZip(filePath, srcFile + "/" + fileName, zipOut);
			}
		} else {
			zipOut.putNextEntry(new ZipEntry(filePath));
			FileInputStream in = new FileInputStream(srcFile);

			byte[] buffer = new byte[2048];
			int len;
			while ((len = in.read(buffer)) != -1) {
				zipOut.write(buffer, 0, len);
			}

			in.close();
		}
	}
}
