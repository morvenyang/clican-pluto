<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<itemDetail id="itemdetail">
			<title><![CDATA[${music.name}]]></title>
			<summary></summary>
			<image style="moviePoster">${music.singerPhoto}</image>
			<table>
				<columnDefinitions><columnDefinition width="50"><title>其他信息</title></columnDefinition><columnDefinition width="50"><title></title></columnDefinition></columnDefinitions>
				<rows>
					<row><label><![CDATA[专辑:${music.albumName}]]></label><label><![CDATA[歌手:${music.singerName}]]></label></row>
					<row><label><![CDATA[风格:${music.style}]]></label><label></label></row>
				</rows>
			</table>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="album_1" onSelect="atv.loadURL('${serverurl}/ctl/sina/playMusic.xml?playUrlDesc=${music.mp3Url}');" onPlay="atv.loadURL('${serverurl}/ctl/sina/playMusic.xml?playUrlDesc=${music.mp3Url}');">
									<title>播放</title>
								</actionButton>
								<c:if test="${podcastURL!=null}">
									<actionButton id="album_2" onSelect="atv.loadURL('${podcastURL}');" onPlay="atv.loadURL('${podcastURL}');">
										<title>Podcast</title>
									</actionButton>
								</c:if>
								<actionButton id="album_3" onSelect="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?feature=4&amp;title='+encodeURIComponent('${music.name}')+'&amp;shareURL=http://music.sina.com.cn/yueku/i/${music.id}.html&amp;imageURL=${music.singerPhoto}');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?feature=4&amp;title='+encodeURIComponent('${music.name}')+'&amp;shareURL=http://music.sina.com.cn/yueku/i/${music.id}.html&amp;imageURL=${music.singerPhoto}');">
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