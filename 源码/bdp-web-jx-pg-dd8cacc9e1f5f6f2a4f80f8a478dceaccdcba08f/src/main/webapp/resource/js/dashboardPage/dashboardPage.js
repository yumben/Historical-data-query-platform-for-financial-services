//拖拽功能

var pannelType = "";
var editPageType = "";
var editModelId = ""; 

//左边菜单
function getMenu() {
	//多级树形菜单	
	ajaxSend('/bdp-web/dashboardPage/selectDashboardCatalog', {},function(data) {
		$("#nav-left").html("");
		var navLeft = $('#nav-left');
		var list = data.list;
		TreeMenu(list, navLeft);
	})
}
//组装菜单树
function TreeMenu(data, obj) {
	var selectHtml = '';
	if(data &&　data != ''){
		for (var i = 0; i < data.length; i++) {
			//console.log(JSON.stringify(data[i])+'父级目录')
			var item = data[i];
			var name = item.dashboard_catalog_name;
			var id = item.dashboard_catalog_id;
			var code = item.dashboard_catalog_code;
			var dashboardPages = item.dashboardPages;
			var childrens = item.childrens;
			selectHtml += '<li><h5 dashboard_catalog_id="'+id+'" dashboard_catalog_code="'+code+'" dashboard_catalog_name="'+name+'">'
			+'<i class="glyphicon glyphicon-minus"></i> '+name+'<em class="fa fa-trash delCatalog"></em><b class="fa fa-pencil editCatalog"></b>'
			+'<em class="addDash glyphicon glyphicon-plus"></em></h5>';
			selectHtml += '<ul>';			
			if(dashboardPages != ''){				
				$.each(dashboardPages,function(index,el){
					selectHtml += '<li><p id = "'+el.dashboard_id+'" dashboard_id="'+el.dashboard_id+'" dashboard_code="'+el.dashboard_code+'" dashboard_catalog_id="'+el.dashboard_catalog_id+'" dashboard_name="'+el.dashboard_name+'">'
					+'<i class="fa fa-file-text-o"></i> '+el.dashboard_name+'<em class="fa fa-trash delDashboard"></em><b class="fa fa-pencil editDashboard"></b></p></li>';					
				})
			}
			if(childrens !=''){
				//console.log(JSON.stringify(childrens)+'子集')				
				$.each(childrens,function(index,el){
					selectHtml += '<li><h5 dashboard_catalog_id="'+el.dashboard_catalog_id+'" dashboard_catalog_code="'+el.dashboard_catalog_code+'" dashboard_catalog_name="'+el.dashboard_catalog_name+'">'
					+'<i class="glyphicon glyphicon-minus"></i> '+el.dashboard_catalog_name+'<em class="fa fa-trash delCatalog"></em><b class="fa fa-pencil editCatalog"></b>'
					+'<em class="addDash glyphicon glyphicon-plus"></em></h5>';					
					var childMenu = el.childrens;
					if(childMenu !=''){
						intMenuAside(childMenu);
					}					
				})
			}
			selectHtml += '</ul></li>';
		}
	}
	obj.append(selectHtml);
	function intMenuAside(childMenu) {
		var menu = childMenu;
		selectHtml += '<ul>';
		//判断childMenu是否存在
		$.each(childMenu,function(index,el){
			var childname = el.dashboard_catalog_name;
			var childid = el.dashboard_catalog_id;
			var childcode = el.dashboard_catalog_code;
			var childdashboardPages = el.dashboardPages;
			var childchildrens = el.childrens;			
			selectHtml += '<li><h5 dashboard_catalog_id="'+childid+'" dashboard_catalog_code="'+childcode+'" dashboard_catalog_name="'+childname+'">'
			+'<i class="glyphicon glyphicon-minus"></i> '+childname+'<em class="fa fa-trash delCatalog"></em>'
			+'<b class="fa fa-pencil editCatalog"></b><em class="addDash glyphicon glyphicon-plus"></em></h5>';
			selectHtml += '<ul>';			
			if(childdashboardPages != ''){				
				$.each(childdashboardPages,function(index,el){
					selectHtml += '<li><p id = "'+el.dashboard_id+'" dashboard_id="'+el.dashboard_id+'" dashboard_code="'+el.dashboard_code+'" dashboard_catalog_id="'+el.dashboard_catalog_id+'" dashboard_name="'+el.dashboard_name+'">'
					+'<i class="fa fa-file-text-o"></i> '+el.dashboard_name+'<em class="fa fa-trash delDashboard"></em><b class="fa fa-pencil editDashboard"></b></p></li>';					
				})
			}
			if(childchildrens !=''){
				//console.log(JSON.stringify(childrens)+'子集')
				$.each(childchildrens,function(index,el){
					selectHtml += '<li><h5 dashboard_catalog_id="'+el.dashboard_catalog_id+'" dashboard_catalog_code="'+el.dashboard_catalog_code+'" dashboard_catalog_name="'+el.dashboard_catalog_name+'">'
					+'<i class="glyphicon glyphicon-minus"></i> '+el.dashboard_catalog_name+'<em class="fa fa-trash"></em>'
					+'<b class="fa fa-pencil"></b><em class="addDash glyphicon glyphicon-plus"></em></h5>';
					var childMenuy = el.childrens;
					if(childMenuy !=''){
						intMenuAside(childMenuy);
					}					
				})
			}
			selectHtml += '</ul></li></ul></li>';
		})
	}
}
//初始化菜单
$(function() {
	//左边菜单
	getMenu();
	$(document).on("click", "#nav-left p", function() { //点击页面菜单
		dashboard_id = $(this).attr("dashboard_id");
		$(".eleShow").show();
		$("#dashboardName").text($(this).attr("dashboard_name"));
		$("#dashboardCode").text($(this).attr("dashboard_code"));
		$("#dashboardId").text($(this).attr("dashboard_id"));
		getPannelPageEle(dashboard_id); //加载页面元素

	})
})

