package com.peacebird.dataserver.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peacebird.dataserver.bean.LoginResult;
import com.peacebird.dataserver.bean.RetailResult;
import com.peacebird.dataserver.bean.SpringProperty;
import com.peacebird.dataserver.model.User;
import com.peacebird.dataserver.service.DataService;
import com.peacebird.dataserver.service.DataServiceV2;
import com.peacebird.dataserver.service.UserService;

@Controller
public class ClientController {

	private final static Log log = LogFactory.getLog(ClientController.class);

	private UserService userService;
	private DataService dataService;
	private DataServiceV2 dataServiceV2;
	private SpringProperty springProperty;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setDataServiceV2(DataServiceV2 dataServiceV2) {
		this.dataServiceV2 = dataServiceV2;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

	public SpringProperty getSpringProperty() {
		return springProperty;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	private boolean isV2(String version) {
		if (StringUtils.isNotEmpty(version) && version.startsWith("2.")) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping("/goodImage")
	public void goodImage(
			@RequestParam(value = "path", required = true) String path,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		byte[] data = FileUtils.readFileToByteArray(new File(springProperty
				.getImageUrlPrefix() + path));
		resp.getOutputStream().write(data);
	}

	@RequestMapping("/checkSession")
	public void checkSession(
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access check session page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			user = this.userService.findUserByUserName(user.getUserName());
			if (user.getExpiredDays() > 0) {
				result = "{\"result\":0,\"message\":\"已登录\"}";
			} else {
				result = this.getNotLoginResult();
			}
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/login")
	public void login(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("userName=" + userName + " login with token=" + token);
		}
		LoginResult lr = null;
		User user = userService.findUserByUserName(userName);
		if (user == null) {
			lr = new LoginResult(1000, "用户名不存在", -1, 60,false);
		} else {
			if (!user.isActive()) {
				lr = new LoginResult(1002, "该用户被禁用,请联系管理员激活该用户", -1, 60,false);
			} else {
				String hashPassword = DigestUtils.shaHex(password);
				if (user.getPassword().equals(hashPassword)) {
					req.getSession().setAttribute("user", user);
					if (StringUtils.isNotEmpty(token)) {
						user.setToken(token);
						userService.saveUser(user);
					}
					if (user.getTimeoutInterval() == null) {
						lr = new LoginResult(0, "登录成功", user.getExpiredDays(),
								60,user.isReset());
					} else {
						lr = new LoginResult(0, "登录成功", user.getExpiredDays(),
								user.getTimeoutInterval(),user.isReset());
					}

				} else {
					lr = new LoginResult(1001, "密码错误", -1, 60,false);
				}
			}
		}
		lr.setJsessionid(req.getSession().getId());
		String result = JSONObject.fromObject(lr).toString();
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/changePassword")
	public void changePassword(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "oldPassword", required = true) String oldPassword,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("userName=" + userName + " change password");
		}
		String result = null;
		User user = userService.findUserByUserName(userName);
		if (user == null) {
			result = this.getErrorResult(1001, "该用户不存在");
		} else {
			String hashPassword = DigestUtils.shaHex(oldPassword);
			if (user.getPassword().equals(hashPassword)) {
				user.setPassword(DigestUtils.shaHex(password));
				user.setReset(false);
				userService.saveUser(user);
				result = this.getErrorResult(0, "密码修改成功");
			} else {
				result = this.getErrorResult(1002, "旧密码有误");
			}
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private String getNotLoginResult() {
		if (log.isDebugEnabled()) {
			log.debug("The user is not logined");
		}
		String notLogin = "{\"result\":1002,\"message\":\"未登录\"}";
		return notLogin;
	}

	private String getErrorResult(int code, String message) {
		return "{\"result\":" + code + ",\"message\":\"" + message + "\"}";
	}

	@RequestMapping("/index")
	public void index(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access index page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			String brands = user.getBrands();
			if (StringUtils.isEmpty(brands)) {
				if (log.isDebugEnabled()) {
					log.debug("There is no avaliable brands for user["
							+ user.getUserName() + "]");
				}
				result = getErrorResult(3001, "当前用户没有可查阅的品牌,请让管理员设置品牌查阅权限");
			} else {
				if (log.isDebugEnabled()) {
					log.debug("There are brands [" + brands + "] for user["
							+ user.getUserName() + "]");
				}
				if (isV2(version)) {
					result = this.dataServiceV2.getIndexResult(
							brands.split(","), this.getDate(date));
				} else {
					result = this.dataService.getIndexResult(brands.split(","),
							this.getDate(date));
				}

			}
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/brand")
	public void brand(@RequestParam(value = "brand") String brand,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access brand page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			if (isV2(version)) {
				result = this.dataServiceV2
						.getBrandResult(brand, getDate(date));
			} else {
				result = this.dataService.getBrandResult(brand, getDate(date));
			}
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/retailChart")
	public void retailChart(@RequestParam(value = "brand") String brand,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access retail chart page");
		}
		List<RetailResult> result;
		if (isV2(version)) {
			result = this.dataServiceV2.getRetailChartResult(brand, type,
					getDate(date));
		} else {
			result = this.dataService.getRetailChartResult(brand, type,
					getDate(date));
		}
		double total = 0;
		for (RetailResult rr : result) {
			if (rr.getDayAmount() != null) {
				total += rr.getDayAmount();
			}
		}
		String dataProvider = JSONArray.fromObject(result).toString();
		;
		try {
			req.setAttribute("dataProvider", dataProvider);
			req.setAttribute("height", 640 + result.size() * 80);
			req.setAttribute("top", 260 + result.size() * 5);
			req.setAttribute("total", (int) total);
			req.getRequestDispatcher("/retail.jsp").forward(req, resp);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/retailChartJson")
	public void retailChartJson(@RequestParam(value = "brand") String brand,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access retail chart json page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			if (isV2(version)) {
				result = this.dataServiceV2.getRetailChartResultForJson(brand,
						type, getDate(date));
			} else {
				result = this.dataService.getRetailChartResultForJson(brand,
						type, getDate(date));
			}
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * kpiView
	 * 
	 * @param brand
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/channel")
	public void channel(@RequestParam(value = "brand") String brand,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access channel page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			if (isV2(version)) {
				result = this.dataServiceV2.getChannelResult(brand,
						getDate(date));
			} else {
				result = this.dataService
						.getChannelResult(brand, getDate(date));
			}

		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/storeRank")
	public void storeRank(@RequestParam(value = "brand") String brand,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access store rank page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {

			if (isV2(version)) {
				result = this.dataServiceV2.getStoreRankResult(brand,
						getDate(date));
			} else {
				result = this.dataService.getStoreRankResult(brand,
						getDate(date));
			}
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/goodRank")
	public void goodRank(@RequestParam(value = "brand") String brand,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access good rank page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			if (isV2(version)) {
				result = this.dataServiceV2.getGoodRankResult(brand,
						getDate(date));
			} else {
				result = this.dataService.getGoodRankResult(brand,
						getDate(date));
			}

		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/storeSum")
	public void storeSum(@RequestParam(value = "brand") String brand,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access store sum page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			result = this.dataServiceV2.getDataRetailStoreSumResult(brand,
					this.getDate(date));
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@RequestMapping("/noRetails")
	public void noRetails(@RequestParam(value = "brand") String brand,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "version", required = false) String version,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("access no retails page");
		}
		User user = (User) req.getSession().getAttribute("user");
		String result = null;
		if (user == null) {
			result = this.getNotLoginResult();
		} else {
			result = this.dataServiceV2.getDataRetailsNoRetailResult(brand,
					this.getDate(date));
		}
		try {
			resp.setContentType("application/json");
			resp.getOutputStream().write(result.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private Date getDate(String strDate) {
		if (StringUtils.isEmpty(strDate)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sdf.parse(strDate);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}
}
