<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/clican.js"/>
<script src="${serverurl}/ctl/taobao/love.js?gender=${gender}&amp;tagId=${slectedTagId}"/>
</head>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<pagedGrid id="pg_bar">
			<c:forEach var="stagList" items="${btagList}">
				<grid id="grid_b" columnCount="10">
					<items>
						<c:forEach var="tag" items="${stagList}">
							<actionButton id="tag_s_${tag.id}" onSelect="atv.loadURL('${serverurl}/ctl/taobao/love.xml?gender=${gender}&amp;tagId=${tag.id}');" onPlay="atv.loadURL('${serverurl}/ctl/taobao/love.xml?gender=${gender}&amp;tagId=${tag.id}');">
								<title><![CDATA[${tag.name}]]></title>
							</actionButton>
						</c:forEach>
					</items>
				</grid>
			</c:forEach>
		</pagedGrid>
		<pagedGrid id="pg">
			<c:forEach var="item" items="${itemList}">
				<grid id="grid_${item.itemId}" columnCount="1">
					<items>
						<goldenPoster id="shelf_item_${item.itemId}" alwaysShowTitles="true" onSelect="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.itemId}');" onPlay="onPhotoSelection();">
							<title><![CDATA[${item.title}]]></title>
							<subtitle><![CDATA[${item.operateTime}]]></subtitle>
							<image>${item.picUrl}</image>
						</goldenPoster>
					</items>
				</grid>
			</c:forEach>
		</pagedGrid>
	</items>
</scroller>
</body>
</atv>