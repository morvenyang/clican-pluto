/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.api;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.taobao.api.ApiException;
import com.taobao.api.Constants;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoParser;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.TaobaoUploadRequest;
import com.taobao.api.internal.parser.json.ObjectJsonParser;
import com.taobao.api.internal.parser.xml.ObjectXmlParser;
import com.taobao.api.internal.util.RequestParametersHolder;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.internal.util.TaobaoLogger;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.internal.util.WebUtils;

/**
 * 
 * @author runzhi
 * @version $Id: DefaultAlipayClient.java, v 0.1 2012-11-4 09:45:21 runzhi Exp $
 */
public class DefaultAlipayClient implements AlipayClient {

    private String serverUrl;
    private String appId;
    private String privateKey;
    private String format         = Constants.FORMAT_JSON;
    private String sign_type      = AlipayConstants.SIGN_TYPE_RSA;

    private int    connectTimeout = 3000;
    private int    readTimeout    = 15000;

    public DefaultAlipayClient(String serverUrl, String appId, String privateKey) {
        this.serverUrl = serverUrl;
        this.appId = appId;
        this.privateKey = privateKey;
    }

    public DefaultAlipayClient(String serverUrl, String appId, String privateKey, String format) {
        this(serverUrl, appId, privateKey);
        this.format = format;
    }

    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request) throws AlipayApiException {
        return execute(request, null);
    }

    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request, String accessToken)
                                                                                             throws AlipayApiException {

        return execute(request, accessToken, "1.0");
    }

    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request, String authToken,
                                                String version) throws AlipayApiException {
        TaobaoParser<T> parser = null;
        if (Constants.FORMAT_XML.equals(this.format)) {
            parser = new ObjectXmlParser<T>(request.getResponseClass());
        } else {
            parser = new ObjectJsonParser<T>(request.getResponseClass());
        }

        return _execute(request, parser, authToken, version);
    }

    private <T extends TaobaoResponse> T _execute(TaobaoRequest<T> request, TaobaoParser<T> parser,
                                                  String authToken, String version)
                                                                                   throws AlipayApiException {
        Map<String, Object> rt = doPost(request, authToken, version);
        if (rt == null) {
            return null;
        }

        T tRsp = null;
        try {
            tRsp = parser.parse((String) rt.get("rsp"));
            tRsp.setBody((String) rt.get("rsp"));

        } catch (RuntimeException e) {
            TaobaoLogger.logBizError((String) rt.get("rsp"));
            throw e;
        } catch (ApiException e) {
            TaobaoLogger.logBizError((String) rt.get("rsp"));
            throw new AlipayApiException(e);
        }

        tRsp.setParams((TaobaoHashMap) rt.get("textParams"));
        if (!tRsp.isSuccess()) {
            TaobaoLogger.logErrorScene(rt, tRsp, "");
        }
        return tRsp;
    }

    public <T extends TaobaoResponse> Map<String, Object> doPost(TaobaoRequest<T> request,
                                                                 String accessToken, String version)
                                                                                                    throws AlipayApiException {
        Map<String, Object> result = new HashMap<String, Object>();
        RequestParametersHolder requestHolder = new RequestParametersHolder();
        TaobaoHashMap appParams = new TaobaoHashMap(request.getTextParams());
        requestHolder.setApplicationParams(appParams);

        TaobaoHashMap protocalMustParams = new TaobaoHashMap();
        protocalMustParams.put(AlipayConstants.METHOD, request.getApiMethodName());
        protocalMustParams.put(AlipayConstants.VERSION, version);
        protocalMustParams.put(AlipayConstants.APP_ID, this.appId);

        Long timestamp = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        df.setTimeZone(TimeZone.getTimeZone(Constants.DATE_TIMEZONE));
        protocalMustParams.put(AlipayConstants.TIMESTAMP, df.format(new Date(timestamp)));
        requestHolder.setProtocalMustParams(protocalMustParams);

        TaobaoHashMap protocalOptParams = new TaobaoHashMap();
        protocalOptParams.put(AlipayConstants.FORMAT, format);
        protocalOptParams.put(AlipayConstants.SIGN_TYPE, this.sign_type);
        protocalOptParams.put(AlipayConstants.ACCESS_TOKEN, accessToken);
        protocalOptParams.put(AlipayConstants.ALIPAY_SDK, Constants.SDK_VERSION);
        requestHolder.setProtocalOptParams(protocalOptParams);

        if (AlipayConstants.SIGN_TYPE_RSA.equals(this.sign_type)) {

            String signContent = AlipaySignature.getSignatureContent(requestHolder);

            protocalMustParams.put(AlipayConstants.SIGN, AlipaySignature.rsaSign(signContent,
                privateKey, Constants.CHARSET_UTF8));

        } else {
            protocalMustParams.put(AlipayConstants.SIGN, "");
        }

        StringBuffer urlSb = new StringBuffer(serverUrl);
        try {
            String sysMustQuery = WebUtils.buildQuery(requestHolder.getProtocalMustParams(),
                Constants.CHARSET_UTF8);
            String sysOptQuery = WebUtils.buildQuery(requestHolder.getProtocalOptParams(),
                Constants.CHARSET_UTF8);

            urlSb.append("?");
            urlSb.append(sysMustQuery);
            if (sysOptQuery != null & sysOptQuery.length() > 0) {
                urlSb.append("&");
                urlSb.append(sysOptQuery);
            }
        } catch (IOException e) {
            throw new AlipayApiException(e);
        }

        String rsp = null;
        try {
            if (request instanceof TaobaoUploadRequest) {
                TaobaoUploadRequest<T> uRequest = (TaobaoUploadRequest<T>) request;
                Map<String, FileItem> fileParams = TaobaoUtils.cleanupMap(uRequest.getFileParams());
                rsp = WebUtils.doPost(urlSb.toString(), appParams, fileParams, connectTimeout,
                    readTimeout);
            } else {
                rsp = WebUtils.doPost(urlSb.toString(), appParams, connectTimeout, readTimeout);
            }
        } catch (IOException e) {
            throw new AlipayApiException(e);
        }
        result.put("rsp", rsp);
        result.put("textParams", appParams);
        result.put("protocalMustParams", protocalMustParams);
        result.put("protocalOptParams", protocalOptParams);
        result.put("url", urlSb.toString());
        return result;
    }

}
