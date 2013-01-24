<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="grid_1" columnCount="12">	
			<items>
				<c:forEach var="area" items="${areas}">
					<actionButton id="area_${area.value}" onSelect="atv.loadURL('${serverurl}/tudou/filter.xml?channelId=${channelId}&amp;area=${area.value}&amp;year=${selectedYear}');" onPlay="atv.loadURL('${serverurl}/tudou/filter.xml?channelId=${channelId}&amp;area=${area.value}&amp;year=${selectedYear}');">
						<title>${area.label}</title>
						<c:if test="${selectedArea==area.value}">
							<image>resource://PlayFocused.png</image>
						</c:if>
					</actionButton>
				</c:forEach>
			</items>
		</grid>
		<grid id="grid_2" columnCount="12">	
			<items>
				<actionButton id="year_all" onSelect="atv.loadURL('${serverurl}/tudou/filter.xml?channelId=${channelId}&amp;year=-1&amp;area=${selectedArea}');" onPlay="atv.loadURL('${serverurl}/tudou/filter.xml?channelId=${channelId}&amp;year=-1&amp;area=${selectedArea}');">
						<title>全部</title>
						<c:if test="${selectedYear==-1}">
							<image>resource://PlayFocused.png</image>
						</c:if>
				</actionButton>
				<c:forEach var="year" begin="${currentYear-6}" step="1" end="${currentYear}">
					<actionButton id="year_${year}" onSelect="atv.loadURL('${serverurl}/tudou/filter.xml?channelId=${channelId}&amp;year=${year}&amp;area=${selectedArea}');" onPlay="atv.loadURL('${serverurl}/tudou/filter.xml?channelId=${channelId}&amp;year=${year}&amp;area=${selectedArea}');">
						<title>${year}</title>
						<c:if test="${selectedYear==year}">
							<image>resource://PlayFocused.png</image>
						</c:if>
					</actionButton>
				</c:forEach>
			</items>
		</grid>
		<grid id="grid_2" columnCount="12">	
			<items>
				<actionButton id="submit" onSelect="atv.loadURL('${serverurl}/tudou/index.xml?channelId=${channelId}&amp;year=${selectedYear}&amp;area=${selectedArea}');" onPlay="atv.loadURL('${serverurl}/tudou/index.xml?channelId=${channelId}&amp;year=${selectedYear}&amp;area=${selectedArea}');">
						<title>提交</title>
				</actionButton>
			</items>
		</grid>
	</items>
</scroller>
</body>
</atv>