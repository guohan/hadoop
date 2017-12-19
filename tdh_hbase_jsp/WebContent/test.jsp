<%@page import="com.kit.UserInfo"%> 

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

 

<html>

  <head>

    <title>forEach标签应用示例</title>

  </head>  <body>

    <h2>forEach标签应用示例</h2>

    <hr>

 <%

  //定义一个用户数组

  String[] zhangs={"zhang1",      "zhang2",      "zhang3",      "zhang4"};

  request.setAttribute("zhangsan",zhangs);

 %>

 <table border=1 width=400>

  <tr align=center >

   <td>内容</td>

   <td>索引值</td>

   <td>共访问过</td>

   <td>是否为第一个成员</td>

   <td>是否为最后一个成员</td>

  </tr>

  <c:forEach items="${zhangsan}" var="z" varStatus="s">

   <tr align=center>

    <td><c:out value="${z}"/></td>

    <td><c:out value="${s.index}"/></td>

    <td><c:out value="${s.count}"/></td>

    <td><c:out value="${s.first}"/></td>

    <td><c:out value="${s.last}"/></td>  

   </tr>

  </c:forEach>

 </table>

 <hr/>  <%

  List list = new ArrayList();

  for(int i=0;i<10;i++){

   UserInfo user = new UserInfo(i,"张"+i);

   list.add(user);

  }

  request.setAttribute("list",list);

 %>

  <table border=1 width=400>

  <tr align=center >

   <td>编号</td>

   <td>姓名</td>

 

  </tr>

  <c:forEach items="${list}" var="u" >

   <tr align=center>

    <td><c:out value="${u.userId}"/></td>

    <td><c:out value="${ u.userName}"/></td>

   </tr>

  </c:forEach>

 </table>

  </body>

</html>

 