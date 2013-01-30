<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
<itemDetail id="shopDetail">
			<title><![CDATA[${shop.nick}]]></title>
			<summary><![CDATA[${shop.bulletin}]]></summary>
			<image style="moviePoster">http://logo.taobao.com/shop-logo${shop.picPath}</image>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="album_1" onSelect="taobaoClient.addToFavorite(${shop.sid},0,'${taobaoHtmlToken}');" onPlay="taobaoClient.addToFavorite(${shop.sid},'${taobaoHtmlToken}');">
									<title>收藏</title>
								</actionButton>
								<actionButton id="album_3" onSelect="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?deviceId='+atv.device.udid+'&amp;feature=10&amp;title=${fn:escapeXml(shop.title)}&amp;shareURL=http://shop${shop.sid}.taobao.com&amp;imageURL=http://logo.taobao.com/shop-logo${fn:escapeXml(shop.picPath)}');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?deviceId='+atv.device.udid+'&amp;feature=10&amp;title=${fn:escapeXml(shop.title)}&amp;shareURL=http://shop${shop.sid}.taobao.com&amp;imageURL=http://logo.taobao.com/shop-logo${fn:escapeXml(shop.picPath)}');">
									<title>分享</title>
								</actionButton>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
			<bottomShelf>
			    <shelf id=""></shelf>
			</bottomShelf>
		</itemDetail>
</body>
</atv>