<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<% String Rowkey = (String)request.getAttribute("Rowkey");%>
<% String Family = (String)request.getAttribute("Family");%>
<% String Qualifier = (String)request.getAttribute("Qualifier");%>
<% String Timestamp = (String)request.getAttribute("Timestamp");%>
<% String Value = (String)request.getAttribute("Value");%>


<%="文章ID为："+Rowkey %> <br><br>
<%="文章标题为："+Family %> <br><br>
<%="文章作者为："+Qualifier %> <br><br>
<%="文章描述为："+Timestamp %> <br><br>
<%="文章正文为："+Value %> <br><br>
hello xiaomin
</body>
</html>