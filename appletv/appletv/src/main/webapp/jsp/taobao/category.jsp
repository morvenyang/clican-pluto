<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
		<listScrollerSplit id="lsp">
			<header>
				<simpleHeader horizontalAlignment="left">
					<title>类目浏览</title>
				</simpleHeader>
			</header>
			<menu>
				<sections>
					<menuSection>
						<items>
							<c:forEach var="category" items="${categoryList}" varStatus="status">
								<c:if test="${category.subTitle==null||fn:length(category.subTitle)==0}">
									<c:if test="${category.children==null||fn:length(category.children)==0}">
										<oneLineMenuItem id="category_${status.count}" onPlay="atv.loadURL('${serverurl}/ctl/taobao/itemList.xml?cid=${category.id}');" onSelect="atv.loadURL('${serverurl}/ctl/taobao/itemList.xml?cid=${category.id}');">
											<label>${category.title}</label>
											<image>${category.picUrl}</image>
										</oneLineMenuItem>
									</c:if>
									<c:if test="${category.children!=null&&fn:length(category.children)>0}">
										<oneLineMenuItem id="category_${status.count}" onPlay="atv.loadURL('${serverurl}/ctl/taobao/category.xml?parentId=${category.id}');" onSelect="atv.loadURL('${serverurl}/ctl/taobao/category.xml?parentId=${category.id}');">
											<label>${category.title}</label>
											<image>${category.picUrl}</image>
										</oneLineMenuItem>
									</c:if>
								</c:if>
								<c:if test="${category.subTitle!=null&&fn:length(category.subTitle)!=0}">
									<c:if test="${category.children==null||fn:length(category.children)==0}">
										<twoLineMenuItem id="category_${status.count}" onPlay="atv.loadURL('${serverurl}/ctl/taobao/itemList.xml?cid=${category.id}');" onSelect="atv.loadURL('${serverurl}/ctl/taobao/itemList.xml?cid=${category.id}');">
											<label>${category.title}</label>
											<label2>${category.subTitle}</label2>
											<image>${category.picUrl}</image>
										</twoLineMenuItem>
									</c:if>
									<c:if test="${category.children!=null&&fn:length(category.children)>0}">
										<twoLineMenuItem id="category_${status.count}" onPlay="atv.loadURL('${serverurl}/ctl/taobao/category.xml?parentId=${category.id}');" onSelect="atv.loadURL('${serverurl}/ctl/taobao/category.xml?parentId=${category.id}');">
											<label>${category.title}</label>
											<label2>${category.subTitle}</label2>
											<image>${category.picUrl}</image>
										</twoLineMenuItem>
									</c:if>
								</c:if>
							</c:forEach>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listScrollerSplit>
	</body>
</atv>