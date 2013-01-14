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
									<leftImage></leftImage>
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
												<simpleHeader></simpleHeader>
											</header>
											<items>
												<pagedGrid id="pg_${varStatus.count}">
													<grid id="g1_${varStatus.count}" columnCount="1">
														<items>
															<moviePoster id="g1mp_${varStatus.count}">
																<image>${weiboStatus.statusPic}</image>
															</moviePoster>
														</items>
													</grid>
													<c:if test="${weiboStatus.originalPic!=null}">
														<grid id="g2_${varStatus.count}" columnCount="1">
															<items>
																<moviePoster id="g2mp_${varStatus.count}">
																	<image>${weiboStatus.originalPic}</image>
																</moviePoster>
															</items>
														</grid>
													</c:if>
													<c:if
														test="${weiboStatus.retweetedStatus!=null&&weiboStatus.retweetedStatus.originalPic!=null}">
														<grid id="g3_${varStatus.count}" columnCount="1">
															<items>
																<moviePoster id="g2mp_${varStatus.count}">
																	<image>${weiboStatus.retweetedStatus.originalPic}</image>
																</moviePoster>
															</items>
														</grid>
													</c:if>
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
						<simpleHeader></simpleHeader>
					</header>
					<items>
						<pagedGrid id="pg">
							<grid id="g1" columnCount="1">
								<items>
									<moviePoster id="g1mp">
										<image>${weiboFirstStatus.statusPic}</image>
									</moviePoster>
								</items>
							</grid>
							<c:if test="${weiboFirstStatus.originalPic!=null}">
								<grid id="g2_${varStatus.count}" columnCount="1">
									<items>
										<moviePoster id="g2mp">
											<image>${weiboFirstStatus.originalPic}</image>
										</moviePoster>
									</items>
								</grid>
							</c:if>
							<c:if
								test="${weiboFirstStatus.retweetedStatus!=null&&weiboFirstStatus.retweetedStatus.originalPic!=null}">
								<grid id="g3_${varStatus.count}" columnCount="1">
									<items>
										<moviePoster id="g2mp_${varStatus.count}">
											<image>${weiboFirstStatus.retweetedStatus.originalPic}</image>
										</moviePoster>
									</items>
								</grid>
							</c:if>
						</pagedGrid>
					</items>
				</scrollerPreview>
			</preview>
		</listWithPreview>
	</body>
</atv>