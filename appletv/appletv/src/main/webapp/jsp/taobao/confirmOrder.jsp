<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="address_grid" columnCount="10">
			<items>
				<c:forEach var="addr" items="${tco.addrList}" varStatus="status1">
					<actionButton id="address_button_${status1.count}" onSelect="${serverurl}/ctl/taobao/changeAddr.xml?areaCode=${addr.areaCode}&amp;addrId=${addr.addrId}">
						<c:if test="${tco.selectedAddrId.equals(addr.addrId)}">
							<title>√地址${status1.count+1}</title>
						</c:if>
						<c:if test="${!tco.selectedAddrId.equals(addr.addrId)}">
							<title>地址${status1.count+1}</title>
						</c:if>
					</actionButton>
				</c:forEach>
			</items>
		</grid>
		<c:forEach var="addr" items="${tco.addrList}" varStatus="status2">
			<collectionDivider>
				<c:if test="${tco.selectedAddrId.equals(addr.addrId)}">
					<title><![CDATA[√地址${status2.count+1}: ${addr.address}]]></title>
				</c:if>
				<c:if test="${!tco.selectedAddrId.equals(addr.addrId)}">
					<title><![CDATA[地址${status2.count+1}: ${addr.address}]]></title>
				</c:if>
			</collectionDivider>
		</c:forEach>
		
		<grid id="address_grid" columnCount="1">
			<items>
				<c:forEach var="item" items="${itemList}">
						<moviePoster id="shelf_item_${item.numIid}" alwaysShowTitles="true" onSelect="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.numIid}&amp;volume=${item.volume}');" onPlay="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.numIid}&amp;volume=${item.volume}');">
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