<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
<head><script src="${serverurl}/javascript/clican.js"/></head>
<body>
		<viewWithNavigationBar id="bar">
			<navigation>
				<navigationItem>
					<title>店铺首页</title>
					<url>${serverurl}/ctl/taobao/shopHome.xml?sellerId=${sellerId}</url>
				</navigationItem>
				<navigationItem>
					<title>店铺分类</title>
					<url>${serverurl}/ctl/taobao/shopCategory.xml?nick=${nick}&amp;shopId=${shopId}</url>
				</navigationItem>
				<navigationItem>
					<title>店铺信息</title>
					<url>${serverurl}/ctl/taobao/shopDetail.xml?nick=${nick}</url>
				</navigationItem>
			</navigation>
		</viewWithNavigationBar>
	</body>
</atv>