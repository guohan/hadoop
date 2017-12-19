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
<title>显示所有表</title>

<link rel="stylesheet" href="<%=context%>/style/css.css">
<LINK href="<%=context%>/style/hotmail.css" rel=stylesheet>
<SCRIPT language=JavaScript src="<%=context%>/js/hotmail.js"></SCRIPT>
<SCRIPT language=JavaScript src="<%=context%>/js/checkForm.js"></SCRIPT>
<script language=JavaScript src="<%=context%>/js/common.js"></script>
<script language="JavaScript" src="<%=context%>/js/PopupCalendar1.js"></script>
<SCRIPT language=JavaScript src="<%=context%>/js/jquery-1.3.2.js"></SCRIPT>
</head>
 <SCRIPT LANGUAGE="JavaScript">
 function checkDetails() {
	 alert("click");
     document.MyForm.action="queryAll.do?operType=checkdetail";
     alert("click");
	 document.MyForm.submit();	
}
 function deleteTable() {
	 alert("click");
     document.MyForm.action="/DeleteTableByName?operType=checkdetail";
     alert("click");
	 document.MyForm.submit();	
}
 </SCRIPT>
 <% String test = (String)request.getAttribute("test");%>
<% List list = (List)request.getAttribute("list");%>
 <%
//   List list2 = new ArrayList();
  request.setAttribute("list2",list);
 %>
 
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
		<form action="adduser.do" method="post">
<table  border="0"  align="center" cellpadding="0" cellspacing="0" class="maintable">
<tr >
<th style="text-align: center; width: 5%">表名称</th>
</tr>
<%="table counts："+list.size() %> <br><br>
<c:forEach items="${list}" var="bean" > 
  <tr class="yi_hang_4" align="center" >
    <td><c:out value="${bean}"/></td>
    <td align="center" width="25%">
<!--     <input type="button"  class="button" name="submit21" value="查看表明细" onclick="checkDetails();"> -->
<!--     <input type="button"  class="button" name="submit21" value="删除表" onclick="deleteTable();"> -->
    </td>
   </tr>
</c:forEach>
</table>
</form>
</body>
</html>