<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
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
							<c:forEach var="weiboStatus" items="${weiboStatusWapper}"
								varStatus="varStatus">
								<imageTextImageMenuItem id="item_${varStatus.count}">
									<leftImage>${weiboStatus.user.profileImageUrl}</leftImage>
									<rightImage></rightImage>
									<imageSeparatorText></imageSeparatorText>
									<c:if test="${weiboStatus.text.length>10" }>
										<label>${weiboStatus.text.substring(0,10)}</label>
									</c:if>
									<c:if test="${weiboStatus.text.length<=10" }>
										<label>${weiboStatus.text}</label>
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
																<c:if test="${weiboStatus.thumbnailPic!=null}">
																	<image>${weiboStatus.thumbnailPic}</image>
																</c:if>
																<c:if
																	test="${weiboStatus.retweetedStatus.thumbnailPic!=null}">
																	<image>${weiboStatus.retweetedStatus.thumbnailPic}</image>
																</c:if>
															</moviePoster>
														</items>
													</grid>
													<grid id="g2_${varStatus.count}" columnCount="1">
														<items>
															<moviePoster id="g2mp_${varStatus.count}">
																<c:if test="${weiboStatus.originalPic!=null}">
																	<image>${weiboStatus.originalPic}</image>
																</c:if>
																<c:if
																	test="${weiboStatus.retweetedStatus!=null&&weiboStatus.retweetedStatus.originalPic!=null}">
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
										<c:if test="${weiboFirstStatus.thumbnailPic!=null}">
											<image>${weiboFirstStatus.thumbnailPic}</image>
										</c:if>
										<c:if test="${weiboFirstStatus.retweetedStatus.thumbnailPic!=null}">
											<image>${weiboFirstStatus.retweetedStatus.thumbnailPic}</image>
										</c:if>
									</moviePoster>
								</items>
							</grid>
							<grid id="g2" columnCount="1">
								<items>
									<moviePoster id="g2mp">
										<c:if test="${weiboFirstStatus.originalPic!=null}">
											<image>${weiboFirstStatus.originalPic}</image>
										</c:if>
										<c:if
											test="${weiboFirstStatus.retweetedStatus!=null&&weiboFirstStatus.retweetedStatus.originalPic!=null}">
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