<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="grid_1" columnCount="12">
					<items>
						<actionButton id="shelf_item_first_page" onSelect="atv.loadURL('${pagiurl}&amp;page=1');" onPlay="atv.loadURL('${pagiurl}&amp;page=1');">
							<title>第一页</title>
						</actionButton>
						<c:forEach var="i" begin="${begin}" end="${end}">
							<actionButton id="shelf_item_${i}_page" onSelect="atv.loadURL('${pagiurl}&amp;page=${i}');" onPlay="atv.loadURL('${pagiurl}&amp;page=${i}');">
								<title>${i+1}</title>
							</actionButton>
						</c:forEach>
						<actionButton id="shelf_item_last_page" onSelect="atv.loadURL('${pagiurl}&amp;page=99');" onPlay="atv.loadURL('${pagiurl}&amp;page=99');">
							<title>最后页</title>
						</actionButton>
					</items>
					</grid>
		<grid id="grid_2" columnCount="6">
			<items>
				<c:forEach var="video" items="${videos}">
						<moviePoster id="shelf_item_${video.id}" alwaysShowTitles="true" onSelect="atv.loadURL('${serverurl}/ctl/baibian/video.xml?title='+encodeURIComponent('${video.title}')+'&amp;id=${video.id}&amp;imageUrl=${video.imageUrl}&amp;mediaUrl='+encodeURIComponent('${video.mediaUrl}'));" onPlay="atv.loadURL('${serverurl}/ctl/baibian/video.xml?title='+encodeURIComponent('${video.title}')+'&amp;id=${video.id}&amp;imageUrl=${video.imageUrl}&amp;mediaUrl='+encodeURIComponent('${video.mediaUrl}'));">
							<title><![CDATA[${video.title}]]></title>
							<image>${video.imageUrl}</image>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
				</c:forEach>
			</items>
		</grid>
				<grid id="grid_3" columnCount="12">
					<items>
						<actionButton id="shelf_item_first_page" onSelect="atv.loadURL('${pagiurl}&amp;page=1');" onPlay="atv.loadURL('${pagiurl}&amp;page=1');">
							<title>第一页</title>
						</actionButton>
						<c:forEach var="i" begin="${begin}" end="${end}">
							<actionButton id="shelf_item_${i}_page" onSelect="atv.loadURL('${pagiurl}&amp;page=${i}');" onPlay="atv.loadURL('${pagiurl}&amp;page=${i}');">
								<title>${i+1}</title>
							</actionButton>
						</c:forEach>
						<actionButton id="shelf_item_last_page" onSelect="atv.loadURL('${pagiurl}&amp;page=99');" onPlay="atv.loadURL('${pagiurl}&amp;page=99');">
							<title>最后页</title>
						</actionButton>
					</items>
					</grid>
			
	</items>
</scroller>
</body>
</atv>