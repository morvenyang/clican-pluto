package com.clican.appletv.service;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.io.InputStream;

import junit.framework.TestCase;

public class HtmlGeneratorTestCase extends TestCase {

	public void testGeneratePng() throws Exception {
		HtmlImageGenerator generator = new HtmlImageGenerator();
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("taobao/confirm_template.htm");
		byte[] data = new byte[is.available()];
		is.read(data);
		is.close();
		String htmlContent = new String(data, "utf-8");
		generator.loadHtml(htmlContent);
		generator.saveAsImage("c:/confirm.png");
	}
}
