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
<% String test = (String)request.getAttribute("test");%>
<% List list = (List)request.getAttribute("list");%>
 <%
//   List list2 = new ArrayList();
  request.setAttribute("list2",list);
 %>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table  border="0"  align="center" cellpadding="0" cellspacing="0" class="maintable">
<!--  <tr> -->
<!--             <td height="16" colspan="3"> <b>查询结果</b> 目前共有<pager:pager formName="MyForm" key="datalist"><pager:total/></pager:pager>条记录符合查询条件 </td> -->
<!--             <td align="right"> -->
<!-- 			</td> -->
<!--           </tr>   -->
<!--           <tr> -->
<!--           <td align="right" width="45%">Rowkey -->
<!-- 	  				<input name="Rowkey" style="width: 50%" type="text" size="15" maxlength="60" value="" id="companyNameId"> -->
<!-- 	  			</td>	 -->
<!--              <td align="right"><input type="button" class="button" name="submit21" value="查询" onClick="query();">    </td> -->
<!--           </tr> -->
<tr>
<th style="text-align: center; width: 20%">Rowkey</th>
<th style="text-align: center; width: 30%">Qualifier</th>
<th style="text-align: center; width: 30%">Value</th>
</tr>
<%-- <%="文章正文为："+test %> <br><br> --%>
<%="count："+list.size() %> <br><br>
<c:forEach items="${list}" var="bean" > 
  <tr align=center>
    <td><c:out value="${bean.rowkey}"/></td>
       <td><c:out value="${bean.qualifier}"/></td>
             <td><c:out value="${bean.value}"/></td>
   </tr>
</c:forEach>
</table>
</body>
</html>