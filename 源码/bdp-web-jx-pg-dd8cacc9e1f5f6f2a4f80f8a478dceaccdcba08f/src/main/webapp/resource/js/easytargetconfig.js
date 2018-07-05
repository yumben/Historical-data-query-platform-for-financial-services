$().ready(function () {
    ajaxSend('/bdp-web/calculation/selectTree', {}, function (data) {
        createRightTree(data);
        $('.rightNav li>ul').hide()
    })
    var templateId = GetQueryString("id");
    var temp = {
        'target_template_id': templateId,
    }
    var params = { "param": JSON.stringify(temp) };
    //回显数据
    ajaxSend('/bdp-web/targetNode/select', params, function (data) {
        visitedUpdate(data);
        // console.log(data, '/bdp-web/targetNode/select');
    })

    var param = {
        'id': templateId,
    }
    //ul.leftNav数据	
    ajaxSend('/bdp-web/easyTarget/selectDataObj', param, function (data) {
        createLeftTree(data);
    })
})
function createRightTree(data) {
    rightTreeData = data;
    var template = '';
    template += '<ul class="rightNav dataNav">';
    $(data.results).each(function (index1, el1) {
        var titleName = el1.label;
        var titleCode = el1.code;
        var titleType = el1.m_type;
        template += '<li code="' + titleCode + '" m_type="' + titleType + '"><h5><i class="fa fa-plus-square"></i>' + titleName + '</h5><ul>';// + '</li>';
        $(el1.childrens).each(function (index2, el2) {
            var childCode = el2.code,
                childName = el2.label,
                childValue = el2.value,
                childType = el2.m_type;
            template += '<li code="' + childCode + '" id="' + childValue + '" m_type="' + childType + '" draggable="true">' + childName + '</li>';
        })
        template += '</ul></li>';
    })
    template += '</ul>';
    $('.rightConfigContent').append(template);
}
function createLeftTree(data) {
    leftTreeData = data;
    var tableMenu = data.list;
    if (tableMenu != '' && tableMenu != null && tableMenu != 'null' && tableMenu != undefined && tableMenu != 'undefined') {
        var showBox = '';
        showBox += '<ul class="leftNav dataNav">';
        $.each(tableMenu, function (index, el) {
            var child = el.columns;
            var metadataId = el.metadata_id;
            var metadataName = el.metadata_name;
            if (child) {
                if (child == '' && child == null) {
                    showBox += '<li fieldName="' + metadataName + '" id="' + metadataId + '">' + metadataName + '</li>';
                } else {
                    showBox += '<li fieldName="' + metadataName + '"><h5><i class="fa fa-minus-square"></i> ' + metadataName + '</h5><ul>';
                    $.each(child, function (indexChild, elChilid) {
                        showBox += '<li fieldName="' + elChilid.metadata_name + '" id="' + elChilid.metadata_id + '" draggable="true">' + elChilid.metadata_name + '</li>';
                    })
                    showBox += '</ul></li>';
                }
            }
        })
        showBox += '</ul></div>';
        $('.leftConfigContent').append(showBox);
    }
}

/* ################################################################################################### */
/* 全局变量保存区 */
var algorithmData = [];//保存算法数据的数组
var allConfig = {
	"target_template_id": (window.location.search).slice(4),
	"exec_order": 1,
	"target_node_code": "",
	"target_node_name": "",
	"calculation_id": "",
	"target_node_type": null,
	"node_left": null,
	"node_top": null,
	"node_width": null,
	"node_height": null,
	"targetNodeOut": [],
	"targetNodeIn": []
};
var evnTarget;//点击dialog内的编辑按钮
/* ################################################################################################### */

