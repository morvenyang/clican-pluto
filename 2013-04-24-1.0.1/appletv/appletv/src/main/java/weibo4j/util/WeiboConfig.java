package weibo4j.util;

import com.clican.appletv.common.SpringProperty;

public class WeiboConfig {

	public static String getValue(String key) {
		SpringProperty sp = SpringProperty.getInstance();
		if (key.equals("client_ID")) {
			return sp.getWeiboAppKey();
		} else if (key.equals("client_SERCRET")) {
			return sp.getWeiboAppSercret();
		} else if (key.equals("redirect_URI")) {
			return sp.getWeiboRedirectURL();
		} else if (key.equals("baseURL")) {
			return sp.getWeiboBaseURL();
		} else if (key.equals("accessTokenURL")) {
			return sp.getWeiboAccessTokenURL();
		} else if (key.equals("authorizeURL")) {
			return sp.getWeiboAuthorizeURL();
		} else if (key.equals("rmURL")) {
			return sp.getWeiboRmURL();
		} else {
			throw new RuntimeException("The key " + key + " is not defined");
		}
	}

}
