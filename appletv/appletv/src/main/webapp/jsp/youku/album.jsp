<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<itemDetail id="itemdetail">
			<title>${album.title}</title>
			<summary>${album.desc}</summary>
			<image style="moviePoster">${album.img}</image>
			<table>
				<columnDefinitions><columnDefinition width="50"><title>其他信息</title></columnDefinition><columnDefinition width="50"><title></title></columnDefinition></columnDefinitions>
				<rows>
					<row><label><![CDATA[导演:${album.director}]]></label><label><![CDATA[年代:${album.showdate}]]></label></row>
					<row><label><![CDATA[分类:${album.genre}]]></label><label><![CDATA[地区:${album.area}]]></label></row>
					<row><label><![CDATA[剧集:]]></label><label><![CDATA[主演:${album.performer}]]></label></row>
				</rows>
			</table>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								
								
								<actionButton id="album_1" onSelect="atv.loadURL('${serverurl}/youku/play.xml?showid=${showid}');" onPlay="atv.loadURL('${serverurl}/youku/play.xml?showid=${showid}');">
									<title>高清</title>
								</actionButton>
								<actionButton id="album_4" onSelect="atv.loadURL('${serverurl}/weibo/createStatus.xml?title=encodeURIComponent(${album.title})&amp;shareURL=http://v.youku.com/v_show/id_${showid}.html&amp;imageURL=${album.img}');" onPlay="atv.loadURL('${serverurl}/weibo/createStatus.xml?title=encodeURIComponent(${album.title})&amp;shareURL=http://v.youku.com/v_show/id_${showid}.html&amp;imageURL=${album.img}');">
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