//加载页面面板元素
function getPannelPageEle(dashboard_id) {
	$("#pannelbox").html("");
	var itemHtml = "";
	var param = {
		"dashboard_id" : dashboard_id
	};
	ajaxSendCloud('/bdp-web/dashboardPage/selectDashboardPage', param, function(data) {
		var json = data.dashboardPage.dashboardEles;
		if (json == "" || json == null || json == "undefined") {
			//$("#initAddShow").show();
			itemHtml = "<div class='pannel-addbox addshowbox' id='initAddShow' ><span><em><img src='../resource/img/panneladdico.png'></em><b>新增图表</b></span></div>";
		} else {
			$.each(json, function(index, item) {
				if (item.item_type == "1") {
					itemHtml += "<div class='item' style='width:" + item.item_width + "px; height:" + item.item_height + "px'  item_model_id='" + item.item_model_id + "' item_type='" + item.item_type + "' dashboard_id='" + item.dashboard_id + "' dashboard_ele_id='" + item.dashboard_ele_id + "' id='" + item.dashboard_ele_id + "'>"
						+ "<div>"
						/*+ "<h5>" + item.name + "</h5>"*/
						+ "<div class='chart_item' id='"+item.item_model_id+item.dashboard_ele_id+"' item_model_id='" + item.item_model_id + "' item_type='" + item.item_type + "' dashboard_ele_id='" + item.dashboard_ele_id + "'></div>"
						+ "</div>"
						+ "<div class='load'><span class='checkshow'><img src='../resource/img/load-mon.gif'><em>正在加载,请稍后。。。</em></span></div>"
						+ "<i class='fa fa-arrows-alt'></i>"
						+ "<b class='fa fa-pencil editbtn'></b>"
						+ "<b class='fa fa-trash deletbtn'></b>"
						+ "</div>";
				} else if (item.item_type == "2") {
					itemHtml += "<div class='item'  style='width:" + item.item_width + "px; height:" + item.item_height + "px' item_model_id='" + item.item_model_id + "' item_type='" + item.item_type + "' dashboard_id='" + item.dashboard_id + "' dashboard_ele_id='" + item.dashboard_ele_id + "' id='" + item.dashboard_ele_id + "'>"
						+ "<div>"
						+ "<span class='indexName'> </span>"
						+ "<span class='index_item' dashboard_ele_id='" + item.dashboard_ele_id + "' item_model_id='" + item.item_model_id + "'></span>"
						+ "</div>"
						+ "<i class='fa fa-arrows-alt'></i>"
						+ "<b class='fa fa-pencil editbtn'></b>"
						+ "<b class='fa fa-trash deletbtn'></b>"
						+ "</div>";
				} else if (item.item_type == "3") {
					itemHtml += "<div class='item'   style='width:" + item.item_width + "px; height:" + item.item_height + "px' item_model_id='" + item.item_model_id + "' item_type='" + item.item_type + "' dashboard_id='" + item.dashboard_id + "' dashboard_ele_id='" + item.dashboard_ele_id + "' id='" + item.dashboard_ele_id + "'>"
						+ "<div>"
						+ "<span class='tableName'></span>"
						+ "<div class='tableList' item_model_id='" + item.item_model_id + "' dashboard_ele_id='" + item.dashboard_ele_id + "'>"
						+ "<table class='table table-striped alltable'>"
						+ "<thead>"
						+ "<tr class='headth'>"
						+ "</tr>"
						+ "</thead>"
						+ "<tbody class='tbodytd'>"
						+ "</tbody>"
						+ "</table>"
						+ "</div>"
						+ "</div>"
						+ "<i class='fa fa-arrows-alt'></i>"
						+ "<b class='fa fa-pencil editbtn'></b>"
						+ "<b class='fa fa-trash deletbtn'></b>"
						+ "</div>";
				}
			});

		}

		$("#pannelbox").append(itemHtml);

		showItemPannel();//加载图表数据
		showIndexItemPannel(); //加载指标数据
		showTableItemPannel(); //加载表格
		//getTableData();//加载表格
		columnArrangeable(); //拖拽

	});

}

