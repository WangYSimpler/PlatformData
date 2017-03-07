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
</head>

<body>
	<br />
	<input type="button" value="测   试" onclick="test();" />

	<div id="test_div"></div>

	<!-- javascript -->
	<script src="<%=path%>/js/jquery.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function (){
		});
		
		function test()
		{
			$.ajax({
				url: "<%=path %>/data/getDatas",
				data: {
					dataObjectCode:"DWD_DPT_AJ_JBXX",
						conditions :""},
				type: "post",
				success: function (data){
					$("#test_div").html(data);
				}
			});
		}
	</script>
</body>
</html>
