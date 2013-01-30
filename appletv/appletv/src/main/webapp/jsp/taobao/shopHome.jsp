<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="grid_2" columnCount="6">
			<items>
				<c:forEach var="item" items="${itemList}">
						<moviePoster id="shelf_item_${item.numIid}" alwaysShowTitles="true" onSelect="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.numIid}&amp;volume=${item.volume}'));" onPlay="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.numIid}&amp;volume=${item.volume}'));">
							<title><![CDATA[${item.title}]]></title>
							<subtitle><![CDATA[￥${item.price} 最近售出${item.volume}]]></subtitle>
							<image>${item.picUrl}</image>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
				</c:forEach>
			</items>
		</grid>
	</items>
</scroller>
</body>
</atv>