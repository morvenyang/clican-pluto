<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/clican.js"></script>
<script src="${serverurl}/javascript/taobao.js"></script>
</head>
<body>
	<scroller id="s">
		<items>
			<c:forEach items="${labelList}" var="label" varStatus="status1">
				<collectionDivider><title><![CDATA[${label}]]></title></collectionDivider>
				<grid id="g${status1.count}">
					<items>
						<c:forEach items="${skuMap}" var="sku" varStatus="status2">
							<actionButton id="ab">
								<title><![CDATA[]]></title>
							</actionButton>
						</c:forEach>
					</items>
				</grid>
			</c:forEach>
		</items>
	</scroller>
</body>
</atv>