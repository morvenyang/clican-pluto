<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<itemDetail id="itemdetail">
			<title><![CDATA[${album.title}]]></title>
			<summary>${album.description}</summary>
			<image style="moviePoster">${album.picurl}</image>
			<table>
				<columnDefinitions><columnDefinition width="50"><title>其他信息</title></columnDefinition><columnDefinition width="50"><title></title></columnDefinition></columnDefinitions>
				<rows>
					<row><label><![CDATA[导演:${album.directors}]]></label><label><![CDATA[年代:${album.year}]]></label></row>
					<row><label><![CDATA[类型:${album.typeDesc}]]></label><label><![CDATA[地区:${album.areaDesc}]]></label></row>
					<row><label><![CDATA[剧集:共${album.size}集]]></label><label><![CDATA[主演:${album.actors}]]></label></row>
				</rows>
			</table>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="album_1" onSelect="atv.loadURL('${serverurl}/ctl/tudou/albumlist.xml?st=2');" onPlay="atv.loadURL('${serverurl}/ctl/tudou/albumlist.xml?st=2');">
									<title>标清</title>
								</actionButton>
								<actionButton id="album_2" onSelect="atv.loadURL('${serverurl}/ctl/tudou/albumlist.xml?st=3');" onPlay="atv.loadURL('${serverurl}/ctl/tudou/albumlist.xml?st=3');">
									<title>高清</title>
								</actionButton>
								<c:if test="${album.hd==1}">
									<actionButton id="album_3" onSelect="atv.loadURL('${serverurl}/ctl/tudou/albumlist.xml?st=4');" onPlay="atv.loadURL('${serverurl}/ctl/tudou/albumlist.xml?st=4');">
										<title>超清</title>
									</actionButton>
								</c:if>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
		</itemDetail>
</body>
</atv>