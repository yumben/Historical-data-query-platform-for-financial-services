//筛选器拖动时候的属性对象
var conditionsDrop = {
	text : '',
	metadataId : '',
	field1 : '',
	value1 : '',
	cond : '',
	isFixed : '',
	condition_table : ''
}
//输出对象拖动时候的
var fieldsDrop = {
	flag : true,
	text : '',
	metadataId : '',
	name : '',
	titleName : '',
	isTotalField : '',
	alias : '',
	func : '',
	dataType : '',
	orderIndex : '',
	show_form : '',
	field_unit_limit : '',
	parent_metadata : '',
	field_table : '',
	table_code : '',
	is_default : '',
	column_type : ''
}
//维度
var groupsDrop = {
	flag : false,
	text : '',
	metadataId : '',
	name : '',
	titleName : '',
	isTotalField : '',
	alias : '',
	func : '',
	field : '',
	dataType : '',
	is_default : '',
	parent_metadata : '',
	group_table : '',
	table_code : ''
};
//拖动开始函数
function dragStar(obj) {
	for (var i = 0; i < obj.length; i++) {
		obj[i].ondragstart = function(ev) {
			this.style.background = '#fff';
			ev.dataTransfer.setData('text', this.innerText);
			ev.dataTransfer.setData('tag', this.getAttribute('tag'));
			ev.dataTransfer.setData('metadataId', clearNull(this.getAttribute('metadata_id')));
			ev.dataTransfer.setData('name', clearNull(this.getAttribute('name')));
			ev.dataTransfer.setData('titleName', clearNull(this.getAttribute('title_name')));
			ev.dataTransfer.setData('isTotalField', clearNull(this.getAttribute('is_total_field')));
			ev.dataTransfer.setData('alias', clearNull(this.getAttribute('alias')));
			ev.dataTransfer.setData('func', clearNull(this.getAttribute('func')));
			ev.dataTransfer.setData('field1', clearNull(this.getAttribute('name')));
			ev.dataTransfer.setData('value1', clearNull(this.getAttribute('value1')));
			ev.dataTransfer.setData('value2', clearNull(this.getAttribute('value2')));
			ev.dataTransfer.setData('cond', clearNull(this.getAttribute('cond')));
			ev.dataTransfer.setData('isFixed', clearNull(this.getAttribute('is_fixed')));
			ev.dataTransfer.setData('dataType', clearNull(this.getAttribute('field_type')));
			ev.dataTransfer.setData('orderIndex', clearNull(this.getAttribute('order_index')));
			ev.dataTransfer.setData('show_form', clearNull(this.getAttribute('show_form')));
			ev.dataTransfer.setData('field_decimal_point', clearNull(this.getAttribute('field_decimal_point')));
			ev.dataTransfer.setData('field_unit_limit', clearNull(this.getAttribute('field_unit_limit')));
			ev.dataTransfer.setData('is_choic', clearNull(this.getAttribute('is_choic')));
			ev.dataTransfer.setData('is_default', 'false');
			ev.dataTransfer.setData('is_compute', 'false');
			ev.dataTransfer.setData('compute_expression', clearNull(this.getAttribute('compute_expression')));
			ev.dataTransfer.setData('parent_metadata', clearNull(this.getAttribute('parent_metadata')));
			ev.dataTransfer.setData('table_code', clearNull(this.getAttribute('table_code')));
			ev.dataTransfer.setData('column_type', clearNull(this.getAttribute('column_type')));
		}
		obj[i].ondragend = function(ev) {
			this.style.background = "none";
		}
	}
}
//维度，输出属性释放生成元素标签
function dragRelease(ev, objAttr, obj) {
	objAttr.text = ev.dataTransfer.getData('text');
	objAttr.tag = ev.dataTransfer.getData('tag');
	objAttr.metadataId = ev.dataTransfer.getData('metadataId');
	objAttr.name = ev.dataTransfer.getData('name');
	objAttr.titleName = ev.dataTransfer.getData('titleName');
	objAttr.isTotalField = ev.dataTransfer.getData('isTotalField');
	objAttr.alias = ev.dataTransfer.getData('alias');
	objAttr.func = ev.dataTransfer.getData('func');
	objAttr.field = ev.dataTransfer.getData('name');
	objAttr.dataType = ev.dataTransfer.getData('dataType');
	objAttr.orderIndex = ev.dataTransfer.getData('order_index');
	objAttr.orderType = ev.dataTransfer.getData('order_type');
	objAttr.show_form = ev.dataTransfer.getData('show_form');
	objAttr.field_decimal_point = ev.dataTransfer.getData('field_decimal_point');
	objAttr.field_unit_limit = ev.dataTransfer.getData('field_unit_limit');
	objAttr.is_default = ev.dataTransfer.getData('is_default');
	objAttr.is_compute = ev.dataTransfer.getData('is_compute');
	objAttr.compute_expression = ev.dataTransfer.getData('compute_expression');
	objAttr.parent_metadata = ev.dataTransfer.getData('parent_metadata');
	objAttr.field_table = ev.dataTransfer.getData('table_code');
	objAttr.group_table = ev.dataTransfer.getData('table_code');
	objAttr.condition_table = ev.dataTransfer.getData('table_code');
	objAttr.table_code = ev.dataTransfer.getData('table_code');
	objAttr.column_type = ev.dataTransfer.getData('column_type');
	creatLi(objAttr, obj);
}
//修改回来的回显
//筛选器修改回显对象
function conditionsBack(obj) {
	var conditionsBack = {
		text : isNoemty(obj.field_name),
		tag : '1',
		metadataId : isNoemty(obj.metadata_id),
		field1 : isNoemty(obj.field1),
		value1 : isNoemty(obj.value1),
		value2 : isNoemty(obj.value2),
		cond : isNoemty(obj.cond),
		isFixed : isNoemty(obj.is_fixed),
		dataType : isNoemty(obj.field_type),
		orderIndex : isNoemty(obj.order_index),
		is_choic : isNoemty(obj.is_choic),
		condition_table : isNoemty(obj.condition_table),
		parent_metadata : isNoemty(obj.parent_metadata),
		valfield : isNoemty(obj.valfield),
		dynamic_field : isNoemty(obj.dynamic_field),
		tips : isNoemty(obj.tips),
	};
	return conditionsBack;
}
//输出修改回显对象
function fieldsBack(obj) {
	var fieldsBack = {
		flag : true,
		tag : '1',
		text : isNoemty(obj.field_name),
		metadataId : isNoemty(obj.metadata_id),
		name : isNoemty(obj.name),
		field : isNoemty(obj.name),
		titleName : isNoemty(obj.title_name),
		isTotalField : isNoemty(obj.is_total_field),
		alias : isNoemty(obj.alias),
		func : isNoemty(obj.func),
		field_eval : isNoemty(obj.field_eval),
		dataType : isNoemty(obj.field_type),
		orderType : isNoemty(obj.order_type),
		orderIndex : isNoemty(obj.order_index),
		show_form : isNoemty(obj.show_form),
		field_decimal_point : isNoemty(obj.field_decimal_point),
		field_unit_limit : isNoemty(obj.field_unit_limit),
		parent_metadata : isNoemty(obj.parent_metadata),
		field_table : isNoemty(obj.field_table),
		is_compute : isNoemty(obj.is_compute),
		is_default : isNoemty(obj.is_default),
		compute_expression : isNoemty(obj.compute_expression),
		column_type : isNoemty(obj.column_type),
	}
	return fieldsBack;
}
; // 维度修改回显
//维度修改回显
function groupsBack(obj) {
	var groupsBack = {
		flag : false,
		tag : '1',
		text : isNoemty(obj.field_name),
		metadataId : isNoemty(obj.metadata_id),
		titleName : isNoemty(obj.field_name),
		isTotalField : isNoemty(obj.is_total_field),
		alias : isNoemty(obj.alias),
		func : isNoemty(obj.func),
		name : isNoemty(obj.field),
		field : isNoemty(obj.field),
		dataType : isNoemty(obj.field_type),
		is_default : isNoemty(obj.is_default),
		parent_metadata : isNoemty(obj.parent_metadata),
		group_table : isNoemty(obj.group_table),
		param : isNoemty(obj.param)
	};
	return groupsBack;
}
//表修改回显
function tablesBack(obj) {
	var tablesBack = {
		'fieldname' : isNoemty(obj.table_title),
		'value' : isNoemty(obj.metadataCode),
		'draggable' : true,
		'metadata_id' : isNoemty(obj.metadata_id),
		'metadata_code' : isNoemty(obj.metadataCode),
		'table_code' : isNoemty(obj.table_code),
		'parent_metadata' : isNoemty(obj.parent_metadata),
		'order_index' : isNoemty(obj.order_index),
		'parent_code' : isNoemty(obj.parent_code),
		'table_name' : isNoemty(obj.table_name),
		'order_index' : isNoemty(obj.order_index)
	};
	return tablesBack;
}
//查询定义满屏显示
function fullScreen() {
	var bodyHeight = $(window).height(); //浏览器当前窗口文档的高度
	var sectionWrap = $('.section-wrap'); //最外层div
	var sectionWrapHeight = $('.section-wrap').outerHeight();
	var sectionMain = $('.section-main'); //内层div
	var sectionMainHeight = $('.section-main').outerHeight();
	var ruleTitle = $('.rule-title'); //右边内容标题
	var ruleTitleHeight = $('.rule-title').outerHeight();
	var rule = $('.rule'); //条件区
	var ruleHeight = $('.rule').outerHeight();
	var ruleContent = $('.rule-content'); //展示字段去高度
	var mainHeight = bodyHeight - 76;
	sectionWrap.height(mainHeight);
	sectionMain.height(mainHeight);
	$('.centerContent').outerHeight(mainHeight);
	ruleContent.outerHeight($('.centerContent').outerHeight() - ruleTitleHeight - ruleHeight);
}
//属性参数获取
//表的属性参数获取
function getTablesAttr(obj, json) {
	var tableJsonList = {};
	tableJsonList['value'] = isNoemty(obj.attr('value'));
	tableJsonList['metadata_id'] = isNoemty(obj.attr('metadata_id'));
	tableJsonList['table_name'] = isNoemty(obj.attr('table_code'));
	tableJsonList['table_comment'] = isNoemty(obj.text());
	tableJsonList['table_name'] = isNoemty(obj.attr('table_name'));
	tableJsonList['alias'] = isNoemty(obj.attr('alias'));
	tableJsonList['table_title'] = isNoemty(obj.text());
	tableJsonList['relationmeta'] = isNoemty(obj.attr('relationmeta'));
	tableJsonList['berelationmeta'] = isNoemty(obj.attr('berelationmeta'));
	tableJsonList['order_index'] = isNoemty(obj.attr('order_index'));
	tableJsonList['parent_code'] = isNoemty(obj.attr('parent_code'));
	json.push(tableJsonList);
}
//维度属性参数获取
function getGroupsAttr(obj, json) {
	var dimenJSon = {};
	dimenJSon['metadata_id'] = isNoemty(obj.attr('metadata_id'));
	dimenJSon['field'] = isNoemty(obj.attr('field'));
	dimenJSon['field_name'] = isNoemty(obj.children('span').text());
	dimenJSon['field_type'] = isNoemty(obj.attr('field_type'));
	dimenJSon['title_name'] = isNoemty(obj.attr('title_name'));
	dimenJSon['is_default'] = isNoemty(obj.attr('is_default'));
	dimenJSon['parent_metadata'] = isNoemty(obj.attr('parent_metadata'));
	dimenJSon['group_table'] = isNoemty(obj.attr('group_table'));
	dimenJSon['param'] = isNoemty(obj.attr('param'));
	if (isNoemty(obj.attr('param')) != '') {
		dimenJSon['fun'] = 'case';
	}
	json.push(dimenJSon);
}
//输出属性参数获取
function getfieldsAttr(obj, json) {
	var attrJSon = {};
	attrJSon['metadata_id'] = isNoemty(obj.attr('metadata_id'));
	attrJSon['name'] = isNoemty(obj.attr('name'));
	attrJSon['field'] = isNoemty(obj.attr('field'));
	attrJSon['func'] = isNoemty(obj.attr('func'));
	attrJSon['alias'] = isNoemty(obj.attr('alias'));
	attrJSon['order_index'] = isNoemty(obj.attr('order_index'));
	attrJSon['parent_metadata'] = isNoemty(obj.attr('parent_metadata'));
	attrJSon['field_table'] = isNoemty(obj.attr('field_table'));
	attrJSon['is_default'] = isNoemty(obj.attr('is_default'));
	attrJSon['column_type'] = isNoemty(obj.attr('column_type'));
	var viewAlias = isNoemty(obj.attr('alias'));
	var viewFunc = isNoemty(obj.children('b').text());
	if (viewAlias == '') {
		viewAlias = ''
	} else {
		viewAlias = '[' + viewAlias + ']';
	}
	if (viewFunc) {
		viewFunc = viewFunc;
	} else {
		viewFunc = ''
	}
	attrJSon['title_name'] = isNoemty(obj.attr('title_name'))
	attrJSon['is_total_field'] = isNoemty(obj.attr('is_total_field'));
	attrJSon['field_name'] = isNoemty(obj.children('span').text());
	attrJSon['field_eval'] = isNoemty(obj.children('b').text());
	attrJSon['field_type'] = isNoemty(obj.attr('field_type'));
	attrJSon['show_form'] = isNoemty(obj.attr('show_form'));
	attrJSon['field_decimal_point'] = isNoemty(obj.attr('field_decimal_point'));
	attrJSon['field_unit_limit'] = isNoemty(obj.attr('field_unit_limit'));
	attrJSon['order_type'] = isNoemty(obj.attr('order_type'));
	attrJSon['is_compute'] = isNoemty(obj.attr('is_compute')); //是否计算字段
	attrJSon['compute_expression'] = isNoemty(obj.attr('compute_expression')); //计算公式
	json.push(attrJSon);
}
//筛选器参数获取
function getconditionsAttr(obj, json, boolVal) {
	var screenJSon = {};
	var fieldType = isNoemty(obj.attr('field_type'));
	var selectCount = isNoemty(obj.attr('cond'));
	var inputVal = isNoemty(obj.attr('value1'));
	var inputVal2 = isNoemty(obj.attr('value2'));
	screenJSon['metadata_id'] = obj.attr('metadata_id');
	screenJSon['field1'] = obj.attr('field1');
	screenJSon['value1'] = inputVal;
	screenJSon['value2'] = inputVal2;
	screenJSon['cond'] = selectCount;
	screenJSon['is_fixed'] = isNoemty(obj.attr('is_fixed'));
	screenJSon['field_name'] = isNoemty(obj.children('h5').find('span').text());
	screenJSon['field_type'] = fieldType;
	screenJSon['is_choic'] = isNoemty(obj.attr('is_choic'));
	screenJSon['order_index'] = isNoemty(obj.attr('order_index'));
	screenJSon['condition_table'] = isNoemty(obj.attr('condition_table'));
	screenJSon['parent_metadata'] = isNoemty(obj.attr('parent_metadata'));
	screenJSon['valfield'] = isNoemty(obj.attr('valfield'));
	if ((fieldType == 'number' || fieldType == 'decimal' || fieldType == 'numeric') && selectCount != 'in' && selectCount != 'notin' && inputVal != '') { //number类型转数值
		inputVal = parseInt(inputVal);
		if (!isNaN(inputVal)) {
			screenJSon['value1'] = inputVal;
			boolVal = false;
		} else {
			boolVal = true;
		}
	}
	if ((fieldType == 'number' || fieldType == 'decimal' || fieldType == 'numeric') && selectCount != 'in' && selectCount != 'notin' && inputVal2 != '') { //number类型转数值
		inputVal2 = parseInt(inputVal2);
		if (!isNaN(inputVal)) {
			screenJSon['value2'] = inputVal2;
			boolVal = false;
		} else {
			boolVal = true;
		}
	}
	if (fieldType == 'number' && selectCount != 'in' && selectCount != 'notin' && inputVal2 != '') { //number类型转数值
		inputVal2 = parseInt(inputVal2);
		if (!isNaN(inputVal2)) {
			screenJSon['value2'] = inputVal2;
			boolVal = false;
		} else {
			boolVal = true;
		}
	}
	if (selectCount == 'in' || selectCount == 'notin' && inputVal != '') {
		if (fieldType == 'number' || fieldType == 'decimal' || fieldType == 'numeric') { //number类型转数值			
			var dataStrArr = inputVal.split(",");
			var dataIntArr = [];
			dataIntArr = dataStrArr.map(function(data) {
				return +data;
			});
			screenJSon['value1'] = dataIntArr;
		} else if (fieldType == 'pk' && inputVal != '') {
			screenJSon['value1'] = inputVal.split(",");
		} else if (inputVal != '') {
			screenJSon['value1'] = inputVal.split(",");
		} else {
			screenJSon['value1'] = [];
		}
		if (fieldType == 'number' || fieldType == 'decimal' || fieldType == 'numeric') { //number类型转数值
			var dataStrArr = inputVal2.split(",");
			var dataIntArr = [];
			dataIntArr = dataStrArr.map(function(data) {
				return +data;
			});
			screenJSon['value2'] = dataIntArr;
		} else {
			screenJSon['value2'] = inputVal2.split(",");
		}
	}
	json.push(screenJSon);
}

