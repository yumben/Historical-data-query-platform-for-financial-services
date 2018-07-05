var processList = newVue("vueArea",{table:{},param:{'pageNo':1,'pageSize':10,'condition':''},total:'',detailList:{},addTaskActionList:{},editTaskActionList:{},
addTaskActionParam:{'actionId':'','actionName':'','paramName':'','paramSeq':'','paramType':'','paramContext':'','paramRemark':''}});
/*$(function(){
		ajaxSelectTaskAction("");	
});


function ajaxSelectTaskAction(condition){
	processList.param.condition=condition;
	jsonpAjax("taskAction/getTaskActionList",processList.param,"getTaskActionList",function(data){
		
		var jsonObject = JSON.stringify(data); 
		jsonObject = jQuery.parseJSON(jsonObject);
		processList.total = jsonObject.total;
		page(processList,jsonObject.list,"page",ajaxSelectTaskAction);
	});
}*/


function lookDedail(i){
	var order = $("tr[data-id='" + i + "']").attr("data-order");
	processList.detailList = processList.table[order];
	processList.detailList.jdbcSqlType = showSqlTypeName(processList.detailList.jdbcSqlType);
	//alert(processList.detailList.actionId);
			 
		var main = $("#showDetailbox");
		layer.open({
			title:'查看详情',
			closeBtn: 1, 
			type:1,
			//area:['450px','200px'],
			shadeClass:true,
			content:main,
			btn:['关闭'],
			btnAlign:'c',
			yes: function(index, layero){
				layer.close(index);
			},
			
			cancel: function(index, layero){ 			 
				
			}	
		});


		

	
}

function delTaskAction(actionId){
	layer.confirm('是否要删除？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
	    jsonpAjax(baseUrl+"taskAction/delTaskAction",{"actionId":actionId},"start",function(data){
			var jsonObject = string4jsonObject(data);
			layer.alert(jsonObject.msg);
			reflashData();
		});
	}, function(){
	});
	
}

function addTaskAction(param){
	var main = $("#Addbox");
		layer.open({
			title:'添加',
			closeBtn: 1, 
			type:1,
			//area:['450px','200px'],
			shadeClass:true,
			content:main,
			btn:['确定','取消'],
			btnAlign:'c',
			yes: function(index, layero){
				jsonpAjax(baseUrl+"taskAction/addTaskAction",processList.addTaskActionList,"start",function(data){
					var jsonObject = JSON.stringify(data); 
					jsonObject = jQuery.parseJSON(jsonObject);
					if(jsonObject.code == '0'){
						layer.close(index);
						reflashData();
						cleanAddTaskActionList();
					}
					alert(jsonObject.msg);
					
				});
			},
			btn2:function(index, layero){
				cleanAddTaskActionList();
			},
			cancel: function(index, layero){ 
				cleanAddTaskActionList();
			}	
		});
	
}

function editTaskAction(i){
	var order = $("tr[data-id='" + i + "']").attr("data-order");
	processList.editTaskActionList = processList.table[order];
	var main = $("#Editbox");
		layer.open({
			title:'编辑',
			closeBtn: 1, 
			type:1,
			//area:['450px','200px'],
			shadeClass:true,
			content:main,
			btn:['确定','取消'],
			btnAlign:'c',
			yes: function(index, layero){
				jsonpAjax(baseUrl+"taskAction/updateTaskAction",processList.editTaskActionList,"start",function(data){
					var jsonObject = JSON.stringify(data); 
					jsonObject = jQuery.parseJSON(jsonObject);
					if('0'==jsonObject.code){
						layer.close(index);
						reflashData();
					}
					alert(jsonObject.msg);
					
				});
			},
			btn2:function(index, layero){
			},
			cancel: function(index, layero){ 
			}	
		});
}


function addTaskActionParam(actionId){
	processList.addTaskActionParam.actionId = actionId;
	var actionName = $($($("tr[data-id='" + actionId + "']")).children()[0]).text();
	
	processList.addTaskActionParam.actionName = actionName;
	var main = $("#addTaskActionParam");
	layer.open({
		title:'添加',
		closeBtn: 1, 
		type:1,
		//area:['450px','200px'],
		shadeClass:true,
		content:main,
		btn:['确定','取消'],
		btnAlign:'c',
		yes: function(index, layero){
			jsonpAjax(baseUrl+"TaskActionParam/addTaskActionParam",processList.addTaskActionParam,"start",function(data){
				var jsonObject = JSON.stringify(data); 
				jsonObject = jQuery.parseJSON(jsonObject);
				if(jsonObject.code == '0'){
					layer.close(index);
					cleanAddTaskActionParam()
				}
				alert(jsonObject.msg);
				
			});
		},
		btn2:function(index, layero){
			cleanAddTaskActionParam();
		},
		cancel: function(index, layero){ 
			cleanAddTaskActionParam();
		}	
	});
	
}

function cleanAddTaskActionList(){
	processList.addTaskActionList = {};
}

function cleanAddTaskActionParam(){
	processList.addTaskActionParam.paramName="";
	processList.addTaskActionParam.paramSeq="";
	processList.addTaskActionParam.paramType="";
	processList.addTaskActionParam.paramContext="";
	processList.addTaskActionParam.paramRemark="";

}



function lookTaskActionParam(url,actionId){
	var actionName = $($($("tr[data-id='" + actionId + "']")).children()[0]).text();
	sessionStorage.setItem('actionId',actionId);
	sessionStorage.setItem('actionName',actionName);
	$(".wrap").load(url);
}

function showSqlTypeName(sqlType){
	if('Q' == sqlType){
		return "query查询";
	}else if('E' == sqlType){
		return "execute执行";
	}else if('P' == sqlType){
		return "Procedure存储过程";
	}else{
		return sqlType;
	}
}