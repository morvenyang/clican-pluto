package com.clican.appletv.core.service.apns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clican.appletv.common.SpringProperty;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ReconnectPolicy.Provided;

public class ApnsServiceImpl implements ApnsService {

	private final static Log log = LogFactory.getLog(ApnsServiceImpl.class);

	private Map<String, String> tokenMap = new ConcurrentHashMap<String, String>();

	private SpringProperty springProperty;

	private com.notnoop.apns.ApnsService sendMessageService;
	

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void destroy() {
		OutputStream os = null;
		try {
			String content = StringUtils.join(tokenMap.keySet(), ",");
			os = new FileOutputStream(springProperty.getApnsTokenFile());
			os.write(content.getBytes("utf-8"));
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}

	public void init() {
		InputStream is = null;
		try {
			File file = new File(springProperty.getApnsTokenFile());
			if (file.exists()) {
				is = new FileInputStream(file);
				byte[] data = new byte[is.available()];
				is.read(data);
				String content = new String(data, "utf-8");
				if (StringUtils.isNotEmpty(content)) {
					for (String value : content.split(",")) {
						tokenMap.put(value, value);
					}
				}
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(springProperty.getApnsCertFile());
		
		ApnsDelegateImpl delegate = new ApnsDelegateImpl();
		if (springProperty.isSystemProxyEnable()) {
			if(springProperty.getApnsCertEnv().equals("production")){
				if(log.isInfoEnabled()){
					log.info("start apns service under production env");
				}
				sendMessageService = APNS.newService()
				.withSocksProxy(springProperty.getSystemProxyHost(), springProperty.getSystemProxyPort())
				.withCert(url.getFile(), springProperty.getApnsCertPassword())
				.withProductionDestination()
				.withReconnectPolicy(Provided.NEVER)
				.withDelegate(delegate).build();
			}else{
				if(log.isInfoEnabled()){
					log.info("start apns service under sandbox env");
				}
				sendMessageService = APNS.newService()
				.withSocksProxy(springProperty.getSystemProxyHost(), springProperty.getSystemProxyPort())
				.withCert(url.getFile(), springProperty.getApnsCertPassword())
				.withSandboxDestination()
				.withReconnectPolicy(Provided.NEVER)
				.withDelegate(delegate).build();
			}
			
		} else {
			if(springProperty.getApnsCertEnv().equals("production")){
				if(log.isInfoEnabled()){
					log.info("start apns service under production env");
				}
				sendMessageService = APNS.newService().withCert(url.getFile(), springProperty.getApnsCertPassword())
				.withProductionDestination()
				.withReconnectPolicy(Provided.NEVER)
				.withDelegate(delegate).build();
			}else{
				if(log.isInfoEnabled()){
					log.info("start apns service under sandbox env");
				}
				sendMessageService = APNS.newService().withCert(url.getFile(), springProperty.getApnsCertPassword())
				.withSandboxDestination()
				.withReconnectPolicy(Provided.NEVER)
				.withDelegate(delegate).build();
			}
			
		}
	}

	@Override
	public void registerToken(String token) {
		tokenMap.put(token, token);
	}

	@Override
	public void sendMessage(String message) {
		if (log.isInfoEnabled()) {
			log.info("send message to " + tokenMap.size() + " devices");
		}
		for(String token:tokenMap.keySet()){
			if(log.isDebugEnabled()){
				log.debug("token:"+token);
			}
	        String jsonMessage = "{\"aps\":{\"alert\":\""+message+"\"}}";
			ApnsNotification result = sendMessageService.push(token, jsonMessage);
			if (log.isDebugEnabled()) {
				log.debug("send apns notification with result:"
						+ result.getIdentifier());
			}
		}
	}

}
