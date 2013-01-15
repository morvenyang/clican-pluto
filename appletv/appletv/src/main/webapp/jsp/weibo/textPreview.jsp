<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<scrollingText id="text">
			<title>内容</title>
			<c:if test="${weiboStatus.retweetedStatus!=null}">
				<text><![CDATA[${weiboStatus.text} @${weiboStatus.retweetedStatus.user.screenName} @${weiboStatus.retweetedStatus.text}]]></text>
			</c:if>
			<c:if test="${weiboStatus.retweetedStatus==null}">
				<text><![CDATA[${weiboStatus.text}]]></text>
			</c:if>
			<buttons>
				<actionButton id="ab1" onSelect="">
					<title>转发</title>
				</actionButton>
				<actionButton id="ab2" onSelect="atv.loadURL('${serverurl}/weibo/showComments.xml?statusId=${weiboStatus.idstr}');">
					<title>评论</title>
				</actionButton>
			</buttons>
		</scrollingText>
	</body>
</atv>