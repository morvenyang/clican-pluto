<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head>
<script src="${serverurl}/javascript/clican.js"/>
<script src="${serverurl}/javascript/taobao.js"/>
<script src="${serverurl}/javascript/photoPreview.js"/>
</head>
<body>
		<listScrollerSplit id="lsp">
			<header>
				<simpleHeader horizontalAlignment="left">
					<title>主题</title>
					<subtitle>图片浏览后,按Plan键查看详细确定键修改图片浏览方式</subtitle>
				</simpleHeader>
			</header>
			<menu>
				<sections>
					<menuSection>
						<items>
							<c:forEach var="tag" items="${tagList}" varStatus="status">
								<oneLineMenuItem id="tag_${status.count}" onPlay="taobaoClient.loadLovePage(${tag.id});" onSelect="taobaoClient.loadLovePage(${tag.id});">
									<label><![CDATA[${tag.name}]]></label>
									<image></image>
								</oneLineMenuItem>
							</c:forEach>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listScrollerSplit>
	</body>
</atv>