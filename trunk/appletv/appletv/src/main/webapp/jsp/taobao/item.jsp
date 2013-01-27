<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<itemDetail id="itemdetail">
			<title><![CDATA[${itemDetail.item.title}]]></title>
			<summary></summary>
			<image style="moviePoster">${itemDetail.item.picUrl}</image>
			<table>
				<columnDefinitions><columnDefinition width="100"><title>其他信息</title></columnDefinition></columnDefinitions>
				<rows>
					<row><label><![CDATA[价格:${itemDetail.item.price}元]]></label></row>
					<row><label><![CDATA[最近售出:${itemDetail.item.volume}]]></label></row>
					<row><label><![CDATA[运费:快递 ${itemDetail.item.expressFee}元;EMS ${itemDetail.item.emsFee}元;平邮 ${itemDetail.item.postFee}元]]></label></row>
					<c:if test="${itemDetail.item.stuffStatus=='new'}">
						<row><label><![CDATA[宝贝类型:全新]]></label></row>
					</c:if>
					<c:if test="${itemDetail.item.stuffStatus=='second'}">
						<row><label><![CDATA[宝贝类型:二手]]></label></row>
					</c:if>
					<c:if test="${itemDetail.item.stuffStatus=='unused'}">
						<row><label><![CDATA[宝贝类型:闲置]]></label></row>
					</c:if>
					<row><label><![CDATA[所在地区:${itemDetail.item.location.city}]]></label></row>
					<row><label><![CDATA[掌柜:${itemDetail.item.nick}]]></label></row>
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