package com.huace.mas.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.huace.mas.entity.Project;
import com.huace.mas.entity.User;
import com.huace.mas.service.DataService;
import com.huace.mas.service.UserService;

@Controller
public class QueryAction {

	private final static Log log = LogFactory.getLog(QueryAction.class);

	private UserService userService;

	private DataService dataService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

	@RequestMapping("/1")
	public void login(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "passWord", required = true) String passWord,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<User> users = userService.findUserByNameAndPassword(userName,
				passWord);
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
	public void projectInfo(
			HttpServletRequest req, HttpServletResponse resp)
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
			@RequestParam(value = "projectID", required = true) String projectID,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OutputStream os = null;
		try {
			os = resp.getOutputStream();
			os.write("[]".getBytes("utf-8"));
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

}
