var $table = $('.table');
$(document).ready(function() {

	$('#btncheck').on('click', function() {
		window.location.href = '/bdp-web/easytarget/easytargetdefine.html'
	})
})
//修改
$(document).on('click', '.vue_tableEdit', function(event) {
	var parentTr = $(event.target).parent().parent();
	window.location.href = '/bdp-web/easytarget/easytargetdefine.html?id=' + parentTr[0].id;
})
//删除
$(document).on('click', '.vue_tableRemove', function(event) {
	var parentTr = $(event.target).parent().parent();
	var id = parentTr[0].id;
	top.layer.confirm('是否确认删除？', {
		btn : [ '确定', '取消' ]
	}, function(index) {
		var params = {
			"id" : id
		};
		ajaxSend('/bdp-web/easyTarget/delete', params, callfunction);
		top.layer.close(index); //如果设定了yes回调，需进行手工关闭
		//调用vue里的函数
		vm.getJSON();
	}, function() {});
})
//配置    
$(document).on('click', '.vue_tableConfig', function(event) {
	var parentTr = $(event.target).parent().parent();
	window.location.href = '/bdp-web/easytarget/easytargetconfig.html?id=' + parentTr[0].id;
})
//执行
$(document).on('click', '.vue_implement', function(event) {
	var parentTr = $(event.target).parent().parent();
	var id = parentTr[0].id;
	var params = {
		"id" : id
	};
	ajaxSend('/bdp-web/easyTarget/execTarget', params, callfunction);
})

function callfunction(data) {
	if (data.ret_code == "0") {
		alert("操作成功！");
	} else {
		alert("操作失败！");
	}
}