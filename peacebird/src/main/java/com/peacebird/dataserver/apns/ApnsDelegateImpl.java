package com.peacebird.dataserver.apns;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.DeliveryError;

public class ApnsDelegateImpl implements ApnsDelegate {

	private final static Log log = LogFactory.getLog(ApnsDelegateImpl.class);

	public void messageSent(ApnsNotification message, boolean resent) {
		if (log.isDebugEnabled()) {
			if(message!=null){
				log.debug("Successfully send for receiverId:"
						+ message.getIdentifier());
			}else{
				log.debug("Successfully send");
			}
		}
	}

	public void cacheLengthExceeded(int newCacheLength) {
		if (log.isDebugEnabled()) {
			log.debug("Cache Length Exceeded:"+newCacheLength);
		}
	}

	public void notificationsResent(int resendCount) {
		if (log.isDebugEnabled()) {
			log.debug("Resend notification:"+resendCount);
		}
	}

	public void messageSent(ApnsNotification apnsnotification) {
		if (log.isDebugEnabled()) {
			if(apnsnotification!=null){
				log.debug("Successfully send for receiverId:"
						+ apnsnotification.getIdentifier());
			}else{
				log.debug("Successfully send");
			}
			
		}
	}

	public void messageSendFailed(ApnsNotification apnsnotification,
			Throwable throwable) {
		if (log.isDebugEnabled()) {
			if(apnsnotification!=null){
				log.debug("Failure send for receiverId:"
						+ apnsnotification.getIdentifier());
			}else{
				log.debug("Failure send");
			}
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
