/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.api;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.TaobaoResponse;

/**
 * 
 * @author runzhi
 */
public interface AlipayClient {

    /**
     * 
     * 
     * @param <T>
     * @param request
     * @return
     * @throws AlipayApiException
     */
    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request) throws AlipayApiException;

    /**
     * 
     * 
     * @param <T>
     * @param request
     * @param accessToken
     * @return
     * @throws AlipayApiException
     */
    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request, String authToken)
                                                                                             throws AlipayApiException;

    /**
     * 
     * 
     * @param <T>
     * @param request
     * @param authToken
     * @param version
     * @return
     * @throws AlipayApiException
     */
    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request, String authToken,
                                                String version) throws AlipayApiException;
}