//拖拽
document.addEventListener("dragstart", function (e) {
    var evn = e || window.event;
    evn.stopPropagation();

    evn.dataTransfer.setData("type", $($(evn.target)[0]).parent().parent().parent()[0].className);
    //left
    evn.dataTransfer.setData("leftAttr", JSON.stringify({
        "id": $(evn.target).attr("id"),
        "metadata_name": $(evn.target).text()
    }));
    //right
    evn.dataTransfer.setData("rightAttr", JSON.stringify({
        "code": $(evn.target).attr("code"),
        "label": $(evn.target).text(),
        "value": $(evn.target).attr("id"),
        "m_type": $(evn.target).attr('m_type')
    }));

});
document.addEventListener("dragover", function (e) {
    var evn = e || window.event;
    evn.preventDefault();
});
document.addEventListener("dragend", function (e) {
    var evn = e || window.event;
});
$(".rightConfigContentSubs")[0].addEventListener("dragenter", function (e) {
    var evn = e || window.event;
    evn.stopPropagation();
});
$(".rightConfigContentSubs")[0].addEventListener("drop", function (e) {
    var evn = e || window.event;
    evn.preventDefault();
    evn.stopPropagation();
    
    var type = evn.dataTransfer.getData("type");//拖拽元素的class
    var leftAttr = JSON.parse(evn.dataTransfer.getData("leftAttr")), rightAttr = JSON.parse(evn.dataTransfer.getData("rightAttr"));
    var newElement;
    //1.判断是否可以插入
    if (evn.target.className != 'rightConfigContentSubs') {
        alert('请放在指定位置空白处！');
        return false;
    }
    if(!judgeIsRepeat(rightAttr,leftAttr)){
        alert('该组件已存在！');
        return false;
    }
    //2.插入div
    if (type == "rightNav dataNav") {
        //获取算法的相关数据,保存在 algorithmData
        var param = {
            "id": rightAttr.value
        }
        var optionData;
        ajaxSend('/bdp-web/calculation/get', param, function (data) {
            optionData = data;
        });
        //将获得的算法对应的数据保存
        algorithmData.push(optionData);
		newElement = createNewDiv(optionData,rightAttr);
        //添加div
        $(evn.target).append(newElement);
		saveDialogInCreate(optionData,rightAttr.code);
		
        var dialogId = "#dialog-message-" + rightAttr.code;
		saveData(dialogId);
    } else {
        newElement = `<div id ="` + leftAttr.id + `" class="draggable ui-widget-content leftContentChild"><i class="glyphicon glyphicon-th-large"></i>` + leftAttr.metadata_name + `<i class="glyphicon glyphicon-remove turnOffI"></i></div>`;
        //添加div
    $(evn.target).append(newElement);     
    }

    //设为可自由拖拽
    $(".rightConfigContentSubs>div:last").draggable({ containment: "#containment-wrapper", scroll: false });
    
})

//查找函数，查找algorithmData对应的数据
function findAlgorithmData(id){
	var attr = ""
    algorithmData.forEach(function(el,index){
        if(el.calculation_id == id){
            attr =  {"el":el,"index":index};
        }
    });
	return attr;
}

//当点击配置按钮时，打开弹窗
$(document).on('click', '.turnOptionI', function (e) {
    var id = $($(e.target).parent()).attr('id');
    var ddate = findAlgorithmData(id);
    var code = ddate.el.calculation_code;
    var dialogId = "#dialog-message-" + code;

	$(dialogId).dialog({
		autoOpen: false,
		height: 500,
		// 模态开启  
		modal: true,
		// 是否可拖拽  
		draggable: true,
		// 最小宽度  
		minWidth: 800,
		buttons: {
			"保存": function () {
				saveData(dialogId);
				$(this).dialog("close");
			},
			"取消": function () {
				$(this).dialog("close");
			}
		}
	});
	$(dialogId).dialog("open");
	// 清除自带的样式
	$(dialogId).attr("style", "");
})

