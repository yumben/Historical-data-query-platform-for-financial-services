
var frameboxConfig = $('.frameboxConfig');
var ___id = GetQueryString('id');

if (___id) {
	var paramConfig = {
		'id' : ___id
	}
	ajaxSend('/bdp-web/viewTableCofig/selectDataObj', paramConfig, successCallBackFun);
	$('#btnConfig').prop('disabled', true);
} else {
	$('#btnConfig').prop('disabled', false);
	$(document).on('click', '#btnConfig', function() {
		ajaxSend('/bdp-web/metadata/selectDataObj', {}, function(data) {
			var tableMenu = data.list;
			if (tableMenu != '' && tableMenu != null && tableMenu != 'null' && tableMenu != undefined && tableMenu != 'undefined') {
				var showBox = '';
				var showBox = '<div class="showbox" id="tableMenu">';
				showBox += '<ul class="tableNav">';
				$.each(tableMenu, function(index, el) {
					var child = el.children;
					var metadataCode = el.metadata_code;
					var metadataName = el.metadata_name;
					if (child) {
						if (child == '' && child == null) {
							showBox += '<li><label><input type="radio" name="metadata_code" value="' + metadataCode + '">' + metadataName + '</label></li>';
						} else {
							showBox += '<li><h5><i class="fa fa-plus-square"></i> ' + metadataName + '</h5><ul>';
							$.each(child, function(indexChild, elChilid) {
								showBox += '<li><label><input type="radio" name="metadata_code" value="' + elChilid.metadata_code + '">' + elChilid.metadata_name + '</label></li>';
							})
							showBox += '</ul></li>';
						}
					}
				})
				showBox += '</ul></div>';
				layer.open({
					type : 1,
					area : [ '350px', '300px' ],
					btn : [ '确定', '取消' ],
					btnAlign : 'c',
					content : showBox,
					yes : function(index, layero) {
						var objShow = $('#tableMenu');
						var radioVal = objShow.find('input[type="radio"]:checked').val();
						var param = {
							'table' : radioVal
						};
						//console.log(JSON.stringify(param))+'---'
						ajaxSend('/bdp-web/viewTableCofig/selectObjAndPropertie', param, successCallBackFun);
						layer.close(index); //如果设定了yes回调，需进行手工关闭
					},
					cancel : function(index, layero) {
						layer.close(index)
					}
				});
			}
		});
	});
}
//回调函数
function successCallBackFun(data) {
	var tableConfig = $('#tableConfig');
	var tableMetadataCode = data.metadata_code; //表code
	var tableId = data.table_id; //表id
	var tableCode = data.table_code; //表名
	var tableName = data.table_name; //表格中文名
	var tableViewTitle = data.view_title; //标题
	var tablePkField = data.pk_field; //主键	
	var tableMetadataId = data.metadata_id; //元数据id
	var tableViewType = data.view_type; //显示类型
	var tableIsinsert = data.is_insert; //是否可新增
	var tableIsEdit = data.is_edit; //是否可编辑
	var tableIsSearch = data.is_search; //是否可查询
	var tableIsDelete = data.is_delete; //是否可删除
	var columns = data.columns; //表属性		
	var tableIdHtml = tableConfig.find('input[name="table_id"]');
	var tableMetadataCodeHtml = tableConfig.find('input[name="metadata_code"]');
	var tableCodeHtml = tableConfig.find('input[name="table_code"]');
	var taleNameHtml = tableConfig.find('input[name="table_name"]');
	var taleTitleHtml = tableConfig.find('input[name="view_title"]');
	var talePkFieldHtml = tableConfig.find('input[name="pk_field"]');
	var tableMetadataIdHtml = tableConfig.find('input[name="metadata_id"]');
	var tableIsinsertHtml = tableConfig.find('input[name="is_insert"]')
	var tableIsEditHtml = tableConfig.find('input[name="is_edit"]');
	var tableIsSearchHtml = tableConfig.find('input[name="is_search"]');
	var tableIsDeleteHtml = tableConfig.find('input[name="is_delete"]');
	tableIdHtml.val(tableId);
	tableMetadataCodeHtml.val(tableMetadataCode);
	tableCodeHtml.val(tableCode);
	taleNameHtml.val(tableName);
	tableMetadataIdHtml.val(tableMetadataId);
	talePkFieldHtml.val(tablePkField);
	taleTitleHtml.val(tableViewTitle);
	viewObj(tableIsinsert, tableIsinsertHtml);
	viewObj(tableIsEdit, tableIsEditHtml);
	viewObj(tableIsSearch, tableIsSearchHtml);
	viewObj(tableIsDelete, tableIsDeleteHtml);
	var attrboxId = $('#attrbox');
	attrboxId.html('');
	if (isNoEmpty(columns)) {
		$.each(columns, function(index, el) {
			var columnId = el.column_id; //属性id
			var columnCode = el.column_code; //字段名
			var columnName = el.column_name; //字段中文名
			var columnType = el.column_type; //展示类型
			var links = el.links; //超链接
			var isPk = el.is_pk; //主键
			var isInsert = el.is_insert; //是否可增加字段
			var isEdit = el.is_edit; //是否可修改字段
			var isSearch = el.is_search; //是否查询字段
			var isView = el.is_view; //是否显示字段
			var viewGroup = el.view_group; //分组
			var orderIndex = el.order_index; //排序
			var relationType = el.relation_type; //外键
			var relationTable = el.relation_table; //外键表
			var relationField = el.relation_field; //外键字段名	
			//console.log(relationType+'###'+relationTable+'***0'+relationField+'9999')
			if (orderIndex == null) {
				orderIndex = '0'
			}
			var attrbox = $('<div class="attrbox"></div>');
			attrboxId.append(attrbox);
			attrbox.append('<div class="sort"><span>排序：</span><input class="form-control sortinput" type="text" name="order_index" value="' + orderIndex + '"></div>');
			attrbox.append('<div class="attrConfig" style="display:none"><span>字段id：</span><input class="form-control" type="text" name="column_id" value="' + columnId + '"></div>');
			attrbox.append('<div class="attrConfig"><span>字段名：</span><input class="form-control" type="text" name="column_code" value="' + columnCode + '" disabled="disabled"></div>');
			attrbox.append('<div class="attrConfig"><span>字段中文名：</span><input class="form-control" type="text" name="column_name" value="' + columnName + '"></div>');
			creatInput(attrbox, columnType, links, isPk, isInsert, isEdit, isSearch, isView, viewGroup, relationType, relationTable, relationField)
		})
	}
	attrboxId.append('<div class="btngroundForm"><button type="button" class="btn btn-primary" id="savaBtn"><i class="fa fa-save"></i> 保存</button><button type="button" class="btn btn-default" id="backBtn"><i class="fa fa-mail-reply-all"></i> 返回</button></div>');
	//判断是修改还是新增的主键回显选择
	if (!___id) {
		$('.attrbox .attrConfig').find('input[name="is_pk"]').prop('disabled', false);
	}
}

