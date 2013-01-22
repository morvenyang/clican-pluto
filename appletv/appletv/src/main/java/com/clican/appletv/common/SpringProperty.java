package com.clican.appletv.common;

public class SpringProperty {

	private static SpringProperty instance;

	private String systemServerUrl;

	private Integer systemHttpconnectionTimeout;
	
	private String systemProxyHost;
	
	private Integer systemProxyPort;
	
	private boolean systemProxyEnable;

	private String tudouSessionid;

	private String tudouRecommendApi;

	private String tudouChannelApi;

	private String tudouAlbumChannelApi;

	private String tudouAlbumVideosApi;

	private String tudouKeywordSearchApi;

	private String tudouSearchApi;

	private String tudouGetFavoriteApi;

	private String tudouSetFavoriteApi;
	
	private String tudouCodePattern;
	private String tudouItemidTransferApi;

	private String qqChannelApi;

	private String qqVideoApi;

	private String qqVideoPlayApi;

	private String qqKeywordSearchApi;

	private String qqSearchVideosApi;

	private String qqSearchAlbumsApi;
	private String qqIdPattern;
	
	private String youkuVideoApi;
	private String youkuVideoPlayApi;
	private String youkuShowidPattern;
	
	private String sohuURLPattern;
	private String sohuIdPattern;
	private String sohuVideoApi;
	
	private String fivesixCodePattern;
	private String fivesixVideoHtmlApi;
	private String fivesixVideoApi;
	
	private String weiboLoginURL;
	private String weiboAppKey;
	private String weiboAppSercret;
	private String weiboRedirectURL;
	private String weiboBaseURL;
	private String weiboAccessTokenURL;
	private String weiboAuthorizeURL;
	private String weiboRmURL;
	private String weiboTokenFile;
	private String weiboTempImageFolder;
	private String weiboTempImageURL;
	private String weiboShortURLPattern;
	
	private String sinaMusicIdPattern;
	private String sinaMusicApi;

	private String xiamiMusicApi;
	private String xiamiMusicIdPattern;
	
	private String baibianChannelApi;
	
	public SpringProperty() {
		instance = this;
	}

	public static SpringProperty getInstance() {
		return instance;
	}

	public String getSystemServerUrl() {
		return systemServerUrl;
	}

	public void setSystemServerUrl(String systemServerUrl) {
		this.systemServerUrl = systemServerUrl;
	}

	public String getTudouSessionid() {
		return tudouSessionid;
	}

	public void setTudouSessionid(String tudouSessionid) {
		this.tudouSessionid = tudouSessionid;
	}

	public String getTudouRecommendApi() {
		return tudouRecommendApi;
	}

	public void setTudouRecommendApi(String tudouRecommendApi) {
		this.tudouRecommendApi = tudouRecommendApi;
	}

	public String getTudouChannelApi() {
		return tudouChannelApi;
	}

	public void setTudouChannelApi(String tudouChannelApi) {
		this.tudouChannelApi = tudouChannelApi;
	}

	public String getTudouAlbumChannelApi() {
		return tudouAlbumChannelApi;
	}

	public void setTudouAlbumChannelApi(String tudouAlbumChannelApi) {
		this.tudouAlbumChannelApi = tudouAlbumChannelApi;
	}

	public String getTudouAlbumVideosApi() {
		return tudouAlbumVideosApi;
	}

	public void setTudouAlbumVideosApi(String tudouAlbumVideosApi) {
		this.tudouAlbumVideosApi = tudouAlbumVideosApi;
	}

	public String getTudouKeywordSearchApi() {
		return tudouKeywordSearchApi;
	}

	public void setTudouKeywordSearchApi(String tudouKeywordSearchApi) {
		this.tudouKeywordSearchApi = tudouKeywordSearchApi;
	}

	public String getTudouSearchApi() {
		return tudouSearchApi;
	}

	public void setTudouSearchApi(String tudouSearchApi) {
		this.tudouSearchApi = tudouSearchApi;
	}

	public String getTudouGetFavoriteApi() {
		return tudouGetFavoriteApi;
	}

	public void setTudouGetFavoriteApi(String tudouGetFavoriteApi) {
		this.tudouGetFavoriteApi = tudouGetFavoriteApi;
	}

	public String getTudouSetFavoriteApi() {
		return tudouSetFavoriteApi;
	}

