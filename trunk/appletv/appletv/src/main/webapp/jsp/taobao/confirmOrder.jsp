<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="address_grid" columnCount="10">
			<items>
				<c:forEach var="addr" items="${tco.addrList}" varStatus="status1">
					<actionButton id="address_button_${status1.count}" onSelect="${serverurl}/ctl/taobao/changeAddr.xml?areaCode=${addr.areaCode}&amp;addrId=${addr.addrId}">
						<c:if test="${tco.selectedAddrId==addr.addrId}">
							<title>√地址${status1.count+1}</title>
						</c:if>
						<c:if test="${tco.selectedAddrId!=addr.addrId}">
							<title>地址${status1.count+1}</title>
						</c:if>
					</actionButton>
				</c:forEach>
			</items>
		</grid>
		<c:forEach var="addr" items="${tco.addrList}" varStatus="status2">
			<collectionDivider>
				<c:if test="${tco.selectedAddrId==addr.addrId}">
					<title><![CDATA[√地址${status2.count+1}: ${addr.address}]]></title>
				</c:if>
				<c:if test="${tco.selectedAddrId!=addr.addrId}">
					<title><![CDATA[地址${status2.count+1}: ${addr.address}]]></title>
				</c:if>
			</collectionDivider>
		</c:forEach>
		<c:forEach var="shop" items="${tco.shopList}" varStatus="status3">
			<collectionDivider><title><![CDATA[${shop.title}]]></title></collectionDivider>
			<grid id="shop_fare_${status3.count}" columnCount="5">
				<items>
					<c:forEach var="fare" items="${shop.fareList}">
						<actionButton onSelect="${serverurl}/ctl/taobao/changeFare.xml?outOrderId=${shop.outOrderId}&amp;fareId=${fare.id}">
							<c:if test="${shop.selectedFareId==fare.id}">
								<title><![CDATA[√${fare.label}]]></title>
							</c:if>
							<c:if test="${shop.selectedFareId!=fare.id}">
								<title><![CDATA[${fare.label}]]></title>
							</c:if>
						</actionButton>
					</c:forEach>
				</items>
			</grid>
			<grid id="shop_item_grid_${status3.count}" columnCount="5">
				<items>
					<c:forEach var="item" items="${shop.itemList}">
						<moviePoster id="shelf_item_${item.itemId}" alwaysShowTitles="true">
							<title><![CDATA[${item.title}]]></title>
							<subtitle><![CDATA[价格:${item.price}元 数量:${item.quantity}]]></subtitle>
							<image>${item.picUrl}</image>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
					</c:forEach>
				</items>
			</grid>
		</c:forEach>
	</items>
</scroller>
</body>
</atv>