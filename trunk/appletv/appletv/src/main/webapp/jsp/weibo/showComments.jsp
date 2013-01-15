<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<scroller id="comments">
			<items>
				<c:forEach var="weiboComment" items="${weiboCommentWapper.comments}">
					<collectionDivider><title><![CDATA[${weiboComment.user.screenName}:${weiboComment.text}]]></title></collectionDivider>
				</c:forEach>
			</items>
		</scroller>
	</body>
</atv>