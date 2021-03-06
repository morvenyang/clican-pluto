<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="/peacebird/amcharts/amcharts.js" type="text/javascript"></script>
<script src="/peacebird/amcharts/pie.js" type="text/javascript"></script>
<style type="text/css">
text{
white-space: pre;
}
</style>
</head>
<body>
<div id="chartdiv" style="width:1024px; height:<%=request.getAttribute("height")%>px;"></div>
<div id="total" style="text-align:center;position:absolute;top:<%=request.getAttribute("top")%>px;left:445px">
<span style="color:#5f5f5f;font-size:30px">总零售收入</span><br/>
<span style="color:#ff6501;font-size:40px"><%=request.getAttribute("total") %></span><br/>
<span style="color:#5f5f5f;font-size:30px">万元</span></div>

<script type="text/javascript">
var chart = AmCharts.makeChart("chartdiv", {
    "type": "pie",
	"theme": "none",
	"marginLeft": 0,
	"marginRight": 0,
	"startRadius": "20%",
	"fontFamily": "Courier",
	"labelText": "[[lineBreakLabel]]",
	"balloon":{"fillColor":"#fffabd","fillAlpha":"1.0","fontSize":35},
	"balloonText":"[[description]]",
    "legend": {
        "markerType": "square",
        "position": "bottom",
		"maxColumns":1,
		"marginLeft": 50,
		"autoMargins": false,
		"fontSize":40,
		"markerLabelGap":50,
		"valueText":"[[description]]",
		"valueWidth": 350,
		"markerSize":40
    },
    "radius":300,
    "dataProvider": <%=request.getAttribute("dataProvider")%>,
    "valueField": "dayAmount",
    "fontSize": 40,
    "titleField": "name",
    "descriptionField":"description",
    "startEffect": "elastic",
    "startDuration": 2,
    "labelRadius": 50,
    "innerRadius": "50%",
    "depth3D": 15,
    "angle": 20,
    "colors":["#74d9c7","#3fbbfb","#f0729b","#a096df","#f5cc69","#f89c4a","#999180","#e4df54","#ba96df","#d54b4b","#83b5d5","#8cde43"]  
});
</script>
</body>
</html>