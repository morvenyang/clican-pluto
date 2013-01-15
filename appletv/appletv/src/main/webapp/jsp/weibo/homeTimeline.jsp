<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
		<listWithPreview id="lwp">
			<header>
				<simpleHeader>
					<title>微薄</title>
				</simpleHeader>
			</header>
			<menu>
				<sections>
					<menuSection>
						<items>
							<imageTextImageMenuItem id="prev" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline2.xml')">
								<leftImage></leftImage>
								<rightImage></rightImage>
								<imageSeparatorText></imageSeparatorText>
								<label>最新</label>
							</imageTextImageMenuItem>
							<imageTextImageMenuItem id="prev" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline2.xml?sinceId=${sinceId}')">
								<leftImage></leftImage>
								<rightImage></rightImage>
								<imageSeparatorText></imageSeparatorText>
								<label>上一页</label>
							</imageTextImageMenuItem>
							<c:forEach var="weiboStatus" items="${weiboStatusWapper.statuses}"
								varStatus="varStatus">
								<imageTextImageMenuItem id="item_${varStatus.count}">
									<leftImage>${weiboStatus.user.profileImageUrl}</leftImage>
									<rightImage>${weiboStatus.idstr}</rightImage>
									<imageSeparatorText></imageSeparatorText>
									<c:if test="${fn:length(weiboStatus.text)>10}">
										<label><![CDATA[${fn:substring(weiboStatus.text,0,10)}]]></label>
									</c:if>
									<c:if test="${fn:length(weiboStatus.text)<=10}">
										<label><![CDATA[${weiboStatus.text}]]></label>
									</c:if>
									<preview>
										<longDescriptionPreview>
									        <title>${weiboStatus.user.screenName}</title>
									        <summary>${weiboStatus.text}</summary>
									        <image>${weiboStatus.originalPic}</image>
									    </longDescriptionPreview>
									</preview>
								</imageTextImageMenuItem>
							</c:forEach>
							<imageTextImageMenuItem id="prev" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline2.xml?maxId=${maxId}')">
								<leftImage></leftImage>
								<rightImage></rightImage>
								<imageSeparatorText></imageSeparatorText>
								<label>下一页</label>
							</imageTextImageMenuItem>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listWithPreview>
	</body>
</atv>