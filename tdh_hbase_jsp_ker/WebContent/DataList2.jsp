<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工单明细查看</title>
<link rel="stylesheet" href="style/css.css">
<link rel="stylesheet" href="css/style.css" type="text/css" />
</head>
<% List flowlist = (List)request.getAttribute("flowlist");%>
<body bgcolor=" 	#FFFFFF">
<div class="flashBg">
<table height="100" border="0"  align="center" cellpadding="0" cellspacing="0" class="maintable" style="background-color: 	#FFFFFF;">
<tr>
<td colspan="100" style="background-color: 	 	#FFFFFF;">
<h5 align="center">[渠道办理详情]|[客户特征详情]</h5>
</td>
</tr>
<c:forEach items="${list}" var="bean" > 
  <tr   >
  <td width="5%">工单编号:</td>
<!--   <th style="text-align: center; width: 5%">工单编号</th> -->
    <td width="5%"><c:out value="${bean.KFGZDBS}"/></td>
      <td width="5%">  业务类别:</td>
      <td width="5%"><c:out value="${bean.YWLBDM}"/></td>
        <td width="5%">  工单来源</td>
       <td width="5%"><c:out value="${bean.GDLY}"/></td>
        <td width="5%">  服务渠道</td>
       <td width="5%"><c:out value="${bean.FWLY}"/></td>
       <td width="5%">  应用时长</td>
       <td width="5%"><c:out value="${bean.YYS}"/></td>
   </tr>
   <tr  >
  <td width="5%">客户姓名:</td>
    <td width="5%"><c:out value="${bean.KHXM}"/></td>
      <td width="5%">  电话号码:</td>
      <td width="5%"><c:out value="${bean.LDHM}"/></td>
        <td width="5%">  坐席工号</td>
       <td width="5%"><c:out value="${bean.ZXGH}"/></td>
        <td width="5%">  受理时间</td>
       <td width="5%"><c:out value="${bean.SLSJ}"/></td>
       <td width="5%">  用电地址</td>
       <td width="5%"><c:out value="${bean.YHDZ}"/></td>
   </tr>
   <tr  >
 <td width="5%">工单状态:</td>
    <td width="5%"> <c:out value="${bean.GZDZTDM}"/></td>
      <td width="5%">  回访标识:</td>
      <td width="5%"><c:out value="${bean.HFBZ}"/></td>
        <td width="5%">  归档标识</td>
       <td width="5%"><c:out value="${bean.GDRBS}"/></td>
        <td width="5%">  归档时间</td>
       <td width="5%"><c:out value="${bean.GDSJ}"/></td>
       <td width="5%">  紧急程度</td>
       <td width="5%"><c:out value="${bean.JJCDDM}"/></td>
   </tr>
   <tr  >
   <td width="5%">重要程度:</td>
    <td width="5%"><c:out value="${bean.ZYCDDM}"/></td>
      <td width="5%"> 直接处理标识:</td>
      <td width="5%"><c:out value="${bean.ZJCLBZ}"/></td>
        <td width="5%">  重复反映标志</td>
       <td width="5%"><c:out value="${bean.CFFYBZ}"/></td>
        <td width="5%">  匿名标志</td>
       <td width="5%"><c:out value="${bean.NMBZ}"/></td>
       <td width="5%">  受理组织</td>
       <td width="5%"><c:out value="${bean.ZZBM}"/></td>
   </tr>
	<tr>
   <td>受理人:</td>
    <td><c:out value="${bean.SLRBS}"/></td>
      <td> 首次评价:</td>
      <td><c:out value="${bean.SCDFPJDM}"/></td>
        <td>  最后评价</td>
       <td><c:out value="${bean.ZHDFPJDM}"/></td>
        <td>  营销范围</td>
       <td><c:out value="${bean.YXFW}"/></td>
       <td>  呼叫方式</td>
       <td><c:out value="${bean.HJFS}"/></td>
   </tr>
<!--    <tr > -->
<!--    <td width="25%">内容描述:</td> -->
<%--     <td><c:out value="${bean.LDNR}"/></td> --%>
<!--       <td> 处理意见:</td> -->
<%--       <td><c:out value="${bean.CLYJ}"/></td> --%>
<!--    </tr> -->
</c:forEach>
</table>
</div>
<%-- <%="count："+flowlist.size() %> <br><br> --%>

<div class="bodyCon08"><!--学员-->
	<div class="students">
		
		  <div id="four_flash">
			<div class="flashBg">
				<ul class="mobile">
				
				
				<c:forEach items="${flowlist}" var="bean" > 
					<li>
						<dd>环节<c:out value="${bean.task_id}"/></dd>
						<p><c:out value="${bean.tache_name}"/></p>
						<table>
						<tr>
						<td>开始时间</td>
						  <td><c:out value="${bean.task_reach_time}"/></td>
						</tr>
						<tr>
						<td>结束时间</td>
							<td><c:out value="${bean.task_commit_time}"/></td>
						</tr>
						<tr>
						<td>节点耗时</td>
						<td><c:out value="${bean.cost_time}"/> </td>
						</tr>
						<tr>
						<td>办理人</td>
							<td><c:out value="${bean.last_commitor}"/></td>
						</tr>
						</table>
<!-- 						<a href=""></a> -->
					</li>
					</c:forEach>
				</ul>
			</div>
			<div class="but_left"><img src="images/qianxleft.png" /></div>
			<div class="but_right"><img src="images/qianxr.png" /></div>
		  </div>
		  
	</div>
</div>

<!-- <script type="text/javascript" src="js/jquery.js"></script> -->
<script language=JavaScript src="js/jquery-1.3.2.js"></script> 
<script type="text/javascript">
//流程
var _index5=0;
$("#four_flash .but_right img").click(function(){
	_index5++;
	if(<%=flowlist.size()%><=3){
		return;
	}
	if(_index5 > 3){
		_index5 = 3;
	}else
	$("#four_flash .flashBg ul.mobile").stop().animate({left:-_index5*326},1000);
	});

	
$("#four_flash .but_left img").click(function(){
	if(<%=flowlist.size()%><=3){
		return;
	}
	_index5--;
	if(_index5 >= 0)
		$("#four_flash .flashBg ul.mobile").stop().animate({left:-_index5*326},1000);
	else
		_index5 = 0;
	});

</script>	

</body>
</html>