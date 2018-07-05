
// 新增和修改计算字段
function calculateFieldMange(parentId, metadataId, type){
	
	if(metadataId != null &&　checkFieldRealtion(metadataId) == "no"){
		alert("该字段正在被使用，无法修改");
		return;
	}
	
	if(parentId == null || parentId == ""){
		alert("请选择一个统计对象");
		return;
	}
	
	var htmlUrl = null;
	if(metadataId == null){
		htmlUrl = "../index/index-addcalculatefield.html?parentId=" + parentId;
	}else{
		htmlUrl = "../index/index-addcalculatefield.html?parentId=" + parentId + "&metadataId=" + metadataId;
	}
	
	top.layer.open({
		title:'新字段',
		closeBtn:2,
		type:2,
		area:['550px','570px'],
		shadeClass:true,
		content:htmlUrl,
		btn:['确定','取消'],
		btnAlign:'c',
		closeBtn:1,
		success: function(layero, index){
		},
		yes: function(index, layero){
    		
    		//按钮【确定】的回调
    		var doc = $(layero).find("iframe")[0].contentWindow.document;
    		var parent = $(layero).find("iframe")[0].contentWindow;
    		var flag=false;
    		var fieldType = $(doc).find("#fieldType").find("em").attr("value");
    		var fieldName = $(doc).find("#fieldName").val();
    		var fieldCode = "caclulate";
    		var sqlCode = $(doc).find("#text-add").val();
    		
			if(fieldName == null || fieldName == ""){
    			alert("字段名称不能为空");
    			return;
    		}
			
    		if(typeof(fieldType)=="undefined" || fieldType == "0"){
    			alert("字段类型不能为空");
    			return;
    		}
    		fieldType = "#" + fieldType;
    		if(sqlCode == null || sqlCode == ""){
    			alert("字段函数不能为空");
    			return;
    		}
    		
    		if(fieldName.trim().length == 0){
    			alert("字段名称不能全部为空格");
    			return;
    		}
    		
    		if(sqlCode.trim().length == 0){
    			alert("字段函数不能全部为空格");
    			return;
    		}
    		
    		// 判断字段名称是否存在
    		var fieldFlag = true;
    		var checkParam = {"parentId":parentId, "metadataId":metadataId, "fieldName":fieldName.trim(), "flag":"checkName"};
    		ajaxSend("/bdp-web/calculateField/checkCalculateFieldNameAction",{"param" : JSON.stringify(checkParam)},function success(data){
    			var proList = data.proList;
    			if(proList != null && proList.length > 0){
    				fieldFlag = false;
    			}
    		});
    		
    		if(fieldFlag == false){
    			alert("字段名称已存在");
    			return;
    		}
    		// 判断sqlcode选中的字段是否存在
    		var fieldCheckName = null;
    		var params = {"flag":"checkSqlCode", "metadataId":parentId, "sqlCode":sqlCode};
			ajaxSend("/bdp-web/calculateField/checkCalculateFieldSqlCodeAction",{"param" : JSON.stringify(params)},function success(data){
				fieldCheckName = data.fieldName;
    			if(fieldCheckName != null){
    				fieldFlag = false;
    			}
			});
			
			if(fieldFlag == false){
    			alert("字段" + fieldCheckName + "不存在");
    			return;
    		}
    		
			// 保存数据
    		var params = {"flag":"save", "metadataId":metadataId, "sqlCode":sqlCode.trim(), "sqlDescribe":sqlCode.trim(), 
    		"fieldName":fieldName.trim(), "fieldCode":fieldCode, "fieldType":fieldType, "fieldFlag":"calculateField"};
    		if(metadataId == null){
    			params.flag = "save";
    			params.metadataId = parentId;
    			ajaxSend("/bdp-web/calculateField/saveCalculateFieldAction",{"param" : JSON.stringify(params)},function success(data){});
    		}else{
    			params.flag = "edit";
    			params.metadataId = metadataId;
    			ajaxSend("/bdp-web/calculateField/editCalculateFieldAction",{"param" : JSON.stringify(params)},function success(data){});
    		}
			
			// 刷新属性菜单
			if(type=="flexibleQuery"){
                //更新左侧属性
            	businessObjPro(parentId);
            	//清空输出属性
            	$("#queryAttr01").empty();
            	$("#headList").empty();
            }else if(type=="Echart"){
    		      //更新左侧属性
    		  	  var params = {"metadataId":parentId,"flag":"initMenu"};
				 // ajaxSend("/pms-web/ajax/ajaxQueryIndexAction",params,initMenu);
    		  	 ajaxSend("/bdp-web/metaqueryChartTemplateController/queryStatisticalObject",params,initMenu);
				  $("#nav-left").empty();
				  $("#search").val("");
				  $("#pro_search").val("");
				  $(".attrshow").hide();
				  $(".filesShow").hide();	
            }else {
            	var params = {"flag":"pro_search", "metadataId":parentId, "name":null};
            	ajaxSend("/bdp-web/index/queryProSearchAction",{"param" : JSON.stringify(params)},initIndexMenu);
            }
			
    		top.layer.close(index); //如果设定了yes回调，需进行手工关闭
	  	},
	  	btn2: function(index, layero){
    		//按钮【取消】的回调
		},
	  	cancel: function(index){ 
	  		//【右上角关闭】的回调
	  	}
	});
	
}

// 删除计算字段
function removeCalculateField(parentId, metadataId, type){
	
	if(checkFieldRealtion(metadataId) == "no"){
		alert("该字段正在被使用，无法删除");
		return;
	}
	
	var params = {"metadataId":metadataId, "flag":"remove"};
	ajaxSend("/bdp-web/calculateField/removeCalculateFieldAction",{"param" : JSON.stringify(params)},function success(data){
		alert("删除成功");
	});
	
	if(type=="flexibleQuery"){
        //更新左侧属性
    	businessObjPro(parentId);
    	//清空输出属性
    	$("#queryAttr01").empty();
    	$("#headList").empty();
    }else if(type=="Echart"){
	      //更新左侧属性
	  	  var params = {"metadataId":parentId,"flag":"initMenu"};
		  //ajaxSend("/pms-web/ajax/ajaxQueryIndexAction",params,initMenu);
	  	 ajaxSend("/bdp-web/metaqueryChartTemplateController/queryStatisticalObject",params,initMenu);
		  $("#nav-left").empty();
		  $("#search").val("");
		  $("#pro_search").val("");
		  $(".attrshow").hide();
		  $(".filesShow").hide();	
    }else {
    	var params = {"flag":"pro_search", "metadataId":parentId, "name":null};
    	ajaxSend("/bdp-web/index/queryProSearchAction",{"param" : JSON.stringify(params)},initIndexMenu);
    }
}

/**
 * 校验该字段是否被使用
 */
function checkFieldRealtion(metadataId){
	
	var params = {"metadataId": metadataId, "flag":"fieldRelation"};
	var result = null;
	ajaxSend("/bdp-web/calculateField/checkCalculateFieldRelationAction",{"param" : JSON.stringify(params)}, function success(data){
		
		if(data.relationList != null && data.relationList.length>0){
			result = "no";
		}else{
			result = "yes";
		}
	});
	
	return result;
}





