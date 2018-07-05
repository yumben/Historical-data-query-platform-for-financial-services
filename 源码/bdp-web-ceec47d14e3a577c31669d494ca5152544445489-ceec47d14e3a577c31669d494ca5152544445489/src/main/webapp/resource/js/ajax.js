/**
 * jquery的ajax提交请求封装函数
 * @param url 请求url
 * @param params 提交的数据
 * @param successCallFun 页面自己的回调函数，无需加引号
 */
function ajaxSend( url,params,successCallFun ){
	var token = getCookie('token');	
	params.token=token;
	$.ajax({
		// 后台处理程序
		url : url,
		// 数据发送方式
		type : "post",  
		// 接受数据格式
		dataType : "json",		
		// 要传递的数据
		data : params,
		traditional:true,
		//不缓存，每次重新请求后台
		cache: false,
		//同步请求 true 异步  false同步  
		async: false,
		// 回传函数
		success : function( responseData,textStatus ){
			if(responseData.errorCode!=null||responseData.errorMessage!=null || responseData.code ==-2){
				try{$("#submit_button").attr("disabled",false );}catch( e){}
				try{$("#submit_button1").attr("disabled",false );}catch( e){}
				try{$("#submit_button2").attr("disabled",false );}catch( e){}
				try{$("#submit_button3").attr("disabled",false );}catch( e){}
				try{$("#submit_button4").attr("disabled",false );}catch( e){}
				try{$("#submit_button5").attr("disabled",false );}catch( e){}
				if(responseData.errorCode=="EBNT1000" && responseData.errorMessage=="会话超时"){
				}else{
					//alert(responseData.errorMessage);
					alert("操作失败！");
					return;
				}
			}
			successCallFun(responseData);
			try{
				parent.sizeChange();
			}catch(e){}
		},
		error : function (XMLHttpRequest,textStatus,errorThrown){
			try{$("#submit_button").attr("disabled",false );}catch( e){}
			try{$("#submit_button1").attr("disabled",false );}catch( e){}
			try{$("#submit_button2").attr("disabled",false );}catch( e){}
			try{$("#submit_button3").attr("disabled",false );}catch( e){}
			try{$("#submit_button4").attr("disabled",false );}catch( e){}
			try{$("#submit_button5").attr("disabled",false );}catch( e){}
		} 
	});
}


function ajaxSendload( url,params,successCallFun){
	var token = getCookie('token');	
	params.token=token;
	$.ajax({
		// 后台处理程序
		url : url,
		// 数据发送方式
		type : "post",  
		// 接受数据格式
		dataType : "json",		
		// 要传递的数据
		data : params,
		traditional:true,
		//不缓存，每次重新请求后台
		cache: false,
		//同步请求 true 异步  false同步  
		async: true,
		//发送请求前
		beforeSend:function(){
			$("#load").show();
		},
		// 回传函数
		success : function( responseData,textStatus ){
			$("#load").hide();
			if(responseData.errorCode!=null||responseData.errorMessage!=null){
				try{$("#submit_button").attr("disabled",false );}catch( e){}
				try{$("#submit_button1").attr("disabled",false );}catch( e){}
				try{$("#submit_button2").attr("disabled",false );}catch( e){}
				try{$("#submit_button3").attr("disabled",false );}catch( e){}
				try{$("#submit_button4").attr("disabled",false );}catch( e){}
				try{$("#submit_button5").attr("disabled",false );}catch( e){}
				if(responseData.errorCode=="EBNT1000" && responseData.errorMessage=="会话超时"){
					$("#load").hide();
				}else{
					//alert(responseData.errorMessage);
					$("#load").hide();
					alert("操作失败！");
					return;
				}
			}
			$("#load").hide();
			successCallFun(responseData);
			try{
				$("#load").hide();
				parent.sizeChange();
			}catch(e){}
		},
		complete:function(){
		    $("#load").hide();
		},
		error : function (XMLHttpRequest,textStatus,errorThrown){
			$("#load").hide();
			try{$("#submit_button").attr("disabled",false );}catch( e){}
			try{$("#submit_button1").attr("disabled",false );}catch( e){}
			try{$("#submit_button2").attr("disabled",false );}catch( e){}
			try{$("#submit_button3").attr("disabled",false );}catch( e){}
			try{$("#submit_button4").attr("disabled",false );}catch( e){}
			try{$("#submit_button5").attr("disabled",false );}catch( e){}
		} 
	});
}


function jsonpAjax(url,param,jsonpCallback,fn){
	var token = getCookie('token');	
	params.token=token;
	 $.ajax({
		    type: "post",
		    async: true,
			data:param,
		    url: url,
		    dataType: "jsonp",
		    jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
		    jsonpCallback:jsonpCallback,//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
		    beforeSend:function(xmlhttp){
	            xmlhttp.setRequestHeader("test", "application/x-www-form-urlencoded");
	              //console.log(xmlhttp+'1111');
            },
		    success: function(data){
		    	fn(data);
		    },
		    error: function(){
				console.log('加载失败');
		    }
	   });
}

