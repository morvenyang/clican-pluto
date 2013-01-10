<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<listScrollerSplit id="bbbb">
			<header>
				<simpleHeader horizontalAlignment="left">
					<title>${album.tt}</title>
					<image src="${album.pic}"></image>
				</simpleHeader>
			</header>
			<menu>
				<sections>
					<menuSection>
						<items>
							<c:forEach var="albumItem" items="${album.vids}" varStatus="status">
								<imageTextImageMenuItem id="albumItem_${status.count+1}" onPlay="atv.loadURL('${serverurl}/qq/play.xml?coverId=${albumItem}');" onSelect="atv.loadURL('${serverurl}/qq/play.xml?coverId=${albumItem}');">
									<leftImage></leftImage>
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