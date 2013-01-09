<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="UTF-8"?>
<atv>
	<head><script src="${serverurl}/javascript/clican.js"/></head>
	<body>
    	<search id="search" showPreview="true">
  			<baseURL>${serverurl}/tudou/keywrodsearchlist.xml?q=</baseURL>
  			<header>
  				<simpleHeader>
  					<title>搜索</title>
  				</simpleHeader>
  			</header>
  		</search>
  </body>
</atv>