<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/clican.js"/>
<script src="${serverurl}/javascript/taobao.js"/>
<script src="${serverurl}/javascript/photoPreview.js"/>
</head>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="address_grid" columnCount="10">
			<items>
				<c:forEach var="addr" items="${tco.addrList}" varStatus="status1">
					<actionButton id="address_button_${status1.count}" onSelect="taobaoClient.changeAddress(${addr.addrId});">
						<c:if test="${tco.selectedAddrId==addr.addrId}">
							<title>√地址${status1.count}</title>
						</c:if>
						<c:if test="${tco.selectedAddrId!=addr.addrId}">
							<title>地址${status1.count}</title>
						</c:if>
					</actionButton>
				</c:forEach>
			</items>
		</grid>
		<c:forEach var="addr" items="${tco.addrList}" varStatus="status2">
			<collectionDivider>
				<c:if test="${tco.selectedAddrId==addr.addrId}">
					<title><![CDATA[√地址${status2.count}: ${addr.address}]]></title>
				</c:if>
				<c:if test="${tco.selectedAddrId!=addr.addrId}">
					<title><![CDATA[地址${status2.count}: ${addr.address}]]></title>
				</c:if>
			</collectionDivider>
		</c:forEach>
		<c:forEach var="shop" items="${tco.shopList}" varStatus="status3">
			<collectionDivider><title><![CDATA[${shop.title}]]></title></collectionDivider>
			<grid id="shop_item_grid_${status3.count}" columnCount="5">
				<items>
					<c:forEach var="item" items="${shop.itemList}">
						<moviePoster id="shelf_item_${item.itemId}" alwaysShowTitles="true">
							<title><![CDATA[${item.title}]]></title>
							<image>${item.picUrl}</image>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
					</c:forEach>
				</items>
			</grid>
		</c:forEach>
		<grid id="submit_grid" columnCount="10">
			<items>
				<actionButton id="submit1" onSelect="onPhotoSelection('${serverurl}/ctl/taobao/getConfirmOrder.png?random=${tco.random}'});" onPlay="onPhotoSelection('${serverurl}/ctl/taobao/getConfirmOrder.png?random=${tco.random}'});">
					<title>详细</title>
				</actionButton>
				<actionButton id="submit1" onSelect="taobaoClient.submitConfirmOrder();" onPlay="taobaoClient.submitConfirmOrder();">
					<title>提交</title>
				</actionButton>
			</items>
		</grid>
	</items>
</scroller>
</body>
</atv>