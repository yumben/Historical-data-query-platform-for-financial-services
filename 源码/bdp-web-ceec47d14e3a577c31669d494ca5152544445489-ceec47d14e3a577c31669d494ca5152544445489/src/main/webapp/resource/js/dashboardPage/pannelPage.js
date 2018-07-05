//拖拽功能
function columnArrangeable() {
	//拖拽div
	$('.item').arrangeable();
	//改变div大小
	var con = document.getElementById('pannelbox');
	var iList = con.getElementsByTagName('i');	
	
	//点击小圆点	
	for (var i = 0; i < iList.length; i++) {
		iList[i].onmousedown = function(ev) {
			var ev = ev || window.event;
			ev.stopPropagation();
			var parentElem = this.parentNode;
			var disX = ev.clientX;
			var disY = ev.clientY;
			var disW = parentElem.offsetWidth; // 获取拖拽前div的宽  
			var disH = parentElem.offsetHeight; // 获取拖拽前div的高 
			//console.log(disW);
			document.onmousemove = function(ev) {
				var ev = ev || window.event;
				ev.stopPropagation();
				var W = ev.clientX - disX + disW;
				var H = ev.clientY - disY + disH;
				if (W < 220) {
					W = 220;
				}
				if (W > 915) {
					W = 915;
				}
				if (H < 140) {
					H = 140;
				}
				if (H > 700) {
					H = 700;
				}
				parentElem.style.width = W + 'px'; // 拖拽后物体的宽  
				parentElem.style.height = H + 'px'; // 拖拽后物体的高 
							
			}
			document.onmouseup = function() {
				document.onmousemove = null;
				document.onmouseup = null;	
				showItemPannelByID(parentElem.getAttribute("item_model_id"),parentElem.getAttribute("dashboard_ele_id"),parentElem.getAttribute("item_type"))
				//浏览器大小改变时重置大小
				
			}						
		}		
	}

}




$(document).on("click","#nav-left01 h5>i",function(){
	var that = $(this);
	var parent = that.parent("h5");
	if(parent.siblings("ul").is(":hidden")){
		parent.addClass("current");
		parent.siblings("ul").slideDown();	
		parent.children("i").removeClass("glyphicon-plus").addClass("glyphicon-minus");		
	}else{
		parent.siblings("ul").slideUp();
		parent.children("i").removeClass("glyphicon-minus").addClass("glyphicon-plus");
		parent.removeClass("current");
	}
})	
$(document).on("click","#nav-left01 p",function(){
	if($(this).hasClass("current")){
		$(this).removeClass("current");
	}else{
		$("#nav-left a").removeClass("current");
		$(this).addClass("current").parent("li").siblings().children("a").removeClass("current");
	}
})
//图表类型选择
$(document).on("click", "#typeChart li", function() {
	$(this).addClass("current").siblings().removeClass("current");
	pannelType = $(this).val();
	if(pannelType == "1"){
		getChartModelList();
	}else if(pannelType == "2"){
		getIndexModelList();
	}else if(pannelType == "3"){
		getTableModelList();
	}
})

//获取模板列表
function getChartModelList() {
	$("#modelList").html("");
	var modelHtml = "";
	var params = {
			"curPage": 1,
			"pageSize": 999999,
			"conditions": [],
			
		};
	var param = {"param":JSON.stringify(params)};
	ajaxSend("/bdp-web/easyChart/selectTemplateList", param, function(data) {
		$("#medelList").html("");
		var results = data.results;
		$.each(results, function(i, obj) {
			modelHtml += "<li><p>"
				+ "	<label><input type='radio' name='pannelModel' value='" + obj.id + "'>"
				+ obj.chart_name + "</label>"
				+ "</p></li>"
		});
		$("#modelList").append(modelHtml);
	});
	modelTypeCurrent()
	
}
function getIndexModelList() {
	$("#modelList").html("");
	var modelHtml = "";
	
	ajaxSend("/bdp-web/easyTarget/selectTemplateList", {}, function(data) {
		$("#medelList").html("");
		var results = data.results;
		$.each(results, function(i, obj) {
			modelHtml += "<li><p>"
				+ "	<label><input type='radio' name='pannelModel' value='" + obj.target_template_id + "'>"
				+ obj.target_name + "</label>"
				+ "</p></li>"
		});
		$("#modelList").append(modelHtml);
	});
	
	if(editPageType == "2"){
		modelTypeCurrent()
	}
	
}
function getTableModelList() {
	$("#modelList").html("");
	var modelHtml ="<li><p><label><input type='radio' name='pannelModel' value='1'> 表格1</label></p></li>"
				+"<li><p><label><input type='radio' name='pannelModel' value='2'> 表格2</label></p></li>";
	$("#modelList").append(modelHtml);
	if(editPageType == "3"){
		modelTypeCurrent()
	}
}

