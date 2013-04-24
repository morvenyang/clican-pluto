<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
	<dialog id="com.sample.error-dialog">
		<c:if test="${title==null}">
			 <title><![CDATA[没有相关数据]]></title>
		</c:if>
	   	<c:if test="${title!=null}">
			 <title><![CDATA[${title}]]></title>
		</c:if>
	    <description></description>
	 </dialog>
</body>
</atv>