<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
	<dialog id="dialog">
			<c:if test="${result}">
				<title>分享成功</title>
			</c:if>
			<c:if test="${!result}">
				<title>分享失败请稍候再试</title>
			</c:if>
			<description></description>
		</dialog>	
</body>
</atv>