//加载图表
function showItemPannel() {
	$("div[class='chart_item']").each(function(i) {
		var elem = $(this);
		var id = $(this).attr("dashboard_ele_id");
		var echart_template_id = $(this).attr("item_model_id");
		var myChart1 = echarts.init(document.getElementById(echart_template_id+id));		
		ajaxSendload('/bdp-web/easyChart/tempQueryByTemplateId', {"echart_template_id":echart_template_id}, function(data) {
			$("#"+id).find(".load").hide();
			chartBack(data.type,myChart1,data.data)
	    	
	    })
	})
}



//根据Id重新加载，加载单个图标对象
function showItemPannelByID(modelId,id,pannelType) {
	if (modelId != "" && modelId != "null") {
		var div = $("div[id='"+id+"']");
		//var div = document.getElementById(id)
		console.log(div)
		div.attr("item_model_id",modelId);
		div.attr("item_type",pannelType);
		div.children("div").remove();
		var item = new Object();
		item["item_model_id"] = modelId;
		item["item_type"] = pannelType;
		item["dashboard_ele_id"] = id;
		var loadHtml = "<div class='load'><span class='checkshow'><img src='../resource/img/load-mon.gif'><em>正在加载,请稍后。。。</em></span></div>";
		div.append(loadHtml);
		var myDiv = createHtml(item);
		div.append(myDiv);
		var subdiv = $("div[id='"+id+"']");
		if("1"==pannelType){
			
			var myChart1 = echarts.init(document.getElementById(modelId+id));		
			ajaxSendload('/bdp-web/easyChart/tempQueryByTemplateId', {"echart_template_id":modelId}, function(data) {
				$("#"+id).find(".load").hide();
				chartBack(data.type,myChart1,data.data)
		    	
		    })
		}else if("2"==pannelType){
			var params = {
					"modelId" : modelId
				}
			ajaxSendloadpannel(subdiv,"/bdp-web/dashboardPage/queryDashboardIndexAction", params, function(data) {
				$("#"+id).find('.load').remove();
				if(data.value == null || data.value == "undefined" || data.value == ""){
					$("#"+id).find('.index_item').text("无");
				}else{
					$("#"+id).find('.indexName').text(data.name);
					$("#"+id).find('.index_item').text(data.value);
				}
				
				
			});
		}else if("3"==pannelType){
			var params = {
					"modelId" : modelId
				}
			var th = subdiv.find(".headth");
			var td = subdiv.find(".tbodytd");
			ajaxSendloadpannel(subdiv,"/bdp-web/dashboardPage/queryDashboardTableAction", params,function(data){
				$("#"+id).find('.load').remove();
				$("#"+id).find('.tableName').text(data.name);
				var json = JSON.parse(data.value);
				$.each(json,function(index,name){
					var thTitle = name.title;
					var tdCon = name.content;
					var thThml = "";				
					$.each(thTitle,function(connt,title){
						console.log("title="+title)
						thThml += "<th>"+title.lable+"</th>"
					});
					$(th).html(thThml);				
					$.each(tdCon,function(conntTd,tdtitle){
						console.log("tdtitle="+tdtitle)
						console.log(tdCon[conntTd]);
						var tdThml = "<tr>";
						 for(var key in tdtitle){ 
							 tdThml += "<td>"+tdtitle[key]+"</td>"
				            } 
						 tdThml += "</tr>";
						 $(td).append(tdThml);
					});
				})
			});
		}
		
		
	}


}



//加载指标卡信息
function showIndexItemPannel() {
	$("span[class='index_item']").each(function(i) {
		
		var id = $(this).attr("dashboard_ele_id");		
		var id_temp = $(this).attr("item_model_id");
		if (id_temp != "" && id_temp != "null") {
			var params = {
				"modelId" : id_temp
			}
			ajaxSendload("/bdp-web/dashboardPage/queryDashboardIndexAction", params, function(data) {
				
				if(data.value == null || data.value == "undefined" || data.value == ""){
					$("#"+id).find('.index_item').text("无");
				}else{
					$("#"+id).find('.indexName').text(data.name);
					$("#"+id).find('.index_item').text(data.value);
				}
			});
		}
	})
}

