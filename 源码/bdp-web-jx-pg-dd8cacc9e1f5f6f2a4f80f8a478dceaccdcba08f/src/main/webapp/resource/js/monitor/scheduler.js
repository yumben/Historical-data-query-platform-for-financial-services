var processList = newVue("all", {
	actionId : '',
	actionIdSelect : '',
	form : {
		processKey : '',
		processName : '',
		isexecutable : '',
		version : '',
		remark : ''
	},
	taskParam : {},
	taskParamUpd : {}
});
var property = {
	width : 1138,
	height : 600,
	// toolBtns:["start round","end round","task
	// round","node","chat","state","plug","join","fork","complex mix"],
	toolBtns : [ "start round", "end round", "task", "node", "join", "fork" ],
	haveHead : true,
	// headBtns:["new","open","save","undo","redo","reload"],//如果haveHead=true，则定义HEAD区的按钮
	headBtns : [ "save", "undo", "redo", "reload" ],
	haveTool : true,
	haveGroup : true,
	useOperStack : true,
// headLabel:true,
// initNum:
};
var remark = {
	cursor : "选择指针",
	direct : "结点连线",
	start : "入口结点",
	"end" : "结束结点",
	"task" : "任务结点",
	node : "自动结点",
	// chat:"决策结点",
	// state:"状态结点",
	// plug:"附加插件",
	fork : "分支结点",
	"join" : "联合结点",
	"complex mix" : "复合结点",
// group:"组织划分框编辑开关"
};
var demo;
ajaxSend("/bdp-web/taskAction/getTaskActionSelect", {}, function(data) {
	var jsonObject = string4jsonObject(data);
	processList.actionIdSelect = jsonObject.list;
});

