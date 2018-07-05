var unit; //金额单位
var metadataId;
$(function() {
	var auth_search = false; //是否有查询权限
	var auth_export = false; //是否有查询权限
	var listTdData = ''; //查询回来存储的数据
	var curPage = 1; //当前页码
	var pageSize = 10; //每页显示数
	var total = 0; //总记录数
	var totalPage = 0; //总页数
	var curPageSize = ''; //当前显示行数
	var templateId = GetQueryString("id");
	var menuCode = GetQueryString("menuCode");
	var param = {
		'id' : templateId,
	}
	ajaxSend('/bdp-web/easyQuery/selectEasyQueryTemplate', param, successCallBackFun);
	function successCallBackFun(data) {
		//console.log(JSON.stringify(data)+'数据查询')
		metadataId = data.metadata_id; //元数据ID
		var tables = data.tables; //表
		var groups = data.groups; //维度
		var conditions = data.conditions; //筛选条件
		var fields = data.fields; //字段
		var first_limit = data.first_limit; //是否特殊限制
		var is_summary = data.is_summary; //是否汇总
		var authOperations = undefined != data.authOperations ? data.authOperations : {}; //拥有的操作权限
		$.each(authOperations, function(index, el) {
			var operation_code = el.operation_code;
			if ("export" == operation_code) {
				auth_export = true;
			} else if ("search" == operation_code) {
				auth_search = true;
			} else if ("close" == operation_code) {
				auth_search = true;
				auth_export = true;
			}
		})
		//外键pk
		var metadataIdVal = {
			'metadata_id' : ''
		};
		if (isNoemty(conditions) != '') {
			$.each(conditions, function(index, el) {
				var typeField = el.field_type;
				if (typeField == 'pk') {
					if (metadataIdVal.metadata_id == '') {
						metadataIdVal.metadata_id = el.metadata_id;
					} else {
						metadataIdVal.metadata_id += ',' + el.metadata_id;
					}
				}
			})
		}
		//维度遍历
		if (isNoemty(groups) != '') {
			if (groups.length > 0) {
				$("#dimenFiled").show();
			}
			for (i = 1; i < groups.length + 1; i++) {
				var elem = $('<div class="easy-tab-item"><span>统计口径' + i + '</span></div>');
				var select = $('<select id="groups_' + i + '" class="form-control"><option value="">请选择</option></select>');
				$.each(groups, function(index, el) {
					select.append('<option value="' + el.field + '" id="' + el.id + '" group_table="' + el.group_table + '">' + el.field_name + '</option>')
				});
				elem.append(select);
				$('#dimenFiled .rule-con-list').append(elem);
			}
			var i = 1;
			$.each(groups, function(index, el) {
				if (el.is_default == 'true') {
					$("#groups_" + i).val(el.field);
					$("#groups_" + i).parent().attr('value', el.field);
					$("#groups_" + i).attr('group_table', el.group_table);
					i++;
				}
			});
		}
		//数据筛选	
		ajaxSend('/bdp-web/metadata/selectMetadataPK', metadataIdVal, function(pklist) {
			if (isNoemty(conditions) != '') {
				$("#conditions").show();
				$.each(conditions, function(index, el) {
					//typeField == 'number' || typeField == 'decimal'||typeField == 'dateTime'
					var seleTime = $('<div class="selectDiv" style="display:none;">'
						+ '<select class="form-control widthselect countSelect">'
						+ '<option value="=">等于</option>'
						+ '<option value="!=">不等于</option>'
						+ '<option value=">">大于</option>'
						+ '<option value="<">小于</option>'
						+ '<option value=">=">大于等于</option>'
						+ '<option value="<=">小于等于</option>'
						+ '<option value="between">区间</option>'
						+ '<option value="in">包含</option>'
						+ '<option value="notin">不包含</option>'
						+ '</select></div>');
					//typeField == 'varchar'
					var selevar = $('<div class="selectDiv" style="display:none;">'
						+ '<select class="form-control widthselect countSelect">'
						+ '<option value="=">等于</option>'
						+ '<option value="!=">不等于</option>'
						+ '<option value="like">匹配</option>'
						+ '<option value="start">开头包含</option>'
						+ '<option value="end">结尾包含</option>'
						+ '<option value="in">包含</option>'
						+ '<option value="notin">不包含</option>'
						+ '</select></div>');
					var is_fixed = el.is_fixed;
					var isNone = '';
					if (is_fixed == '1') {
						isNone = 'style="display:none"';
					}
					var button = $('<button class="btn btn-default moreIco dataSelect"><i class="moreIco-img"><image src="../resource/img/i-image.png"></image></i></button>');
					var typeField = el.field_type;
					var valule1 = isNoemty(el.value1);
					var valfield = isNoemty(el.valfield);
					var valule2 = isNoemty(el.value2);
					var cond = isNoemty(el.cond);
					var tips = isNoemty(el.tips);
					var elem = $('<div ' + isNone + ' class="easy-tab-item" condition_table="' + el.condition_table + '" field1="' + el.field1 + '" id="' + el.id + '" field_type="' + el.field_type + '">'
						+ '<span>' + el.field_name + '</span></div>');
					elem.attr({
						'cond' : cond,
						'valule1' : valule1,
						'valule2' : valule2,
						'valfield' : valfield
					})
					if (typeField == 'pk') {
						var selectPk = $('<select class="form-control widthselect countSelect" disabled style="display:none;">'
							+ '<option value="in">包含</option>'
							+ '</select>');
						elem.append(selectPk);
						var checkParent = $('<div class="input_close"><i class="closeInput closeIcon"></i></div>');
						elem.append(checkParent);
						var checkDown = $('<input class="form-control checkDown countVal" tag="' + valule1 + '" value="' + valfield + '">');
						var listCheckJson = pklist[el.metadata_id];
						var is_choic = true;
						if (el.is_choic == '1') {
							is_choic = false;
						}
						checkParent.append(checkDown);
						checkDown.hsCheckData({
							isShowCheckBox : is_choic, //默认为false
							selectOnceText : null,
							selectVal : null,
							selectText : null,
							selectAlltVal : null,
							data : listCheckJson
						})
						if(valule1 == '' && tips !=''){
							tipFun(checkParent,tips)
						}

					} else if (typeField == 'dateTime') {
						elem.append(seleTime);
						seleTime.find('option[value="in"]').remove();
						seleTime.find('option[value="notin"]').remove();
						var dateHtml = $('<div class="input_close"><input class="countVal form-control datepicker dateStar" value="' + valule1 + '">'
							+ '<i class="closeInput"></i></div>');
						elem.append(dateHtml);
						if (cond == 'between') {
							elem.append('<b>至</b>');
							var dateHtmlTwo = $('<div class="input_close"><input type="text" class="countVal1 form-control datepicker dateEnd" value="' + valule2 + '">'
								+ '<i class="closeInput"></i></div>')
							elem.append(dateHtmlTwo);
							if(valule2 == '' && tips !=''){
								tipFun(dateHtmlTwo,tips)
							}
						}
						elem.append(button);
						intDate();
						var coundVal = seleTime.find('option');
						$.each(coundVal, function() {
							var optVal = $(this).val();
							if (optVal == cond) $(this).attr('selected', 'selected');
						})
						if(valule1 == '' && tips !=''){
							tipFun(dateHtml,tips);
						}						
						
					} else if (typeField == 'number' || typeField == 'decimal' || typeField == 'numeric') {
						elem.append(seleTime);
						var chartInput = $('<div class="input_close">'
							+ '<input type="text" class="countVal form-control" value="' + valule1 + '" ><i class="closeInput"></i>'
							+ '</div>');
						elem.append(chartInput);
						if (cond == 'between') {
							elem.append('<b>至</b>');
							var chartInputTwo = $('<div class="input_close"><input type="text" class="countVal1 form-control" value="' + valule2 + '" ><i class="closeInput"></i></div>');
							elem.append(chartInputTwo);
							if(valule2 == '' && tips !=''){
								tipFun(chartInputTwo,tips)
							}
						}
						elem.append(button);
						var coundVal = seleTime.find('option');
						$.each(coundVal, function() {
							var optVal = $(this).val();
							if (optVal == cond) $(this).attr('selected', 'selected');
						})
						if(valule1 == '' && tips !=''){
							tipFun(chartInput,tips);
						}
					} else {
						elem.append(selevar);
						var chartInput = $('<div class="input_close">'
							+ '<input type="text" class="countVal form-control" value="' + valule1 + '"><i class="closeInput"></i>'
							+ '</div>');
						elem.append(chartInput);
						elem.append(button);
						var coundVal = selevar.find('option');
						$.each(coundVal, function() {
							var optVal = $(this).val();
							if (optVal == cond) $(this).attr('selected', 'selected');
						})
						if(valule1 == '' && tips !=''){
							tipFun(chartInput,tips);
						}
						
					}
					if (cond == 'between' && typeField == 'dateTime') {
						elem.find('input').addClass('queryInputWidthTime');
					} else if (cond == 'between' && (typeField == 'number' || typeField == 'decimal' || typeField == 'numeric')) {
						elem.find('input').addClass('queryInputWidth');
					} else {
						elem.find('input').removeClass('queryInputWidth');
						elem.find('input').removeClass('queryInputWidthTime');
					}
					$('#conditions .rule-con-list').append(elem);
					intDate();
				})
			} else {
				$("#conditions").hide();
			}
		});
		//显示字段
		if (isNoemty(fields) != '') {
			$('#money_class > div').html('');
			$('#date_class > div').html('');
			$('#customer_class > div').html('');
			$('#loan_number_class > div').html('');
			$('#other_class > div').html('');			
			$.each(fields, function(index, el) {
				var isCheck = '';
				var type = el.field_type;
				var aliasText = '';
				if (el.alias != '') {
					aliasText = '[' + el.alias + ']';
				}
				if (el.is_default == 'true') {
					isCheck = 'checked';
				}
				if (el.is_compute == 'true') {
					var oDiv = $('<div class="easy-tab-item">' + '<label>'
						+ '<input type="checkbox" value="1" ' + isCheck + '>'
						+ isNoemty(el.field_name) + '</label>'
						+ '</div>');
				} else {
					var oDiv = $('<div class="easy-tab-item">' + '<label>'
						+ '<input type="checkbox" value="1" ' + isCheck + '>'
						+ isNoemty(el.title_name) + isNoemty(aliasText) + isNoemty(el.field_eval) + '</label>'
						+ '</div>');
				}
				oDiv.attr({
					'id' : isNoemty(el.id),
					'order_type' : isNoemty(el.order_type),
					'is_total_field' : isNoemty(el.is_total_field),
					'field_type' : isNoemty(el.field_type),
					'metadata_id' : isNoemty(el.metadata_id),
					'name' : isNoemty(el.name),
					'func' : isNoemty(el.func),
					'alias' : isNoemty(el.alias),
					'field_name' : isNoemty(el.field_name),
					'show_form' : isNoemty(el.show_form),
					'field_unit_limit' : isNoemty(el.field_unit_limit),
					'field_decimal_point' : isNoemty(el.field_decimal_point),
					'field_unit_limit' : isNoemty(el.field_unit_limit),
					'is_compute' : isNoemty(el.is_compute),
					'compute_expression' : isNoemty(el.compute_expression),
					'field_table' : isNoemty(el.field_table),
					'is_default' : isNoemty(el.is_default),
					'column_type':isNoemty(el.column_type),
					'order_index':isNoemty(el.order_index)
				})
				oDiv.find('input[type="checkbox"]').attr({
					'id' : isNoemty(el.id),
					'order_type' : isNoemty(el.order_type),
					'is_total_field' : isNoemty(el.is_total_field),
					'field_type' : isNoemty(el.field_type),
					'metadata_id' : isNoemty(el.metadata_id),
					'name' : isNoemty(el.name),
					'func' : isNoemty(el.func),
					'alias' : isNoemty(el.alias),
					'field_name' : isNoemty(el.field_name),
					'show_form' : isNoemty(el.show_form),
					'field_unit_limit' : isNoemty(el.field_unit_limit),
					'field_decimal_point' : isNoemty(el.field_decimal_point),
					'field_unit_limit' : isNoemty(el.field_unit_limit),
					'is_compute' : isNoemty(el.is_compute),
					'compute_expression' : isNoemty(el.compute_expression),
					'field_table' : isNoemty(el.field_table),
					'is_default' : isNoemty(el.is_default),
					'column_type':isNoemty(el.column_type),
					'order_index':isNoemty(el.order_index)
				})
				var column_type = isNoemty(el.column_type);
				if(column_type == 'money_class'){
					$('#money_class > div').append(oDiv);
				}else if(column_type == 'date_class'){
					$('#date_class > div').append(oDiv);
				}else if(column_type == 'customer_class'){
					$('#customer_class > div').append(oDiv);
				}else if(column_type == 'loan_number_class'){
					$('#loan_number_class > div').append(oDiv);
				}else{
					$('#other_class > div').append(oDiv);
				}
				//$('#filedText > div').append(oDiv);
			})
		}
		if (first_limit == 'true') {
			$('.rankBox').css('display', 'inline-block');
		} else {
			$('.rankBox').hide();
			$('.rank').val('');
		}
		$('input[name="is_summary"]').val(is_summary);

	}

	//筛选条件
	$(document).on('click','.dataSelect',function(){
	//$('.dataSelect').click(function() {
		var dataSelect = '';
		var typeField = $(this).parent('.easy-tab-item').attr('field_type');
		var _this = $(this);
		var oldVal = $(this).siblings('div.selectDiv').find('select').val();
		dataSelect = $(this).siblings('div.selectDiv');
		var emHtml = $(this).siblings('div.input_close').find('em').text();		
		var showHtml = '<div id="currentSelect">' + dataSelect.html() + '</div>';
		layer.open({
			title : '筛选条件',
			closeBtn : 2,
			type : 1,
			area : [ 'auto', 'auto' ],
			shadeClass : true,
			content : showHtml,
			btn : [ '确定', '取消' ],
			btnAlign : 'c',
			closeBtn : 1,
			success : function(layero, index) {
				$('#currentSelect').find('select').val(oldVal);
			},
			yes : function(index, layero) {
				var text = $('#currentSelect').children('select.countSelect').val();
				var bewttHtml = $('<b>至</b> <div class="input_close">'
					+ '<input type="text" class="countVal1 form-control"><i class="closeInput"></i>'
					+ '</div>');
				var countValLen = _this.siblings('.input_close').find('.countVal1');
				if (text == 'between') {
					if (countValLen.length == 0) {
						if (typeField == 'dateTime') {
							bewttHtml.find('input').addClass('datepicker');
							bewttHtml.find('input').addClass('dateEnd');
							_this.before(bewttHtml);							
							intDate();
							var ValLen1 = _this.siblings('.input_close').find('.countVal1');
							if(ValLen1.length > 0){
								ValLen1.parent().append('<em>'+emHtml+'</em>');
							}							
						} else {
							_this.before(bewttHtml);
							var ValLen1 = _this.siblings('.input_close').find('.countVal1');
							if(ValLen1.length > 0){
								ValLen1.parent().append('<em>'+emHtml+'</em>');
							}
						}
					}
				} else {
					_this.siblings('.input_close').find('.countVal1').parent().remove();
					_this.siblings('b').remove();
				}

				if (text == 'between' && typeField == 'dateTime') {
					_this.siblings('.input_close').find('input').addClass('queryInputWidthTime');
				} else if (text == 'between' && (typeField == 'number' || typeField == 'decimal' || typeField == 'numeric')) {
					_this.siblings('.input_close').find('input').addClass('queryInputWidth');
				} else {
					_this.siblings('.input_close').find('input').removeClass('queryInputWidth');
					_this.siblings('.input_close').find('input').removeClass('queryInputWidthTime');
				}
				_this.siblings('div.selectDiv').find('select').val(text);
				layer.close(index); //如果设定了yes回调，需进行手工关闭
			},
			btn2 : function(index, layero) {
				//按钮【取消】的回调
			},
			cancel : function(index) {
				//【右上角关闭】的回调
			}
		});
	})
	//点击查询按钮
	$('#searchBtn').click(function() {
		if (!auth_search) { //没有权限查询
			layer.alert("该用户没有权限查询数据！");
			return false;
		}
		curPage = '1';
		selectData();
		var ruleConList = $('.easy-menu-listhide');
		var easyTabCon = $('#easyTabCon > div');
		$.each(ruleConList, function() {
			var sibling = $(this).siblings('em');
			if (sibling.children('b').text() == '收缩') {
				sibling.children('b').text('展开');
				$(this).css({
					'height' : '24px',
				});
				sibling.children('i').addClass('glyphicon-chevron-down').removeClass('glyphicon-chevron-up');
				fullScreen();
			}
			if (easyTabCon.is(':hidden')) {

			} else {
				easyTabCon.hide();
			}
		})

	})
	//点击导出按钮
	$('#exportBtn').click(function() {
		if (!auth_export) { //没有权限查询
			layer.alert("该用户没有权限导出数据！");
			return false;
		}
		curPage = '1';
		exportData();
	})

	//获取查询、导出json参数
	function paramJson(valJson) {
		//表头排序获取
		var thFieldSort = $('#thFiled > th');
		var thTitleSort = $('#thTitle > li');
		var orderArr = [];
		var fieldTextOrder = $('.easy-select-filed .easy-tab-item input[type="checkbox"]');
		if (thFieldSort.length == '0') {
			$.each(fieldTextOrder, function(index, el) {
				var order_type = $(this).attr('order_type');
				if (isNoemty(order_type) != '') {
					order_type = order_type;
				} else {
					order_type = '';
				}
				if ($(this).is(':checked') && order_type) {
					var fieldTextJsonList = {};
					fieldTextJsonList['field'] = $(this).attr('name');
					fieldTextJsonList['thId'] = $(this).attr('id');
					fieldTextJsonList['order_type'] = isNoemty($(this).attr('order_type'));
					fieldTextJsonList['is_compute'] = isNoemty($(this).attr('is_compute'));
					if ($(this).attr('alias') != '') {
						fieldTextJsonList['alias'] = $(this).attr('alias');
					} else {
						fieldTextJsonList['alias'] = $(this).attr('name');
					}
					orderArr.push(fieldTextJsonList);
				}
			})
			valJson['orders'] = orderArr;
		} else {
			$.each(thTitleSort, function(index, el) {
				var orderJson = {};
				if ($(this).attr('sort')) {
					orderJson['field'] = $(this).attr('code');
					orderJson['thId'] = $(this).attr('id');
					orderJson['order_type'] = isNoemty($(this).attr('sort'));
					orderJson['alias'] = $(this).attr('name');
					orderArr.push(orderJson);
				}
			})
			valJson['orders'] = orderArr;
		}
		//维度
		var dimenField = $('#dimenFiled > div > div > select');
		var dimenFieldJson = [];
		$.each(dimenField, function(index, el) {
			var dimenFieldJsonList = {};
			var fieldId = $(this).attr('id');
			var fieldVal = $(this).val();
			var fieldName = $(this).find('option:selected').text();
			var thId = $(this).find('option:selected').attr('id');
			var group_table = $(this).find('option:selected').attr('group_table');
			if (fieldName == '请选择') {
				fieldName = '';
			} else {
				fieldName
			}
			dimenFieldJsonList['id'] = fieldId;
			dimenFieldJsonList['thId'] = thId;
			dimenFieldJsonList['field'] = fieldVal;
			dimenFieldJsonList['field_name'] = fieldName;
			dimenFieldJsonList['group_table'] = group_table;
			dimenFieldJson.push(dimenFieldJsonList);
		})
		//筛选器
		var conditions = $('#conditions .easy-tab-item');
		var conditionsInput = $('#conditions .easy-tab-item .countVal');
		var isSize = false;
		$.each(conditionsInput, function(index, el) {
			var val = $(this).val();
			var val2 = $(this).parent('.input_close').siblings('.input_close').children('.countVal1').val();
			if (parseInt(val) - parseInt(val2) > 0) {
				isSize = true;
			}
		})
		if (isSize) {
			layer.alert('输入的 数值必须大于前一位！');
			return false;
		}
		var conditionsJson = [];
		var isNumber = false;
		var isTrue = false;
		$.each(conditions, function(index, el) {
			var conditionsJsonList = {};
			var text = $(this).children('span').text();
			var selectCount = $(this).find('select.countSelect').val();
			var field1 = $(this).attr('field1');
			var inputVal = $.trim($(this).find('.countVal').val());
			var inputValTag = $(this).find('.countVal').attr('tag');
			var inputVal2 = $.trim($(this).find('.countVal1').val());
			var fieldId = $(this).attr('id');
			var fieldType = $(this).attr('field_type');
			var condition_table = $(this).attr('condition_table');
			if (fieldType == 'pk' && inputVal != '') {
				conditionsJsonList['value1'] = inputValTag;
			} else {
				conditionsJsonList['value1'] = inputVal;
			}
			if (selectCount == 'between' && (fieldType == 'number' || fieldType == 'decimal')) {
				if (inputVal == '' && inputVal2 != '') {
					selectCount = '<=';
					conditionsJsonList['value1'] = parseInt(inputVal2);
					inputVal2 = '';
				} else if (inputVal != '' && inputVal2 == '') {
					selectCount = '>=';
				} else {
					selectCount == 'between';
				}
			} else if (selectCount == 'between' && fieldType == 'dateTime') {
				if (inputVal == '' && inputVal2 != '') {
					selectCount = '<=';
					conditionsJsonList['value1'] = inputVal2;
					inputVal2 = '';
				} else if (inputVal != '' && inputVal2 == '') {
					selectCount = '>=';
				} else {
					selectCount == 'between';
				}
			}
			//判断填写了筛选器的值就必须选择条件
			if (inputVal != '' && selectCount == '') {
				isTrue = true;
			}
			conditionsJsonList['field_name'] = text;
			conditionsJsonList['cond'] = selectCount;
			conditionsJsonList['value2'] = inputVal2;
			conditionsJsonList['id'] = fieldId;
			conditionsJsonList['field1'] = field1;
			conditionsJsonList['condition_table'] = condition_table;
			if ((fieldType == 'number' || fieldType == 'decimal') && selectCount != 'in' && selectCount != 'notin' && inputVal != '') { //number类型转数值
				inputVal = parseInt(inputVal);
				if (!isNaN(inputVal)) {
					conditionsJsonList['value1'] = inputVal;
					isNumber = false;
				} else {
					isNumber = true;
				}
			}
			if ((fieldType == 'number' || fieldType == 'decimal') && selectCount != 'in' && selectCount != 'notin' && inputVal2 != '') { //number类型转数值
				inputVal2 = parseInt(inputVal2);
				if (!isNaN(inputVal)) {
					conditionsJsonList['value2'] = inputVal2;
					//console.log(JSON.stringify(conditionsJsonList));
					isNumber = false;
				} else {
					isNumber = true;
				}
			}
			if (fieldType == 'number' && selectCount != 'in' && selectCount != 'notin' && inputVal2 != '') { //number类型转数值
				inputVal2 = parseInt(inputVal2);
				if (!isNaN(inputVal2)) {
					conditionsJsonList['value2'] = inputVal2;
					//console.log(JSON.stringify(conditionsJsonList));
					isNumber = false;
				} else {
					isNumber = true;
				}
			}
			if (selectCount == 'in' || selectCount == 'notin' && inputVal != '') {
				if (fieldType == 'number' || fieldType == 'decimal') { //number类型转数值
					var dataStrArr = inputVal.split(",");
					var dataIntArr = [];
					dataIntArr = dataStrArr.map(function(data) {
						return +data;
					});
					conditionsJsonList['value1'] = dataIntArr;
				} else if (fieldType == 'pk' && inputVal != '' && inputValTag != '') {
					conditionsJsonList['value1'] = inputValTag.split(",");
				} else if (inputVal != '') {
					conditionsJsonList['value1'] = inputVal.split(",");
				} else {
					conditionsJsonList['value1'] = [];
				}
				if (fieldType == 'number' || fieldType == 'decimal') { //number类型转数值
					var dataStrArr = inputVal2.split(",");
					var dataIntArr = [];
					dataIntArr = dataStrArr.map(function(data) {
						return +data;
					});
					conditionsJsonList['value2'] = dataIntArr;
				} else {
					conditionsJsonList['value2'] = inputVal2.split(",");
				}
			}
			conditionsJson.push(conditionsJsonList);
		})
		if (isNumber) {
			layer.alert('必须输入数字');
			return false;
		}
		if (isTrue) {
			layer.alert('请选择筛选条件！');
			return false;
		}
		//字段
		var fieldText = $('.easy-select-filed .easy-tab-item input[type="checkbox"]');
		var fieldTextJson = [];
		$.each(fieldText, function(index, el) {
			if ($(this).is(':checked')) {
				var fieldTextJsonList = {};
				var fieldId = isNoemty($(this).attr('id'));
				var titleName = isNoemty($(this).parent('label').text());
				var metadataId = isNoemty($(this).attr('metadata_id'));
				var func = isNoemty($(this).attr('func'));
				var alias = isNoemty($(this).attr('alias'));
				var name = isNoemty($(this).attr('name'));
				var fieldType = isNoemty($(this).attr('field_type'));
				var isTotalField = isNoemty($(this).attr('is_total_field'));
				var field_name = isNoemty($(this).attr('field_name'));
				var is_compute = isNoemty($(this).attr('is_compute'));
				var field_decimal_point = isNoemty($(this).attr('field_decimal_point'));
				var field_unit_limit = isNoemty($(this).attr('field_unit_limit'));
				var compute_expression = isNoemty($(this).attr('compute_expression'));
				var field_table = $(this).attr('field_table');
				var is_default = $(this).attr('is_default');
				fieldTextJsonList['field_table'] = field_table;
				fieldTextJsonList['id'] = fieldId;
				fieldTextJsonList['title_name'] = titleName;
				fieldTextJsonList['metadata_id'] = metadataId;
				fieldTextJsonList['field_type'] = fieldType;
				fieldTextJsonList['is_total_field'] = isTotalField;
				fieldTextJsonList['func'] = func;
				fieldTextJsonList['is_total_field'] = isTotalField;
				fieldTextJsonList['field_name'] = field_name;
				fieldTextJsonList['name'] = name;
				fieldTextJsonList['alias'] = alias;
				fieldTextJsonList['field_name'] = field_name;
				fieldTextJsonList['is_compute'] = is_compute;
				fieldTextJsonList['compute_expression'] = compute_expression;
				fieldTextJsonList['field_decimal_point'] = field_decimal_point;
				fieldTextJsonList['field_unit_limit'] = field_unit_limit;
				fieldTextJsonList['is_default'] = is_default;
				fieldTextJson.push(fieldTextJsonList);
			}
		})
		var all_fields = [];
		$.each(fieldText, function(index, el) {
			var fieldTextJsonList = {};
			var fieldId = isNoemty($(this).attr('id'));
			var titleName = isNoemty($(this).parent('label').text());
			var metadataId = isNoemty($(this).attr('metadata_id'));
			var func = isNoemty($(this).attr('func'));
			var alias = isNoemty($(this).attr('alias'));
			var name = isNoemty($(this).attr('name'));
			var fieldType = isNoemty($(this).attr('field_type'));
			var isTotalField = isNoemty($(this).attr('is_total_field'));
			var field_name = isNoemty($(this).attr('field_name'));
			var is_compute = isNoemty($(this).attr('is_compute'));
			var field_decimal_point = isNoemty($(this).attr('field_decimal_point'));
			var field_unit_limit = isNoemty($(this).attr('field_unit_limit'));
			var compute_expression = isNoemty($(this).attr('compute_expression'));
			var field_table = $(this).attr('field_table');
			var is_default = $(this).attr('is_default');
			fieldTextJsonList['is_default'] = is_default;
			fieldTextJsonList['field_table'] = field_table;
			fieldTextJsonList['id'] = fieldId;
			fieldTextJsonList['title_name'] = titleName;
			fieldTextJsonList['metadata_id'] = metadataId;
			fieldTextJsonList['field_type'] = fieldType;
			fieldTextJsonList['is_total_field'] = isTotalField;
			fieldTextJsonList['func'] = func;
			fieldTextJsonList['is_total_field'] = isTotalField;
			fieldTextJsonList['field_name'] = field_name;
			fieldTextJsonList['name'] = name;
			fieldTextJsonList['alias'] = alias;
			fieldTextJsonList['field_name'] = field_name;
			fieldTextJsonList['is_compute'] = is_compute;
			fieldTextJsonList['compute_expression'] = compute_expression;
			fieldTextJsonList['field_decimal_point'] = field_decimal_point;
			fieldTextJsonList['field_unit_limit'] = field_unit_limit;
			all_fields.push(fieldTextJsonList);
		})
		valJson['groups'] = dimenFieldJson;
		valJson['conditions'] = conditionsJson;
		valJson['fields'] = fieldTextJson;
		valJson['all_fields'] = all_fields;
		valJson['query_template_id'] = templateId;
		//模版名称
		var templateName = $('#' + templateId, window.parent.document).children("p").text();
		valJson['templateName'] = templateName;
		var firstInput = $.trim($('input[name="first_limit"]').val());
		if (firstInput != '') {
			valJson['first_limit'] = firstInput;
		}
		var valis_summary = $.trim($('input[name="is_summary"]').val());
		valJson['is_summary'] = valis_summary;

		var selectPage = $('#selectPage').val();
		var totalVar = $('#exportBtn').attr("totalVar");
		if (selectPage == '') {
			selectPage = '10';
			curPageSize = 10;
		} else if (selectPage != curPageSize) {
			curPage = '1';
			selectPage = selectPage;
			curPageSize = selectPage
		}
		valJson['curPage'] = curPage;
		valJson['pageSize'] = selectPage;
		valJson['totalPage'] = totalVar;
		valJson['metadata_id'] = metadataId;
		//表头字段展示
		var thTitleLi = $('#thTitle');
		thTitleLi.html('');
		var thField = $('#thFiled');
		thField.html('');
		var dimeJsonTd = [];
		$.each(dimenField, function(index, el) {
			var dimeListTd = {};
			var val = $(this).val();
			var text = $(this).find('option:selected').text();
			var thId = $(this).find('option:selected').attr('id');
			if (val == '') {
				text = '';
			} else {
				text = $(this).find('option:selected').text();
				dimeListTd['fieldText'] = text;
				dimeListTd['fieldData'] = val;
				dimeListTd['thId'] = thId;
				dimeJsonTd.push(dimeListTd);
			}
		})
		viewTh(dimeJsonTd, thField, orderArr, thTitleLi);
		var initCheck = $('.easy-select-filed > div input[type="checkbox"]');
		var initCheckArry = [];
		var isThTrue = false;
		getafield(initCheckArry, initCheck, isThTrue);
		 function sortprice(a,b){  
             return a.order_index-b.order_index  
          } 
		viewTh(initCheckArry.sort(sortprice), thField, orderArr, thTitleLi);
		//表头的宽度计算赋值		
		var tbodyCon = $('#tbodyCon');
		var thFieldTh = thField.find('th');
		var li = thTitleLi.find('li');
		var table = $('#table');
		numWidth(thFieldTh, li);
		tbodyCon.scroll(function() {
			moveTitle(table, thTitleLi)
		});
	}

	//点击查询函数
	function selectData() {
		unit = $(".unit").val();
		var paramSearch = {};
		paramJson(paramSearch);
		paramSearch['unit'] = unit;
		var is_remove = $('input[name="is_remove"]');
		if(is_remove.is(':checked')){
			paramSearch['is_remove'] = 'true';
		}else{
			paramSearch['is_remove'] = 'false';
		}
		//var is_remove = 
		var params = {
			"param" : JSON.stringify(paramSearch)
		};		
		//取合计的接口		
		var tdObj = $('#tdData');
		var ths = $("#thFiled").find("th");
		var tfList = $('#tfData');
		ajaxSend('/bdp-web/easyQuery/queryTotal', params, function(data) {
			tfList.html('');
			//console.log(JSON.stringify(data) + '取合计的接口');
			var totalResultCount = isNoemty(data.totalResult); //合计字段节点
			var Fieldtotal = isNoemty(totalResultCount.total); //总合计
			if (JSON.stringify(Fieldtotal) !='{}') {				
				var flag = false;				
				totalHtml(tfList, Fieldtotal, ths, flag);
				//表头的宽度计算赋值
				var thTitle = $('#thTitle');
				var tbodyCon = $('#tbodyCon');
				var li = thTitle.find('li');
				var table = $('#table');
				numWidth(ths, li);
				tbodyCon.scroll(function() {
					moveTitle(table, thTitle)
				});	
			}
		})		
		//查询数据和小计的接口
		ajaxSendload('/bdp-web/easyQuery/queryDataList', params, function(data) {
			tdObj.html('');
			//console.log(JSON.stringify(data) + '查询数据和小计的接口')
			var list = isNoemty(data.results);//获取的数据列表
			var totalResult = isNoemty(data.totalResult);//小计
			var pageTotal = isNoemty(totalResult.pageTotal);//小计
			tdObj.html('');			
			if(list !='' && list.length >0){				
				tdDataHtml(list, ths, tdObj);//数据列表拼接
				//小计
				if(JSON.stringify(pageTotal) !='{}'){
					var flag = true;
					totalHtml(tdObj, pageTotal, ths, flag);
				}				
				//表头的宽度计算赋值
				var thTitle = $('#thTitle');
				var tbodyCon = $('#tbodyCon');
				var li = thTitle.find('li');
				var table = $('#table');
				numWidth(ths, li);
				tbodyCon.scroll(function() {
					moveTitle(table, thTitle)
				});					
			}else {				
				tdObj.append('<tr><td colspan="' + ths.length + '" style="text-align:center;font-size:16px;">查询无相关数据！</td></tr>')
				//表头的宽度计算赋值	
				var thTitleLi = $('#thTitle');
				var tbodyCon = $('#tbodyCon');
				var li = thTitleLi.find('li');
				var table = $('#table');
				numWidth(ths, li);
				tbodyCon.scroll(function() {
					moveTitle(table, thTitleLi)
				});
			}	

			var objectList = data.objectList; //导出的最大限制数
			$("#exportBtn").attr("objectList", objectList[0]);
		})		
		//取总记录的接口
		ajaxSend('/bdp-web/easyQuery/queryTotalCount', params, function(data) {
			//console.log(JSON.stringify(data) + '取总记录的接口')
			total = data.total; //总记录数
			totalPage = data.totalPage; //总页数	
			getPageBar(curPage, pageSize, total, totalPage);
		})
	}

	//点击导出按钮
	function exportData() {
		var totalVar = parseInt($("#exportBtn").attr("totalVar"));
		var objectlist = parseInt($("#exportBtn").attr("objectlist"));
		if (totalVar <= objectlist && 0 != totalVar) {
			unit = $(".unit").val();
			var paramSearch = {};
			paramJson(paramSearch);
			paramSearch['unit'] = unit;
			var params = {
				"param" : JSON.stringify(paramSearch)
			};
			var token = getCookie('token');

			var iframe = $("<iframe>"); //定义一个iframe
			iframe.attr("style", "display:none");
			iframe.attr("name", "nm_iframe");
			$("body").append(iframe); //将iframe放置在web中

			var form = $("<form>"); //定义一个form表单
			form.attr("style", "display:none");
			form.attr("target", "nm_iframe");
			form.attr("method", "post");
			form.attr("action", '/bdp-web/easyQuery/exportEasyqueryData');
			var fileInput1 = $("<input>");
			fileInput1.attr("type", "hidden");
			fileInput1.attr("name", "token"); //设置属性的名字
			fileInput1.attr("value", token); //设置属性的值
			var fileInput2 = $("<input>");
			fileInput2.attr("type", "hidden");
			fileInput2.attr("name", "param"); //设置属性的名字
			fileInput2.attr("value", JSON.stringify(paramSearch)); //设置属性的值
			$("body").append(form); //将表单放置在web中
			form.append(fileInput1);
			form.append(fileInput2);
			form.submit(); //表单提交   
			form.remove();
		} else if (0 == totalVar) {
			layer.alert("导出数据为空！");
		} else if (totalVar > objectlist) {
			layer.alert("当前导出数据超过最大限制条数"+objectlist+"条！");
		} else {
			layer.alert("请先查询！");
		}



	}

	//分页点击事件
	$(document).on('click', '.page span a', function() {
		var rel = $(this).attr("rel");
		if (rel) {
			curPage = rel;
			selectData();
		}
	})

	//表头点击排序
	$(document).on('click', '#thTitle > li', function() {
		var is_compute = $(this).attr('is_compute');
		if (is_compute != 'true') {
			var thField = $('#thFiled').find('th');
			var thIndex = $(this).index();
			$(this).siblings('li').find('i').remove();
			$(this).siblings('li').find('b').remove();
			$(this).siblings('li').removeAttr('sort');
			thField.find('i').remove();
			thField.find('b').remove();
			thField.removeAttr('sort');
			var iElem = $(this).children('i');
			var bElem = $(this).children('b');
			var iElemTh = thField.eq(thIndex).children('i');
			var bElemTh = thField.eq(thIndex).children('b');
			if (iElem.length <= 0) {
				$(this).append('<i class="fa fa-long-arrow-up"></i>');
				$(this).append('<b>(升序)</b>');
				$(this).attr('sort', 'asc');

				thField.eq(thIndex).append('<i class="fa fa-long-arrow-up"></i>');
				thField.eq(thIndex).append('<b>(升序)</b>');
				thField.eq(thIndex).attr('sort', 'asc');

			} else {
				if (iElem.hasClass('fa-long-arrow-up')) {
					iElem.addClass('fa-long-arrow-down').removeClass('fa-long-arrow-up');
					bElem.text('(降序)');
					$(this).attr('sort', 'desc');

					iElemTh.addClass('fa-long-arrow-down').removeClass('fa-long-arrow-up');
					bElemTh.text('(降序)');
					thField.eq(thIndex).attr('sort', 'desc');

				} else {
					iElem.addClass('fa-long-arrow-up').removeClass('fa-long-arrow-down');
					bElem.text('(升序)');
					$(this).attr('sort', 'asc');

					iElemTh.addClass('fa-long-arrow-up').removeClass('fa-long-arrow-down');
					bElemTh.text('(升序)');
					thField.eq(thIndex).attr('sort', 'asc');
				}
			}
			curPage = '1';
			selectData();
		} else {
			layer.alert('计算字段不能排序！')
		}
	})
	//数据筛选文本框删除
	$(document).on('click', '.closeInput', function(event) {
		event.stopPropagation();
		var sib = $(this).siblings('input');
		sib.val('');
		sib.attr('tag', '');
		$(this).hide();
		$(this).siblings('em').show();
		$('#checkListLi').find('input').each(function(index, el) {
			if ($(el).attr('checked') == 'checked') {
				$(el).attr('checked', false);
			}
		})
	})
	$(document).on('focus', '.input_close > input', function(event) {
		var sib = $(this).siblings('i');
		sib.show();
	})
	$(document).on('change', '.input_close > input', function(event) {
		var sib = $(this).siblings('i');
		if ($(this).val() != '') {
			sib.show();
		} else {
			sib.hide();
		}
	})


})

