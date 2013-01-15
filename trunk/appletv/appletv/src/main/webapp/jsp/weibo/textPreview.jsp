<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<scrollingText id="text">
			<title>内容</title>
			<text><![CDATA[${status.text}]]></text>
			<buttons>
				<actionButton id="ab1" onSelect="">
					<title>转发</title>
				</actionButton>
				<actionButton id="ab2" onSelect="">
					<title>评论</title>
				</actionButton>
			</buttons>
		</scrollingText>
	</body>
</atv>