//判断表格信息回显
function viewObj(obj, elEment) {
	if (obj == '1') {
		elEment.prop("checked", true);
		elEment.val('1');
	}
}

//字段属性html元素函数
function creatInput(obj, type, links, isPk, isInsert, isEdit, isSearch, isView, viewGroup, relationType, relationTable, relationField) {
	//是否有链接
	if (isNoEmpty(links)) {
		links = links;
	} else {
		links = ''
	}
	obj.append('<div class="attrConfig">'
		+ '<span>超链接：</span>'
		+ '<input class="form-control" type="text" name="links" value="' + links + '" placeholder="http://www.baicu.com">'
		+ '</div>');
	//分组回显
	if (isNoEmpty(viewGroup)) {
		viewGroup = viewGroup;
	} else {
		viewGroup = '';
	}
	obj.append('<div class="attrConfig">'
		+ '<span>分组：</span>'
		+ '<input class="form-control" type="text" name="view_group" value="' + viewGroup + '">'
		+ '</div>');
	//是否有主键
	var isCheckPk = '';
	var checkVal = '0';
	if (isPk == '1') {
		isCheckPk = 'checked';
		checkVal = '1';
	} else {
		isCheckPk = 'disabled';
		checkVal = '0';
	}
	obj.append('<div class="attrConfig">'
		+ '<label><input type="checkbox" value="' + checkVal + '" name="is_pk" ' + isCheckPk + '>是否是主键字段</label>'
		+ '</div>');

	//是否新增
	var isCheckInsert = '';
	var InsertVal = '0';
	if (isInsert == '1') {
		isCheckInsert = 'checked';
		InsertVal = '1';
	} else {
		isCheckInsert = '';
		InsertVal = '0';
	}
	obj.append('<div class="attrConfig">'
		+ '<label><input type="checkbox" value="' + InsertVal + '" name="is_insert" ' + isCheckInsert + '>是否做增加字段</label>'
		+ '</div>');
	//是否编辑
	var isCheckEdit = '';
	var EditVal = '0';
	if (isEdit == '1') {
		isCheckEdit = 'checked';
		EditVal = '1';
	} else {
		isCheckEdit = '';
		EditVal = '0';
	}
	obj.append('<div class="attrConfig">'
		+ '<label><input type="checkbox" value="' + EditVal + '" name="is_edit" ' + isCheckEdit + '>是否做修改字段</label>'
		+ '</div>');

	//是否查询字段
	var isCheckSearch = '';
	var SearchVal = '0';
	if (isSearch == '1') {
		isCheckSearch = 'checked';
		SearchVal = '1';
	} else {
		isCheckSearch = '';
		SearchVal = '0';
	}
	obj.append('<div class="attrConfig">'
		+ '<label><input type="checkbox" value="' + SearchVal + '" name="is_search" ' + isCheckSearch + '>是否做查询字段</label>'
		+ '</div>');
	//是否显示字段
	var isCheckView = '';
	var ViewVal = '0';
	if (isView == '1') {
		isCheckView = 'checked';
		ViewVal = '1';
	} else {
		isCheckView = '';
		ViewVal = '0';
	}
	obj.append('<div class="attrConfig">'
		+ '<label><input type="checkbox" value="' + ViewVal + '" name="is_view" ' + isCheckView + '>是否显示字段</label>'
		+ '</div>');
	//展示类型
	var seleceHtml = $('<div class="attrConfig">'
		+ '<span>展示类型：</span>'
		+ '<select class="form-control" name="column_type" value="radio">'
		+ '<option value="text">单行文本框</option>'
		+ '<option value="password">密码框</option>'
		+ '<option value="textTime">日期框</option>'
		+ '<option value="radio" selected >单选框</option>'
		+ '<option value="checked" >多选框</option>'
		+ '<option value="select" >下拉选框</option>'
		+ '<option value="textarea">多行输入框</option>'
		+ '</select>'
		+ '</div>')
	obj.append(seleceHtml);
	if (isNoEmpty(type)) {
		seleceHtml.find('select').val(type);
	} else {
		seleceHtml.find('select').val('text');
	}
	//下拉选框、单选框、多选框的外键
	if (type == 'radio' || type == 'checked' || type == 'select') {
		relationHtml(obj, relationType, relationTable, relationField);
	}
}


