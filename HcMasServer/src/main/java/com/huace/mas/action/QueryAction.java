package com.huace.mas.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.huace.mas.bean.DateJsonValueProcessor;
import com.huace.mas.bean.KpiData;
import com.huace.mas.bean.SpringProperty;
import com.huace.mas.entity.Project;
import com.huace.mas.entity.User;
import com.huace.mas.service.DataService;
import com.huace.mas.service.PushService;
import com.huace.mas.service.UserService;

@Controller
public class QueryAction {

	private final static Log log = LogFactory.getLog(QueryAction.class);

	private final static String USER_NAME = "USER_NAME";
	private final static String TOKEN = "TOKEN";
	private UserService userService;

	private DataService dataService;

	private PushService pushService;

	private SpringProperty springProperty;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

	public void setPushService(PushService pushService) {
		this.pushService = pushService;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}
	
	@RequestMapping("/0")
	public void init(
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String projectServerConf = springProperty.getProjectServerConf();
		JSONArray jsonObject = JSONArray.fromObject(projectServerConf);
		OutputStream os = null;
		try {
			os = resp.getOutputStream();
			os.write(jsonObject.toString().getBytes("utf-8"));
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	@RequestMapping("/1")
	public void login(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "passWord", required = true) String passWord,
			@RequestParam(value = "token", required = false) String token,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<User> users = userService.findUserByNameAndPassword(userName,
				passWord);

		if (users.size() > 0) {
			req.getSession().setAttribute(USER_NAME, userName);
			req.getSession().setAttribute(TOKEN, token);
		}
		for (User user : users) {
			user.setJsessionid(req.getSession().getId());
			user.setAppName(springProperty.getAppName());
			user.setCopyRight(springProperty.getCopyRight());
		}
		JSONArray jsonObject = JSONArray.fromObject(users);
		OutputStream os = null;
		try {
			os = resp.getOutputStream();
			os.write(jsonObject.toString().getBytes("utf-8"));
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	@RequestMapping("/2")
	public void projectInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Project> projects = dataService.findAllProjects();
		JSONArray jsonObject = JSONArray.fromObject(projects);
		OutputStream os = null;
		try {
			os = resp.getOutputStream();
			os.write(jsonObject.toString().getBytes("utf-8"));
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	@RequestMapping("/3")
	public void data(
			@RequestParam(value = "projectID", required = true) Long projectID,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<List<Object>> result = dataService.getKpisForProject(projectID);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
		JSONArray jsonObject = JSONArray.fromObject(result, jsonConfig);
		OutputStream os = null;
		try {
			String userName = (String) req.getSession().getAttribute(USER_NAME);
			String token = (String) req.getSession().getAttribute(TOKEN);
			if (StringUtils.isNotEmpty(userName)
					&& StringUtils.isNotEmpty(token)) {
				pushService.registerToken(userName, token, projectID);
			}
			os = resp.getOutputStream();
			os.write(jsonObject.toString().getBytes("utf-8"));
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	@RequestMapping("/4")
	public void histData(
			@RequestParam(value = "projectID", required = true) Long projectID,
			@RequestParam(value = "kpiType", required = true) String kpiType,
			@RequestParam(value = "pointName", required = true) String pointName,
			@RequestParam(value = "startDate", required = true) String startDate,
			@RequestParam(value = "endDate", required = true) String endDate,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date start = null;
		Date end = null;
		try {
			start = sdf.parse(startDate);
			end = sdf.parse(endDate);
		} catch (Exception e) {
			log.error("", e);
		}
		List<KpiData> result = dataService.queryKpiData(projectID, kpiType,
				pointName, start, end);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new DateJsonValueProcessor("yyyy/MM/dd HH:mm:ss"));
		JSONArray jsonObject = JSONArray.fromObject(result, jsonConfig);
		OutputStream os = null;
		try {
			os = resp.getOutputStream();
			os.write(jsonObject.toString().getBytes("utf-8"));
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

}