//算法关闭
$(document).on('click', '.turnOffI', function (e) {
    var id = $($(e.target).parent()).attr('id');
    var ddate = findAlgorithmData(id);    
    var code = ddate.el.calculation_code;
    
    //删除该算法保存的数据
    algorithmData.splice(ddate.index,1);
    //删除该算法对应的弹出框
    var dialogId = "#dialog-message-" + code;
    $(dialogId).remove();
    //删除算法div
    $($(e.target).parent()).remove();

    allConfig = {
		"target_template_id": (window.location.search).slice(4),
		"exec_order": 1,
		"target_node_code": "",
		"target_node_name": "",
		"calculation_id": "",
		"target_node_type": null,
		"node_left": null,
		"node_top": null,
		"node_width": null,
		"node_height": null,
		"targetNodeOut": [],
		"targetNodeIn": []
	}
})

//点击展开，收缩
$(document).on('click', '.dataNav li > h5', function () {
	var sibling = $(this).siblings('ul');
	var paren = $(this).parent('li').siblings('li').children('ul');
	paren.hide();
	$(this).parent('li').siblings('li').children('h5').find('i').addClass('fa-plus-square').removeClass('fa-minus-square');
	if (sibling.is(':hidden')) {
		sibling.show();
		$(this).children('i').addClass('fa-minus-square').removeClass('fa-plus-square');
	} else {
		sibling.hide();
		$(this).children('i').addClass('fa-plus-square').removeClass('fa-minus-square');
	}
})

//创建新div函数，data：算法数据；obj：属性值对象
function createNewDiv(data,obj) {
    var newElement;
    var ins = data.calculationParamIns;
    var outs = data.calculationParamOuts;
    newElement = `<div code="` + obj.code + `" id="` + obj.value + `" m_type="` + obj.m_type + `"class="draggable ui-widget-content rightContentChild" label="` + obj.label + `" style="position:relative;"><i class="glyphicon glyphicon-edit"></i>` + obj.label + `<i class="glyphicon glyphicon-cog turnOptionI"></i><i class="glyphicon glyphicon-remove turnOffI"></i><table class="table table-striped table-condensed table-bordered"><tr><td>key</td><td>数据类型</td></tr>`;//</table></div>
    ins.forEach(function (el, index) {
        newElement += `<tr><td>` + el.calculation_param_key + `</td><td>` + el.calculation_param_datatype + `</td></tr>`;
    })
//    outs.forEach(function (el, index) {
//        newElement += `<tr><td>` + el.calculation_param_key + `</td><td>` + el.calculation_param_datatype + `</td></tr>`;
//    })
    newElement += `</table></div>`;
    return newElement;
}

//判断是否可以拖拽，若有重复的则不能拖拽
function judgeIsRepeat(obj1,obj2){
    if ($('.rightConfigContentSubs>div').length != 0) {
		var temp = 0;
		$('.rightConfigContentSubs>div').each(function (index, el) {
			if (obj1.value == $(el).attr('id') || obj2.id == $(el).attr('id')) {
				temp++;
				alert('该组件已存在！')
			}
		})
		if (temp == 0) {
            return true
        }else{
            return false;
        }
	} else {
		return true
	}
}

