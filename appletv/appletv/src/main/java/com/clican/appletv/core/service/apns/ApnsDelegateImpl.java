package com.clican.appletv.core.service.apns;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.DeliveryError;

public class ApnsDelegateImpl implements ApnsDelegate {

	private final static Log log = LogFactory.getLog(ApnsDelegateImpl.class);

	public void messageSent(ApnsNotification apnsnotification) {
		if (log.isDebugEnabled()) {
			log.debug("Successfully send for receiverId:"
					+ apnsnotification.getIdentifier());
		}
	}

	public void messageSendFailed(ApnsNotification apnsnotification,
			Throwable throwable) {
		if (log.isDebugEnabled()) {
			log.debug("Failure send for receiverId:"
					+ apnsnotification.getIdentifier());
		}
	}

	public void connectionClosed(DeliveryError deliveryerror, int identifier) {
		try {
			if (log.isInfoEnabled()) {
				log.info("APNS delivery error:" + deliveryerror);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
