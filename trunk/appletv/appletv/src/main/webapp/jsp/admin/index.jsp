<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理</title>
</head>
<body>
<form action="/appletv/ctl/apns/sendMessage.do" method="post">
		APNS消息:<input type="text" name="message" size="50"/><input type="submit" value="发送"/><br/> 
</form>
</body>
</html>