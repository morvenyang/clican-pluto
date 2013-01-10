<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
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
					<row><label><![CDATA[剧集:共${album.getVids().size()}集]]></label><label><![CDATA[主演:${album.actor}]]></label></row>
				</rows>
			</table>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="album_1" onSelect="atv.loadURL('${serverurl}/qq/albumlist.xml');" onPlay="atv.loadURL('${serverurl}/qq/albumlist.xml');">
									<title>高清</title>
								</actionButton>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
		</itemDetail>
</body>
</atv>