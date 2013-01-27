<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<viewWithNavigationBar id="bar">
			<navigation>
				<navigationItem>
					<title>全部</title>
					<url>${serverurl}/ctl/weibo/homeTimeline.xml</url>
				</navigationItem>
				<navigationItem>
					<title>视频</title>
					<url>${serverurl}/ctl/weibo/homeTimeline.xml?feature=3</url>
				</navigationItem>
				<navigationItem>
					<title>音乐</title>
					<url>${serverurl}/ctl/weibo/homeTimeline.xml?feature=4</url>
				</navigationItem>
				<navigationItem>
					<title>收藏</title>
					<url>${serverurl}/ctl/weibo/favorite.xml</url>
				</navigationItem>
			</navigation>
		</viewWithNavigationBar>
	</body>
</atv>