//简易图表跟灵活定义相同的函数与变量
var selectFieldJson = '';
//字段拖动
var selectList = document.getElementById('selectList');
if (typeof (selectList) == "undefined" || selectList == null) {
} else {
	var selectListLi = selectList.getElementsByTagName('li');
}
//计算对象
var treeObj = $('#treeObj')[0];
//维度
var dimen = document.getElementById('dimen');
if (typeof (dimen) == "undefined" || dimen == null) {
} else {
	var dimenLi = dimen.getElementsByTagName('li');
}
//输出属性	
var outAttr = document.getElementById('outAttr');
if (typeof (outAttr) == "undefined" || outAttr == null) {
} else {
	var outAttrLi = outAttr.getElementsByTagName('li');
}

//筛选器
var ruleScreen = document.getElementById('ruleScreen');

//拖出关联表请求关联表
function calFunTable() {
	var choseTree = $('#treeObj').find('li');
	var param = {
		'param' : ''
	};
	$.each(choseTree, function(index, el) {
		var metadataId = $(this).attr('metadata_id');
		if (param.param == '') {
			param.param = metadataId;
		} else {
			param.param += ',' + metadataId;
		}
	})
	ajaxSend('/bdp-web/metadata/selectRelationMetadataJson', param, successTableLeftDrop);
}
//表回调函数	
function successTableLeftDrop(data) {
	//console.log(JSON.stringify(data)+'@!!!!!')
	$('#tableMenu').html('');
	var tableMenu = data.list;
	if (tableMenu != '' && tableMenu != null && tableMenu != 'null' && tableMenu != undefined && tableMenu != 'undefined') {
		var showBox = '';
		showBox += '<ul class="tableNav leftTableNav">';
		$.each(tableMenu, function(index, el) {
			var child = el.children;
			var parenCode = el.metadataCode;
			var metadataCode = el.metadataCode;
			var metadataName = el.metadataName;
			var metadataId = el.metadataId;
			var relationMeta = el.relationMeta;
			var beRelationMeta = el.beRelationMeta;
			if (child) {
				if (child == '' && child == null) {
					showBox += '<li fieldName="' + metadataName + '" metadata_id="' + metadataId + '" relationMeta="' + relationMeta + '" beRelationMeta="' + beRelationMeta + '" value="' + metadataCode + '" draggable="true">' + metadataName + '</li>';
				} else {
					showBox += '<li fieldName="' + metadataName + '" metadata_code="' + parenCode + '"><h5><i class="fa fa-plus-square"></i> ' + metadataName + '</h5><ul>';
					$.each(child, function(indexChild, elChilid) {
						showBox += '<li fieldName="' + elChilid.metadataName + '" table_name="' + elChilid.property.table_name + '" parent_code="' + parenCode + '" metadata_id="' + elChilid.metadataId + '" value="' + elChilid.metadataCode + '" relationMeta="' + elChilid.relationMeta + '" beRelationMeta="' + elChilid.beRelationMeta + '" draggable="true">' + elChilid.metadataName + '</li>';
					})
					showBox += '</ul></li>';
				}
			}
		})
		showBox += '</ul>';
		$('#tableMenu').append(showBox);
	//dragStarTable(tableListLi);
	}
}
//拖出表的字段的请求
function calFunField() {
	var choseTree = $('#treeObj').children('li');
	$('#selectList').html('');
	var param = {
		'table' : ''
	};
	$.each(choseTree, function(index, item) {
		var val = $(this).attr('metadata_id');
		if (param.table == '') {
			param.table = val;
		} else {
			param.table += ',' + val;
		}
	})
	//console.log(JSON.stringify(param)+'!!!')
	ajaxSend('/bdp-web/metadata/selectObjAndPropertie', param, successCallBackFun);
//console.log(JSON.stringify(selectAllField)+'!9999!!')		
}
//字段回调函数
function successCallBackFun(ret) {
	//console.log(JSON.stringify(ret)+'表的字段01');	
	selectFieldJson = ret;
	$.each(ret.list, function(i, data) {
		//选择表的属性赋值
		var tableName = data.table_name;
		var metadataName = data.metadata_name;
		var tableMetadataId = data.metadata_id;
		var tableComment = data.table_comment;
		var tableMetadataCode = data.metadata_code;
		var tableCode = data.table_code;
		var tableAlias = '';
		//表字段生成
		var list = data.columns;
		if (isNoemty(list) != '') {
			if (list.length > 0) {
				var selectList = $('#selectList');
				selectList.append('<p class="listTableName" table_code="' + data.table_code + '"><i class="fa fa-minus-square"></i>' + metadataName + '</p>');
				$.each(list, function(index, el) {
					var li = $('<li draggable="true" tag="1">' + el.metadata_name + '</li>');
					li.attr({
						'metadata_id' : el.metadata_id,
						'name' : el.column_code,
						'alias' : '',
						'title_name' : el.column_name,
						'is_total_field' : '0',
						'field_type' : el.data_type,
						'table_name' : metadataName,
						'table_code' : tableCode,
						'parent_metadata' : el.parent_metadata,
						'column_type' : isNoemty(el.column_type),
					})
					selectList.append(li);
				})
			}
		}

		//拖动元素存值
		dragStar(selectListLi);
	});

}
//选择表搜索
function tableSearch() {
	var tableNavLi = $('.tableNav li ul li');
	var tableInputVal = $.trim($('#tableSearchInput').val());
	$.each(tableNavLi, function(index, el) {
		$(this).show();
		var LiText = $(this).attr('fieldname');
		if (tableInputVal == '') {
			$(this).show();
		}
		if (LiText.indexOf(tableInputVal) >= 0) {
			$(this).parent().prev().trigger('click');
			$(this).parent('li').show();
			$(this).parent('li').parent('ul').parent('li').show();
		} else {
			$(this).hide();
		}
	})
}
//删除表li时执行
function delTreeObjLi(a) {
	var orderIndex = a.parent().attr('order_index');
	var parentId = a.parent().attr('metadata_id');
	var parensib = a.parent().siblings();
	var dimen = $('#dimen > li');
	var outAttr = $('#outAttr > li');
	var ruleScreen = $('#ruleScreen > div');
	var ruleDetailList = $('#ruleDetailList > span');
	//删除表li
	if (orderIndex == '1') {
		(a.parent()).remove();
		parensib.remove();
		dimen.remove();
		outAttr.remove();
		$('#ruleScreen > div').remove();
		$('#ruleDetailList').empty();
	} else {
		(a.parent()).remove();
		removeField(dimen, parentId);
		removeField(outAttr, parentId);
		removeField(ruleScreen, parentId);
		removeField(ruleDetailList, parentId);
	//creatTemplateTable($('#outAttr'));
	}
	calFunTable();
	calFunField();
}
//删除表时维度，输出字段移除
function removeField(listLi, ID) {
	$.each(listLi, function(index, el) {
		var parentMetadata = $(this).attr('parent_metadata');
		if (ID == parentMetadata) {
			$(this).remove();
		}
	})
}
$(function() {
	//计算对象表的拖动	
	var tableMenuDrop = document.getElementById('tableMenu');
	if (typeof (tableMenuDrop) == "undefined" || tableMenuDrop == null) {
	} else {
		tableMenuDrop.addEventListener("dragstart", function(e) {
			var evn = e || window.event;
			evn.stopPropagation();
			targetEleLi = evn.target;
			evn.dataTransfer.setData("aaa", evn.target.value);
		});
		tableMenuDrop.addEventListener("dragover", function(e) {
			var evn = e || window.event;
			evn.preventDefault();
			x = evn.pageX;
			y = evn.pageY;
		});
		treeObj.ondragover = function(ev) {
			ev.preventDefault();
		}
		treeObj.ondrop = function(ev) {
			ev.preventDefault();
			var newEleLi = targetEleLi.cloneNode(true);
			var _this = $(this);
			var treeObjText = $('#treeObj').children('li');
			var metadata_id = $(newEleLi)[0].getAttribute('metadata_id');
			//console.log(treeObjText.length+'!!!!');
			var isTrue = false;
			$.each(treeObjText, function() {
				if (metadata_id == $(this).attr('metadata_id')) {
					isTrue = true;
				}
			})
			if (!isTrue) {
				if ($(newEleLi)[0].getAttribute('tag')) {
					alert('字段不能放置到“业务对象”！');
					return false;
				}
				var clickI = '<i class="fa fa-close"> </i>';
				$(newEleLi).append(clickI);
				$(this).append(newEleLi);

				var choseTree = $('#treeObj');
				sortIndex(choseTree);
				//关联表
				calFunTable();
				calFunField();
				$('.tableNav > li >ul').show();
				$('.tableNav > li >h5 > i').addClass('fa-minus-square').removeClass('fa-plus-square');
			}
		}

	}



	//点击已选择的表删除
	$(document).on('click', '#treeObj>li>i', function() {
		delTreeObjLi($(this));
		//关联表
		calFunTable();
		calFunField();
		var choseTree = $('#treeObj');
		sortIndex(choseTree);
		if (choseTree.children('li').length == '0') {
			$('#chartType').empty();
		}
	})
	//表的搜索
	$('#select-search-btntable').click(function() {
		tableSearch();
		calFunField();
		$('#tableMenu > ul > li > h5 > i').addClass('fa-minus-square').removeClass('fa-plus-square');
		$('#tableMenu > ul > li > ul').show();
	});
	$('#tableSearchInput').keydown(function(event) {
		if (event.keyCode == 13) {
			tableSearch();
			calFunField();
			$('#tableMenu > ul > li > h5 > i').addClass('fa-minus-square').removeClass('fa-plus-square');
			$('#tableMenu > ul > li > ul').show();
		}
	})
	//字段搜索框
	//搜索框
	$('#select-search-btn').click(fieldSearch);
	$('#select-search-text').on('change', function() {
		setTimeout(fieldSearch(), 200);
	});
	//搜索框
	function fieldSearch() {
		var tempFieldList = [];
		var searchText = $.trim($("#select-search-text").val());
		var choseTree = $('#treeObj').find('li');
		var param;
		//若为空 重新查找
		if (searchText == '') {
			$('#selectList').empty();
			calFunField();
		} else {
			var selectList = $('#selectList');
			selectList.html('');
			$.each(selectFieldJson.list, function(i, data) {
				//选择表的属性赋值
				var tableName = data.table_name;
				var metadataName = data.metadata_name;
				var tableMetadataId = data.metadata_id;
				var tableComment = data.table_comment;
				var tableMetadataCode = data.metadata_code;
				var tableCode = data.table_code;
				//表字段生成
				var list = data.columns;
				//console.log(JSON.stringify(list)+'搜索的字段555');
				if (isNoemty(list) != '') {
					if (list.length > 0) {
						selectList.append('<p class="listTableName" table_code="' + data.table_code + '"><i class="fa fa-minus-square"></i>' + metadataName + '</p>');
						//console.log(JSON.stringify(list)+'####')
						$.each(list, function(index, el) {
							var nametext = el.column_name;
							//console.log(nametext+'####'+searchText)
							if (isNoemty(nametext).indexOf(searchText) >= 0) {
								var li = $('<li draggable="true" tag="1">' + el.metadata_name + '</li>');
								li.attr({
									'metadata_id' : el.metadata_id,
									'name' : el.column_code,
									'alias' : '',
									'title_name' : el.column_name,
									'is_total_field' : '0',
									'field_type' : el.data_type,
									'table_name' : metadataName,
									'table_code' : tableCode,
									'parent_metadata' : el.parent_metadata,
									'column_type' : el.column_type,
								})
								selectList.append(li);
							}
						})
					}
				}

				//拖动元素存值
				dragStar(selectListLi);
			});
		}
	}
	$('#select-search-text').keydown(function(event) {
		if (event.keyCode == 13) {
			fieldSearch();
		}
	})

	//清空选择的维度，字段属性，筛选器
	$(document).on('click', '.rule-con > b.clearAttr', function() {
		var parentId = $(this).parents('.rule-con').children('.rule-con-list').children('ul').attr('id');
		if (parentId == 'outAttr') {
			$(this).parents('.rule-con').children('.rule-con-list').children('ul').empty();
			$('#ruleDetailList').empty();
		} else {
			$(this).parents('.rule-con').children('.rule-con-list').children('ul').empty();
		}
		//关联表
		calFunTable();
		calFunField();
	})
	$(document).on('click', '#ruleScreen > h3 > b.clearAttr', function() {
		$(this).parents('#ruleScreen').children('div').remove();
	}) //	
	$(document).on('click', '.rule-con > b#treeBtn', function() {
		var parentId = $(this).parents('.rule-con').children('.rule-con-list').children('ul').attr('id');
		if (parentId == 'outAttr') {
			$(this).parents('.rule-con').children('.rule-con-list').children('ul').empty();
			$('#ruleDetailList').empty();
		} else {
			$(this).parents('.rule-con').children('.rule-con-list').children('ul').empty();
		}
		$('#dimen > li').remove();
		$('#outAttr > li').remove();
		$('#outAttr > li').remove();
		$('#ruleScreen > div').remove();
		$('#ruleDetailList > span').remove();
		$('#chartType').empty();
		//关联表
		calFunTable();
		calFunField();
	})
	$(document).on('click', '#ruleScreen > h3 > b.clearAttr', function() {
		$(this).parents('#ruleScreen').children('div').remove();
	})
})
//字段选择的收缩展开
//字段搜索展开事件
$(document).on('click', '.listTableName', function() {
	var iHtml = $(this).find('i');
	var text = $(this).attr('table_code');
	if (iHtml.hasClass('fa-plus-square')) {
		iHtml.addClass('fa-minus-square').removeClass('fa-plus-square');
		$('#selectList li').each(function(index, el) {
			if ($(el).attr('table_code') == text) {
				$(el).css('display', 'block');
			}
		})
	} else if (iHtml.hasClass('fa-minus-square')) {
		iHtml.addClass('fa-plus-square').removeClass('fa-minus-square');
		$('#selectList li').each(function(index, el) {
			if ($(el).attr('table_code') == text) {
				$(el).css('display', 'none');
			}
		})
	}

})
//收缩展开
$(document).on('click', '.rule-con > em', function() {
	var sibling = $(this).siblings('.rule-con-list');
	var sibling01 = $(this).siblings('.easy-menu-listhide');
	height = sibling.outerHeight();
	if ($(this).children('b').text() == '收缩') {
		$(this).children('b').text('展开');
		sibling.css({
			'height' : '40px',
		});
		sibling01.css({
			'height' : '24px',
		})
		$(this).children('i').addClass('glyphicon-chevron-down').removeClass('glyphicon-chevron-up');
		fullScreen();
	} else if ($(this).children('b').text() == '展开') {
		$(this).children('b').text('收缩');
		sibling.css({
			'height' : 'auto',
		});
		sibling01.css({
			'height' : 'auto',
		})
		$(this).children('i').addClass('glyphicon-chevron-up').removeClass('glyphicon-chevron-down');
		fullScreen();
	}

})
/*灵活查询全选*/
$(document).on('click', '.easy-select-filed > span input[type="checkbox"]', function(event) {
	event.stopPropagation();
	selectAllField($(this));
})
$(document).on('click', '.easy-select-filed .easy-tab-item input[type="checkbox"]', function(event) {
	event.stopPropagation();
	if ($(this).is(':checked')) {
		$(this).prop('checked', true);
	} else {
		$(this).prop('checked', false);
	}
})
/*判断是否全选*/
function selectAllField(obj) {
	var check = obj.parents('span').siblings('div').children('div').find('input[type="checkbox"]');
	if (obj.is(':checked')) {
		check.prop('checked', true);
		obj.prop('checked', true);
	} else {
		check.prop('checked', false);
		obj.prop('checked', false);
	}
}
//灵活查询定义界面选择表的多级菜单展开收缩
$(document).on('click', '.tableNav li > h5', function() {
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
//点击维度、输出属性显示隐藏下拉
$(document).on('click', '.rule-con-list li', function(event) {
	event.stopPropagation();
	var paren = $(this).parent('ul').attr('id');
	var child = $(this).children('.attrDowm');
	$('.attrDowm').hide();
	var fieldType = $(this).attr('field_type');
	var spanHide = child.find('span');
	var pHide = child.find('p');
	if (fieldType == 'number' || fieldType == 'numeric' || fieldType == 'decimal') {
	} else {
		$.each(spanHide, function(index, el) {
			var spanVal = $(this).attr('value');
			if (spanVal == 'sum' || spanVal == 'avg') {
				$(this).parent('li').hide();
			} else {
				$(this).parent('li').show();
			}
		});
		$.each(pHide, function(index, el) {
			var pVal = $(this).attr('value');
			if (pVal == 'fieldformat') {
				$(this).parent('li').hide();
			} else {
				$(this).parent('li').show();
			}
		});

	}
	var is_default = $(this).attr('is_default');
	if (paren == 'outAttr') {
		if (is_default == 'true') {
			child.find('span[value="setdefaule"]').text('取消默认输出');
		}
	} else {
		if (is_default == 'true') {
			child.find('span[value="setdefaule"]').text('取消默认选中');
		}
	}

	if (child.is(':hidden')) {
		child.show();
	} else {
		child.hide();
	}
})
$(document).click(function(event) {
	$('.attrDowm').hide();
});

//二级鼠标移入移除显示隐藏
$(document).on("mouseover", ".attrDowm li", function() {
	var widthLi = $(this).outerWidth();
	$(this).children('ul').css('left', widthLi).show();
})
$(document).on("mouseout", ".attrDowm li", function() {
	$(this).children('ul').hide();
})


//维度下拉属性生成函数
function attrDowm(obj, json, isTotal, isDefault) {
	var elHtml = '';
	elHtml += '<ul class="attrDowm">';
	$.each(json, function(index, el) {
		var child = el.child;
		var code = el.code;
		var label = el.label;
		if (child) {
			if (child == '' || child == null) {
				if (code == 'set') {
					elHtml += '<li><p value="' + code + '" class="edit' + code + '">' + label + '</p></li>';
				} else if (code == 'fieldformat') {
					elHtml += '<li><p value="' + code + '" class="' + code + '">' + label + '</p></li>';
				} else if (code == 'isTotal') {
					if (isTotal == '1') {
						elHtml += '<li><span value="' + code + '">取消合计</span></li>';
					} else {
						elHtml += '<li><span value="' + code + '">' + label + '</span></li>';
					}
				} else if (code == 'setSection') {
					if (obj == 'outAttr') {
						if (isDefault == 'true') {
							elHtml += '<li><span value="' + code + '">取消默认输出</span></li>';
						} else {
							elHtml += '<li><span value="' + code + '">' + label + '</span></li>';
						}
					} else {
						if (isDefault == 'true') {
							elHtml += '<li><span value="' + code + '">取消默认选中</span></li>';
						} else {
							elHtml += '<li><span value="' + code + '">' + label + '</span></li>';
						}
					}

				} else {
					elHtml += '<li><span value="' + code + '">' + label + '</span></li>';
				}
			} else {
				elHtml += '<li><h5><i class="fa fa-angle-right"></i> ' + label + '</h5><ul>';
				$.each(child, function(indexChild, elChilid) {
					elHtml += '<li><span value="' + elChilid.code + '">' + elChilid.label + '</span></li>';
				})
				elHtml += '</ul></li>';
			}
		} else {
			elHtml += '<li><span value="' + code + '">' + label + '</span></li>';
		}
	})
	elHtml += '</ul>';
	$(obj).append(elHtml);
}
//维度删除属性函数
function deletAttr(obj) {
	obj.parentNode.parentNode.removeChild(obj.parentNode);
}
//获取分页条
function getPageBar(curPage, pageSize, total, totalPage) {
	//页码大于最大页数
	if (curPage > totalPage)
		curPage = totalPage;
	//页码小于1
	if (curPage < 1 && total == 0)
		curPage = 1;
	if (total == 0)
		curPage = 0;
	pageStr = "<div class='totalPage'><em id=\"totalVar\" totalVar=\"" + total + "\">共" + total + "条</em><em id=\"totalPage\" totalPage=\"" + totalPage + "\"><b>" + curPage
		+ "</b>/" + totalPage + "</em></div>";
	//如果是第一页
	if (curPage == 1) {
		pageStr += "<span>首页</span><span>上一页</span>";
	} else {
		pageStr += "<span><a href='javascript:void(0)' rel='1'>首页</a></span><span><a href='javascript:void(0)' rel='" + (curPage - 1) + "'>上一页</a></span>";
	}
	//如果是最后页
	if (curPage >= totalPage) {
		pageStr += "<span>下一页</span><span>尾页</span>";
	} else {
		pageStr += "<span><a href='javascript:void(0)' rel='" + (parseInt(curPage) + 1) + "'> 下一页</a></span><span><a href='javascript:void(0)' rel='" + totalPage + "'>尾页</a></span>";
	}

	$('.page').html(pageStr);
	$('#exportBtn').attr("totalVar", total);
}
//获取多选字段值的函数
function getafield(arr, obj, flag) {
	$.each(obj, function(index, el) {
		var fieldList = {};
		var _this = $(this);
		var parent = $(this).parents('.easy-tab-item');
		if (_this.is(':checked')) {
			var text = _this.parent().text();
			var name = isNoemty(parent.attr('name'));
			var thId = isNoemty(parent.attr('id'));
			var metadataId = isNoemty(parent.attr('metadata_id'));
			var nameText = isNoemty(parent.attr('column_code'));
			var alias = parent.attr('alias');
			var titleName = isNoemty(parent.attr('title_name'));
			var istotalfield = isNoemty(parent.attr('is_total_field'));
			var dataType = isNoemty(parent.attr('field_type'));
			var field = isNoemty(parent.attr('field'));
			var func = isNoemty(parent.attr('func'));
			var orderType = isNoemty(parent.attr('order_type'));
			var show_form = isNoemty(parent.attr('show_form'));
			var field_unit_limit = isNoemty(parent.attr('field_unit_limit'));
			var field_decimal_point = isNoemty(parent.attr('field_decimal_point'));
			var is_compute = isNoemty(parent.attr('is_compute'));
			var order_index = isNoemty(parent.attr('order_index'));
			fieldList['is_compute'] = is_compute;
			fieldList['orderType'] = orderType;
			fieldList['fieldData'] = name;
			fieldList['fieldText'] = text;
			fieldList['thId'] = thId;
			fieldList['name'] = name;
			fieldList['metadata_id'] = metadataId;
			fieldList['column_code'] = nameText;
			if (flag == true) {
				if (alias) {
					fieldList['alias'] = alias;
				} else {
					fieldList['alias'] = name;
				}
			} else {
				fieldList['alias'] = alias;
			}

			fieldList['func'] = func;
			fieldList['title_name'] = titleName;
			fieldList['is_total_field'] = istotalfield;
			fieldList['data_type'] = dataType;
			fieldList['field'] = field;
			fieldList['show_form'] = show_form;
			fieldList['field_unit_limit'] = field_unit_limit;
			fieldList['field_decimal_point'] = field_decimal_point;
			fieldList['order_index'] = order_index;
			arr.push(fieldList);
		}
	})
}
//全选字段数组JSON
function getAllJson(arr, flag) {
	arr.length = 0;
	var fieldObj = $('.easy-select-filed .easy-tab-item input[type="checkbox"]');
	getafield(arr, fieldObj, flag);
}
//显示文本框只能输入中文，数字，字母，下划线
function checkInput(obj) {
	var testReg = /^[a-zA-Z0-9_\u4e00-\u9fa5\s]*$/;
	var text = obj.value;
	if (!text.match(testReg)) {
		obj.value = '';
		return false;
	}
}
function reg_check_val(nickname) {
	var regex = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z_])+([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z_]|[0-9])*$");
	var res = regex.test(nickname);
	if (res == true) {
		return true;
	} else {
		layer.alert("只能输入中文，英文以及下划线开头！");
		return false;
	}
}
//只能输入数字
function checkNum(val) {
	//定义正则表达式部分
	var regex = /^\d+$/;
	var res = regex.test(val);
	if (res == true) {
		return true;
	} else {
		layer.alert("只能输入数字！");
		return false;
	}
}
//查询定义排序
function sortIndex(obj) {
	//字段释放增加排序
	if (obj) {
		$obj = $(obj);
		var child = $obj.children('li');
		var num = 1;
		$.each(child, function(index, el) {
			$(this).attr('order_index', num++);
		})
	}
}
//查询筛选器排序
function sortIndexScreen(obj) {
	//字段释放增加排序
	if (obj) {
		$obj = $(obj);
		var child = $obj.children('div');
		var num = 1;
		$.each(child, function(index, el) {
			$(this).attr('order_index', num++);
		})
	}
}
//把属性值为null的留空
function clearNull(attrVal) {
	return attrVal == null ? "" : attrVal;
}
//千分位转换
function formatNum(str) {
	var temp = str.toString()
	var re = /^[0-9]+.?[0-9]*$/;
	var re1 = /^\-[0-9]+.?[0-9]*$/; //负数校验

	//console.log("formatNum1111===="+temp)
	if (re.test(str) || re1.test(str)) {
		//console.log("formatNum===="+temp)
		var newStr = "";
		var count = 0;
		if (temp.indexOf(".") == -1) { //没有小数
			for (var i = temp.length - 1; i >= 0; i--) {
				if (count % 3 == 0 && count != 0) {
					if ("-" != temp.charAt(i)) {
						newStr = temp.charAt(i) + "," + newStr; //碰到3的倍数则加上“,”号
					} else {
						newStr = temp.charAt(i) + newStr; //负号就不用加","
					}
				} else {
					newStr = temp.charAt(i) + newStr;
				}
				count++;
			}
			temp = newStr


		} else { //有小数
			for (var i = temp.indexOf(".") - 1; i >= 0; i--) {
				if (count % 3 == 0 && count != 0) {
					if ("-" != temp.charAt(i)) {
						newStr = temp.charAt(i) + "," + newStr; //碰到3的倍数则加上“,”号
					} else {
						newStr = temp.charAt(i) + newStr; //负号就不用加","
					}

				} else {
					newStr = temp.charAt(i) + newStr; //逐个字符相接起来
				}
				count++;
			}

			var pointNum = str.toString().split(".")[1]; //小数值
			temp = newStr + "." + pointNum;


		}
	}
	return temp;
}
//数值显示格式转换
function fieldShowForm(show_form, value) {
	if (show_form == 'thousand') {
		return formatNum(value);
	} else {
		return value;
	}

}


