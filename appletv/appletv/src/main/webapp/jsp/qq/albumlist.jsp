<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
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
							<c:forEach var="albumItem" items="${album.albumItems}" varStatus="status">
								<oneLineMenuItem id="albumItem_${status.count+1}" onPlay="appletv.playQQVideo('${playdescurl}&amp;vid=${albumItem.vid}','${serverurl}');" onSelect="appletv.playQQVideo('${playdescurl}&amp;vid=${albumItem.vid}','${serverurl}');">
									<label>${albumItem.title}</label>
								</oneLineMenuItem>
							</c:forEach>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listScrollerSplit>
	</body>
</atv>