//加载表格信息
function showTableItemPannel() {
	
	$("div[class='tableList']").each(function(i) {
		var th = $(this).find(".headth");
		var td = $(this).find(".tbodytd");
		var elem = $(this);
		var id = $(this).attr("dashboard_ele_id");
		var id_temp = $(this).attr("item_model_id");
		if (id_temp != "" && id_temp != "null") {
			var params = {
				"modelId" : id_temp
			}
			ajaxSendloadpannel(elem,"/bdp-web/dashboardPage/queryDashboardTableAction", params,function(data){
				var json = JSON.parse(data.value);
				$("#"+id).find('.tableName').text(data.name);
				$.each(json,function(index,name){
					var thTitle = name.title;
					var tdCon = name.content;
					var thThml = "";				
					$.each(thTitle,function(connt,title){
						thThml += "<th>"+title.lable+"</th>"
					});
					$(th).html(thThml);				
					$.each(tdCon,function(conntTd,tdtitle){
						var tdThml = "<tr>";
						 for(var key in tdtitle){ 
							 tdThml += "<td>"+tdtitle[key]+"</td>"
				            } 
						 tdThml += "</tr>";
						 $(td).append(tdThml);
					});
				})
			});
		}
	})
	
}

//回显模板
function modelTypeCurrent(editModelId){
	
	$("input[name='pannelModel']").each(function(){//回显模板类型
		if($(this).attr("value") == editModelId){
			$(this).prop("checked",true)
		}
	});
}

//清编辑数据
function clearEditCache(){
	var editPageType = "";
	var editModelId = ""; 
}

//加载表格
function getTableData(){	
	
	$("div[class='tableList']").each(function(i) {
		var th = $(this).find(".headth");
		var td = $(this).find(".tbodytd");
		ajaxSendload("/bdp-web/pannelController/ajaxQueryPannelTableAction",function(data){
			$.each(data,function(index,name){
				var thTitle = name.title;
				var tdCon = name.content;
				var thThml = "";				
				$.each(thTitle,function(connt,title){
					thThml += "<th>"+title.lable+"</th>"
				});
				$(th).html(thThml);				
				$.each(tdCon,function(conntTd,tdtitle){
					console.log(tdCon[conntTd]);
					var tdThml = "<tr>";
					 for(var key in tdtitle){ 
						 tdThml += "<td>"+tdtitle[key]+"</td>"
			            } 
					 tdThml += "</tr>";
					 $(td).append(tdThml);
				});
			})
		})
	})
}

//组装页面
function assemblyPage(item){
	console.log(JSON.stringify(item))
	var itemHtml = "<div class='item'  style='width:" + item.item_width + "px; height:" + item.item_height + "px'  item_model_id='" + item.item_model_id + "' item_type='" + item.item_type + "' dashboard_ele_id='" + item.dashboard_ele_id + "' id='" + item.dashboard_ele_id + "'>";
	itemHtml += createHtml(item);
	itemHtml +=  "<div class='load'><span class='checkshow'><img src='../resource/img/load-mon.gif'><em>正在加载,请稍后。。。</em></span></div>"
				+ "<i class='fa fa-arrows-alt'></i>"
				+ "<b class='fa fa-pencil editbtn'></b>"
				+ "<b class='fa fa-trash deletbtn'></b>"
				+ "</div>";
	$("#pannelbox").append(itemHtml);
	
	showItemPannelByID(item.item_model_id,item.dashboard_ele_id,item.item_type);
		
}

//组装页面
function createHtml(item){
	
	var itemHtml = "";
	if (item.item_type == "1") {
		itemHtml += "<div>"
			+ "<div class='chart_item' id='"+item.item_model_id+item.dashboard_ele_id+"'  item_model_id='" + item.item_model_id + "'  item_type='" + item.item_type + "' dashboard_ele_id='" + item.dashboard_ele_id + "'></div>"
			+ "</div>"
	} else if (item.item_type == "2") {
		itemHtml += "<div>"
			+ "<span class='indexName'> </span>"
			+ "<span class='index_item' dashboard_ele_id='" + item.dashboard_ele_id + "' item_type='" + item.item_type + "' item_model_id='" + item.item_model_id + "' ></span>"
			+ "</div>"
	} else if (item.item_type == "3") {
		itemHtml += "<div>"
				+ "<span class='tableName'></span>"
				+ "<div class='tableList' item_model_id='" + item.item_model_id + "' item_type='" + item.item_type + "' dashboard_ele_id='" + item.dashboard_ele_id + "' >"
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
	}
	return itemHtml;
}

function clearShowboxCache(){
	$("input[name='pannelModel']").prop("checked", false);
	$(".typeChart li").removeClass("current");
	$("#modelList").html("");
}