//添加页面面板元素
function addPageEle() {

	dashboard_id = $("#dashboardId").text();
	console.log("dashboard_id="+dashboard_id);
	var result = {};
	var currentTime = getNowFormatDate();
	if (pannelType == "") {
		alert("请选择图表类型");
		return false;
	}
	var pannelModel = $("input[name='pannelModel']:checked").val();
	if (pannelModel == null || pannelModel == "") {
		alert("请选择模型");
		return false;
	}
	var item_width = "";
	var item_height = "";
	if (pannelType == "1") {
		item_width = 620;
		item_height = 330;
	} else if (pannelType == "2") {
		item_width = 620;
		item_height = 330;
	} else {
		item_width = 620;
		item_height = 230;
	}
	var proJson = {
		"item_type" : pannelType,
		"item_width" : item_width,
		"item_height" : item_height,
		"item_model_id" : pannelModel,
		"dashboard_id":dashboard_id,
		"item_orderby" : ""
	} ;
	var param = {"param":JSON.stringify(proJson)}
	
	ajaxSendCloud('/bdp-web/dashboardPage/addDashboardEle', param ,function(data) {
		result = data;
		assemblyPage(data.results[0]);

	});
	return result;
}


//新建弹窗页面面板元素		
$(document).on("click",".addshowbox",function(){
	clearShowboxCache();
	var main = $("#showbox");
	layer.open({
		title : '新建图表',
		closeBtn : 2,
		type : 1,
		area : [ 'auto', 'auto' ],
		shadeClass : true,
		content : main,
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		closeBtn : 1,
		yes : function(index, layero) {

			var result = addPageEle(); //添加页面元素
			if (result.ret_code == 0) {
				alert("添加成功");
				layer.close(index); //如果设定了yes回调，需进行手工关闭	
				pannelType = ""; //图标类型。全局变量恢复到初始状态
				$("#initAddShow").remove();
				columnArrangeable(); //拖拽功能
				$("input[name='pannelModel']").prop("checked", false);
				$(".typeChart li").removeClass("current");
			} else {
				if (result.ret_code == "" || result.ret_code == "undefined" || result.ret_message == null || result.ret_message == "undefined") {
					alert("添加失败");
				} else {
					alert(result.msg);
				}
			}


		},
		btn2 : function(index, layero) {
			clearShowboxCache();
			$(".attrshow").hide();
		},
		cancel : function(index, layero) {
			layer.close(index);
			clearShowboxCache();
		}
	});
})

