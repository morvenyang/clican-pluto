<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<shelf id="sort_bar">
			<sections>
				<shelfSection>
					<items>
						<actionButton id="shelf_item_default" onSelect="atv.loadURL('${sorturl}&amp;sort=defalut');" onPlay="atv.loadURL('${sorturl}&amp;sort=defalut');">
							<title>人气</title>
							<c:if test="${sort=='default'}">
								<image>${serverurl}/image/taobao/button/down.png</image>
							</c:if>
						</actionButton>
						<actionButton id="shelf_item_credit" onSelect="atv.loadURL('${sorturl}&amp;sort=credit_desc');" onPlay="atv.loadURL('${sorturl}&amp;sort=credit_desc');">
							<title>信用</title>
							<c:if test="${sort=='credit_desc'}">
								<image>${serverurl}/image/taobao/button/down.png</image>
							</c:if>
						</actionButton>
						<c:if test="${sort=='price_desc'}">
							<actionButton id="shelf_item_price" onSelect="atv.loadURL('${sorturl}&amp;sort=price_asc');" onPlay="atv.loadURL('${sorturl}&amp;sort=price_asc');">
								<title>价格</title>
								<image>${serverurl}/image/taobao/button/down.png</image>
							</actionButton>
						</c:if>
						<c:if test="${sort=='price_asc'}">
							<actionButton id="shelf_item_price" onSelect="atv.loadURL('${sorturl}&amp;sort=price_desc');" onPlay="atv.loadURL('${sorturl}&amp;sort=price_desc');">
								<title>价格</title>
								<image>${serverurl}/image/taobao/button/up.png</image>
							</actionButton>
						</c:if>
						<c:if test="${sort!='price_desc' && sort!='price_asc'}">
							<actionButton id="shelf_item_price" onSelect="atv.loadURL('${sorturl}&amp;sort=price_desc');" onPlay="atv.loadURL('${sorturl}&amp;sort=price_desc');">
								<title>价格</title>
							</actionButton>
						</c:if>
						<c:if test="${sort=='commissionNum_desc'}">
							<actionButton id="shelf_item_commission" onSelect="atv.loadURL('${sorturl}&amp;sort=commissionNum_asc');" onPlay="atv.loadURL('${sorturl}&amp;sort=commissionNum_asc');">
								<title>销量</title>
								<image>${serverurl}/image/taobao/button/down.png</image>
							</actionButton>
						</c:if>
						<c:if test="${sort=='commissionNum_asc'}">
							<actionButton id="shelf_item_commission" onSelect="atv.loadURL('${sorturl}&amp;sort=commissionNum_desc');" onPlay="atv.loadURL('${sorturl}&amp;sort=commissionNum_desc');">
								<title>销量</title>
								<image>${serverurl}/image/taobao/button/up.png</image>
							</actionButton>
						</c:if>
						<c:if test="${sort!='commissionNum_desc' && sort!='commissionNum_asc'}">
							<actionButton id="shelf_item_commission" onSelect="atv.loadURL('${sorturl}&amp;sort=commissionNum_desc');" onPlay="atv.loadURL('${sorturl}&amp;sort=commissionNum_desc');">
								<title>销量</title>
							</actionButton>
						</c:if>
					</items>
				</shelfSection>
			</sections>
		</shelf>
		<grid id="grid_1" columnCount="12">
					<items>
						<actionButton id="shelf_item_first_page" onSelect="atv.loadURL('${pagiurl}&amp;page=1');" onPlay="atv.loadURL('${pagiurl}&amp;page=1');">
							<title>第一页</title>
						</actionButton>
						<c:forEach var="i" begin="${begin}" end="${end}">
							<actionButton id="shelf_item_${i}_page" onSelect="atv.loadURL('${pagiurl}&amp;page=${i}');" onPlay="atv.loadURL('${pagiurl}&amp;page=${i}');">
								<title>${i+1}</title>
							</actionButton>
						</c:forEach>
						<actionButton id="shelf_item_last_page" onSelect="atv.loadURL('${pagiurl}&amp;page=${totalPage}');" onPlay="atv.loadURL('${pagiurl}&amp;page=${totalPage}');">
							<title>最后页</title>
						</actionButton>
					</items>
					</grid>
		<grid id="grid_2" columnCount="6">
			<items>
				<c:forEach var="item" items="${itemList}">
						<moviePoster id="shelf_item_${item.numIid}" alwaysShowTitles="true" onSelect="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.numIid}&amp;volume=${item.volume}');" onPlay="atv.loadURL('${serverurl}/ctl/taobao/item.xml?itemId=${item.numIid}&amp;volume=${item.volume}');">
							<title><![CDATA[${item.title}]]></title>
							<subtitle><![CDATA[￥${item.price} 最近售出${item.volume}]]></subtitle>
							<image>${item.picUrl}</image>
							<defaultImage>resource://Poster.png</defaultImage>
						</moviePoster>
				</c:forEach>
			</items>
		</grid>
				<grid id="grid_3" columnCount="12">
					<items>
						<actionButton id="shelf_item_first_page" onSelect="atv.loadURL('${pagiurl}&amp;page=1');" onPlay="atv.loadURL('${pagiurl}&amp;page=1');">
							<title>第一页</title>
						</actionButton>
						<c:forEach var="i" begin="${begin}" end="${end}">
							<actionButton id="shelf_item_${i}_page" onSelect="atv.loadURL('${pagiurl}&amp;page=${i}');" onPlay="atv.loadURL('${pagiurl}&amp;page=${i}');">
								<title>${i+1}</title>
							</actionButton>
						</c:forEach>
						<actionButton id="shelf_item_last_page" onSelect="atv.loadURL('${pagiurl}&amp;page=${totalPage}');" onPlay="atv.loadURL('${pagiurl}&amp;page=${totalPage}');">
							<title>最后页</title>
						</actionButton>
					</items>
					</grid>
			
	</items>
</scroller>
</body>
</atv>