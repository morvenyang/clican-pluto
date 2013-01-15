<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<photoBrowser id="pb">
			<header><simpleHeader><title>图片</title></simpleHeader></header>
			<photoGrid id="pg">
				<photos>
					<photo id="p1">
						<assets>
							<photoAsset height="1024" width="1024" src="${status.originalPic}"/>
						</assets>
					</photo>
				</photos>
			</photoGrid>
			<buttons>
				<actionButton id="ab1">
					<title>转发</title>
				</actionButton>
				<actionButton id="ab2">
					<title>评论</title>
				</actionButton>
			</buttons>
		</photoBrowser>
	</body>
</atv>