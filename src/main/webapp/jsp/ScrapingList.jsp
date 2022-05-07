<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="pojo.ScrapingHtml"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	以下サイトに更新がありました。
	<br>
	<%
	ArrayList<ScrapingHtml> scrapingHtml = (ArrayList<ScrapingHtml>) request.getAttribute("changedlist");
	%>
	<%
	for (int i = 0; i < scrapingHtml.size(); i++) {
	%>
	<p>
		<a href="<%=scrapingHtml.get(i).getSiteUrl()%>"><%=scrapingHtml.get(i).getSiteName()%></a>
	</p>
	<br />
	<%
	}
	%>
</body>
</html>