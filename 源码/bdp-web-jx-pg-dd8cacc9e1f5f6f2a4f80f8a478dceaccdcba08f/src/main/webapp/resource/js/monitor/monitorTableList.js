//var processInstanceList = newVue("vueArea",{table:{},param:{'pageNo':1,'pageSize':10},total:''});
var _json4Table = new $.jsonp4Table({
	action : "/bdp-web/jsonController/selectProcessInstance",
	jsonpCallback : 'selectProcessInstance',
	formatter : operateFormatter
});
$(function() {
	_json4Table.drewTable({});
});

function operateFormatter(id, obj) {
	var text = "";
	if (obj['status'] == '失败') {
		text = [
			'<a onclick="lookMonitor(\'monitor.html\',\'' + id + '\')">查看详情</a>',
			'<a onclick="getLogs(\'' + id + '\')">报错日志</a>'
		].join('');
	} else {
		text = [
			'<a onclick="lookMonitor(\'monitor.html\',\'' + id + '\')">查看详情</a>'
		].join('');
	}
	return text;
}

function getLogs(id) {
	ajaxSend("/bdp-web/processInstanceLogs/getLogs", {
		"processInstanceId" : id
	}, function(data) {
		layer.alert(data.msg);
	});
}

function lookMonitor(url, param) {
	$("#param").val(param);
	$(".contentload").load(url);
}

function restart(id) {
	ajaxSend("/bdp-web/processInstance/restart", {
		"processInstanceId" : id
	}, function(data) {
		if (data.flag) {
			layer.alert("重新加载成功", function() {
				window.location.href = window.location.href;
			});
		} else {
			layer.alert("重新加载失败");
		}
	});
}