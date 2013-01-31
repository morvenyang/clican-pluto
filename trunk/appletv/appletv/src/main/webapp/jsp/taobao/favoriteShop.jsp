<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="grid_2" columnCount="6">
			<items>
				<c:forEach var="shop" items="${shopList}" varStatus="status">
						<moviePoster id="shelf_item_${status.count}" alwaysShowTitles="true" onSelect="atv.loadURL('${serverurl}/ctl/taobao/shop.xml?nick='+encodeURIComponent('${item.nick}'));" onPlay="atv.loadURL('${serverurl}/ctl/taobao/shop.xml?nick='+encodeURIComponent('${item.nick}'));">
							<title><![CDATA[${shop.title}]]></title>
							<image>${shop.picPath}</image>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
				</c:forEach>
			</items>
		</grid>
	</items>
</scroller>
</body>
</atv>