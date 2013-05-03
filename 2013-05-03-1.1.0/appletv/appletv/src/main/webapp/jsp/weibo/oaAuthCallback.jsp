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
	<form action="bind.do">
		AppleTV 设备号(请注意大小写保值一致):<input type="text" name="deviceId" value="${deviceId}"/>
		<input type="submit" value="绑定" />
	</form>
</body>
</html>