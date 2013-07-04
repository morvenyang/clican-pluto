<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/clican.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/ejs.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/taobao.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/userconfig.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/tudou.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/soku.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/tu.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/weivideo.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/baidumusic.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="">
		<input type="text" name="input" id="input"/> 
		<input type="button" value="提交" onclick="${callback}(document.getElementById('input').value);"/>
	</form>
</body>
</html>