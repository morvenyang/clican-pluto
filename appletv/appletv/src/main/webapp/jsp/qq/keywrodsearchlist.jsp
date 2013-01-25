<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
	<head>
		<script src="${serverurl}/javascript/clican.js"/>
		<script src="${serverurl}/javascript/ejs.js"/>
		<script src="${serverurl}/javascript/qq.js"/>
	</head>
	<body>
		<searchResults id="trailers.searchResults">
			<menu>
				<sections>
					<menuSection>
						<header>
							<horizontalDivider alignment="left"
								accessibilityLabel="">
								<title>支持全拼搜索和拼音首字母搜索</title>
							</horizontalDivider>
						</header>
						<items>
							<c:if test="${keywordList.size==0}">
								<posterMenuItem id="trailer_0" accessibilityLabel=""
									onSelect="" onPlay="">
									<label>请继续输入</label>
									<defaultImage></defaultImage>
								</posterMenuItem>
							</c:if>
							<c:if test="${keywordList.size!=0}">
								<c:forEach var="keyword" items="${keywordList}">
									<posterMenuItem id="trailer_0" accessibilityLabel=""
									onSelect="qqClient.loadIndexPage('${keyword}',0,1001);" onPlay="qqClient.loadIndexPage('${keyword}',0,1001);">
										<label><![CDATA[${keyword}]]></label>
									</posterMenuItem>
								</c:forEach>
							</c:if>
						</items>
					</menuSection>
				</sections>
			</menu>
		</searchResults>
	</body>
</atv>