//保存函数
$(document).on('click', '#savaBtn', function() {
	//表格信息
	var tableConfig = $('#tableConfig');
	var tableJson = {};
	var tableText = '';
	var tableRadio = '';
	var tableCheckbox = '';
	var tableSelect = '';
	var tableArea = '';
	var tablePassword = '';
	getForm(tableConfig, tableText, tableRadio, tableCheckbox, tableSelect, tableArea, tablePassword, tableJson);
	//属性信息
	var attrJson = [];
	var attrBoxList = $('#attrbox > .attrbox');
	$.each(attrBoxList, function(index, el) {
		var text = '';
		var password = '';
		var radio = '';
		var checkbox = '';
		var select = '';
		var area = '';
		var json = {};
		var inputaval = $(this);
		getForm(inputaval, text, radio, checkbox, select, area, password, json);
		attrJson.push(json);
	});
	tableJson.columns = attrJson;
	tableJson.table_id = ___id;
	var param = {
		'json' : JSON.stringify(tableJson)
	};
	//console.log(JSON.stringify(tableJson)+'999')
	ajaxSend('/bdp-web/viewTableCofig/add', param, function(data) {
		layer.msg(data.msg);
		window.location.href = '/bdp-web/PageConfig/index.html?id=1';
	});
})
//返回
$(document).on('click', '#backBtn', function() {
	window.history.back(-1);
})
//点击改变多选框的值
$(document).on('click', '.frameboxConfig input[type="checkbox"]', function() {
	if ($(this).is(':checked')) {
		$(this).val('1');
	} else {
		$(this).val('0');
	}
})
//勾选主键时
$(document).on('click', '.attrbox .attrConfig input[name="is_pk"]', function() {
	var pkField = $('#tableConfig').find('input[name="pk_field"]');
	var filed = $(this).parents('.attrbox').find('input[name="column_code"]');
	var sibling = $(this).parents('.attrbox').siblings('.attrbox').find('input[name="is_pk"]');
	if ($(this).is(':checked')) {
		sibling.val('0');
		sibling.prop('disabled', true);
		$(this).val('1');
		pkField.val(filed.val());
	} else {
		$(this).val('0');
		pkField.val('');
		$('.attrbox').find('input[name="is_pk"]').prop('disabled', false);
	}
})

