<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<photoBrowser id="pb">
			<header><simpleHeader><title>图片</title></simpleHeader></header>
			<photoGrid id="pg">
				<photos>
					<photo id="p1">
						<assets>
							<c:if test="${weiboStatus.originalPic!=null&&fn:length(weiboStatus.originalPic)!=0}">
								<photoAsset height="1024" width="1024" src="${weiboStatus.originalPic}"/>
							</c:if>
							<c:if test="${weiboStatus.retweetedStatus.originalPic!=null&&fn:length(weiboStatus.retweetedStatus.originalPic)!=0}">
								<photoAsset height="1024" width="1024" src="${weiboStatus.retweetedStatus.originalPic}"/>
							</c:if>
						</assets>
					</photo>
				</photos>
			</photoGrid>
			<buttons>
				<actionButton id="ab1" onSelect="">
					<title>转发</title>
				</actionButton>
				<actionButton id="ab2" onSelect="">
					<title>评论</title>
				</actionButton>
			</buttons>
		</photoBrowser>
	</body>
</atv>