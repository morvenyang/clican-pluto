<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<c:if test="${album.size<7}">
<head><script src="${serverurl}/javascript/clican.js"/></head>
</c:if>
<body>
<itemDetail id="itemdetail">
			<title>${album.tt}</title>
			<summary>${album.desc}</summary>
			<image style="moviePoster">${album.pic}</image>
			<table>
				<columnDefinitions><columnDefinition width="50"><title>其他信息</title></columnDefinition><columnDefinition width="50"><title></title></columnDefinition></columnDefinitions>
				<rows>
					<row><label><![CDATA[导演:${album.dctor}]]></label><label><![CDATA[年代:${album.year}]]></label></row>
					<row><label><![CDATA[评分:${album.score}]]></label><label><![CDATA[地区:${album.area}]]></label></row>
					<row><label><![CDATA[剧集:共${album.size}集]]></label><label><![CDATA[主演:${album.actor}]]></label></row>
				</rows>
			</table>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								<c:if test="${album.size<7}">
									<c:forEach var="albumItem" items="${album.albumItems}" varStatus="status">
										<actionButton id="album_${status.count}" onSelect="appletv.playQQVideo('${playdescurl}&amp;vid=${albumItem.vid}','${serverurl}');" onPlay="appletv.playQQVideo('${playdescurl}&amp;vid=${albumItem.vid}','${serverurl}');">
											<title>第${status.count}集</title>
										</actionButton>
									</c:forEach>
								</c:if>
								<c:if test="${album.size>7}">
									<actionButton id="album_1" onSelect="atv.loadURL('${serverurl}/qq/albumlist.xml');" onPlay="atv.loadURL('${serverurl}/qq/albumlist.xml');">
										<title>高清</title>
									</actionButton>
								</c:if>
								<actionButton id="album_8" onSelect="atv.loadURL('${serverurl}/weibo/createStatus.xml?title='+encodeURIComponent('${album.tt}')+'&amp;shareURL=http://v.qq.com/cover/${fn:substring(album.id,0,1)}/${album.id}.html&amp;imageURL=${album.pic}');" onPlay="atv.loadURL('${serverurl}/weibo/createStatus.xml?title='+encodeURIComponent('${album.tt}')+'&amp;shareURL=http://v.qq.com/cover/${fn:substring(album.id,0,1)}/${album.id}.html&amp;imageURL=${album.pic}');">
									<title>分享</title>
								</actionButton>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
		</itemDetail>
</body>
</atv>