//增加目录
$("#addCatalog").on("click", function() {
	var parent_id="";
	var main = $("#addcatalog");
	layer.open({
		title : '增加目录',
		closeBtn : 2,
		type : 1,
		area : [ '400px', '200px' ],
		shadeClass : true,
		content : main,
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		closeBtn : 1,
		success : function(layero, index) {
			// return;
		},
		yes : function(index, layero) {
			var dashboard_catalog_name = $("#dashboard_catalog_name").val();
			if (dashboard_catalog_name == "" || dashboard_catalog_name == null) {
				alert("请输入目录名称");
				return false;
			}
			var proJson ={
				"dashboard_catalog_name" : dashboard_catalog_name,
				"parent_id":parent_id
			};
			
			var param = {"param" : JSON.stringify(proJson)};
			ajaxSendCloud('/bdp-web/dashboardCatalog/add',param,
				function(data) {
					if(data.ret_code == 0){
						alert("增加成功")
						getMenu();
						layer.close(index); //如果设定了yes回调，需进行手工关闭
						$("#dashboard_catalog_name").val("");
					}else{
						alert("增加失败")
					}
				
			});


		},
		btn2 : function(index, layero) {
			//按钮【取消】的回调
			$("#dashboard_catalog_name").val("");
		},
		cancel : function(index) {
			//【右上角关闭】的回调
			$("#dashboard_catalog_name").val("");
		}
	});
});


//增加页面
$(document).on("click",".addDash",function() {
	var dashboard_catalog_id = $(this).parent("h5").attr("dashboard_catalog_id");
	var main = $("#addcon");
	layer.open({
		title : '增加页面',
		closeBtn : 2,
		type : 1,
		area : [ '400px', '200px' ],
		shadeClass : true,
		content : main,
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		closeBtn : 1,
		success : function(layero, index) {
			// return;
		},
		yes : function(index, layero) {
			var dashboard_name = $("#dashboard_name").val();
			if (dashboard_name == "" || dashboard_name == null) {
				alert("请输入页面名称");
				return false;
			}
			var proJson ={
				"dashboard_name" : dashboard_name,
				"dashboard_catalog_id":dashboard_catalog_id,
				"dashboardEles" : []
			};
			
			var param = {"param" : JSON.stringify(proJson)};
			ajaxSendCloud('/bdp-web/dashboardPage/addDashboardPage',param,
				function(data) {
				 	console.log(JSON.stringify(data))
					if(data.ret_code == 0){
						alert("增加成功")
						var dashboardPage = data.dashboardPage;
						getPannelPageEle(dashboardPage.dashboard_id);//加载新页面的面板
						//重置页面基础数据
						$("#dashboardName").text(dashboardPage.dashboard_name);
						$("#dashboardId").text(dashboardPage.dashboard_id);
						$("#dashboardCode").text(dashboardPage.dashboard_code);
						//刷新左边菜单
						getMenu();
						//该页面列表高亮
						$('#'+dashboardPage.dashboard_id).addClass('current');
						layer.close(index); //如果设定了yes回调，需进行手工关闭
						$("#dashboard_name").val("");
					}else{
						alert("增加失败")
					}
				
			});


		},
		btn2 : function(index, layero) {
			//按钮【取消】的回调
			$("#dashboard_name").val("");
		},
		cancel : function(index) {
			//【右上角关闭】的回调
			$("#dashboard_name").val("");
		}
	});
});

//保存修改后的页面板元素大小及顺序（排版保存）
$("#saveEle").on("click", function() {
	var item = $("div.item");
	if (item.length == 0) {
		alert("没有数据更新，不需要保存");
		return false;
	}
	var elesArr = [];
	for (i = 0, len = item.length; i < len; i++) {
		var arr = {};
		arr["dashboard_ele_id"] = $(item[i]).attr("dashboard_ele_id");
		arr["item_type"] = $(item[i]).attr("item_type");
		arr["item_model_id"] = $(item[i]).attr("item_model_id");
		arr["item_width"] = $(item[i]).width();
		arr["item_height"] = $(item[i]).height();
		arr["item_orderby"] = i + 1 + "";
		elesArr.push(arr);

	}
	
	var param = {
		"dashboard_code":$("#dashboardCode").text(),
		"dashboard_name":$("#dashboardName").text(),
		"dashboard_id":$("#dashboardId").text(),
		"dashboardEles" : elesArr
	};
	ajaxSendCloud('/bdp-web/dashboardPage/editDashboardPage',{"param":JSON.stringify(param)}, function(data) {
		
		if (data.ret_code == 0) {
			alert("保存成功");
			getPannelPageEle($("#dashboardId").text());//重新加载页面元素
		}else{
			alert("保存失败");
		}

	});

});


