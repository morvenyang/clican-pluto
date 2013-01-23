<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<head><script src="${serverurl}/javascript/photoPreview.js"/></head>
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
							<imageTextImageMenuItem id="top" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline.xml?feature=${weiboFeature}')">
								<leftImage></leftImage>
								<rightImage></rightImage>
								<imageSeparatorText></imageSeparatorText>
								<label>最新</label>
							</imageTextImageMenuItem>
							<imageTextImageMenuItem id="prev" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline.xml?sinceId=${sinceId}&amp;feature=${weiboFeature}')">
								<leftImage></leftImage>
								<rightImage></rightImage>
								<imageSeparatorText></imageSeparatorText>
								<label>上一页</label>
							</imageTextImageMenuItem>
							<c:forEach var="weiboStatus" items="${weiboStatusWapper.statuses}"
								varStatus="varStatus">
								<c:if test="${weiboStatus.videoUrl!=null}">
									<imageTextImageMenuItem id="item_${varStatus.count}" onSelect="atv.loadURL('${serverurl}/weibo/textPreview.xml?statusId=${weiboStatus.idstr}&amp;fullText=${weiboStatus.fullTextEncode}');" onPlay="${weiboStatus.videoUrl}">
										<leftImage>${weiboStatus.user.profileImageUrl}</leftImage>
										<rightImage>${serverurl}/image/weibo/video.png</rightImage>
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
										         <summary><![CDATA[${weiboStatus.fullText} ${weiboStatus.unknownUrl}]]></summary>
										         <image src="${weiboStatus.retweetedStatus.originalPic}"></image>
										    </longDescriptionPreview>
										</preview>
									</imageTextImageMenuItem>
								</c:if>
								<c:if test="${weiboStatus.videoUrl==null&&weiboStatus.musicUrl!=null}">
									<imageTextImageMenuItem id="item_${varStatus.count}" onSelect="atv.loadURL('${serverurl}/weibo/textPreview.xml?statusId=${weiboStatus.idstr}&amp;fullText=${weiboStatus.fullTextEncode}');" onPlay="${weiboStatus.musicUrl}">
										<leftImage>${weiboStatus.user.profileImageUrl}</leftImage>
										<rightImage>${serverurl}/image/weibo/music.png</rightImage>
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
										         <summary><![CDATA[${weiboStatus.fullText} ${weiboStatus.unknownUrl}]]></summary>
										         <image src="${weiboStatus.retweetedStatus.originalPic}"></image>
										    </longDescriptionPreview>
										</preview>
									</imageTextImageMenuItem>
								</c:if>
								<c:if test="${weiboStatus.videoUrl==null&&weiboStatus.musicUrl==null}">
									<imageTextImageMenuItem id="item_${varStatus.count}" onSelect="atv.loadURL('${serverurl}/weibo/textPreview.xml?statusId=${weiboStatus.idstr}&amp;fullText=${weiboStatus.fullTextEncode}');" onPlay="onPhotoSelection('${weiboStatus.originalPic}');">
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
										         <summary><![CDATA[${weiboStatus.fullText} ${weiboStatus.unknownUrl}]]></summary>
										         <image src="${weiboStatus.originalPic}"></image>
										    </longDescriptionPreview>
										</preview>
									</imageTextImageMenuItem>
								</c:if>
							</c:forEach>
							<imageTextImageMenuItem id="next" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline.xml?maxId=${maxId}&amp;feature=${weiboFeature}')">
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