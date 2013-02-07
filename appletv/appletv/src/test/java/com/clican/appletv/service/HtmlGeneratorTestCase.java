package com.clican.appletv.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import gui.ava.html.image.generator.HtmlImageGenerator;

public class HtmlGeneratorTestCase extends TestCase {

	public void testGeneratePng() throws Exception {
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(Thread.currentThread()
				.getContextClassLoader().getResource("taobao").getFile()));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		Template temp = cfg.getTemplate("confirm.ftl");
		Map<String, Object> rootMap = new HashMap<String, Object>();
		OutputStream os = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(os);
		temp.process(rootMap, out);
		HtmlImageGenerator generator = new HtmlImageGenerator();
		generator.saveAsImage("c:/confirm.png");
	}
}
