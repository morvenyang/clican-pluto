<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/clican.js?ver=1"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/qq.js?ver=2"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/ejs.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/taobao.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<input type="button" onclick="taobaoClient.login('clicanclican','clican@810428');" value="淘宝登录"/>
<input type="button" onclick="taobaoClient.loadFavoritePage('<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="我的淘宝"/>
<input type="button" onclick="taobaoClient.getToken();" value="获取令牌"/>
<input type="button" onclick="taobaoClient.addToFavorite('12729301574','<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="收藏"/>
<input type="button" onclick="taobaoClient.addToFavorite(21238720782,'<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="收藏2"/>
<input type="button" onclick="appletv.showDialog('测试模拟','')" value="测试模拟"/>
<input type="button" onclick="qqClient.loadIndexPage(1,1,14);" value="电影"/>
<input type="button" onclick="qqClient.loadIndexPage('黑猫警长',0,1001);" value="查询"/>
<input type="button" onclick="qqClient.loadAlbumPage('hcjk4lpe3pnn84e');" value="剧集"/>
<input type="button" onclick="qqClient.loadItemsPage('hcjk4lpe3pnn84e');" value="剧集列表"/>
<input type="button" onclick="qqClient.playVideo('p00116ducjk');" value="播放"/>

<input type="button" onclick="appletv.loadTest();" value="测试"/>
<input type="button" onclick="appletv.loadAlbumXml(152199347,30,1,0,'<%=request.getAttribute("serverurl")%>');" value="测试剧集详细页面"/>
<input type="button" onclick="appletv.loadAlbumListXml(152199347,30,1,2,'<%=request.getAttribute("serverurl")%>');" value="测试剧集列表页面"/>
</body>
</html>