<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/clican.js"></script>
<script type="text/javascript" src="<%=request.getAttribute("serverurl")%>/javascript/taobao.js"></script>
</head>
<body>
<itemDetail id="itemdetail">
			<title><![CDATA[${item.title}]]></title>
			<summary></summary>
			<image style="moviePoster">${item.picUrl}</image>
			<table>
				<columnDefinitions><columnDefinition width="100"><title>其他信息</title></columnDefinition></columnDefinitions>
				<rows>
					<row><label><![CDATA[价格:${item.price}元]]></label></row>
					<c:if test="${promotion!=null}">
						<row><label><![CDATA[${promotion}]]></label></row>
					</c:if>
					<row><label><![CDATA[最近售出:${item.volume}]]></label></row>
					<row><label><![CDATA[运费:快递 ${item.expressFee}元;EMS ${item.emsFee}元;平邮 ${item.postFee}元]]></label></row>
					<c:if test="${item.stuffStatus=='new'}">
						<row><label><![CDATA[宝贝类型:全新]]></label></row>
					</c:if>
					<c:if test="${item.stuffStatus=='second'}">
						<row><label><![CDATA[宝贝类型:二手]]></label></row>
					</c:if>
					<c:if test="${item.stuffStatus=='unused'}">
						<row><label><![CDATA[宝贝类型:闲置]]></label></row>
					</c:if>
					<row><label><![CDATA[所在地区:${item.location.city}]]></label></row>
					<row><label><![CDATA[掌柜:${item.nick}]]></label></row>
				</rows>
			</table>
			<centerShelf>
				<shelf id="album">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="album_1" onSelect="taobaoClient.addToFavorite(${item.numIid},${taobaoHtmlToken});" onPlay="taobaoClient.addToFavorite(${item.numIid},'${taobaoHtmlToken}');">
									<title>收藏</title>
								</actionButton>
								<actionButton id="album_2" onSelect="taobaoClient.getSellerIdByShopUrl('${fn:escapeXml(shopClickUrl)}');" onPlay="taobaoClient.getSellerIdByShopUrl('${fn:escapeXml(shopClickUrl)}');');">
									<title>店铺</title>
								</actionButton>
								<actionButton id="album_3" onSelect="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?feature=10&amp;title=${fn:escapeXml(item.title)}&amp;shareURL=${fn:escapeXml(item.detailUrl)}&amp;imageURL=${fn:escapeXml(item.picUrl)}');" onPlay="atv.loadURL('${serverurl}/ctl/weibo/createStatus.xml?feature=10&amp;title=${fn:escapeXml(item.title)}&amp;shareURL=${fn:escapeXml(item.detailUrl)}&amp;imageURL=${fn:escapeXml(item.picUrl)}');">
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