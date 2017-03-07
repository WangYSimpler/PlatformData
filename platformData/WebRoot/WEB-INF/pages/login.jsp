<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>登录</title>
	<link href="<%=path %>/css/mis.css" rel="stylesheet" type="text/css" />
</head>

<body class="log_body">
	<form action="<%=path %>/sys/log/login" method="post" id="myForm" name="myForm">
	<div class="log_box_out">
		<div class="log_box">
			<div class="log_one_input">
				<p class="log_input_name">账 号</p>
				<input class="log_input" type="text" id="no" name="no" />
			</div>
			
			<div class="log_one_input">
				<p class="log_input_name">密 码</p>
				<input class="log_input" type="password" id="pwd" name="pwd" />
			</div>
			
			<div style="max-height: 5px; text-align: center; color: red;" id="msg_div">
			${msg }
			</div>
			
			<a class="log_btn" href="javascript:void(0)" id="login_a">登 录</a>
		</div>
	</div>
	</form>
	<p class="log_footer">江苏高弗特信息科技有限公司 服务热线：400-880-1976 版权所有：2010-2016 All Rights Reserved</p>
	
	<!-- javascript -->
	<script src="<%=path%>/js/jquery.js" type="text/javascript"></script>
	<script src="<%=path %>/js/common.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function (){
			
		});
		
		$("#login_a").click(function () {
			if ($("#no").val() == "" || $("#pwd").val() == "")
			{
				$("#msg_div").html("账号和密码必填");
				return;
			}
			
			myForm.submit();
		});
	</script>
</body>
</html>
