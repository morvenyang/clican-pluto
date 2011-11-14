/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company
 * and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.cms.ui.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class VelocityResourceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4259309490836963415L;

	private final static Log log = org.apache.commons.logging.LogFactory.getLog(VelocityResourceServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String prefix = request.getContextPath() + request.getServletPath();
		if (request.getRequestURI().startsWith(prefix)) {
			String path = request.getRequestURI().replaceFirst(prefix, "");
			if (path.startsWith("/")) {
				path = path.substring(1,path.indexOf(";"));
			}
			// request.getSession().setAttribute("propertyDescriptionList", new
			// ArrayList<PropertyDescription>());
			Writer w = new OutputStreamWriter(response.getOutputStream(), "utf-8");
			InputStream is = null;
			try {
				is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
				byte[] data = new byte[is.available()];
				is.read(data);
				String content = new String(data, "utf-8");
				Template t = null;
				VelocityContext velocityContext = new VelocityContext();
				HttpSession session = request.getSession();
				Enumeration<String> en = session.getAttributeNames();
				while (en.hasMoreElements()) {
					String name = en.nextElement();
					velocityContext.put(name, session.getAttribute(name));
				}
				SimpleNode node = RuntimeSingleton.getRuntimeServices().parse(content, path);
				t = new Template();
				t.setName(path);
				t.setRuntimeServices(RuntimeSingleton.getRuntimeServices());
				t.setData(node);
				t.initDocument();
				t.merge(velocityContext, w);
				w.flush();
			} catch (Exception e) {
				log.error("", e);
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} finally {
				if (w != null) {
					w.close();
				}
				if (is != null) {
					is.close();
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

	}

}

// $Id$