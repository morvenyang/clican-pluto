package com.clican.appletv.core.service.subtitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.clican.appletv.core.service.BaseClient;

public class SubTitleClientImpl extends BaseClient implements SubTitleClient {

	@Override
	public String generateHash(String fileUrl) {
		Map<String, String> respHeaders = httpGetForHeader(fileUrl, null, null)
				.getHeaderMap();
		String contentRange = respHeaders.get("Content-Range");
		long totalLength = 0;
		int index = contentRange.indexOf("/");
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
			String range = offset + "-" + (offset + 4096 - 1);
			headers.put("Range", range);
			byte[] data = httpGetByData(fileUrl, headers, null);
			String hash = DigestUtils.md5Hex(data);
			hashes.add(hash);
		}
		return StringUtils.join(hashes, ";");
	}

}