//日期区间选框值
$(document).on('click', '.dateStar', function() {
	var sibData = $(this).parent('.input_close').siblings('.input_close').find('.dateEnd');
	//console.log(sibData.val() + '结束日期');
	$(this).datetimepicker("setEndDate", sibData.val())
});
$(document).on('click', '.dateEnd', function() {
	var sibData = $(this).parent('.input_close').siblings('.input_close').find('.dateStar');
	//console.log(sibData.val() + '开始日期');
	$(this).datetimepicker("setStartDate", sibData.val())
});
//表头选取的字段
function viewTh(arr, obj, orderArr, liObj) {
	//console.log(JSON.stringify(arr)+'---###')
	$.each(arr, function(index, el) {
		var isOrder = false;
		var sort = '';
		$.each(orderArr, function(i, o) {
			if (o.thId == el.thId) {
				isOrder = true;
				sort = o.order_type;
			}
		});
		var text = el.fieldText;
		if (el.alias && el.alias != 'undefined' && el.alias != '') {
			text = el.alias;
		} else {
			el.alias = el.fieldData;
		}
		var thHead = $('<th name="' + el.alias + '" is_compute="' + el.is_compute + '" code="' + el.fieldData + '" id="' + el.thId + '" field_type="' + el.data_type + '" show_form="' + el.show_form + '" field_unit_limit="' + el.field_unit_limit + '" field_decimal_point="' + el.field_decimal_point + '">' + text + '</th>');
		var liHtml = $('<li name="' + el.alias + '" is_compute="' + el.is_compute + '" code="' + el.fieldData + '" id="' + el.thId + '" field_type="' + el.data_type + '" show_form="' + el.show_form + '" field_unit_limit="' + el.field_unit_limit + '" field_decimal_point="' + el.field_decimal_point + '">' + text + '</li>');
		//判断是不是默认就有了排序有就push进orderArr
		if ('asc' == el.orderType && orderArr.length == 0) {
			var orderJson = {};
			orderJson['field'] = el.fieldData;
			orderJson['thId'] = el.thId;
			orderJson['order_type'] = 'asc';
			orderArr.push(orderJson);
			thHead = $('<th name="' + el.alias + '" is_compute="' + el.is_compute + '" code="' + el.fieldData + '" id="' + el.thId + '" sort="asc" field_type="' + el.data_type + '" show_form="' + el.show_form + '" field_unit_limit="' + el.field_unit_limit + '" field_decimal_point="' + el.field_decimal_point + '">' + text + '</th>');
			thHead.append('<i class="fa fa-long-arrow-up"></i>');
			thHead.append('<b>(升序)</b>');
			liHtml = $('<li name="' + el.alias + '" is_compute="' + el.is_compute + '" code="' + el.fieldData + '" id="' + el.thId + '" sort="asc" field_type="' + el.data_type + '" show_form="' + el.show_form + '" field_unit_limit="' + el.field_unit_limit + '" field_decimal_point="' + el.field_decimal_point + '">' + text + '</li>');
			liHtml.append('<i class="fa fa-long-arrow-up"></i>');
			liHtml.append('<b>(升序)</b>');

		} else if ('desc' == el.orderType && orderArr.length == 0) {
			var orderJson = {};
			orderJson['field'] = el.fieldData;
			orderJson['thId'] = el.thId;
			orderJson['order_type'] = 'desc';
			orderArr.push(orderJson);
			thHead = $('<th name="' + el.alias + '" is_compute="' + el.is_compute + '" code="' + el.fieldData + '" id="' + el.thId + '" sort="desc" field_type="' + el.data_type + '" show_form="' + el.show_form + '" field_unit_limit="' + el.field_unit_limit + '" field_decimal_point="' + el.field_decimal_point + '">' + text + '</th>');
			thHead.append('<i class="fa fa-long-arrow-down"></i>');
			thHead.append('<b>(降序)</b>');
			liHtml = $('<li name="' + el.alias + '" is_compute="' + el.is_compute + '" code="' + el.fieldData + '" id="' + el.thId + '" sort="desc" field_type="' + el.data_type + '" show_form="' + el.show_form + '" field_unit_limit="' + el.field_unit_limit + '" field_decimal_point="' + el.field_decimal_point + '">' + text + '</li>');
			liHtml.append('<i class="fa fa-long-arrow-down"></i>');
			liHtml.append('<b>(降序)</b>');
		}
		obj.append(thHead);
		liObj.append(liHtml);
		if (isOrder) {
			thHead.attr('sort', sort);
			liHtml.attr('sort', sort);
			if (sort == 'asc') {
				thHead.append('<i class="fa fa-long-arrow-up"></i>');
				thHead.append('<b>(升序)</b>');
				liHtml.append('<i class="fa fa-long-arrow-up"></i>');
				liHtml.append('<b>(升序)</b>');
			} else if (sort == 'desc') {
				thHead.append('<i class="fa fa-long-arrow-down"></i>');
				thHead.append('<b>(降序)</b>');
				liHtml.append('<i class="fa fa-long-arrow-down"></i>');
				liHtml.append('<b>(降序)</b>');
			}
		}
	})
}
//查询回来数据的显示
function tdDataHtml(data, thElem, obj) {
	$.each(data, function(index, el) {
		var row = $('<tr></tr>');
		$.each(thElem, function(i, elTh) {
			var key = $(this).attr("name"); //获取key
			var show_form = $(this).attr("show_form"); //获取数值显示格式
			var field_unit_limit = $(this).attr("field_unit_limit"); //使用单位
			var field_decimal_point = $(this).attr("field_decimal_point"); //小数点位数
			var tdValue = el[key];
			if (isNoemty(tdValue) == '') {
				tdValue = ''
			} else {
				tdValue = el[key];
			}
			tdValue = fieldShowForm(show_form, tdValue);
			row.append('<td name="' + key + '">' + tdValue + '</td>');
		})
		obj.append(row);
	})
}
//清空筛选器
$(document).on('click', '#cleatCondition', function() {
	var isDivNone = $('#conditions').find('.easy-tab-item');
	$.each(isDivNone, function() {
		var field_type = $(this).attr('field_type');
		var valule1 = $(this).attr('valule1');
		var valule2 = isNoemty($(this).attr('valule2'));
		var cond = $(this).attr('cond');
		var valfield = $(this).attr('valfield');
		//var spanText = $(this).children('span').text();
		//var condText = condFun(cond);
		if ($(this).is(':hidden')) {
		} else {
			if (valule2 == '' && cond != 'between') {
				$(this).find('.countVal1').parent('.input_close').remove();
				$(this).find('.countVal').parent('.input_close').siblings('b').remove();
			}
			if (field_type == 'pk') {
				$(this).find('.countVal').val(valfield);
				$(this).find('.countVal').attr('tag', valule1);
			} else {
				$(this).find('.countVal').val(valule1);
			}
			$(this).find('.countVal1').val(valule2);
			$(this).find('.countSelect').val(cond);
			var countValLen = $(this).find('.countVal1');
			var bewttHtml = $('<b>至</b> <div class="input_close">'
				+ '<input type="text" class="countVal1 form-control" value="' + valule2 + '"><i class="closeInput"></i>'
				+ '</div>');
			if (countValLen.length == '1') {
			} else {
				if (cond == 'between' && field_type == 'dateTime') {
					$(this).find('.countVal1').parent('.input_close').remove();
					$(this).find('.countVal').parent('.input_close').siblings('b').remove();
					bewttHtml.find('input').addClass('datepicker');
					bewttHtml.find('input').addClass('dateEnd');
					$(this).find('button').before(bewttHtml);
					intDate();
				} else if (cond == 'between' && (field_type == 'number' || field_type == 'decimal')) {
					$(this).find('.countVal1').parent('.input_close').remove();
					$(this).find('.countVal').parent('.input_close').siblings('b').remove();
					$(this).find('button').before(bewttHtml);
				} else {
					$(this).find('.countVal1').parent('.input_close').remove();
					$(this).find('.countVal').parent('.input_close').siblings('b').remove();
				}
			}
		}
	})
	var dimenDefault = $('#dimenFiled').find('.easy-tab-item');
	$.each(dimenDefault, function(index, el) {
		var val = $(this).attr('value');
		var select = $(this).find('select');
		select.val(val);
	})
})
//合计html
function totalHtml(obj, countData, thData, flag) {
	var trTotal = $('<tr></tr>');	
	$.each(thData, function(index, el) {		
		var keyTh = $(this).attr("name"); //获取key
		var show_form = $(this).attr("show_form"); //获取数值显示格式
		var field_unit_limit = $(this).attr("field_unit_limit"); //使用单位
		var field_decimal_point = $(this).attr("field_decimal_point"); //小数点位数
		var keyVal = '';
		var value= '';
		for (var key in countData) {
			keyVal = key;
			var tdValue = countData[keyTh];
			if (keyVal == keyTh) {
				value = isNoemty(tdValue);
			}
		}
		var text = '';
		var isHide = '';
		if (value != '') {
			isHide = 'display:inline-block';
		} else {
			isHide = 'display:none';
			value = '';
		}
		value = fieldShowForm(show_form, value);
		if (flag == true) {
			text = '小计';
		} else {
			text = '总合计';
		}		
		trTotal.append('<td><b style="' + isHide + '">' + text + '：</b>' + value + '</td>');
	});
	obj.append(trTotal);
}
//判断第二个输入值不能小于第二个
$(document).on('blur', '.countVal1', function() {
	var val = $(this).val();
	var val2 = $(this).siblings('.countVal').val();
	if (parseInt(val) - parseInt(val2) < 0) {
		layer.alert('输入的 数值必须大于前一位！');
		return false;
	}
})
//计算固定表头的宽度函数
function numWidth(thData, liData) {
	$.each(thData, function(index, el) {
		var widthTh = $(this).outerWidth();
		$.each(liData, function(num, liEl) {
			var _this = $(this);
			if (index == num) {
				_this.outerWidth(widthTh);
			}
		})
	})
}
//滚动条移动表头移动
function moveTitle(tableElem, titleEl) {
	var tableLeft = tableElem.offset().left;
	if (tableLeft == '1') {
		titleEl.css('marginLeft', '0');
	} else {
		titleEl.css('marginLeft', tableLeft);
	}
}
$(document).on('click', '#table tbody >tr', function() {
	if (!$(this).attr('class')) {
		$(this).attr('class', 'chose');
		return false;
	}
	$(this).attr('class') == 'chose' ? $(this).attr('class', '') : $(this).attr('class', 'chose');
})
//提示语
function tipFun(obj,val){
	var emElem = $('<em></em');	
	emElem.html(val);							
	var inputElem = obj.find('input');							
	obj.append(emElem);							
	$(inputElem).focus(function(){
		$(this).siblings('em').hide();
		
     })
    $(inputElem).blur(function(){
       var val = $(this).val();
        if(val == ""){
        	$(this).siblings('em').show();					              
        }
    })  
}
$(document).on('click','.input_close > em',function(){
	$(this).hide();
    $(this).siblings('input[type="text"]').trigger("click");
    $(this).siblings('input[type="text"]').trigger("focus");
})

