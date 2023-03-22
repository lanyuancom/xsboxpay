$(function(){
	
	//用户登录
	$("#LoginSubmit").click(function(){
		var SSID=$("#LOGINSSID").val();
		var CODE=$("#LOGINCODE").val();
		var USER=$("#LOGINUSER").val();
		var PASS=$("#LOGINPASS").val();
		
		USER=='' ?  $("#usernote").text('商户编号不能为空') : $("#usernote").empty();
		SSID=='' ?  $("#ssidnote").text('操作员号不能为空') : $("#ssidnote").empty();
		PASS=='' ?  $("#passnote").text('登陆密码不能为空') : $("#passnote").empty();
		CODE=='' ?  $("#codenote").text('验证码不能为空') : $("#codenote").empty();
		if(USER=='' || PASS=='' || SSID=='')  return false;
		$.post("login.html",{LOGINSSID:SSID,LOGINCODE:CODE,LOGINUSER:USER,LOGINPASS:PASS},function(backdata){
					if(backdata.status==1)
					{
						//window.top.location.reload();
						window.top.location='index.html';
					}
					else if(backdata.status==0)
					{
						$("#usernote").text(backdata.info.user);
						$("#ssidnote").text(backdata.info.ssid);
						$("#passnote").text(backdata.info.pass);
						$("#codenote").text(backdata.info.code);
					}
				},'json');
		
																 
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
})