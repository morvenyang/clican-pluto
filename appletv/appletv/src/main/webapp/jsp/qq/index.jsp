<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<c:if test="${channel.value==1001}">
<head><script src="${serverurl}/javascript/clican.js"/></head>
</c:if>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<shelf id="menu_bar">
			<sections>
				<shelfSection>
					<items>
						<c:forEach var="channel" items="${channels}">
								<c:if test="${channel.value==1001}">
									<moviePoster id="shelf_item_${channel.value}" alwaysShowTitles="true" accessibilityLabel="${channel.label}" featured="true" onSelect="atv.loadURL('${serverurl}/ctl/qq/search.xml');" onPlay="atv.loadURL('${serverurl}/ctl/qq/search.xml');">
										<title>${channel.label}</title>
										<image>${serverurl}/image/qq/channel/channel_${channel.value}.png</image>
										<defaultImage>resource://Poster.png</defaultImage>
									</moviePoster>
								</c:if>
								<c:if test="${channel.value!=1001}">
									<moviePoster id="shelf_item_${channel.value}" alwaysShowTitles="true" accessibilityLabel="${channel.label}" featured="true" onSelect="atv.loadURL('${serverurl}/ctl/qq/index.xml?channelId=${channel.value}');" onPlay="atv.loadURL('${serverurl}/ctl/qq/index.xml?channelId=${channel.value}');">
										<title>${channel.label}</title>
										<image>${serverurl}/image/qq/channel/channel_${channel.value}.png</image>
										<defaultImage>resource://Poster.png</defaultImage>
									</moviePoster>
								</c:if>
						</c:forEach>
					</items>
				</shelfSection>
			</sections>
		</shelf>
		<c:if test="${channel.value!=3}">
		<grid id="grid_1" columnCount="12">
					<items>
						<actionButton id="shelf_item_first_page" onSelect="atv.loadURL('${pagiurl}&amp;page=0');" onPlay="atv.loadURL('${pagiurl}&amp;page=0');">
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
		</c:if>
		<grid id="grid_2" columnCount="6">
			<items>
				<c:forEach var="video" items="${videos}">
						<c:if test="${video.isalbum!=1}">
							<moviePoster id="shelf_item_${video.coverId}" alwaysShowTitles="true" onSelect="atv.loadURL('${serverurl}/ctl/qq/album.xml?coverId=${video.coverId}');" onPlay="atv.loadURL('${serverurl}/ctl/qq/album.xml?coverId=${video.coverId}');">
								<title><![CDATA[${video.title}]]></title>
								<image>${video.pic}</image>
								<defaultImage>resource://Poster.png</defaultImage>
							</moviePoster>
						</c:if>
						<c:if test="${video.isalbum==1}">
							<moviePoster id="shelf_item_${video.id}" alwaysShowTitles="true" onSelect="qqClient.playVideo('${playdescurl}&amp;vid=${video.id}','${serverurl}');" onPlay="qqClient.playVideo('${playdescurl}&amp;vid=${video.id}','${serverurl}');">
								<title><![CDATA[${video.tt}]]></title>
								<subtitle><![CDATA[${video.subTt}]]></subtitle>
								<image>${video.pic}</image>
								<defaultImage>resource://Poster.png</defaultImage>
							</moviePoster>
						</c:if>
				</c:forEach>
			</items>
		</grid>
		<c:if test="${channel.value!=3}">
				<grid id="grid_3" columnCount="12">
					<items>
						<actionButton id="shelf_item_first_page" onSelect="atv.loadURL('${pagiurl}&amp;page=0');" onPlay="atv.loadURL('${pagiurl}&amp;page=0');">
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
					</c:if>
			
	</items>
</scroller>
</body>
</atv>