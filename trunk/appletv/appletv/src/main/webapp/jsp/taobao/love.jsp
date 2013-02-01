<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<shelf id="sort_bar">
			<sections>
				<shelfSection>
					<items>
						<c:forEach var="tag" items="${tagList}">
							<actionButton id="tag_${tag.id}" onSelect="atv.loadURL('${serverurl}/ctl/taobao/love.xml?gender=${gender}&amp;tagId=${tag.id}');" onPlay="atv.loadURL('${serverurl}/ctl/taobao/love.xml?gender=${gender}&amp;tagId=${tag.id}');">
								<title><![CDATA[${tag.name}]]></title>
								<image></image>
							</actionButton>
						</c:forEach>
					</items>
				</shelfSection>
			</sections>
		</shelf>
		<grid id="grid_2" columnCount="1000">
			<items>
				<c:forEach var="item" items="${itemList}">
						<goldenPoster id="shelf_item_${item.itemId}" alwaysShowTitles="true" onSelect="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.itemId}');" onPlay="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.itemId}');">
							<title><![CDATA[${item.title}]]></title>
							<subtitle><![CDATA[${item.operateTime}]]></subtitle>
							<image>${item.picUrl}</image>
						</goldenPoster>
				</c:forEach>
			</items>
		</grid>
	</items>
</scroller>
</body>
</atv>