//根据拖拽算法生成的div，保存相应的dialog模板
function saveDialogInCreate(data,code){
    var newDialogTemplate = `<table id="dialog-message-`+code+`" title="算法配置" class="table table-hover table-condensed configDialogTable" style="width:100%;display:none">`//</table>
    newDialogTemplate += `<thead>
                            <tr>
                                <th>key值</th>
                                <th>数据格式</th>
                                <th>默认值</th>
                                <th>编辑</th>
                            </tr>
                        </thead><tbody>`;//</tbody>
    data.calculationParamIns.forEach(function(el,index){
        newDialogTemplate += `<tr part="ins">
                                <td>`+ el.calculation_param_key + `</td>
                                <td>`+ el.calculation_param_datatype + `</td>
                                <td>`+ isNoemty(el.calculation_param_default_value) + `</td>
                                <td><button type="button" name="other" class="tableButton button_ins_` + index + `" value="` + isNoemty(el.calculation_param_default_value).split(/["']/).join("'") + `">...</button></td>
                            </tr>`
    });
//    data.calculationParamOuts.forEach(function(el,index){
//        newDialogTemplate += `<tr part="outs">
//                                <td>`+ el.calculation_param_key + `</td>
//                                <td>`+ el.calculation_param_datatype + `</td>
//                                <td>`+ isNoemty(el.calculation_param_default_value) + `</td>
//                                <td><button type="button" name="other" class="tableButton button_ins_` + index + `" value="` + isNoemty(el.calculation_param_default_value).split(/["']/).join('/') + `">...</button></td>
//                            </tr>`
//    });
    newDialogTemplate += '</tbody></table>'
    $("body").append(newDialogTemplate);
}

//打开dialog时，点击保存，保存输入的数据
function saveData(id){
    var optionDiv = $('.rightContentChild');
    //获取算法名
	allConfig.target_node_name = optionDiv.attr('label');
	allConfig.target_node_code = optionDiv.attr('code');
	//清空数据
	allConfig.targetNodeIn = [];
    allConfig.targetNodeOut = [];
    //读取每一行(tr)，获取数据
    var template = id+">tbody>tr";
	$(template).each(function (index1, el1) {
		var temp = {
			"parent_id": "",
			"param_order": null,
			"param_key": null,
			"datatype": null,
			"param_default_value": null
		};
		if ($(el1).attr("part") == "ins") {
			temp.targetNodeInProperty = [];
			$(el1.children).each(function (index2, el2) {
				if (index2 == 0) {
					temp.param_key = el2.innerText;
				} else if (index2 == 1) {
					temp.datatype = el2.innerText;
				} else if (index2 == 3) {
					temp.param_default_value = $(el2.children[0]).attr("value");
				}
			})
			allConfig.targetNodeIn.push(temp);
		} else if ($(el1).attr("part") == "outs") {
			temp.targetNodeOutProperty = [];
			$(el1.children).each(function (index2, el2) {
				if (index2 == 0) {
					temp.param_key = el2.innerText;
				} else if (index2 == 1) {
					temp.datatype = el2.innerText;
				} else if (index2 == 3) {
					temp.param_default_value = $(el2.children[0]).attr("value");
				}
			})
			allConfig.targetNodeOut.push(temp);
		}
	});
}

//输入框弹窗
$("#dialog-textarea").dialog({
	autoOpen: false,
	// width: 600,
	// 模态开启  
	modal: true,
	// 是否可拖拽  
	draggable: true,
	// 最小宽度  
	buttons: {
		"保存": function () {
			$(evnTarget).attr("value", $('#input-value')[0].value);
			$(evnTarget).parent().prev().text($('#input-value')[0].value);
			$(this).dialog("close");
		},
		"取消": function () {
			$(this).dialog("close");
		}
	},
});

$("#dialog-textarea").dialog({
	autoOpen: false,
	// width: 600,
	// 模态开启  
	modal: true,
	// 是否可拖拽  
	draggable: true,
	// 最小宽度  
	buttons: {
		"保存": function () {
			$(evnTarget).attr("value", $('#input-value')[0].value);
			$(evnTarget).parent().prev().text($('#input-value')[0].value);
			$(this).dialog("close");
		},
		"取消": function () {
			$(this).dialog("close");
		}
	},
});

//dialog里的table里的button
$(document).on('click', '.tableButton', function (event) {
	$('#input-value')[0].value = $(event.target).attr('value');
	$("#dialog-textarea").dialog("open");
	//获取当前点击的button
	evnTarget = event.target;
})

//返回按钮
$(document).on('click', '.returnLastPage', function (event) {
	window.location.href = '/bdp-web/easytarget/easytarget-index.html';
})

//保存按钮
$(document).on('click', '.saveConfig', function (event) {
	//如果有算法存在就保存，否则不保存
	if ($('.rightContentChild').length != 0) {
		var style = $($('.rightContentChild')[0]).attr('style').split(/[:;]/);
		allConfig.calculation_id = $($('.rightContentChild')[0]).attr('id');
		allConfig.node_left = parseInt(style[3]);
		allConfig.node_top = parseInt(style[5]);
		allConfig.node_width = $('.rightContentChild').width();
		allConfig.node_height = $('.rightContentChild').height();
		var params = { "param": JSON.stringify(allConfig) };
		ajaxSend('/bdp-web/targetNode/add', params, function (data) {

		})
		window.location.href = '/bdp-web/easytarget/easytarget-index.html';
	} else {
		//将保存信息置空
		allConfig = {
			"target_template_id": (window.location.search).slice(4),
			"exec_order": 1,
			"target_node_code": "",
			"target_node_name": "",
			"calculation_id": "",
			"target_node_type": null,
			"node_left": null,
			"node_top": null,
			"node_width": null,
			"node_height": null,
			"targetNodeOut": [],
			"targetNodeIn": []
		}
		var params = { "param": JSON.stringify(allConfig) };
		ajaxSend('/bdp-web/targetNode/add', params, function (data) {

		})
		window.location.href = '/bdp-web/easytarget/easytarget-index.html';
	}
})

//回显显示已配置的信息
function visitedUpdate(data) {
	var length = data.list.length;
	var list = data.list[length - 1];
	//allConfig = list;
	var targetLi, template = '';
	//如果有保存信息
	if (length != 0) {
		createDivInCallback(list);
	}
}

//
function createDivInCallback(data) {
	//在右边列表找到对应的算法，赋值属性
	var divAttr = [];
	var optionData;
	//获取该算法相应的信息
	var param = {
		"id": data.calculation_id
	};
	ajaxSend('/bdp-web/calculation/get', param, function (data2) {
		//全局变量,保存返回的该算法的相关信息
		optionData = data2;
    })
    //保存算法的数据
    giveValue(data,optionData);//保存默认值
    algorithmData.push(optionData);
	//添加div
	$('.rightNav li').each(function (index, el) {
		if ($(el).attr('id') == data.calculation_id) {
			divAttr = el.attributes;
		}
	})
	if (data.calculation_id) {
		var template = `<div code="` + data.target_node_code + `" id="` + data.calculation_id + `" m_type="` + divAttr[2].nodeValue + `"class="draggable ui-widget-content rightContentChild" style="position:relative;left:` + data.node_left + `px;top:` + data.node_top + `px;" label="` + data.target_node_name + `"><i class="glyphicon glyphicon-edit"></i>` + data.target_node_name + `<i class="glyphicon glyphicon-cog turnOptionI"></i><i class="glyphicon glyphicon-remove turnOffI"></i><table class="table table-striped table-condensed table-bordered"><tr><td>key</td><td>数据类型</td></tr>`;//</table></div>
		var ins = data.targetNodeIn;
		var outs = data.targetNodeOut;
		ins.forEach(function (el, index) {
			template += `<tr><td>` + el.param_key + `</td><td>` + el.datatype + `</td></tr>`;
		})
//		outs.forEach(function (el, index) {
//			template += `<tr><td>` + el.param_key + `</td><td>` + el.datatype + `</td></tr>`;
//		})
		template += `</table></div>`;
        $('.rightConfigContentSubs').append(template);
        //更新对应的dialog
		saveDialogInCreate(optionData,data.target_node_code);
		//更新allConfig
		var dialogId = "#dialog-message-" + data.target_node_code;
		saveData(dialogId);
		//设定可任意拖拽放置
		$(".rightConfigContentSubs>div:last").draggable({ containment: "#containment-wrapper", scroll: false });
	}
}

function giveValue(data,optionData) {
	data.targetNodeIn.forEach(function (el, index) {
		optionData.calculationParamIns[index].calculation_param_default_value = el.param_default_value;
	})
//	data.targetNodeOut.forEach(function (el, index) {
//		optionData.calculationParamOuts[index].calculation_param_default_value = el.param_default_value;
//	})	
}