$(document)
	.ready(
		function() {
			var dataList = $("#dataList").val();
			var paramData = $("#paramData").val();
			dataList = jQuery.parseJSON(decodeURI(dataList));
			if (paramData != '') {
				processList.taskParamUpd = jQuery
					.parseJSON(decodeURI(paramData));
			}
			// 清空个工作区载入的数据
			demo = $.createGooFlow($("#demo"), property);
			demo.setNodeRemarks(remark);
			demo.onItemDel = function(id, type) {
				if (confirm("确定要删除该单元吗?")) {
					this.blurItem();
					return true;
				} else {
					return false;
				}
			}
			demo.clearData();
			demo.loadData(dataList);
			// console.log(JSON.stringify(demo.$nodeData));
			demo.onItemFocus = function(id, model) {
				$("#propertyForm").show();
				$(".inforshow").show();
				var obj;
				$("#ele_model").val(model);
				$("#ele_id").val(id);
				$("#ele_idTr").hide();
				$("#ele_typeTr").hide();
				if (model == "line") {
					obj = this.$lineData[id];
					$("#ele_type").val(obj.M);
					$("#ele_from").val(obj.from);
					$("#ele_to").val(obj.to);
					$("#ele_condition").val(obj.condition);
					$("#ele_left").val("");
					$("#ele_top").val("");
					$("#ele_width").val("");
					$("#ele_heightTr").val("");
					$("#ele_actionIdTr").val("");
					$("#ele_leftTr").hide();
					$("#ele_topTr").hide();
					$("#ele_widthTr").hide();
					$("#ele_heightTr").hide();
					$("#ele_typeTr").hide();
					$("#ele_actionIdTr").hide();
					// $("#ele_fromTr").show();
					// $("#ele_toTr").show();
					$("#ele_conditionTr").show();
				} else if (model == "node") {
					obj = this.$nodeData[id];
					$("#ele_type").val(obj.type);
					$("#ele_left").val(obj.left);
					$("#ele_top").val(obj.top);
					$("#ele_width").val(obj.width);
					$("#ele_height").val(obj.height);
					$("#ele_actionId").val(obj.actionId);
					$("#ele_from").val("");
					$("#ele_to").val("");
					$("#ele_conditionTr").val("");
					$("#ele_fromTr").hide();
					$("#ele_toTr").hide();
					$("#ele_conditionTr").hide();
					// $("#ele_leftTr").show();
					// $("#ele_topTr").show();
					// $("#ele_widthTr").show();
					// $("#ele_heightTr").show();
					// $("#ele_typeTr").show();
					$("#ele_actionIdTr").show();
					$("#paramInfo").show();
				}
				$("#ele_name").val(obj.name);
				if ($("#" + id).is(".item_round")) {
					$("#ele_actionIdTr").hide();
					$("#paramInfo").hide();
				}
				return true;
			}; // setName(id,name,type)

			demo.onFreshClick = function() {
				demo.clearData();
				var data = demo.exportData();
				demo.loadData(data);
			};

			var save = $("#saveBtn");
			save.setName = function(id, name, type) {
				alert("11111");
				var obj;

			}

			/*
			 * $("#saveBtn").click(function(id,model) { var obj;
			 * $("#ele_model").val(model); });
			 */

			demo.onItemBlur = function(id, model) {
				document.getElementById("propertyForm").reset();
				$("#propertyForm").hide();
				return true;
			};

			// 保存按钮
			demo.onBtnSaveClick = function(obj) {
				var main = $("#showbox");
				// console.log(main.html());
				var pid = $("#param").val();
				if (pid != 'add') {
					$(".attrshow").hide();
					// var pid =
					// $("#param").val();//通过sessionStorage获得load的参数
					var json = JSON.stringify(demo.exportData());
					var data = {
						"json" : json,
						"processId" : pid,
						"entity" : JSON.stringify(processList.form),
						"taskParam" : JSON
							.stringify(processList.taskParam)
					};

					ajaxSend("/bdp-web/process/saveJson",
						data,
						function(data) {
							if (data.status) {
								layer.alert("保存成功！", function() {
									window.location.href = window.location.href;
								});
								$("#scheduler").click();

							} else {
								layer.alert("保存失败！");
							}
						});
				} else {
					layer
						.open({
							title : '新增流程',
							closeBtn : 2,
							type : 1,
							// area:['450px','200px'],
							shadeClass : true,
							content : $("#showbox"),
							btn : [ '确定', '取消' ],
							btnAlign : 'c',
							closeBtn : 1,
							yes : function(index, layero) {
								layer.close(index); // 如果设定了yes回调，需进行手工关闭
								$(".attrshow").hide();
								// var pid =
								// $("#param").val();//通过sessionStorage获得load的参数
								var json = JSON.stringify(demo
									.exportData());
								var data = {
									"json" : json,
									"processId" : pid,
									"entity" : JSON
										.stringify(processList.form),
									"taskParam" : JSON
										.stringify(processList.taskParam)
								};
								ajaxSend("/bdp-web/process/saveJson",
									data,
									function(data) {
										if (data.status) {
											layer.alert("保存成功！", function() {
												window.location.href = window.location.href;
											});
											$("#scheduler").click();

										} else {
											layer
												.alert("保存失败！");
										}
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
				}
				return true;
			};

			// 刷新按钮
			/*
			 * demo.onFreshClick=function(obj){ alert("刷新成功"); return
			 * true; };
			 */
			$("#ele_actionId").change(function() {
				var taskId = $("#ele_id").val();
				processList.taskParamUpd[taskId] = {};
			});

		});

function taskParam(e) {
	// console.log(processList.taskParamUpd)
	var id = $("#ele_actionId").val();
	var taskId = $("#ele_id").val();
	if (id != '' && !jQuery.isEmptyObject(processList.taskParamUpd[taskId])) { // 更新的操作
		var currentParam = processList.taskParamUpd[taskId];
		$("#taskParam").html("");
		$
			.each(
				currentParam,
				function(index, obj) {
					var json = {
						'taskParamName' : obj.taskParamName,
						'taskParamSeq' : obj.taskParamSeq,
						'taskId' : taskId
					};

					var htmlText = "<tr data='" + JSON.stringify(json)
						+ "'>" + "<th>" + obj.remark
						+ "：</th>" + "<td>";
					if (obj.taskParamType == 'pikey') {
						htmlText += "<input class='form-control' id='input"
							+ index
							+ "' disabled='disabled' type='text' value=''/>"
					} else {
						htmlText += "<input class='form-control' id='input"
							+ index
							+ "'  type='text' value='"
							+ obj.taskParamContext + "'/>"
					}
					htmlText += "</td>"
						+ "<td>"
						+ "<select class='form-control width-select' id='select"
						+ index
						+ "'>"
						+ "<option value=''>请选择</option>"
						+ "<option value='fix'>常量</option>"
						+ "<option value='pikey' onclick='disabInput(this)'>流程实例Id</option>"
						+ "<option value='vari'>参数值</option>"
						+ "</select>" + "</td>" + "</tr>";
					$("#taskParam").append(htmlText);
					$("#select" + index).val(obj.taskParamType);
					$("#select" + index).on("change", function() {
						var id = "input" + index;
						if ($(this).val() == "pikey") {
							$("#" + id).val("");
							$("#" + id).attr("disabled", true);
						} else {
							$("#" + id).attr("disabled", false);
						}

					});
				});
		layer.open({
			title : '参数信息',
			closeBtn : 1,
			type : 1,
			area : [ 'auto' ],
			shadeClass : true,
			content : $("#paramDiv"),
			btn : [ '确定', '取消' ],
			btnAlign : 'c',
			yes : function(index, layero) {
				layer.close(index); // 如果设定了yes回调，需进行手工关闭
				var array = [];
				$("#taskParam").find("tr").each(function(i, obj) {
					var input = $("#input" + i).val();
					var select = $("#select" + i).val();
					var data = $(obj).attr("data");
					data = jQuery.parseJSON(data);
					data['taskParamContext'] = input;
					data['taskParamType'] = select;
					array.push(data);
				});
				processList.taskParam[taskId] = array;
				processList.taskParamUpd[taskId] = array;
			},
			btn2 : function(index, layero) {},
			cancel : function(index, layero) {
				layer.close(index);
			}
		});
	} else if (id != ''
		&& jQuery.isEmptyObject(processList.taskParamUpd[taskId])) {
		$("#taskParam").html("");
		ajaxSend("/bdp-web/TaskActionParam/getTaskActionListById",
			{
				"id" : id
			},
			function(data) {
				//var data = string4jsonObject(json);
				console.log(data);
				$.each(data.entityList,function(index, obj) {
							var json = {
								'taskParamName' : obj.paramName,
								'taskParamSeq' : obj.paramSeq,
								'remark' : obj.paramRemark,
								'taskId' : taskId
							};

							var htmlText = "<tr data='"
								+ JSON.stringify(json)
								+ "'>"
								+ "<th>"
								+ obj.paramRemark
								+ "：</th>"
								+ "<td>"
								+ "<input class='form-control' id='input"
								+ index
								+ "'  type='text' value=''/>"
								+ "</td>"
								+ "<td>"
								+ "<select class='form-control width-select' data='"
								+ index
								+ "' id='select"
								+ index
								+ "'>"
								+ "<option value=''>请选择</option>"
								+ "<option value='fix'>常量</option>"
								+ "<option value='pikey' onclick='disabInput(this)'>流程实例Id</option>"
								+ "<option value='vari'>参数值</option>"
								+ "</select>" + "</td>"
								+ "</tr>";
							$("#taskParam").append(htmlText);
							$("#select" + index)
								.on(
									"change",
									function() {
										var id = "input"
											+ index;
										if ($(this).val() == "pikey") {
											$("#" + id)
												.val("");
											$("#" + id)
												.attr(
													"disabled",
													true);
										} else {
											$("#" + id)
												.attr(
													"disabled",
													false);
										}

									});
						});
				layer.open({
					title : '参数信息',
					closeBtn : 1,
					type : 1,
					area : [ 'auto' ],
					shadeClass : true,
					content : $("#paramDiv"),
					btn : [ '确定', '取消' ],
					btnAlign : 'c',
					yes : function(index, layero) {
						layer.close(index); // 如果设定了yes回调，需进行手工关闭
						var array = [];
						$("#taskParam").find("tr").each(function(i, obj) {
							var input = $("#input" + i).val();
							var select = $("#select" + i).val();
							var data = $(obj).attr("data");
							data = jQuery.parseJSON(data);
							data['taskParamContext'] = input;
							data['taskParamType'] = select;
							array.push(data);
						});
						processList.taskParam[taskId] = array;
						processList.taskParamUpd[taskId] = array;
						return false;
					},
					btn2 : function(index, layero) {},
					cancel : function(index, layero) {
						layer.close(index);
					}
				});
			});
	} else {
		layer.alert("请先选择actionId");
	}
}
;

function propertyForm() {
	var id = $("#ele_id").val();
	var type = $("#ele_model").val();
	if (type == "line") {
		demo.$lineData[id].from = $("#ele_from").val();
		demo.$lineData[id].to = $("#ele_to").val();
		demo.$lineData[id].name = $("#ele_name").val();
		demo.$lineData[id].condition = $("#ele_condition").val();
		$("#" + id).find("text").each(function() {
			$(this).text($("#ele_name").val())
		});
	} else {
		demo.$nodeData[id].type = $("#ele_type").val();
		demo.$nodeData[id].left = parseInt($("#ele_left").val());
		demo.$nodeData[id].top = parseInt($("#ele_top").val());
		demo.$nodeData[id].width = parseInt($("#ele_width").val());
		demo.$nodeData[id].height = parseInt($("#ele_height").val());
		//demo.$nodeData[id].=$("#ele_from").val("");
		//demo.$nodeData[id].=$("#ele_to").val("");
		demo.$nodeData[id].actionId = $("#ele_actionId").val();
		demo.$nodeData[id].name = $("#ele_name").val();
	}
	if (type == "node" || type == "join" || type == "fork" || type == "task") {
		$("#" + id).find("td").each(function() {
			if ($(this).text() != "") {
				$(this).text($("#ele_name").val())
			}
		});
	}
	if ($("#" + id).is(".item_round")) {
		$("#" + id).find("div").each(function() {

			if ($(this).text() != "") {
				$(this).text($("#ele_name").val())
			}
		});
	}
	console.log(JSON.stringify(demo.exportData()));
	layer.alert("修改成功！");
}