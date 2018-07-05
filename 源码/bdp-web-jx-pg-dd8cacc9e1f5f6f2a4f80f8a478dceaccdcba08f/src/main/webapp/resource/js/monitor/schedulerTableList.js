var processList = newVue("showbox", {
	form : {
		processKey : '',
		processName : '',
		isexecutable : '',
		version : '',
		remark : ''
	}
});
var _json4Table = new $.json4Table({
	action : "/bdp-web/process/selectProcess",
	formatter : operateFormatter
});
$(function() {
	_json4Table.drewTable({});
});

function operateFormatter(id) {
	return [ '<a onclick="eidtProcess(\'' + id + '\')">编辑流程</a>' +
	'<a onclick="lookScheduler(\'scheduler.html\',\'' + id + '\')">编辑任务</a>' +
	'<a class="start-td" onclick="startScheduling(\'' + id + '\')">启动</a> ' +
	'<a class="delet-td" onclick="delProcess(\'' + id + '\')">删除</a>' ].join('');
}

function lookScheduler(url, param) {
	ajaxSend("/bdp-web/process/selectProcessObj", {
		"processId" : param
	}, function(data) {
		$("#param").val(param);
		$("#dataList").val(JSON.stringify(data.jsonData));
		if (data.taskParam != undefined) {
			$("#paramData").val(JSON.stringify(data.taskParam)); //task参数信息
		}
		$(".contentload").load(url);
	});
}

function startScheduling(id) {
	ajaxSend("/bdp-web/jsonController/getParamInfo", {
		"processId" : id
	}, function(jsonObject) {
		console.log(jsonObject);
		$("#paramInfoTable").html("");
		var tableHtml = "";
		$.each(jsonObject.list,function(i,obj){
			tableHtml += "<div>";
			tableHtml += "<tr>";
			tableHtml += "<th><b>"+obj.taskParamName+"</b>：</th>";
			tableHtml += "<td><input value=''  type='text'  class='form-control'></td>";
			tableHtml += "</tr>";
			tableHtml += "</div>";
		});
		$("#paramInfoTable").append(tableHtml);
		layer.open({
			title : '编辑流程',
			closeBtn : 2,
			type : 1,
			//area:['450px','200px'],
			shadeClass : true,
			content : $("#paramInfoBox"),
			btn : [ '确定', '取消' ],
			btnAlign : 'c',
			closeBtn : 1,
			yes : function(index, layero) {
				layer.close(index);
				var param = {};
				$("#paramInfoTable").find("div").each(function(i,obj){//find获取内容放到对象里面
					var $obj = $(obj);
					var b = $($obj.find("b"));
					var c = $($obj.find("input"));
					param[b.text()] = c.val();
				});
				ajaxSend("/bdp-web/jsonController/startScheduling", {
					"processId" : id,
					"json" : JSON.stringify(param)
				}, function(jsonObject) {
					layer.confirm(jsonObject.msg, {
						btn : [ '是', '否' ] //按钮
					}, function(index) {
						layer.close(index);
						var url = window.location.search;
						window.location.href = getContextPath() + '/monitor/monitorTableList.html' + url;
					}, function() {});
				});
				
			},
			btn2 : function(index, layero) {
				$(".attrshow").hide();
				
			},
			cancel : function(index, layero) {
				layer.close(index);
				$(".attrshow").hide();
			}
		});
	});
}

function getContextPath() {
	var pathName = document.location.pathname;
	var index = pathName.substr(1).indexOf("/");
	var result = pathName.substr(0, index + 1);
	return result;
}

function eidtProcess(processId) {
	ajaxSend("/bdp-web/process/getProcess", {
		"processId" : processId
	}, function(jsonObject) {
		processList.form = jsonObject.entity;
	});

	var main = $("#showbox");
	layer.open({
		title : '编辑流程',
		closeBtn : 2,
		type : 1,
		//area:['450px','200px'],
		shadeClass : true,
		content : main,
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		closeBtn : 1,
		yes : function(index, layero) {
			layer.close(index); //如果设定了yes回调，需进行手工关闭
			$(".attrshow").hide();
			ajaxSend("/bdp-web/process/saveProcess", {
				'json' : JSON.stringify(processList.form),
				"processId" : processId
			}, function(jsonObject) {
				layer.alert(jsonObject.msg);
				_json4Table.drewTable({});
				emptyFrom(processList.form);
			});
		},
		btn2 : function(index, layero) {
			$(".attrshow").hide();
			emptyFrom(processList.form);
		},
		cancel : function(index, layero) {
			layer.close(index);
			$(".attrshow").hide();
			emptyFrom(processList.form);
		}
	});
}

function delProcess(processId) {
	layer.confirm('是否要删除？', {
		btn : [ '确定', '取消' ] //按钮
	}, function() {
		ajaxSend("/bdp-web/process/delProcess", {
			"processId" : processId
		}, function(jsonObject) {
			layer.alert(jsonObject.msg);
			_json4Table.drewTable({});
		});
	}, function() {});
}

function submit() {
	var processName = $("#processName").val();
	var params = {
		"processName" : processName
	};
	_json4Table.drewTable(params);
}