//单位转换
function unitChange(field_unit_limit, field_unit, value) {
	if (field_unit_limit == 'true') {
		var re = /^[0-9]+.?[0-9]*$/;
		var re1 = /^\-[0-9]+.?[0-9]*$/; //负数校验
		var pointLength = 0; //小數點位數
		if (value.toString().indexOf(".") != -1) {
			pointLength = value.toString().split(".")[1].length
		}
		if (re.test(value) || re1.test(value)) {
			if (field_unit == "2") {
				value = (parseFloat(value) / 1000).toFixed(3 + pointLength);
			} else if (field_unit == "3") {
				value = (parseFloat(value) / 10000).toFixed(4 + pointLength);
			} else if (field_unit == "4") {
				value = (parseFloat(value) / 100000000).toFixed(8 + pointLength);
			} else {
			}
		}
	}
	return value;
}


//小数位计算
function formatPoint(str, num) {
	var temp = decimal(str, num);
	var temp = temp.toString()
	var re = /^[0-9]+.?[0-9]*$/;
	var re1 = /^\-[0-9]+.?[0-9]*$/; //负数校验
	if (num == null || num == "null" || num == undefined || num == "undefined" || num == '') {
		return str; //返回原值
	} else {
		num = parseInt(num);
	}
	if (re.test(str) || re1.test(str)) {
		var newStr = "";
		var count = 0;
		if (temp.indexOf(".") == -1) { //不带小数
			if (num > 0) {
				var point = ".";
				for (var i = num - 1; i >= 0; i--) {
					point += "0";
				}
				temp = temp + point; //自动补小数点
			}

		} else {
			newStr = temp.split(".")[0];
			if (num == 0) { //小数位为0位，则直接返回整数部分
				return newStr;
			}
			var pointLength = temp.split(".")[1].toString().length;
			if (pointLength > num) {
				//Math.round(num*100)/100
				//var aNew = str.replace(re,"$1");
				//temp = newStr + (temp + "00").substr((temp + "00").indexOf("."), num+1);

			} else if (pointLength < num) {
				for (var i = num - pointLength; i > 0; i--) {
					str += "0";
				}
				return str;
			} else {
				return str;
			}

		}
	}
	return temp;
}

/**
* 四舍五入
*/
function decimal(num, v) {
	var vv = Math.pow(10, v);
	return Math.round(num * vv) / vv;
}