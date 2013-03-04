package com.clican.appletv.ui.controllers;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clican.appletv.common.SpringProperty;

@Controller
public class OtherController {

	private final static Log log = LogFactory.getLog(OtherController.class);

	@Autowired
	private SpringProperty springProperty;

	@RequestMapping("/log.do")
	public void logText(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		InputStream is = request.getInputStream();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		int read = -1;
		while ((read = is.read(buffer)) != -1) {
			os.write(buffer, 0, read);
		}
		String logText = new String(os.toByteArray(), "UTF-8");
		is.close();
		os.close();
		if (log.isDebugEnabled()) {
			log.debug("log=" + logText);
		}
	}

	@RequestMapping("/newpage.xml")
	public void newPage(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("xml") String xml)
			throws IOException {
		response.setContentType("text/xml;charset=utf-8");
		OutputStream os = response.getOutputStream();
		os.write(xml.getBytes("utf-8"));
		os.close();
	}

	@RequestMapping("/jstest.do")
	public String jstest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		request.setAttribute("serverurl", springProperty.getSystemServerUrl());
		return "jstest";
	}

	@RequestMapping("/download.do")
	public void download(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		this.processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// Validate the requested file
		// ------------------------------------------------------------

		// Get requested file by path info.
		String requestedFile = request.getPathInfo();

		// Check if file is actually supplied to the request URL.
		if (requestedFile == null) {
			// Do your thing if the file is not supplied to the request URL.
			// Throw an exception, or send 404, or show default/warning page, or
			// just ignore it.
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// URL-decode the file name (might contain spaces and on) and prepare
		// file object.
		File file = new File("c:/file.mp4");

		// Check if file actually exists in filesystem.
		if (!file.exists()) {
			// Do your thing if the file appears to be non-existing.
			// Throw an exception, or send 404, or show default/warning page, or
			// just ignore it.
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Prepare some variables. The ETag is an unique identifier of the file.
		String fileName = file.getName();
		long length = file.length();
		long lastModified = file.lastModified();
		String eTag = fileName + "_" + length + "_" + lastModified;

		// Validate request headers for caching
		// ---------------------------------------------------

		// Validate and process range
		// -------------------------------------------------------------

		// Prepare some variables. The full Range represents the complete file.
		Range full = new Range(0, length - 1, length);
		List<Range> ranges = new ArrayList<Range>();

		// Validate and process Range and If-Range headers.
		String range = request.getHeader("Range");

		if (range != null) {
			System.out.println("Range=" + range);
			// Range header should match format "bytes=n-n,n-n,n-n...". If not,
			// then return 416.
			if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
				response.setHeader("Content-Range", "bytes */" + length); // Required
																			// in
																			// 416.
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return;
			}
			for (String part : range.substring(6).split(",")) {
				// Assuming a file with length of 100, the following
				// examples returns bytes at:
				// 50-80 (50 to 80), 40- (40 to length=100), -20
				// (length-20=80 to length=100).
				long start = sublong(part, 0, part.indexOf("-"));
				long end = sublong(part, part.indexOf("-") + 1, part.length());

				if (start == -1) {
					start = length - end;
					end = length - 1;
				} else if (end == -1 || end > length - 1) {
					end = length - 1;
				}

				// Check if Range is syntactically valid. If not, then
				// return 416.
				if (start > end) {
					response.setHeader("Content-Range", "bytes */" + length); // Required
					response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
					return;
				}
				// Add range.
				ranges.add(new Range(start, end, length));
			}

		}

		// Prepare and initialize response
		// --------------------------------------------------------

		// Get content type by file name and set default GZIP support and
		// content disposition.
		String contentType = "video/mp4";
		String disposition = "inline";

		response.setHeader("Content-Disposition", disposition + ";filename=\""
				+ fileName + "\"");
		response.setHeader("Accept-Ranges", "bytes");

		// Send requested file (part(s)) to client
		// ------------------------------------------------

		// Prepare streams.
		RandomAccessFile input = null;
		OutputStream output = null;

		try {
			// Open streams.
			input = new RandomAccessFile(file, "r");
			output = response.getOutputStream();
			if (ranges.size() == 1) {
				// Return single part of file.
				Range r = ranges.get(0);
				response.setContentType(contentType);
				System.out.println("Content-Range:" + "bytes " + r.start + "-"
						+ r.end + "/" + r.total);
				response.setHeader("Content-Range", "bytes " + r.start + "-"
						+ r.end + "/" + r.total);
				response.setHeader("Content-Length", String.valueOf(r.length));
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.
				copy(input, output, r.start, r.length);
			} else {
				System.out.println("Content-Range:" + "all");
				response.setContentType(contentType);
				response.setHeader("Content-Length", String.valueOf(length));
				copy(input, output, 0, length);
			}
		} finally {
			// Gently close streams.
			close(output);
			close(input);
		}
	}

	/**
	 * Returns true if the given match header matches the given value.
	 * 
	 * @param matchHeader
	 *            The match header.
	 * @param toMatch
	 *            The value to be matched.
	 * @return True if the given match header matches the given value.
	 */
	private static boolean matches(String matchHeader, String toMatch) {
		String[] matchValues = matchHeader.split("\\s*,\\s*");
		Arrays.sort(matchValues);
		return Arrays.binarySearch(matchValues, toMatch) > -1
				|| Arrays.binarySearch(matchValues, "*") > -1;
	}

	/**
	 * Returns a substring of the given string value from the given begin index
	 * to the given end index as a long. If the substring is empty, then -1 will
	 * be returned
	 * 
	 * @param value
	 *            The string value to return a substring as long for.
	 * @param beginIndex
	 *            The begin index of the substring to be returned as long.
	 * @param endIndex
	 *            The end index of the substring to be returned as long.
	 * @return A substring of the given string value as long or -1 if substring
	 *         is empty.
	 */
	private static long sublong(String value, int beginIndex, int endIndex) {
		String substring = value.substring(beginIndex, endIndex);
		return (substring.length() > 0) ? Long.parseLong(substring) : -1;
	}

	/**
	 * Copy the given byte range of the given input to the given output.
	 * 
	 * @param input
	 *            The input to copy the given range to the given output for.
	 * @param output
	 *            The output to copy the given range from the given input for.
	 * @param start
	 *            Start of the byte range.
	 * @param length
	 *            Length of the byte range.
	 * @throws IOException
	 *             If something fails at I/O level.
	 */
	private static void copy(RandomAccessFile input, OutputStream output,
			long start, long length) throws IOException {
		byte[] buffer = new byte[1024];
		int read;

		if (input.length() == length) {
			// Write full range.
			while ((read = input.read(buffer)) > 0) {
				output.write(buffer, 0, read);
			}
		} else {
			// Write partial range.
			input.seek(start);
			long toRead = length;
			long totalRead = 0;
			while ((read = input.read(buffer)) > 0) {
				totalRead+=read;
				System.out.println("total output:"+totalRead);
				if ((toRead -= read) > 0) {
					output.write(buffer, 0, read);
				} else {
					output.write(buffer, 0, (int) toRead + read);
					break;
				}
			}
		}
	}

	/**
	 * Close the given resource.
	 * 
	 * @param resource
	 *            The resource to be closed.
	 */
	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException ignore) {
				// Ignore IOException. If you want to handle this anyway, it
				// might be useful to know
				// that this will generally only be thrown when the client
				// aborted the request.
			}
		}
	}

	// Inner classes
	// ------------------------------------------------------------------------------

	/**
	 * This class represents a byte range.
	 */
	protected class Range {
		long start;
		long end;
		long length;
		long total;

		/**
		 * Construct a byte range.
		 * 
		 * @param start
		 *            Start of the byte range.
		 * @param end
		 *            End of the byte range.
		 * @param total
		 *            Total length of the byte source.
		 */
		public Range(long start, long end, long total) {
			this.start = start;
			this.end = end;
			this.length = end - start + 1;
			this.total = total;
		}

	}
}
