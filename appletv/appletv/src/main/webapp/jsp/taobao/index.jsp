<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/clican.js"></script>
<script src="${serverurl}/javascript/taobao.js"></script>
</head>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="grid_2" columnCount="2">
			<items>
				 <goldenPoster id="gp1" onSelect="atv.loadURL('${serverurl}/ctl/taobao/category.xml');" onPlay="atv.loadURL('${serverurl}/ctl/taobao/category.xml');">
                	<image>${serverurl}/image/taobao/category.png</image>
                	<title>类目浏览</title>
                 </goldenPoster>
                 <goldenPoster id="gp2" onSelect="taobaoClient.loadFavoriteItemPage('${taobaoHtmlToken}');" onPlay="taobaoClient.loadFavoriteItemPage('${taobaoHtmlToken}');">
                	<image>${serverurl}/image/taobao/favorite.png</image>
                	<title>收藏的商品</title>
                 </goldenPoster>
                 <goldenPoster id="gp3" onSelect="taobaoClient.loadFavoriteShopPage('${taobaoHtmlToken}');" onPlay="taobaoClient.loadFavoriteShopPage('${taobaoHtmlToken}');">
                	<image>${serverurl}/image/taobao/favorite.png</image>
                	<title>收藏的店铺</title>
                 </goldenPoster>
			</items>
		</grid>
	</items>
</scroller>
</body>
</atv>