<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<shelf id="menu_bar">
			<sections>
				<shelfSection>
					<items>
						<moviePoster id="shelf_item_recommand" accessibilityLabel="推荐" featured="true" onSelect="atv.loadURL('${serverurl}/tudou/index.xml');" onPlay="atv.loadURL('${serverurl}/tudou/index.xml');">
							<title>推荐</title>
							<image>${serverurl}/image/tudou/channel/channel_recommand.png</image>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
						<c:forEach var="channel" items="${channels}">
							<moviePoster id="shelf_item_${channel.value}" accessibilityLabel="${channel.label}" featured="true" onSelect="atv.loadURL('${serverurl}/tudou/channel.xml?channelId=${channel.value}');" onPlay="atv.loadURL('${serverurl}/tudou/channel.xml?channelId=${channel.value}');">
								<title>${channel.label}</title>
								<image>${serverurl}/image/tudou/channel/channel_${channel.value}.png</image>
								<defaultImage>resource://Poster.png</defaultImage>
							</moviePoster>
						</c:forEach>
					</items>
				</shelfSection>
			</sections>
		</shelf>
		<grid id="grid_1" columnCount="6">
			<items>
				<c:forEach var="video" items="${videos}">
					<moviePoster id="shelf_item_${video.itemid}" onSelect="atv.loadURL('${serverurl}/play.xml?itemid=${video.itemid}');" onPlay="atv.loadURL('${serverurl}/appletv/play.xml?itemid=${video.itemid}');">
						<title><![CDATA[${video.title}]]></title>
						<subtitle><![CDATA[${video.title}]]></subtitle>
						<image>${video.picurl}</image>
						<defaultImage>resource://Poster.png</defaultImage>
					</moviePoster>
				</c:forEach>
			</items>
		</grid>
		<shelf id="foot_bar">
			<sections>
				<shelfSection>
					<items>
						<moviePoster id="shelf_item_first_page" featured="true" onSelect="atv.loadURL('${pagiurl}&amp;page=0');" onPlay="atv.loadURL('${pagiurl}&amp;page=0');">
							<title>第一页</title>
							<subtitle>第一页</subtitle>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
						<c:forEach var="i" begin="${begin}" end="${end}">
							<moviePoster id="shelf_item_${i}_page" featured="true" onSelect="atv.loadURL('${pagiurl}&amp;page=${i}');" onPlay="atv.loadURL('${pagiurl}&amp;page=${i}');">
								<title>${i+1}</title>
								<subtitle>${i+1}</subtitle>
								<defaultImage>resource://Poster.png</defaultImage>
							</moviePoster>
						</c:forEach>
						<moviePoster id="shelf_item_last_page" featured="true" onSelect="atv.loadURL('${pagiurl}&amp;page=99');" onPlay="atv.loadURL('${pagiurl}&amp;page=99');">
							<subtitle>最后页</subtitle>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
					</items>
				</shelfSection>
			</sections>
		</shelf>
	</items>
</scroller>
</body>
</atv>