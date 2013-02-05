<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/clican.js?ver=1"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/qq.js?ver=2"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/ejs.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/taobao.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/photoPreview.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<input type="button" onclick="taobaoClient.login('','');" value="淘宝登录"/>
<input type="button" onclick="taobaoClient.addToShoppingCart(16270348608,18655253252,'${taobaoHtmlTid}');" value="加入购物车"/>
<input type="button" onclick="taobaoClient.loadFavoriteItemPage('<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="商品收藏"/>
<input type="button" onclick="taobaoClient.loadFavoriteShopPage('<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="店铺收藏"/>
<input type="button" onclick="taobaoClient.getToken();" value="获取令牌"/>
<input type="button" onclick="appletv.loadURL('http://127.0.0.1/appletv/ctl/taobao/item.xml?itemId=16270348608&volume=12147&shopClickUrl='+encodeURIComponent('http://s.click.taobao.com/t?e=zGU34CA7K%2BPkqB04MQzdgG3VSuWRIvnJbEpKV5PcuKFufezxnvbpzyrw2Hthrecou0ks9%2Bc0ABgwjRVXLhlDBy4t2D5QGkgibcZogNYFDbPWq%2F2C8x%2FfbL6mrOqTOhR6%2BlJFYTO2yF%2FSd1o0eSe40pFAJI%2FJiogx5BqD%2BfjddvM1PA%3D%3D&spm=2014.21373190.1.0'));" value="商品"/>
<input type="button" onclick="onPhotoSelections('http://img01.taobaocdn.com/bao/uploaded/i1/T1IKfYXmtmXXcdjlIW_024455.jpg,http://img03.taobaocdn.com/bao/uploaded/i3/804320856/T2xSWHXllXXXXXXXXX_!!804320856.jpg,http://img01.taobaocdn.com/bao/uploaded/i1/804320856/T2rt5wXaJXXXXXXXXX_!!804320856.jpg,http://img03.taobaocdn.com/bao/uploaded/i3/804320856/T2lJ1wXcpXXXXXXXXX_!!804320856.jpg,http://img03.taobaocdn.com/bao/uploaded/i3/804320856/T2XB9dXmJXXXXXXXXX_!!804320856.jpg');
;" value="图片"/>
<input type="button" onclick="taobaoClient.addToFavorite('12729301574','<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="收藏"/>
<input type="button" onclick="taobaoClient.addToFavorite(21238720782,'<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="收藏2"/>
<input type="button" onclick="taobaoClient.loadItemsByCategory(69941669,438317049,'0MLGt8nPytA');" value="加载分类商品"/>
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