function ajaxSendCloud( url,params,successCallFun ){
	var token = getCookie('token');	
	params.token=token;
	$.ajax({
		// 后台处理程序
		url : url,
		// 数据发送方式
		type : "post",  
		// 接受数据格式
		dataType : "json",		
		// 要传递的数据
		data : params,
		traditional:true,
		//不缓存，每次重新请求后台
		cache: false,
		//同步请求 true 异步  false同步  
		async: false,
		// 回传函数
		success : function( responseData,textStatus ){
			if(responseData.errorCode!=null||responseData.errorMessage!=null){
				try{$("#submit_button").attr("disabled",false );}catch( e){}
				try{$("#submit_button1").attr("disabled",false );}catch( e){}
				try{$("#submit_button2").attr("disabled",false );}catch( e){}
				try{$("#submit_button3").attr("disabled",false );}catch( e){}
				try{$("#submit_button4").attr("disabled",false );}catch( e){}
				try{$("#submit_button5").attr("disabled",false );}catch( e){}
				if(responseData.errorCode=="EBNT1000" && responseData.errorMessage=="会话超时"){
				}else{
					//alert(responseData.errorMessage);
					alert("操作失败！");
					return;
				}
			}
			/*var json = responseData.json;
			json = jQuery.parseJSON(json);*/
			successCallFun(responseData);
			try{
				parent.sizeChange();
			}catch(e){}
		},
		error : function (XMLHttpRequest,textStatus,errorThrown){
			try{$("#submit_button").attr("disabled",false );}catch( e){}
			try{$("#submit_button1").attr("disabled",false );}catch( e){}
			try{$("#submit_button2").attr("disabled",false );}catch( e){}
			try{$("#submit_button3").attr("disabled",false );}catch( e){}
			try{$("#submit_button4").attr("disabled",false );}catch( e){}
			try{$("#submit_button5").attr("disabled",false );}catch( e){}
		} 
	});
}

function ajaxSendloadpannel(obj,url,params,successCallFun){
	var token = getCookie('token');	
	params.token=token;
	$.ajax({
		// 后台处理程序
		url : url,
		// 数据发送方式
		type : "post",  
		// 接受数据格式
		dataType : "json",		
		// 要传递的数据
		data : params,
		traditional:true,
		//不缓存，每次重新请求后台
		cache: false,
		//同步请求 true 异步  false同步  
		async: true,
		//发送请求前
		beforeSend:function(){
			//$(".load").show();
			obj.parent("div").siblings("div.load").show();
		},
		// 回传函数
		success : function( responseData,textStatus ){
			if(responseData.errorCode!=null||responseData.errorMessage!=null){
				try{$("#submit_button").attr("disabled",false );}catch( e){}
				try{$("#submit_button1").attr("disabled",false );}catch( e){}
				try{$("#submit_button2").attr("disabled",false );}catch( e){}
				try{$("#submit_button3").attr("disabled",false );}catch( e){}
				try{$("#submit_button4").attr("disabled",false );}catch( e){}
				try{$("#submit_button5").attr("disabled",false );}catch( e){}
				if(responseData.errorCode=="EBNT1000" && responseData.errorMessage=="会话超时"){
					
				}else{
					//alert(responseData.errorMessage);
					obj.siblings(".load").hide();
					alert("操作失败！");
					return;
				}
			}
						
			successCallFun(responseData);
			try{
				obj.parent("div").siblings("div.load").hide();
				parent.sizeChange();
			}catch(e){}
			
		},
		complete:function(){		    
			obj.parent("div").siblings("div.load").hide();
		},
		error : function (XMLHttpRequest,textStatus,errorThrown){
			obj.parent("div").siblings("div.load").hide();
			try{$("#submit_button").attr("disabled",false );}catch( e){}
			try{$("#submit_button1").attr("disabled",false );}catch( e){}
			try{$("#submit_button2").attr("disabled",false );}catch( e){}
			try{$("#submit_button3").attr("disabled",false );}catch( e){}
			try{$("#submit_button4").attr("disabled",false );}catch( e){}
			try{$("#submit_button5").attr("disabled",false );}catch( e){}
		} 
	});
	
	
}

/**
 * jquery的ajax提交请求封装函数
 * @param url 请求url
 * @param params 提交的数据
 * @param serviceController
 * @param successCallFun 页面自己的回调函数，无需加引号
 */
function ajaxSendService( url,params,serviceController,successCallFun ){
	var token = getCookie('token');	
	var param = {"token":token,"param" :JSON.stringify(params),"menuUrl":serviceController};
	$.ajax({
		// 后台处理程序
		url : url,
		// 数据发送方式
		type : "post",  
		// 接受数据格式
		dataType : "json",		
		// 要传递的数据
		data : param,
		traditional:true,
		//不缓存，每次重新请求后台
		cache: false,
		//同步请求 true 异步  false同步  
		async: false,
		// 回传函数
		success : function( responseData,textStatus ){
			if(responseData.errorCode!=null||responseData.errorMessage!=null || responseData.code ==-2){
				try{$("#submit_button").attr("disabled",false );}catch( e){}
				try{$("#submit_button1").attr("disabled",false );}catch( e){}
				try{$("#submit_button2").attr("disabled",false );}catch( e){}
				try{$("#submit_button3").attr("disabled",false );}catch( e){}
				try{$("#submit_button4").attr("disabled",false );}catch( e){}
				try{$("#submit_button5").attr("disabled",false );}catch( e){}
				if(responseData.errorCode=="EBNT1000" && responseData.errorMessage=="会话超时"){
				}else{
					//alert(responseData.errorMessage);
					alert("操作失败！");
					return;
				}
			}
			successCallFun(responseData);
			try{
				parent.sizeChange();
			}catch(e){}
		},
		error : function (XMLHttpRequest,textStatus,errorThrown){
			try{$("#submit_button").attr("disabled",false );}catch( e){}
			try{$("#submit_button1").attr("disabled",false );}catch( e){}
			try{$("#submit_button2").attr("disabled",false );}catch( e){}
			try{$("#submit_button3").attr("disabled",false );}catch( e){}
			try{$("#submit_button4").attr("disabled",false );}catch( e){}
			try{$("#submit_button5").attr("disabled",false );}catch( e){}
		} 
	});
}


