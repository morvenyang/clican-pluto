<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
    <itemDetail id="status">
	        <title>${weiboStatus.user.screenName}</title>
	        <c:if test="${weiboStatus.retweetedStatus!=null}">
	        	<summary><![CDATA[${weiboStatus.text}
	            @${weiboStatus.retweetedStatus.user.screenName}:${weiboStatus.retweetedStatus.text}
	            ]]></summary>
	        </c:if>
	        <c:if test="${weiboStatus.retweetedStatus==null}">
	        	<summary><![CDATA[${weiboStatus.text}]]></summary>
	        </c:if>
	        <c:if test="${weiboStatus.retweetedStatus==null}">
	            <image style="moviePoster">${weiboStatus.thumbnailPic}</image>
	        </c:if>
	        <c:if test="${weiboStatus.retweetedStatus!=null}">
	            <image style="moviePoster">${weiboStatus.retweetedStatus.thumbnailPic}</image>
	        </c:if>
	        <centerShelf>
				<shelf id="centerShelf">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="shelf_1" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title>详细</title>
								</actionButton>
								<actionButton id="shelf_5" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title>赞</title>
								</actionButton>
								<actionButton id="shelf_6" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title><![CDATA[转发(${weiboStatus.repostsCount})]]></title>
								</actionButton>
								<actionButton id="shelf_7" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title>收藏</title>
								</actionButton>
								<actionButton id="shelf_8" onSelect="atv.loadURL('${serverurl}/weibo/index.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title><![CDATA[评论(${weiboStatus.commentsCount})]]></title>
								</actionButton>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</centerShelf>
				
			<bottomShelf>
				<shelf id="bottomShelf">
					<sections>
						<shelfSection>
							<items>
								<actionButton id="shelf_2" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline.xml');" onPlay="atv.loadURL('${serverurl}/weibo/index.xml');">
									<title><![CDATA[<<]]></title>
								</actionButton>
								<c:if test="${weiboPage>1||prevIndex>0}">
								<actionButton id="shelf_3" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline.xml?index=${prevIndex}');" onPlay="atv.loadURL('${serverurl}/weibo/homeTimeline.xml?index=${prevIndex}');">
									<title><![CDATA[<]]></title>
								</actionButton>
								</c:if>
								<actionButton id="shelf_4" onSelect="atv.loadURL('${serverurl}/weibo/homeTimeline.xml?index=${nextIndex}');" onPlay="atv.loadURL('${serverurl}/weibo/homeTimeline.xml?index=${nextIndex}');">
									<title><![CDATA[>]]></title>
								</actionButton>
							</items>
						</shelfSection>
					</sections>
				</shelf>
			</bottomShelf>
	    </itemDetail>
  </body>
</atv>