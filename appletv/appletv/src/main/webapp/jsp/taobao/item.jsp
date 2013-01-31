<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/clican.js"></script>
<script src="${serverurl}/javascript/taobao.js"></script>
<script src="${serverurl}/javascript/photoPreview.js"/>
</head>
<body>
<itemDetail id="itemdetail">
			<title><![CDATA[${item.title}]]></title>
			<summary></summary>
			<image style="moviePoster">${item.picUrl}</image>
			<table>
				<columnDefinitions><columnDefinition width="20"><title>其他信息</title></columnDefinition><columnDefinition width="80"><title></title></columnDefinition></columnDefinitions>
				<rows>
					<row><label></label><label><![CDATA[价格:${item.price}元]]></label></row>
					<c:if test="${promotion!=null}">
						<row><<label></label>label><![CDATA[${promotion}]]></label></row>
					</c:if>
					<row><label></label><label><![CDATA[最近售出:${item.volume}]]></label></row>
					<row><label></label><label><![CDATA[运费:快递 ${item.expressFee}元;EMS ${item.emsFee}元;平邮 ${item.postFee}元]]></label></row>
					<c:if test="${item.stuffStatus=='new'}">
						<row><label></label><label><![CDATA[宝贝类型:全新]]></label></row>
					</c:if>
					<c:if test="${item.stuffStatus=='second'}">
						<row><label></label><label><![CDATA[宝贝类型:二手]]></label></row>
					</c:if>
					<c:if test="${item.stuffStatus=='unused'}">
						<row><label></label><label><![CDATA[宝贝类型:闲置]]></label></row>
					</c:if>
					<row><label></label><label><![CDATA[所在地区:${item.location.city}]]></label></row>
					<row><label></label><label><![CDATA[掌柜:${item.nick}]]></label></row>
				</rows>
			</table>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								<c:if test="${imageUrls!=null}">
									<actionButton id="album_0" onSelect="onPhotoSelections('${fn:escapeXml(imageUrls)}');" onPlay="onPhotoSelections('${fn:escapeXml(imageUrls)}');">
										<title>图片</title>
									</actionButton>
								</c:if>
								<actionButton id="album_1" onSelect="taobaoClient.addToFavorite(${item.numIid},1,'${taobaoHtmlToken}');" onPlay="taobaoClient.addToFavorite(${item.numIid},'${taobaoHtmlToken}');">
									<title>收藏</title>
								</actionButton>
								<actionButton id="album_2" onSelect="atv.loadURL('${serverurl}/ctl/taobao/shop.xml?nick='+encodeURIComponent('${item.nick}'));" onPlay="atv.loadURL('${serverurl}/ctl/taobao/shop.xml?nick='+encodeURIComponent('${item.nick}'));">
									<title>店铺</title>
								</actionButton>
								<actionButton id="album_3" onSelect="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?deviceId='+atv.device.udid+'&amp;feature=10&amp;title='+encodeURIComponent('${fn:escapeXml(item.title)}')+'&amp;shareURL=${fn:escapeXml(item.detailUrl)}&amp;imageURL=${fn:escapeXml(item.picUrl)}');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?deviceId='+atv.device.udid+'&amp;feature=10&amp;title='+encodeURIComponent('${fn:escapeXml(item.title)}')+'&amp;shareURL=${fn:escapeXml(item.detailUrl)}&amp;imageURL=${fn:escapeXml(item.picUrl)}');">
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