	public void setTudouSetFavoriteApi(String tudouSetFavoriteApi) {
		this.tudouSetFavoriteApi = tudouSetFavoriteApi;
	}

	public String getQqChannelApi() {
		return qqChannelApi;
	}

	public void setQqChannelApi(String qqChannelApi) {
		this.qqChannelApi = qqChannelApi;
	}

	public String getQqVideoApi() {
		return qqVideoApi;
	}

	public void setQqVideoApi(String qqVideoApi) {
		this.qqVideoApi = qqVideoApi;
	}

	public String getQqVideoPlayApi() {
		return qqVideoPlayApi;
	}

	public void setQqVideoPlayApi(String qqVideoPlayApi) {
		this.qqVideoPlayApi = qqVideoPlayApi;
	}

	public String getQqKeywordSearchApi() {
		return qqKeywordSearchApi;
	}

	public void setQqKeywordSearchApi(String qqKeywordSearchApi) {
		this.qqKeywordSearchApi = qqKeywordSearchApi;
	}

	public String getQqSearchVideosApi() {
		return qqSearchVideosApi;
	}

	public void setQqSearchVideosApi(String qqSearchVideosApi) {
		this.qqSearchVideosApi = qqSearchVideosApi;
	}

	public String getQqSearchAlbumsApi() {
		return qqSearchAlbumsApi;
	}

	public void setQqSearchAlbumsApi(String qqSearchAlbumsApi) {
		this.qqSearchAlbumsApi = qqSearchAlbumsApi;
	}

	public Integer getSystemHttpconnectionTimeout() {
		return systemHttpconnectionTimeout;
	}

	public void setSystemHttpconnectionTimeout(
			Integer systemHttpconnectionTimeout) {
		this.systemHttpconnectionTimeout = systemHttpconnectionTimeout;
	}

	public String getWeiboAppKey() {
		return weiboAppKey;
	}

	public void setWeiboAppKey(String weiboAppKey) {
		this.weiboAppKey = weiboAppKey;
	}

	public String getWeiboAppSercret() {
		return weiboAppSercret;
	}

	public void setWeiboAppSercret(String weiboAppSercret) {
		this.weiboAppSercret = weiboAppSercret;
	}

	public String getWeiboBaseURL() {
		return weiboBaseURL;
	}

	public void setWeiboBaseURL(String weiboBaseURL) {
		this.weiboBaseURL = weiboBaseURL;
	}

	public String getWeiboAccessTokenURL() {
		return weiboAccessTokenURL;
	}

	public void setWeiboAccessTokenURL(String weiboAccessTokenURL) {
		this.weiboAccessTokenURL = weiboAccessTokenURL;
	}

	public String getWeiboAuthorizeURL() {
		return weiboAuthorizeURL;
	}

	public void setWeiboAuthorizeURL(String weiboAuthorizeURL) {
		this.weiboAuthorizeURL = weiboAuthorizeURL;
	}

	public String getWeiboRmURL() {
		return weiboRmURL;
	}

	public void setWeiboRmURL(String weiboRmURL) {
		this.weiboRmURL = weiboRmURL;
	}

	public String getWeiboRedirectURL() {
		return weiboRedirectURL;
	}

	public void setWeiboRedirectURL(String weiboRedirectURL) {
		this.weiboRedirectURL = weiboRedirectURL;
	}

	public String getWeiboTokenFile() {
		return weiboTokenFile;
	}

	public void setWeiboTokenFile(String weiboTokenFile) {
		this.weiboTokenFile = weiboTokenFile;
	}

	public String getWeiboLoginURL() {
		return weiboLoginURL;
	}

	public void setWeiboLoginURL(String weiboLoginURL) {
		this.weiboLoginURL = weiboLoginURL;
	}

	public String getWeiboTempImageFolder() {
		return weiboTempImageFolder;
	}

	public void setWeiboTempImageFolder(String weiboTempImageFolder) {
		this.weiboTempImageFolder = weiboTempImageFolder;
	}

	public String getWeiboTempImageURL() {
		return weiboTempImageURL;
	}

	public void setWeiboTempImageURL(String weiboTempImageURL) {
		this.weiboTempImageURL = weiboTempImageURL;
	}

	public String getSystemProxyHost() {
		return systemProxyHost;
	}

	public void setSystemProxyHost(String systemProxyHost) {
		this.systemProxyHost = systemProxyHost;
	}

	public Integer getSystemProxyPort() {
		return systemProxyPort;
	}

