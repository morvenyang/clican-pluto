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
								<imageTextImageMenuItem id="item_${varStatus.count}" onSelect="atv.loadURL('${serverurl}/weibo/textPreview.xml?statusId=${weiboStatus.idstr}');" onPlay="atv.loadURL('${serverurl}/weibo/imagePreview.xml?statusId=${weiboStatus.idstr}');">
									<leftImage>${weiboStatus.user.profileImageUrl}</leftImage>
									<rightImage></rightImage>
									<imageSeparatorText></imageSeparatorText>
									<c:if test="${fn:length(weiboStatus.text)>10}">
										<label><![CDATA[${fn:substring(weiboStatus.text,0,10)}]]></label>
									</c:if>
									<c:if test="${fn:length(weiboStatus.text)<=10}">
										<label><![CDATA[${weiboStatus.text}]]></label>
									</c:if>
									<preview>
										<longDescriptionPreview>
									        <title><![CDATA[${weiboStatus.user.screenName}]]></title>
									        <c:if test="${weiboStatus.retweetedStatus!=null}">
									        	<summary><![CDATA[${weiboStatus.text} @${weiboStatus.retweetedStatus.user.screenName} @${weiboStatus.retweetedStatus.text}]]></summary>
									        	<c:if test="${weiboStatus.retweetedStatus.originalPic!=null&&fn:length(weiboStatus.retweetedStatus.originalPic)!=0}">
									        		<image src1080="${weiboStatus.retweetedStatus.originalPic}"></image>
									        	</c:if>
									        </c:if>
									        <c:if test="${weiboStatus.retweetedStatus==null}">
									        	 <summary><![CDATA[${weiboStatus.text}]]></summary>
									        	 <c:if test="${weiboStatus.originalPic!=null&&fn:length(weiboStatus.originalPic)!=0}">
									         		<image src1080="${weiboStatus.originalPic}"></image>
									         	</c:if>
									        </c:if>
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