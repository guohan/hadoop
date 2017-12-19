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
<% List list = (List)request.getAttribute("list");%>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table  border="0"  align="center" cellpadding="0" cellspacing="0" class="maintable">
<tr>
<th style="text-align: center; width: 10%">ROW_ID</th>
<th style="text-align: center; width: 10%">cl:BATCJ_ID</th>
<th style="text-align: center; width: 10%">cl:CREATEDATE</th>
<th style="text-align: center; width: 10%">cl:QCOUNT</th>
<th style="text-align: center; width: 10%">cl:RCOUNT</th>
<th style="text-align: center; width: 10%">cl:RULECODE</th>
<th style="text-align: center; width: 10%">cl:SCORE</th>
<th style="text-align: center; width: 10%">cl:SCOUNT</th>
<th style="text-align: center; width: 10%">cl:SYS_ID</th>
</tr>
<%="count："+list.size() %> <br><br>
<c:forEach items="${list}" var="bean" > 
  <tr align=center>
    <td><c:out value="${bean.ROW_ID}"/></td>
       <td><c:out value="${bean.BATCJ_ID}"/></td>
       <td><c:out value="${bean.CREATEDATE}"/></td>
       <td><c:out value="${bean.QCOUNT}"/></td>
       <td><c:out value="${bean.RCOUNT}"/></td>
       <td><c:out value="${bean.RULECODE}"/></td>
       <td><c:out value="${bean.SCORE}"/></td>
       <td><c:out value="${bean.SCOUNT}"/></td>
        <td><c:out value="${bean.SYS_ID}"/></td>
   </tr>
</c:forEach>
</table>
</body>
</html>