	public void setSystemProxyPort(Integer systemProxyPort) {
		this.systemProxyPort = systemProxyPort;
	}

	public boolean isSystemProxyEnable() {
		return systemProxyEnable;
	}

	public void setSystemProxyEnable(boolean systemProxyEnable) {
		this.systemProxyEnable = systemProxyEnable;
	}

	public String getWeiboShortURLPattern() {
		return weiboShortURLPattern;
	}

	public void setWeiboShortURLPattern(String weiboShortURLPattern) {
		this.weiboShortURLPattern = weiboShortURLPattern;
	}

	public String getYoukuVideoApi() {
		return youkuVideoApi;
	}

	public void setYoukuVideoApi(String youkuVideoApi) {
		this.youkuVideoApi = youkuVideoApi;
	}

	public String getYoukuVideoPlayApi() {
		return youkuVideoPlayApi;
	}

	public void setYoukuVideoPlayApi(String youkuVideoPlayApi) {
		this.youkuVideoPlayApi = youkuVideoPlayApi;
	}

	public String getYoukuShowidPattern() {
		return youkuShowidPattern;
	}

	public void setYoukuShowidPattern(String youkuShowidPattern) {
		this.youkuShowidPattern = youkuShowidPattern;
	}

	public String getQqIdPattern() {
		return qqIdPattern;
	}

	public void setQqIdPattern(String qqIdPattern) {
		this.qqIdPattern = qqIdPattern;
	}

	

	public String getTudouCodePattern() {
		return tudouCodePattern;
	}

	public void setTudouCodePattern(String tudouCodePattern) {
		this.tudouCodePattern = tudouCodePattern;
	}

	public String getTudouItemidTransferApi() {
		return tudouItemidTransferApi;
	}

	public void setTudouItemidTransferApi(String tudouItemidTransferApi) {
		this.tudouItemidTransferApi = tudouItemidTransferApi;
	}

	public String getSohuURLPattern() {
		return sohuURLPattern;
	}

	public void setSohuURLPattern(String sohuURLPattern) {
		this.sohuURLPattern = sohuURLPattern;
	}

	public String getSohuVideoApi() {
		return sohuVideoApi;
	}

	public void setSohuVideoApi(String sohuVideoApi) {
		this.sohuVideoApi = sohuVideoApi;
	}

	public String getFivesixCodePattern() {
		return fivesixCodePattern;
	}

	public void setFivesixCodePattern(String fivesixCodePattern) {
		this.fivesixCodePattern = fivesixCodePattern;
	}

	public String getFivesixVideoApi() {
		return fivesixVideoApi;
	}

	public void setFivesixVideoApi(String fivesixVideoApi) {
		this.fivesixVideoApi = fivesixVideoApi;
	}

	public String getFivesixVideoHtmlApi() {
		return fivesixVideoHtmlApi;
	}

	public void setFivesixVideoHtmlApi(String fivesixVideoHtmlApi) {
		this.fivesixVideoHtmlApi = fivesixVideoHtmlApi;
	}

	public String getSohuIdPattern() {
		return sohuIdPattern;
	}

	public void setSohuIdPattern(String sohuIdPattern) {
		this.sohuIdPattern = sohuIdPattern;
	}

	public String getSinaMusicIdPattern() {
		return sinaMusicIdPattern;
	}

	public void setSinaMusicIdPattern(String sinaMusicIdPattern) {
		this.sinaMusicIdPattern = sinaMusicIdPattern;
	}

	public String getSinaMusicApi() {
		return sinaMusicApi;
	}

	public void setSinaMusicApi(String sinaMusicApi) {
		this.sinaMusicApi = sinaMusicApi;
	}

	public String getXiamiMusicApi() {
		return xiamiMusicApi;
	}

	public void setXiamiMusicApi(String xiamiMusicApi) {
		this.xiamiMusicApi = xiamiMusicApi;
	}

	public String getXiamiMusicIdPattern() {
		return xiamiMusicIdPattern;
	}

	public void setXiamiMusicIdPattern(String xiamiMusicIdPattern) {
		this.xiamiMusicIdPattern = xiamiMusicIdPattern;
	}

	public String getBaibianChannelApi() {
		return baibianChannelApi;
	}

	public void setBaibianChannelApi(String baibianChannelApi) {
		this.baibianChannelApi = baibianChannelApi;
	}

}
