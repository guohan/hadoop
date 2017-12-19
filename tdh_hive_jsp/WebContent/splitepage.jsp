<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String context = request.getContextPath(); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示明细数据</title>
<link rel="stylesheet" href="<%=context%>/style/css.css">
<LINK href="<%=context%>/style/hotmail.css" rel=stylesheet>
<SCRIPT language=JavaScript src="<%=context%>/js/hotmail.js"></SCRIPT>
<SCRIPT language=JavaScript src="<%=context%>/js/checkForm.js"></SCRIPT>
<script language=JavaScript src="<%=context%>/js/common.js"></script>
<script language="JavaScript" src="<%=context%>/js/PopupCalendar1.js"></script>
<SCRIPT language=JavaScript src="<%=context%>/js/jquery-1.3.2.js"></SCRIPT>
</head>
<% String test = (String)request.getAttribute("test"); %>
<% String jspURL = "queryAll.do"; %>
<% List list = (List)request.getAttribute("list"); %>
<% String url = "http://localhost:8080/tdh_hive_jsp/queryPageAll.do"; %>
<% int curPage = ((Integer)(request.getAttribute("cur"))).intValue(); %>
<% int size = ((Integer)(request.getAttribute("size"))).intValue(); %>

<script type="text/javascript">
	function jump(){
		var text = document.getElementById("number").value;
		window.location.href="<%=url%>?cur="+text;
	}
</script>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
 <a href="<%=url%>?cur=1">首页</a>
 <a href="<%=url%>?cur=<%=curPage-1<=0?1:curPage-1%>" >上一页</a>
 <a href="<%=url%>?cur=<%=curPage<size?curPage+1:size%>" >下一页</a>	
 <a href="<%=url%>?cur=<%=size%>">尾页</a> 
 <input type="text" id="number" />
 <button onclick="jump()">跳转到</button> 
<br><br>
<table  border="0"  align="center" cellpadding="0" cellspacing="0" class="maintable">
<tr>
<th style="text-align: center; width: 20%">key值</th>
<th style="text-align: center; width: 30%">value值</th>
</tr>
<%="count："+list.size() %> <br><br>
<%="当前页："+curPage %>  <br><br>
<c:forEach items="${list}" var="bean" > 
  <tr align=center>
    <td><c:out value="${bean.key}"/></td>
       <td><c:out value="${bean.value}"/></td>
   </tr>
</c:forEach>
</table>
</body>
</html>