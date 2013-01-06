<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<listScrollerSplit id="bbbb">
			<header>
				<simpleHeader horizontalAlignment="left">
					<title>${album.title}</title>
					<image src="${album.picurl}"></image>
				</simpleHeader>
			</header>
			<menu>
				<sections>
					<menuSection>
						<items>
							<c:forEach var="albumItem" items="${album.albumitems}" varStatus="status">
								<imageTextImageMenuItem id="albumItem_${status.count+1}" onPlay="atv.loadURL('${serverurl}/tudou/play.xml?itemid=${albumItem.itemid}&amp;st=${st}');" onSelect="atv.loadURL('${serverurl}/tudou/play.xml?itemid=${albumItem.itemid}&amp;st=${st}');">
									<leftImage>${album.picurl}</leftImage>
									<imageSeparatorText></imageSeparatorText>
									<label>第${status.count}集</label>
									<rightImage></rightImage>
								</imageTextImageMenuItem>
							</c:forEach>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listScrollerSplit>
	</body>
</atv>