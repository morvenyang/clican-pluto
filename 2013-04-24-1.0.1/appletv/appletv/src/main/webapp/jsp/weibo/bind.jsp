<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>AppleTV3 Sina Weibo</title>
</head>
<body>
	<c:if test="${result}">
		<p>绑定成功</p>
	</c:if>
	<c:if test="${!result}">
		<p><a href="${weiboLoginURL}">绑定失败请重试</a></p>
	</c:if>
</body>
</html>