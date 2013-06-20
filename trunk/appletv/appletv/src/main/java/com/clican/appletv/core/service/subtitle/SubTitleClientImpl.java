package com.clican.appletv.core.service.subtitle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.clican.appletv.common.PostResponse;
import com.clican.appletv.core.service.BaseClient;

public class SubTitleClientImpl extends BaseClient implements SubTitleClient {

	@Override
	public String generateHash(String fileUrl) {
		Map<String, String> respHeaders = httpGetForHeader(fileUrl, null, null)
				.getHeaderMap();
		String contentRange = respHeaders.get("Content-Range");
		long totalLength = 0;
		int index = -1;
		if (StringUtils.isNotEmpty(contentRange)) {
			index = contentRange.indexOf("/");
		}
		if (index >= 0) {
			totalLength = Long.parseLong(contentRange.substring(index + 1)
					.trim());
		} else {
			totalLength = Long.parseLong(respHeaders.get("Content-Length")
					.trim());
		}
		long[] offsets = new long[] { 4096L, totalLength / 3 * 2,
				totalLength / 3, totalLength - 8192 };
		Map<String, String> headers = new HashMap<String, String>();
		List<String> hashes = new ArrayList<String>();
		for (long offset : offsets) {
			String range = "bytes=" + offset + "-" + (offset + 4096 - 1);
			headers.put("Range", range);
			byte[] data = httpGetByData(fileUrl, headers, null);
			String hash = DigestUtils.md5Hex(data);
			hashes.add(hash);
		}
		return StringUtils.join(hashes, ";");
	}

	@Override
	public String downloadSubTitle(String fileUrl) {
		DataInputStream dis = null;
		ByteArrayOutputStream bs = null;
		try {
			String filehash = this.generateHash(fileUrl);
			String shortname = "The Walking Dead";
			String pathinfo = "E:\\2.mkv";
			String vhash = null;
			byte[] b1 = "SP,aerSP,aer 1543 &e(".getBytes("utf-8");
			byte[] b2 = new byte[] { (byte) 0xd7 };
			byte[] b3 = new byte[] { (byte) 0x02 };
			byte[] b4 = (" 2.mkv" + " " + filehash).getBytes("utf-8");
			bs = new ByteArrayOutputStream();
			try {
				bs.write(b1);
				bs.write(b2);
				bs.write(b3);
				bs.write(b4);
			} catch (Exception e) {
				log.error("", e);
			}
			byte[] b = bs.toByteArray();
			log.debug(new String(b));
			vhash = DigestUtils.md5Hex(b);

			Map<String, String> params = new HashMap<String, String>();
			params.put("filehash", filehash);
			params.put("pathinfo", pathinfo);
			params.put("shortname", shortname);
			params.put("vhash", vhash);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("lang", "chn");
			headers.put("Accept", "*/*");
			headers.put("Host", "svplayer.shooter.cn");
			headers.put("User-Agent", "SPlayer Build 1543");
			PostResponse pr = this.httpPost(
					"http://svplayer.shooter.cn/api/subapi.php", null, params,
					"multipart/form-data", null, headers, null);
			byte[] data = pr.getData();
			dis = new DataInputStream(new ByteArrayInputStream(data));
			return parsePackages(dis);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				bs.close();
			} catch (Exception e) {

			}
			try {
				dis.close();
			} catch (Exception e) {

			}
		}
		return null;
	}

	private String parsePackages(DataInputStream dis) throws IOException {
		int packageCount = dis.readByte();
		for (int i = 0; i < packageCount; i++) {
			dis.readInt();
			int descLength = dis.readInt();
			byte[] descData = new byte[descLength];
			dis.read(descData);
			if (log.isDebugEnabled()) {
				log.debug("package[" + i + "] desc:"
						+ new String(descData, "gbk"));
			}
			dis.readInt();
			int fileCount = dis.readByte();
			for (int j = 0; j < fileCount; j++) {
				String fileContent = parseFiles(dis);
				if (StringUtils.isNotEmpty(fileContent)) {
					return fileContent;
				}
			}
		}
		return null;
	}

	private String parseFiles(DataInputStream dis) throws IOException {
		dis.readInt();
		int fileExtNameLength = dis.readInt();
		byte[] fileExtNameData = new byte[fileExtNameLength];
		dis.read(fileExtNameData);

		int fileDataLength = dis.readInt();
		byte[] fileData = new byte[fileDataLength];
		dis.read(fileData);
		String fileContent = null;
		if (fileData[0] == (byte) 0x1f && fileData[1] == (byte) 0x8b) {
			// gzip
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(
					fileData));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read = -1;
			while ((read = gis.read(buffer)) != -1) {
				bos.write(buffer, 0, read);
			}
			fileData = bos.toByteArray();
		}

		if (fileData[0] == (byte) 0xff && fileData[1] == (byte) 0xfe) {
			fileContent = new String(fileData, "UTF-16");
		} else {
			fileContent = new String(fileData, "GBK");
		}
		if (log.isDebugEnabled()) {
			log.debug(fileContent);
		}
		return fileContent;
	}
}
