<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<body>
<scroller id="com.sample.movie-shelf">
	<items>
		<grid id="grid_1" columnCount="12">	
			<items>
				<c:forEach var="area" items="areas">
					<actionButton id="area_${area.value}" onSelect="atv.loadURL('${serverurl}/tudou/filter?area=${area.value}&amp;year=${selectedYear}');" onPlay="atv.loadURL('${serverurl}/tudou/filter?area=${area.value}&amp;year=${selectedYear}');">
						<title>${area.label}</title>
					</actionButton>
				</c:forEach>
			</items>
		</grid>
		<grid id="grid_2" columnCount="12">	
			<items>
				<actionButton id="year_all" onSelect="atv.loadURL('${serverurl}/tudou/filter?year=-1&amp;area=${selectedArea}');" onPlay="atv.loadURL('${serverurl}/tudou/filter?year=-1&amp;area=${selectedArea}');">
						<title>全部</title>
				</actionButton>
				<c:forEach var="year" begin="${currentYear}" step="-1" end="${currentYear-6}">
					<actionButton id="year_${year}" onSelect="atv.loadURL('${serverurl}/tudou/filter?year=${year}&amp;area=${selectedArea}');" onPlay="atv.loadURL('${serverurl}/tudou/filter?year=${year}&amp;area=${selectedArea}');">
						<title>${year}</title>
					</actionButton>
				</c:forEach>
			</items>
		</grid>
	</items>
</scroller>
</body>
</atv>