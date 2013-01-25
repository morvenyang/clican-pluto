<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/clican.js?ver=1"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/qq.js?ver=2"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/ejs.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<input type="button" onclick="qqClient.loadIndexPage(1,1,3);" value="测试"/>
<input type="button" onclick="qqClient.loadIndexPage(1,1,14);" value="电影"/>
<input type="button" onclick="qqClient.loadIndexPage('黑猫警长',0,1001);" value="查询"/>
<input type="button" onclick="qqClient.loadAlbumPage('hcjk4lpe3pnn84e');" value="juji"/>
<input type="button" onclick="appletv.loadTest();" value="测试"/>
<input type="button" onclick="appletv.loadAlbumXml(152199347,30,1,0,'<%=request.getAttribute("serverurl")%>');" value="测试剧集详细页面"/>
<input type="button" onclick="appletv.loadAlbumListXml(152199347,30,1,2,'<%=request.getAttribute("serverurl")%>');" value="测试剧集列表页面"/>
</body>
</html>