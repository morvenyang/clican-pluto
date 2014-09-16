package com.huace.mas.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huace.mas.apns.ApnsService;
import com.huace.mas.bean.SpringProperty;
import com.huace.mas.dao.DataDao;
import com.huace.mas.dao.UserDao;
import com.huace.mas.service.PushService;

public class PushServiceImpl implements PushService {

	private final static Log log = LogFactory.getLog(PushServiceImpl.class);

	private DataDao dataDao;

	private UserDao userDao;

	private ApnsService apnsService;

	private Map<String, String> tokens;

	private SpringProperty springProperty;

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setApnsService(ApnsService apnsService) {
		this.apnsService = apnsService;
	}

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	@SuppressWarnings("unchecked")
	public void init() {
		try {
			File file = new File(springProperty.getTokenFile());
			if (file.exists()) {
				String str = new String(FileUtils.readFileToByteArray(new File(
						springProperty.getTokenFile())), "utf-8");
				tokens = new HashMap<String,String>(JSONObject.fromObject(str));
			} else {
				file.getParentFile().mkdirs();
			}
		} catch (Exception e) {
			log.error("", e);
		}finally{
			if(tokens==null){
				tokens = new HashMap<String,String>();
			}
		}
	}

	@Override
	public void pushMsg() {
		if (log.isInfoEnabled()) {
			log.info("start push message");
		}
		
	}

	@Override
	public synchronized void registerToken(String userName, String token) {
		tokens.put(userName, token);
		try {
			FileUtils.writeByteArrayToFile(
					new File(springProperty.getTokenFile()), JSONObject
							.fromObject(tokens).toString().getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		}

	}

}