//修改面板类型及模型
$(document).on("click",".editbtn",function(){
	
	editModelId = $(this).parent("div").attr("item_model_id");
	editPageType = $(this).parent("div").attr("item_type");
	var dashboard_ele_id = $(this).parent("div").attr("dashboard_ele_id");
	$("#typeChart li").each(function(){//回显图标类型
		if($(this).attr("value") == editPageType){
			$(this).addClass("current");
		}else{
			$(this).removeClass("current");
		}
		//根据图标类型显示模板列表
		if(editPageType == "1"){
			getChartModelList();
		}else if(editPageType == "2"){
			getIndexModelList();
		}else if(editPageType == "3"){
			getTableModelList();
		}
	});
	modelTypeCurrent(editModelId);
	var main = $("#showbox");
	layer.open({
		title : '修改图表',
		closeBtn : 2,
		type : 1,
		area : [ 'auto', 'auto' ],
		shadeClass : true,
		content : main,
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		closeBtn : 1,
		yes : function(index, layero) {
			
			if (pannelType == "" ) {
				if(editPageType == ""){
					alert("请选择图表类型");
					return false;
				}else{
					pannelType = editPageType;
				}
			}
			var pannelModel = $("input[name='pannelModel']:checked").val();
			if (pannelModel == null || pannelModel == "") {
				alert("请选择模型");
				return false;
			}
			var elesJson =  {
				"dashboard_ele_id":dashboard_ele_id,
				"item_type" : pannelType,
				"item_model_id" : pannelModel,
				"item_width":$(this).parent("div").width(),
				"item_height":$(this).parent("div").height()
			};
			var param = {
				"param" : JSON.stringify(elesJson)
			};
			ajaxSendCloud('/bdp-web/dashboardPage/editDashboardPageEle', param, function(data) {
				
				if(data.ret_code == 0){
					alert("修改成功");
					layer.close(index); //如果设定了yes回调，需进行手工关闭	
					//更新报表名称；
					//$("div[dashboard_ele_id='"+dashboard_ele_id+"']").attr("name",itemName);
					clearEditCache();//清编辑全局信息
					//重新加载改报表，暂时不做	
					showItemPannelByID(pannelModel,dashboard_ele_id,pannelType);					
					
				}else{
					alert("修改失败");
				}
				

			});


		},
		btn2 : function(index, layero) {
			$("input[name='pannelModel']").prop("checked", false);
			$(".typeChart li").removeClass("current");
			$(".attrshow").hide();
			clearEditCache();//清编辑全局信息
		},
		cancel : function(index, layero) {
			layer.close(index);
			$("input[name='pannelModel']").prop("checked", false);
			$(".typeChart li").removeClass("current");
			clearEditCache();//清编辑全局信息
		}
	});
})

//删除面板
$(document).on("click",".deletbtn",function(){
	var dashboard_ele_id = $(this).parent("div").attr("dashboard_ele_id");
	var elesJson = {"dashboard_ele_id":dashboard_ele_id};
	ajaxSendCloud('/bdp-web/dashboardPage/delDashboardPageEle', elesJson, function(data) {
		
		if(data.ret_code == 0 || data.code == 0){
			alert("删除成功")
			$("div[dashboard_ele_id='"+dashboard_ele_id+"']").remove();
			if($("#pannelbox > div").length <=0){
				$("#pannelbox").html("<div class='pannel-addbox addshowbox' id='initAddShow' ><span><em><img src='../resource/img/panneladdico.png'></em><b>新增图表</b></span></div>");
			}
		}else{
			alert("删除失败")
		}
		

	});
})

//页面删除
$(document).on("click","#nav-left li p>em.delDashboard",function(e){
	e.stopPropagation();
	var dashboard_id =  $(this).parent("p").attr("dashboard_id");
	var param = {"dashboard_id":dashboard_id};
	ajaxSendCloud('/bdp-web/dashboardPage/delDashboardPage', param, function(data) {
		if(data.ret_code == 0 || data.code == 0){
			alert("删除成功");
			dashboardClear();
			getMenu();
			
		}else{
			alert("删除失败");
		}
		
		

	});
})