//展示类型是多选框、单选框、下拉框显示column_data_type
$(document).on('change', '.attrbox select[name="column_type"] ', function() {
	var val = $(this).val();
	var parent = $(this).parents('.attrbox');
	var pkafield = parent.find('.attrConfigFk');
	pkafield.hide();	
	if (val == 'password' ||val == 'text' || val == 'textTime' || val == 'textarea') {
		pkafield.hide();
	} else {
		if(pkafield.length > 0){
			pkafield.show();
		}else{
			relationHtml(parent);
		}
		
	}
})

//外键字典拼接
function relationHtml(obj, relationType, relationTable, relationField) {
	var selectRelat = $('<div class="attrConfig attrConfigFk"><span>关联类型：</span>'
		+ '<select class="form-control" name="relation_type">'
		+ '<option value="dict">字典</option>'
		+ '<option value="fk">外键</option>'
		+ '</select>'
		+ '</div>');
	obj.append(selectRelat);
	if (isNoEmpty(relationType)) {
		selectRelat.find('select').val(relationType);
	} else {
		selectRelat.find('select').val('dict');
	}

	var tableVal = '';
	var filedVal = '';
	if (isNoEmpty(relationTable)) {
		tableVal = relationTable;
	} else {
		tableVal = '';
	}
	obj.append('<div class="attrConfig attrConfigFk"><span>表名：</span>'
		+ '<input class="form-control" type="text" name="relation_table" value="' + tableVal + '">'
		+ '</div>');
	if (isNoEmpty(relationField)) {
		filedVal = relationField;
	} else {
		filedVal = '';
	}
	var relHtml = $('<div class="attrConfig attrConfigFk"><span>字段名：</span>'
		+ '<input class="form-control" type="text" name="relation_field" value="">'
		+ '</div>');
	relHtml.find('input[name="relation_field"]').val(filedVal);
	obj.append(relHtml);
}