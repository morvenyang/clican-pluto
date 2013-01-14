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
										<scrollerPreview id="sp_${varStatus.count}">
											<header>
												<simpleHeader><image>${weiboStatus.user.profileImageUrl}</image></simpleHeader>
											</header>
											<items>
												<pagedGrid id="pg_${varStatus.count}">
													<grid id="g1_${varStatus.count}" columnCount="1">
														<items>
															<moviePoster id="g1mpt_${varStatus.count}">
																<image>${weiboStatus.statusPic}</image>
															</moviePoster>
															<moviePoster id="g1mpp_${varStatus.count}">
																<c:if test="${weiboStatus.thumbnailPic!=null&&fn:length(weiboStatus.thumbnailPic)!=0}">
																	<image>${weiboStatus.thumbnailPic}</image>
																</c:if>
																<c:if
																	test="${weiboStatus.retweetedStatus.thumbnailPic!=null&&fn:length(weiboStatus.retweetedStatus.thumbnailPic)!=0}">
																	<image>${weiboStatus.retweetedStatus.thumbnailPic}</image>
																</c:if>
															</moviePoster>
														</items>
													</grid>
													<grid id="g2_${varStatus.count}" columnCount="1">
														<items>
															<moviePoster id="g2mp_${varStatus.count}">
																<c:if test="${weiboStatus.originalPic!=null&&fn:length(weiboStatus.originalPic)!=0}">
																	<image>${weiboStatus.originalPic}</image>
																</c:if>
																<c:if
																	test="${weiboStatus.retweetedStatus!=null&&weiboStatus.retweetedStatus.originalPic!=null&&fn:length(weiboStatus.retweetedStatus.originalPic)!=0}">
																	<image>${weiboStatus.retweetedStatus.originalPic}</image>
																</c:if>
															</moviePoster>
														</items>
													</grid>
												</pagedGrid>
											</items>
										</scrollerPreview>
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
			<preview>
				<scrollerPreview id="sp">
					<header>
						<simpleHeader><image>${weiboStatus.user.profileImageUrl}</image></simpleHeader>
					</header>
					<items>
						<pagedGrid id="pg">
							<grid id="g1" columnCount="1">
								<items>
									<moviePoster id="g1mpt">
										<image>${weiboFirstStatus.statusPic}</image>
									</moviePoster>
									<moviePoster id="g1mpp">
										<c:if test="${weiboFirstStatus.thumbnailPic!=null&&fn:length(weiboStatus.thumbnailPic)!=0}">
											<image>${weiboFirstStatus.thumbnailPic}</image>
										</c:if>
										<c:if test="${weiboFirstStatus.retweetedStatus.thumbnailPic!=null&&fn:length(weiboStatus.retweetedStatus.thumbnailPic)!=0}">
											<image>${weiboFirstStatus.retweetedStatus.thumbnailPic}</image>
										</c:if>
									</moviePoster>
								</items>
							</grid>
							<grid id="g2" columnCount="1">
								<items>
									<moviePoster id="g2mp">
										<c:if test="${weiboFirstStatus.originalPic!=null&&fn:length(weiboStatus.originalPic)!=0}">
											<image>${weiboFirstStatus.originalPic}</image>
										</c:if>
										<c:if
											test="${weiboFirstStatus.retweetedStatus!=null&&weiboFirstStatus.retweetedStatus.originalPic!=null&&fn:length(weiboStatus.retweetedStatus.originalPic)!=0}">
											<image>${weiboFirstStatus.retweetedStatus.originalPic}</image>
										</c:if>
									</moviePoster>
								</items>
							</grid>
						</pagedGrid>
					</items>
				</scrollerPreview>
			</preview>
		</listWithPreview>
	</body>
</atv>