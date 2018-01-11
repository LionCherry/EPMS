<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EPMS-<s:property value="#session.role.name" />-教育实习经费标准</title>
<link rel="stylesheet" type="text/css" href="styles/style.css">
</head>


<body><center>
	<jsp:include page="/model/common_top.jsp" flush="true" /><%-- 动态包含  页面头 --%>
		
<div class="bag">
	<% boolean[] MoneyPBFieldDisplay=new boolean[]{
			false,false,
			true,true,true,
			true,true,true,
			true,true,false};
		int i; %>
	<table class="wtable" style="cellpadding:0;cellspacing:0;table-layout:fixed;"><tbody id="Table_2" style="display:;">
		<tr><td colspan="100">
			<div class="listHeader">
				教育实习经费标准
			</div>
		</td></tr>
		<tr><td colspan="100" style="width:20%;border:0;height:25px;">
			&nbsp;
		</td></tr>
		<s:form action="function_moneyPB_MoneyPBBaseInfo_execute" method="post" cssClass="myform">
		<tr class="wtableHeader" style="height:auto;">
			<td>条目</td>
			<td>北京市内</td>
			<td>其他地区</td>
		</tr>
		<% i=0; %>
		<s:iterator value="%{base[0].fields}" var="__opField" status="__opFieldStatus">
			<% if(MoneyPBFieldDisplay[i++]){ %>
			<tr class="wtableContent">
			<% }else{ %>
			<tr class="wtableContent" style="display:none;">
			<% } %>
				<td><!-- 条目 -->
					<s:property value="#__opField.description" />
				</td>
				<td><!-- 0 -->
					<s:textfield value="base[0].%{#__opField.name}"
					style="width:100%;height:100%;border:0;text-align:center;"
					theme="simple" />
				</td>
				<td><!-- 1 -->
					<s:textfield value="base[1].%{#__opField.name}"
					style="width:100%;height:100%;border:0;text-align:center;"
					theme="simple" />
				</td>
			</tr>
		</s:iterator>
		<tr>
			<s:submit value="提交修改" cssClass="button"/>
		</tr>
	</s:form>
	</tbody></table>
	
</div>

	<jsp:include page="/model/common_bottom.jsp" flush="true" /><%-- 动态包含  页面头 --%>
</center></body>
</html>