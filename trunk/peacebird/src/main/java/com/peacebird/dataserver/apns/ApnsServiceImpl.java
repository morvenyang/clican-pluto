package com.peacebird.dataserver.apns;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ReconnectPolicy.Provided;
import com.peacebird.dataserver.bean.SpringProperty;

public class ApnsServiceImpl implements ApnsService {

	private final static Log log = LogFactory.getLog(ApnsServiceImpl.class);

	private SpringProperty springProperty;

	private com.notnoop.apns.ApnsService sendMessageService;

	public void setSpringProperty(SpringProperty springProperty) {
		this.springProperty = springProperty;
	}

	public void destroy() {

	}

	public void init() {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource(springProperty.getApnsCertFile());

		ApnsDelegateImpl delegate = new ApnsDelegateImpl();
		if (springProperty.isSystemProxyEnable()) {
			if (springProperty.getApnsCertEnv().equals("production")) {
				if (log.isInfoEnabled()) {
					log.info("start apns service under production env");
				}
				sendMessageService = APNS
						.newService()
						.withSocksProxy(springProperty.getSystemProxyHost(),
								springProperty.getSystemProxyPort())
						.withCert(url.getFile(),
								springProperty.getApnsCertPassword())
						.withProductionDestination()
						.withReconnectPolicy(Provided.NEVER)
						.withDelegate(delegate).build();
			} else {
				if (log.isInfoEnabled()) {
					log.info("start apns service under sandbox env");
				}
				sendMessageService = APNS
						.newService()
						.withSocksProxy(springProperty.getSystemProxyHost(),
								springProperty.getSystemProxyPort())
						.withCert(url.getFile(),
								springProperty.getApnsCertPassword())
						.withSandboxDestination()
						.withReconnectPolicy(Provided.NEVER)
						.withDelegate(delegate).build();
			}

		} else {
			if (springProperty.getApnsCertEnv().equals("production")) {
				if (log.isInfoEnabled()) {
					log.info("start apns service under production env");
				}
				sendMessageService = APNS
						.newService()
						.withCert(url.getFile(),
								springProperty.getApnsCertPassword())
						.withProductionDestination()
						.withReconnectPolicy(Provided.NEVER)
						.withDelegate(delegate).build();
			} else {
				if (log.isInfoEnabled()) {
					log.info("start apns service under sandbox env");
				}
				sendMessageService = APNS
						.newService()
						.withCert(url.getFile(),
								springProperty.getApnsCertPassword())
						.withSandboxDestination()
						.withReconnectPolicy(Provided.NEVER)
						.withDelegate(delegate).build();
			}

		}
	}

	@Override
	public void sendMessage(String message, String token) {
		if (log.isInfoEnabled()) {
			log.info("send message to device:" + token);
		}

		String jsonMessage = "{\"aps\":{\"alert\":\"" + message + "\"}}";
		ApnsNotification result = sendMessageService.push(token, jsonMessage);
		if (log.isDebugEnabled()) {
			log.debug("send apns notification with result:"
					+ result.getIdentifier());
		}

	}

}