//页面修改
$(document).on("click","#nav-left li p>b.editDashboard", function() {
	var dashboard_catalog_id = $(this).parent("p").attr("dashboard_catalog_id");
	var dashboard_id = $(this).parent("p").attr("dashboard_id");
	var dashboard_code = $(this).parent("p").attr("dashboard_code");
	var dashboard_name = $(this).parent("p").attr("dashboard_name");
	$("#dashboard_name").val(dashboard_name);
	var main = $("#addcon");
	layer.open({
		title : '修改页面',
		closeBtn : 2,
		type : 1,
		area : [ '400px', '200px' ],
		shadeClass : true,
		content : main,
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		closeBtn : 1,
		success : function(layero, index) {
			// return;
		},
		yes : function(index, layero) {
			var dashboard_name = $("#dashboard_name").val();
			if (dashboard_name == "" || dashboard_name == null) {
				alert("请输入页面名称");
				return false;
			}
			var proJson ={
				"dashboard_name" : dashboard_name,
				"dashboard_id":dashboard_id,
				"dashboard_code":dashboard_code,
				"dashboard_catalog_id":dashboard_catalog_id,
				"dashboardEles" : []
			};
			
			var param = {"param" : JSON.stringify(proJson)};
			ajaxSendCloud('/bdp-web/dashboardPage/editDashboardPage',param,
				function(data) {
					if(data.ret_code == 0){
						alert("修改成功");
						getMenu();
						layer.close(index); //如果设定了yes回调，需进行手工关闭
						$("#dashboard_name").val("");	
						$("#dashboardName").text(dashboard_name);
						
					}else{
						alert("修改失败");
					}
				
			});


		},
		btn2 : function(index, layero) {
			//按钮【取消】的回调
			$("#dashboard_name").val("");
		},
		cancel : function(index) {
			//【右上角关闭】的回调
			$("#dashboard_name").val("");
		}
	});
});

//目录删除
$(document).on("click","#nav-left li h5>em.delCatalog",function(e){
	e.stopPropagation();
	var dashboard_catalog_id =  $(this).parent("h5").attr("dashboard_catalog_id");
	var param = {"dashboard_catalog_id":dashboard_catalog_id};
	ajaxSendCloud('/bdp-web/dashboardCatalog/delete', param, function(data) {
		
		if(data.ret_code == 0 || data.code == 0){
			alert("删除成功");
			dashboardClear();
			getMenu();
			
		}else{
			alert("删除失败");
		}
		

	});
})

//目录修改
$(document).on("click","#nav-left li h5>b.editCatalog", function() {
	var dashboard_catalog_id = $(this).parent("h5").attr("dashboard_catalog_id");
	var dashboard_catalog_code = $(this).parent("h5").attr("dashboard_catalog_code");
	var dashboard_catalog_name = $(this).parent("h5").attr("dashboard_catalog_name");
	$("#dashboard_catalog_name").val(dashboard_catalog_name);
	var main = $("#addcatalog");
	layer.open({
		title : '修改目录',
		closeBtn : 2,
		type : 1,
		area : [ '400px', '200px' ],
		shadeClass : true,
		content : main,
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		closeBtn : 1,
		success : function(layero, index) {
			// return;
		},
		yes : function(index, layero) {
			var currentTime = getNowFormatDate();
			var dashboard_catalog_name = $("#dashboard_catalog_name").val();
			if (dashboard_catalog_name == "" || dashboard_catalog_name == null) {
				alert("请输入目录名称");
				return false;
			}
			var proJson ={
				"dashboard_catalog_id" : dashboard_catalog_id,
				"dashboard_catalog_code":dashboard_catalog_code,
				"dashboard_catalog_name" : dashboard_catalog_name
			};
			
			var param = {"param" : JSON.stringify(proJson)};
			ajaxSendCloud('/bdp-web/dashboardCatalog/edit',param,
				function(data) {
					if(data.ret_code == 0){
						alert("修改成功")
						getMenu();
						layer.close(index); //如果设定了yes回调，需进行手工关闭
						$("#dashboard_catalog_name").val("");
					}else{
						alert("修改失败")
					}
				
			});


		},
		btn2 : function(index, layero) {
			//按钮【取消】的回调
			$("#dashboard_catalog_name").val("");
		},
		cancel : function(index) {
			//【右上角关闭】的回调
			$("#dashboard_catalog_name").val("");
		}
	});
});

//右边面板数据清除
function dashboardClear(){
	$("#pannelbox").html("");
	$("#dashboardName").text("");
	$("#dashboardId").text("");
	$("#dashboardCode").text("");
}


