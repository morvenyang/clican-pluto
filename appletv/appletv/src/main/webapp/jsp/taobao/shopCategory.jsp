<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
		<listScrollerSplit id="lsp">
			<header>
				<simpleHeader horizontalAlignment="left">
					<title>店铺类目浏览</title>
				</simpleHeader>
			</header>
			<menu>
				<sections>
					<menuSection>
						<items>
							<c:forEach var="category" items="${categoryList}" varStatus="status">
								<c:if test="${!category.hasChild}">
									<oneLineMenuItem id="category_${status.count}" onPlay="atv.loadURL('${serverurl}/ctl/taobao/itemList.xml?cid=${category.id}');" onSelect="atv.loadURL('${serverurl}/ctl/taobao/itemList.xml?cid=${category.id}');">
										<label><![CDATA[${category.title}]]></label>
										<image>${category.picUrl}</image>
									</oneLineMenuItem>
								</c:if>
								<c:if test="${category.hasChild}">
									<oneLineMenuItem id="category_${status.count}" onPlay="atv.loadURL('${serverurl}/ctl/taobao/shopCategory.xml?parentId=${category.id}');" onSelect="atv.loadURL('${serverurl}/ctl/taobao/shopCategory.xml?parentId=${category.id}');">
										<label><![CDATA[${category.title} >>]]></label>
										<image>${category.picUrl}</image>
									</oneLineMenuItem>
								</c:if>
							</c:forEach>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listScrollerSplit>
	</body>
</atv>