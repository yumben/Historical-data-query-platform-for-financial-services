//存cookie
function setCookie(keyVal,cookieVal){
	$.cookie(keyVal,cookieVal,{
		expires:7,
		path:'/'
	});
}
//取cookie
function getCookie(cookieVal){
	var str = $.cookie(cookieVal);
	return str;
}
//清除cookie
function clearCookie(keyVal){
	$.cookie(keyVal, '', {
		expires: -1,
		path: '/'
	});
}

$(function(){	
	$('#loginBtn').click(function(){	
		//alert('点击登录按钮');
		var userName = $('#userName').val();
		var passWord = $('#passWord').val();
		var param = {
				'userName':userName,
				'passWord':hex_md5(passWord)
		};
		if(userName == ''){
			layer.alert('请输入用户名！');
			return false;
		}
		if(passWord == ''){
			layer.alert('请输入密码！');
			return false;
		}
		//console.log(JSON.stringify(param)+'####')
		ajaxSend('/bdp-web/auth/login', param,function(data){			
			if(data.code=='suc'){
				//console.log(JSON.stringify(data)+'###');
				var token=data.token;
				setCookie('token',token);
				setCookie('userName',data.userName);
				setCookie('userCNName',data.userCNName);
				window.location.href = '/bdp-web/easyquery/index.html';
			}else{
				layer.alert("用户名或密码不正确");
			}
		});
		//$(this).prop('disabled',true);
	})
})



