<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/clican.js"></script>
<script src="${serverurl}/javascript/taobao.js"></script>
</head>
<body>
	<scroller id="s">
		<items>
			<c:forEach items="${tsc.skuLabelList}" var="label" varStatus="status1">
				<collectionDivider><title><![CDATA[${label}]]></title></collectionDivider>
				<grid id="g${status1.count}" columnCount="5">
					<items>
						<c:forEach items="${tsc.skuDisplayLabelValueMap[label]}" var="tsu" varStatus="status2">
							<actionButton id="ab${status2.count}" onPlay="atv.loadURL('${serverurl}/ctl/taobao/itemSelect.xml?itemId=${tsc.item.numIid}&amp;selectedValue=${status1.count-1}:${tsu.value}');" onSelect="atv.loadURL('${serverurl}/ctl/taobao/itemSelect.xml?itemId=${tsc.item.numIid}&amp;selectedValue=${status1.count-1}:${tsu.value}');">
								<c:if test="${fn:contains(tsc.selectedValueString,tsu.value)}">
									<title><![CDATA[√${tsu.label}]]></title>
								</c:if>
								<c:if test="${!fn:contains(tsc.selectedValueString,tsu.value)}">
									<title><![CDATA[${tsu.label}]]></title>
								</c:if>
							</actionButton>
						</c:forEach>
					</items>
				</grid>
			</c:forEach>
			<c:if test="${tsc.selectedSku!=null}">
				<collectionDivider><title><![CDATA[库存:${tsc.selectedSku.quantity} 价格:${tsc.selectedSku.price}]]></title></collectionDivider>
				<grid id="addToShoppingCart" columnCount="2">
					<items>
						<actionButton id="addshop" onPlay="taobaoClient.addToShoppingCart(${tsc.item.numIid},${tsc.selectedSku.skuId},'${taobaoHtmlTid}');" onSelect="taobaoClient.addToShoppingCart(${tsc.item.numIid},${tsc.selectedSku.skuId},'${taobaoHtmlTid}');">
							<title>进购物车</title>
						</actionButton>
						<actionButton id="clean" onPlay="taobaoClient.loadConfirmOrderPage('${taobaoHtmlToken}');" onSelect="taobaoClient.loadConfirmOrderPage('${taobaoHtmlToken}');">
							<title>结           算</title>
						</actionButton>
					</items>
				</grid>
			</c:if>
			<c:if test="${tsc.selectedSku==null}">
				<collectionDivider><title><![CDATA[库存:${tsc.item.num} 价格:${tsc.item.price}]]></title></collectionDivider>
				<grid id="addToShoppingCart" columnCount="2">
					<items>
						<actionButton id="addshop" onPlay="taobaoClient.addToShoppingCart(${tsc.item.numIid},${tsc.item.numIid},'${taobaoHtmlTid}');" onSelect="taobaoClient.addToShoppingCart(${tsc.item.numIid},${tsc.item.numIid},'${taobaoHtmlTid}');">
							<title>进购物车</title>
						</actionButton>
						<actionButton id="clean" onPlay="taobaoClient.loadConfirmOrderPage('${taobaoHtmlToken}');" onSelect="taobaoClient.loadConfirmOrderPage('${taobaoHtmlToken}');">
							<title>结           算</title>
						</actionButton>
					</items>
				</grid>
			</c:if>
		</items>
		
	</scroller>
</body>
</atv>