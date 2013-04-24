<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<itemDetail id="itemdetail">
			<title><![CDATA[${video.title}]]></title>
			<summary></summary>
			<image style="moviePoster">${video.imageUrl}</image>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="album_1" onSelect="atv.loadURL('${video.mediaUrl}');" onPlay="atv.loadURL('${video.mediaUrl}');">
									<title>播放</title>
								</actionButton>
								<actionButton id="album_2" onSelect="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?feature=11&amp;deviceId='+atv.device.udid+'&amp;title='+encodeURIComponent('${video.title}')+'&amp;shareURL=http://sina.vstar365.com/sina/video/detail/${video.id}&amp;imageURL=${video.imageUrl}');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?title='+encodeURIComponent('${video.title}')+'&amp;shareURL=http://sina.vstar365.com/sina/video/detail/${video.id}&amp;imageURL=${video.imageUrl}');">
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