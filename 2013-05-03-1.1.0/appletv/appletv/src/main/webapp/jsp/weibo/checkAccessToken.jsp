<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?><atv>
<body>
	    <dialog id="dialog">
	        <title><![CDATA[请通过以下URL和你的DeviceID进行新浪微博授权, 然后返回重试]]></title>
	        <description><![CDATA[
	         <c:if test="${simulate!=null&&simulate=='native'}">
	         URL:<a href="http://clican.org">http://clican.org</a>
	         </c:if>
	        <c:if test="${simulate==null||simulate!='native'}">
	         URL:http://clican.org
	         </c:if>
	        DeviceID:${deviceId}
	        <c:if test="${deviceId==null||fn:length(deviceId)==0}">如果DeviceID没有显示请通过Clican-->Weibo进入</c:if>
	        <c:if test="${deviceId!=null&&fn:length(deviceId)>0&&simulate!=null&&simulate=='native'}">直接点击授权链接:<a href="${weiboLoginURL}">授权</a></c:if>
	        ]]></description>
	    </dialog>
</body>
</atv>