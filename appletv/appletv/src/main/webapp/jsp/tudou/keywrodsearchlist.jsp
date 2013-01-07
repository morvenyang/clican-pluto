<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
	<body>
    <listScrollerSplit id="keywordsearchlist">
			<header>
				<simpleHeader horizontalAlignment="left">
					<title>${q}</title>
				</simpleHeader>
			</header>
			<menu>
				<sections>
					<menuSection>
						<items>
							<c:forEach var="keyword" items="${keywordList}" varStatus="status">
								<imageTextImageMenuItem id="keyword_${status.count+1}" onPlay="atv.loadURL('${serverurl}/tudou/play.xml?itemid=${albumItem.itemid}&amp;st=${st}');" onSelect="atv.loadURL('${serverurl}/tudou/play.xml?itemid=${albumItem.itemid}&amp;st=${st}');">
									<leftImage></leftImage>
									<imageSeparatorText></imageSeparatorText>
									<label>${keyword}</label>
									<rightImage></rightImage>
								</imageTextImageMenuItem>
							</c:forEach>
						</items>
					</menuSection>
				</sections>
			</menu>
		</listScrollerSplit>
  </body>
</atv>