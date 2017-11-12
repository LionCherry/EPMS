<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EPMS-教务处-时间节点管理（<s:property value="annual.year" />年）</title>
<link rel="stylesheet" type="text/css" href="styles/style.css">
</head>

<body>
	<center>
		<jsp:include page="/model/common_top.jsp" flush="true" /><%-- 动态包含  页面头 --%>

<div class="bag"><table class="wtable">
	<s:form action="sudo_TimeManager_execute" method="post" cssClass="myform" theme="simple">
		<tr class="wtableHeader">
			<td>序号</td>
			<td>project(<s:property value="annual.year" />)</td>
			<td>time start</td>
			<td>time end</td>
		</tr>
		<s:iterator value="times" var="__tableRow" status="__status">
			<tr class="wtableContent">
				<td>
					<s:property value="%{#__tableRow.id}" />
					<s:hidden name="times[%{#__status.index}].id"
					value="%{id}" theme="simple"/>
				</td>
				<td style="width:380px;">
					<s:property value="%{#__tableRow.project}" />
					<s:hidden name="times[%{#__status.index}].project"
					value="%{#__tableRow.project}"  theme="simple"/>
				</td>
				<td style="width:180px;">
					<s:textfield name="times[%{#__status.index}].time1" theme="simple">
						<s:param name="value">
							<s:date name="%{#__tableRow.time1}" format="yyyy-MM-dd HH:mm:ss"/>
						</s:param>
					</s:textfield>
				</td>
				<td style="width:180px;">
					<s:textfield name="times[%{#__status.index}].time2" theme="simple">
						<s:param name="value">
							<s:date name="%{#__tableRow.time2}" format="yyyy-MM-dd HH:mm:ss"/>
						</s:param>
					</s:textfield>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="4">
				<s:submit value="提交修改" cssClass="button" theme="simple" />
			</td>
		</tr>
		<s:hidden name="annual.year" value="%{annual.year}" theme="simple" />
	</s:form>
	
</table>
</div>

		<jsp:include page="/model/common_bottom.jsp" flush="true" /><%-- 动态包含  页面头 --%>
	</center>
</body>
</html>