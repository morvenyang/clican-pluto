<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<scroller id="comments">
			<items>
				<c:if test="${fn:length(weiboCommentWapper.comments)==0}">
					<collectionDivider><title><![CDATA[没有相关评论]]></title></collectionDivider>
				</c:if>
				<c:forEach var="weiboComment" items="${weiboCommentWapper.comments}">
					<collectionDivider><title><![CDATA[${weiboComment.user.screenName}:${weiboComment.text}]]></title></collectionDivider>
				</c:forEach>
				<shelf id="action">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="comment"><title>评论</title></actionButton>
								<actionButton id="next" onSelect="atv.loadURL('${serverurl}/weibo/showComments.xml?statusId=${statusId}&amp;maxId=${maxId}')"><title>下一页</title></actionButton>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</items>
		</scroller>
	</body>
</atv>