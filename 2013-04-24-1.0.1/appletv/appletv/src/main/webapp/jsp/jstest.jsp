<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/clican.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/qq.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/yyets.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/ejs.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/taobao.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/photoPreview.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/xunlei.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/tudou.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/youku.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="">
		<input type="text" name="input" id="input" size="200"/> 
		<input type="button" value="提交" onclick="eval(document.getElementById('input').value);"/>
</form>

<input type="button" onclick="taobaoClient.login('clicantest001','clican@001');" value="淘宝登录"/>
<input type="button" onclick="alert(Date.parse(new Date()));" value="当前时间"/>

<input type="button" onclick="taobaoClient.addToShoppingCart(16270348608,18655253252,'${taobaoHtmlTid}');" value="加入购物车"/>
<input type="button" onclick="taobaoClient.addToShoppingCart(15425635127,15425635127,'${taobaoHtmlTid}');" value="加入购物车2"/>
<input type="button" onclick="taobaoClient.loadFavoriteItemPage('<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="商品收藏"/>
<input type="button" onclick="taobaoClient.loadFavoriteShopPage('<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="店铺收藏"/>
<input type="button" onclick="taobaoClient.loadConfirmOrderPage('${taobaoHtmlToken}');" value="加载结算页面"/>
<input type="button" onclick="taobaoClient.changeAddress(1062799477);" value="修改收货地址"/>
<input type="button" onclick="taobaoClient.submitConfirmOrder();" value="提交订单"/>
<input type="button" onclick="taobaoClient.loadItemPage(20604348044,0);" value="dange1"/>
<input type="button" onclick="taobaoClient.loadItemPage(13764231039,'');" value="dange2"/>
<input type="button" onclick="taobaoClient.loadItemPage(16341035020,'');" value="dange3"/>

<input type="button" onclick="taobaoClient.getToken();" value="获取令牌"/>
<input type="button" onclick="appletv.loadURL('http://127.0.0.1/appletv/ctl/taobao/item.xml?itemId=16270348608&volume=12147&shopClickUrl='+encodeURIComponent('http://s.click.taobao.com/t?e=zGU34CA7K%2BPkqB04MQzdgG3VSuWRIvnJbEpKV5PcuKFufezxnvbpzyrw2Hthrecou0ks9%2Bc0ABgwjRVXLhlDBy4t2D5QGkgibcZogNYFDbPWq%2F2C8x%2FfbL6mrOqTOhR6%2BlJFYTO2yF%2FSd1o0eSe40pFAJI%2FJiogx5BqD%2BfjddvM1PA%3D%3D&spm=2014.21373190.1.0'));" value="商品"/>
<input type="button" onclick="onPhotoSelections('http://img01.taobaocdn.com/bao/uploaded/i1/T1IKfYXmtmXXcdjlIW_024455.jpg,http://img03.taobaocdn.com/bao/uploaded/i3/804320856/T2xSWHXllXXXXXXXXX_!!804320856.jpg,http://img01.taobaocdn.com/bao/uploaded/i1/804320856/T2rt5wXaJXXXXXXXXX_!!804320856.jpg,http://img03.taobaocdn.com/bao/uploaded/i3/804320856/T2lJ1wXcpXXXXXXXXX_!!804320856.jpg,http://img03.taobaocdn.com/bao/uploaded/i3/804320856/T2XB9dXmJXXXXXXXXX_!!804320856.jpg');" value="图片"/>
<input type="button" onclick="taobaoClient.addToFavorite('12729301574',1,'<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="收藏"/>
<input type="button" onclick="taobaoClient.addToFavorite(21238720782,1,'<%=request.getSession().getAttribute("taobaoHtmlToken")%>');" value="收藏2"/>
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

<br/>
<input type="button" onclick="userconfig.loadConfigPage();" value="设置"/>
<input type="button" onclick="appletv.showInputTextPage('设置本地服务器IP','本地服务器用于代理视频内容的加载,提升加载速度减少延迟卡顿现象',userconfig.saveLocalServerIP,'userconfig.saveLocalServerIP','');" value="设置IP" />
<input type="button" onclick="appletv.playM3u8('http://localhost/m4u8')" value="play m3u8" />
<br/>
<input type="button" onclick="yyetsClient.loadIndexPage('',1,'tv');" value="人人影视"/>
<input type="button" onclick="yyetsClient.loadVideoPage('11057');" value="行尸走肉"/>
<input type="button" onclick="yyetsClient.loadVideoPage('11012');" value="尼基塔"/>
<input type="button" onclick="yyetsClient.listVideosInSeason('3','');" value="视频list"/>
<br/>
<input type="button" onclick="xunleiClient.play('ed2k://|file|%E8%A1%8C%E5%B0%B8%E8%B5%B0%E8%82%89.The.Walking.Dead.S03E13.Chi_Eng.HDTVrip.720X400.mp4|203034532|CD40755A174FE5EA525208DA18DD3333|h=FCU3MZ65SO2FDBVQZ2IQPKG2TJJHKFBY|/');" value="Xunlei播放"/>
<br/>
<input type="button" onclick="tudouClient.loadIndexPage('',1,22);" value="土豆电影"/>
<input type="button" onclick="tudouClient.loadIndexPage('',1,30);" value="土豆电视"/>
<input type="button" onclick="tudouClient.loadIndexPage('',1,31);" value="土豆综艺"/>
<input type="button" onclick="tudouClient.loadIndexPage('',1,5);" value="土豆搞笑"/>
<input type="button" onclick="tudouClient.loadIndexPage('',1,9);" value="土豆动画"/>
<input type="button" onclick="tudouClient.loadVideoPage('http://www.tudou.com/albumcover/R_VQZQw4YKs.html',30,1);" value="土豆详细1"/>
<input type="button" onclick="tudouClient.loadVideoPage('http://www.tudou.com/albumplay/RNSP3yYN0Co/Yj1VB4-71xo.html',31,0);" value="土豆详细2"/>
<input type="button" onclick="tudouClient.loadVideoPage('http://www.tudou.com/programs/view/P1ExBZSGszE/',5,0);" value="土豆详细3"/>
<input type="button" onclick="tudouClient.loadVideoPage('http://www.tudou.com/albumcover/rPLUN_vz3OY.html',9,1);" value="火影"/>


<input type="button" onclick="tudouClient.loadItemsPage();" value="土豆详细"/>
<input type="button" onclick="tudouClient.loadSearchPage();" value="搜索"/>
<input type="button" onclick="tudouClient.loadIndexPage('行尸走肉',1,1001);" value="搜索hmjz"/>

<br/>
<input type="button" onclick="youkuClient.loadIndexPage('',1,96);" value="优库电影"/>
<input type="button" onclick="youkuClient.loadIndexPage('',1,97);" value="优库电视"/>
<input type="button" onclick="youkuClient.loadVideoPage('z2ab3cf18a05911df97c0',96,true,'http://res.mfs.ykimg.com/050D000050D93FE997927344320DE4A1');" value="电影"/>
<input type="button" onclick="youkuClient.loadVideoPage('zd6f6b2063ec011e2b356',97,true,'http://res.mfs.ykimg.com/050D0000512D62E49792734BB700AC33');" value="电视剧"/>
<input type="button" onclick="youkuClient.loadItemsPage();" value="电视剧列表"/>
<input type="button" onclick="youkuClient.play('XNTE5NDkyODQ4');" value="play"/>

</body>
</html>