<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
</head>
<body>
<form action="/appletv/ctl/auth/login.do">
		用户名:<input type="text" name="userName" size="50"/><br/> 
		密&nbsp;&nbsp;&nbsp;&nbsp;码:<input type="password" name="password" size="50"/> <br/>
		<p style="color:red">${message}</p>
		<input type="submit" value="登录"/>
</form>
</body>
</html>