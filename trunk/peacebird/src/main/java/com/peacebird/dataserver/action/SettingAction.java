package com.peacebird.dataserver.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

@Scope(ScopeType.PAGE)
@Name("settingAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class SettingAction extends BaseAction {

	private final static Log log = LogFactory.getLog(SettingAction.class);

	private Date currentDate;
	
	private String clientVersion;

	public void load() {
		if (StringUtils.isNotEmpty(this.getSpringProperty().getYesterday())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				currentDate = sdf
						.parse(this.getSpringProperty().getYesterday());
			} catch (Exception e) {
				log.error("", e);
			}
		}
		clientVersion = this.getSpringProperty().getClientVersion();
	}

	public void save() {
		if (currentDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			this.getSpringProperty().setYesterday(sdf.format(currentDate));
		} else {
			this.getSpringProperty().setYesterday(null);
		}
		this.getSpringProperty().setClientVersion(clientVersion);
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

}
