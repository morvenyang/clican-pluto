<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<scrollingText id="text">
			<title>内容</title>
				<text><![CDATA[${fullText}]]></text>
			<buttons>
				<actionButton id="ab1" onSelect="atv.loadURL('${serverurl}/ctl/weibo/doRepost.xml?statusId=${statusId}');">
					<title>转发</title>
				</actionButton>
				<actionButton id="ab2" onSelect="atv.loadURL('${serverurl}/ctl/weibo/showComments.xml?statusId=${statusId}');">
					<title>评论</title>
				</actionButton>
				<actionButton id="ab3" onSelect="atv.loadURL('${serverurl}/ctl/weibo/doFavorite.xml?statusId=${statusId}');">
					<title>收藏</title>
				</actionButton>
			</buttons>
		</scrollingText>
	</body>
</atv>