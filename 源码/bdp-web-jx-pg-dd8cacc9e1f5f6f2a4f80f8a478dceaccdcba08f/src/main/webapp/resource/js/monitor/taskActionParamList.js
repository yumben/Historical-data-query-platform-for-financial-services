var taskActionParamsList = newVue("vueArea",{table:{},param:{'pageNo':1,'pageSize':10,'actionId':'','actionName':''},total:'',addTaskActionParam:{},editTaskActionParamList:{},
addTaskActionParam:{'actionId':'','actionName':'','paramName':'','paramSeq':'','paramType':'','paramContext':'','paramRemark':''}});
taskActionParamsList.param.actionId = sessionStorage.getItem("actionId");
taskActionParamsList.param.actionName = sessionStorage.getItem("actionName");
/*$(function(){
	ajaxSelectTaskActionParam();
});


function ajaxSelectTaskActionParam(){
	jsonpAjax("TaskActionParam/getTaskActionParamList",taskActionParamsList.param,"getTaskActionList",function(data){
		
		var jsonObject = JSON.stringify(data); 
		jsonObject = jQuery.parseJSON(jsonObject);
		taskActionParamsList.total = jsonObject.total;
		page(taskActionParamsList,jsonObject.list,"page",ajaxSelectTaskActionParam);
	});
}*/



function delTaskActionParam(paramId){
	layer.confirm('是否要删除？', {
	  btn: ['确定','取消'] //按钮
	}, function(){
	    jsonpAjax(baseUrl+"TaskActionParam/delTaskActionParam",{"paramId":paramId},"start",function(data){
			var jsonObject = string4jsonObject(data);
			layer.alert(jsonObject.msg);
			loadData();
		});
	}, function(){
	});
	
}



function editTaskActionParam(i){
	var order = $("tr[data-id='" + i + "']").attr("data-order");
	taskActionParamsList.editTaskActionParamList = taskActionParamsList.table[order];
	//taskActionParamsList.editTaskActionParamList.actionId = taskActionParamsList.param.actionId;
	var main = $("#Editbox");
		layer.open({
			title:'编辑',
			closeBtn: 2, 
			type:1,
			//area:['450px','200px'],
			shadeClass:true,
			content:main,
			btn:['确定','取消'],
			btnAlign:'c',
			closeBtn:1,	
			yes: function(index, layero){
				jsonpAjax(baseUrl+"TaskActionParam/updateTaskActionParam",taskActionParamsList.editTaskActionParamList,"start",function(data){
					var jsonObject = JSON.stringify(data); 
					jsonObject = jQuery.parseJSON(jsonObject);
					if('0'==jsonObject.code){
						layer.close(index);
						loadData();
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


function addTaskActionParam(){
	taskActionParamsList.addTaskActionParam.actionId = sessionStorage.getItem("actionId");
	var main = $("#addTaskActionParam");
	layer.open({
		title:'添加',
		closeBtn: 2, 
		type:1,
		//area:['450px','200px'],
		shadeClass:true,
		content:main,
		btn:['确定','取消'],
		btnAlign:'c',
		closeBtn:1,	
		yes: function(index, layero){
			jsonpAjax(baseUrl+"TaskActionParam/addTaskActionParam",taskActionParamsList.addTaskActionParam,"start",function(data){
				var jsonObject = JSON.stringify(data); 
				jsonObject = jQuery.parseJSON(jsonObject);
				if(jsonObject.code == '0'){
					layer.close(index);
					loadData();
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


function cleanAddTaskActionParam(){
	taskActionParamsList.addTaskActionParam={};

}



function backToTaskAction(url){
	window.location.href = url + window.location.search;
}