<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?><atv>
<body>
	    <dialog id="dialog">
	        <title><![CDATA[请通过以下URL和你的DeviceID进行新浪微博授权, 然后返回重试]]></title>
	        <description><![CDATA[
	        URL:http://clican.org/appletv/index.html
	        DeviceID:${deviceId}
	         <c:if test="${deviceId==null||fn:length(deviceId)==0}">
	         	如果DeviceID没有显示请通过Clican-->Weibo进入
	         </c:if>
	         <c:if test="${deviceId!=null&&fn:length(deviceId)>0}">
	         	或直接点击链接:${weiboLoginURL}
	         </c:if>
	        ]]></description>
	    </dialog>
</body>
</atv>