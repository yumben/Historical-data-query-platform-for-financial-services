var timer = "";
//var setcontent = document.getElementById('setcontent');
var width = document.documentElement.clientWidth;
//setcontent.width = width;
window.onresize = function(){
	var width = document.documentElement.clientWidth;
	//setcontent.width = width;
}


var property={
	width:1136,
	height:600,
	toolBtns:["start round","end round","task round","node","chat","state","plug","join","fork","complex mix"],
	haveHead:false,
	headBtns:["new","open","save","undo","redo","reload"],//如果haveHead=true，则定义HEAD区的按钮
    haveTool:false,
	haveGroup:true,
	useOperStack:true
};
var remark={
	cursor:"选择指针",
	direct:"结点连线",
	start:"入口结点",
	"end":"结束结点",
	"task":"任务结点",
	node:"自动结点",
	chat:"决策结点",
	state:"状态结点",
	plug:"附加插件",
	fork:"分支结点",
	"join":"联合结点",
	"complex mix":"复合结点",
	group:"组织划分框编辑开关"
};
var demo;
$(document).ready(function(){
	demo=$.createGooFlow($("#demo"),property);
	demo.setNodeRemarks(remark);
	var pid = $("#param").val();//通过sessionStorage获得load的参数
	ajaxRefresh(pid);
});

//load组件的方法
function ajaxRefresh(processInstanceId){  	
	var processId = {"processId":processInstanceId};
	
	ajaxSend("/bdp-web/jsonController/getJson",processId,function(data){
		demo.loadData(data);	
		timer = setInterval(function() {ajaxRe(processInstanceId);console.log('timer')},5000);
	});
} 
  //定时刷新获取状态的方法
function ajaxRe(processInstanceId) {
	var processId = {
		"processId" : processInstanceId
	};

	ajaxSend("/bdp-web/jsonController/getJson", processId, function(list) {
		var statu = list.nodes;
		for ( var key in statu) {
			console.log(JSON.stringify(statu));
			$("#" + key).attr("status", statu[key].status);
		};
	});
}

