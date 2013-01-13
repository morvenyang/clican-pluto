<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
    <itemDetail id="status">
	        <title>${status.user.screenName}</title>
	        <c:if test=${status.retweetedStatus!=null}>
	        	<summary><![CDATA[${status.text}
	            @${status.retweetedStatus.screenName}:${status.retweetedStatus.text}
	            ]]></summary>
	        </c:if>
	        <c:if test=${status.retweetedStatus==null}>
	        	<summary><![CDATA[${status.text}]]></summary>
	        </c:if>
	        <c:if test="{status.retweetedStatus==null}">
	            <image style="moviePoster">${status.thumbnailPic}</image>
	        </c:if>
	        <c:if test="{status.retweetedStatus!=null}">
	            <image style="moviePoster">${status.retweetedStatus.thumbnailPic}</image>
	        </c:if>
	        <centerShelf>
				<shelf id="shelf">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="shelf_1" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title>赞</title>
								</actionButton>
								<actionButton id="shelf_2" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title><![CDATA[转发(${status.repostsCount})]]></title>
								</actionButton>
								<actionButton id="shelf_3" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title>收藏</title>
								</actionButton>
								<actionButton id="shelf_4" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title><![CDATA[评论(${status.commentsCount})]]></title>
								</actionButton>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
	    </itemDetail>
  </body>
</atv>