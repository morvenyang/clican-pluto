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
				<grid id="g${status1.count}">
					<items>
						<c:forEach items="${tsc.skuDisplayLabelValueMap[label]}" var="tsu" varStatus="status2">
							<actionButton id="ab${status2.count}" onPlay="${serverurl}/ctl/taobao/itemDetail.xml?itemId=${tsc.item.numIid}&amp;selectedValue=${status1.count-1}:${tsu.value}" onSelect="">
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
			<collectionDivider><title><![CDATA[库存:${tsc.selectedSku.quantity} 价格:${tsc.selectedSku.price}]]></title></collectionDivider>
		</items>
		
	</scroller>
</body>
</atv>