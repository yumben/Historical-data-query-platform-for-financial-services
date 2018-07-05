//全选维度、输出属性字段属性值
//var selectAllfiledJson = [];
$(function() {	
	var templateId = GetQueryString("id");
	var param = {
		'id' : templateId,
	}
	var selectAllField = ''; //表字段
	//判断是修改还是新增
	if (templateId) {
		//alert('修改')
		ajaxSendload('/bdp-web/easyQuery/definitionGet', param, editSucess);
	} else {
		calFunTable();		
	}
	//维度释放生成
	dimen.ondragover = function(ev) {
		ev.preventDefault();
	}
	dimen.ondrop = function(ev) {
		ev.preventDefault();
		dragRelease(ev, groupsDrop, dimen);
	}
	//输出属性释放生成
	outAttr.ondragover = function(ev) {
		ev.preventDefault();
	}
	outAttr.ondrop = function(ev) {
		ev.preventDefault();
		dragRelease(ev, fieldsDrop, outAttr);
		$('#outAttr > li').arrangeable();
		sortIndex(outAttr);
		fullScreen();
		 
	}
	//筛选器释放生成
	ruleScreen.ondragover = function(ev) {
		ev.preventDefault();
	}
	ruleScreen.ondrop = function(ev) {
		ev.preventDefault();
		dragScreen(ev, conditionsDrop, ruleScreen);	
		sortIndexScreen(ruleScreen);
	}
	//修改回显回调函数
	function editSucess(data) {	
		//console.log(JSON.stringify(data)+'修改回显查询定义');
		var list = data.resultJson;
		var querytemplate = list.querytemplate;
		$('#metadata_id').val(querytemplate.metadata_id);
		$('#query_code').val(querytemplate.query_code);
		$('#query_name').val(querytemplate.query_name);
		$('#query_des').val(querytemplate.query_des);
		$('#query_type').val(querytemplate.query_type);
		if (querytemplate.first_limit == 'true') {
			$('.special input[name="first_limit"]').prop('checked', true);
			$('.special input[name="is_summary"]').prop('disabled', true);
		}
		if(querytemplate.is_summary == 'true'){
			$('.special input[name="is_summary"]').prop('checked', true);
			$('.special input[name="first_limit"]').prop('disabled', true);
		}
		//表
		var tables = querytemplate.tables;		
		if(tables && tables.length > 0){
			$.each(tables, function(index, el) {
				var objAttr = tablesBack(el);				
				var newLi = $('<li>'+ el.table_title +'<i class="fa fa-close"></i></li>').attr(objAttr);
				$('#treeObj').append(newLi);

			})
		}
		//表所有字段
		//触发“查询表”按钮
		calFunTable();
		calFunField();		
		//维度
		var groups = querytemplate.groups;
		//console.log(JSON.stringify(groups)+'维度字段');
		if (groups) {
			if (groups.length > 0) {
				$.each(groups, function(index, el) {
					var objAttr = groupsBack(el);					
					creatLi(objAttr, dimen);					
				})
			}
		}
		//输出属性
		var fields = querytemplate.fields;		
		if (fields) {
			var ruleDetailList = $('#ruleDetailList'); //展示字段容器
			if (fields.length > 0) {				
				$.each(fields, function(index, el) {
					var objAttr = fieldsBack(el);
					creatLi(objAttr, outAttr);
					$('#outAttr > li').arrangeable();
				})
			}
		}
		//筛选器
		var conditions = querytemplate.conditions;
		//console.log(JSON.stringify(conditions)+'@####')
		if (conditions) {
			if (conditions.length > 0) {
				$.each(conditions, function(index, el) {
					var objAttr = conditionsBack(el);					
					creatScreenHtml(objAttr, ruleScreen);					
				})
			}
		}
	}

	//保存
	$('#btnsave').click(function() {		
		var showBox = $('#saveEasy');
		var param = {};
		getSaveData(param, showBox);
	})
	//保存的参数获取
	function getSaveData(param, obj) {
		var outAttrLi = $('#outAttr');
		sortIndex(outAttrLi);
		//获取表属性		
		var tableJson = [];
		var tableLi = $('#treeObj>li');
		tableLi.each(function(){
			getTablesAttr($(this),tableJson);			
		})
		//获取维度字段
		var definJson = [];
		var dimen = $('#dimen > li');
		dimen.each(function() {
			getGroupsAttr($(this),definJson);			
		})
		//获取输出属性字段
		var outAttrJson = [];
		var outAttr = $('#outAttr > li');
		outAttr.each(function() {
			getfieldsAttr($(this),outAttrJson);			
		})
		//判输出属性跟维度是否相等
		var isCompare = false;
		//console.log(JSON.stringify(definJson)+'!!@@@'+JSON.stringify(outAttrJson))
		$.each(definJson, function(index, el) {			
			var fieldName = el.field_name;
			$.each(outAttrJson, function(indexAttr, elAttr) {
				var titleName = elAttr.title_name;
				var alias = elAttr.alias;
				var text = '';
				if (isNoemty(alias) != '') {
					text = alias;
				} else {
					text = titleName;
				}
				if (fieldName == text) {
					isCompare = true;
				}
			})
		})
		var arr = [];
		$.each(definJson, function(index, el) {
			arr.push(el);
		});
		$.each(outAttrJson, function(index, el) {
			arr.push(el);
		});
		var res = isHavSame(arr);
		
		if (res.code=='1') {
			layer.alert('输出属性名称或者维度名称有相同：'+res.msg);
			return false;
		}
		//获取筛选条件字段
		var ruleScreenJson = [];
		var ruleScreen = $('#ruleScreen > div');
		var isNumber = false;
		ruleScreen.each(function() {
			var screenJSon = {};
			var fieldType = isNoemty($(this).attr('field_type'));
			var selectCount = isNoemty($(this).attr('cond'));
			var inputVal = isNoemty($(this).attr('value1'));	
			var inputVal2 = isNoemty($(this).attr('value2'));
			var tips = isNoemty($(this).attr('tips'));
			screenJSon['metadata_id'] = $(this).attr('metadata_id');
			screenJSon['field1'] = $(this).attr('field1');
			screenJSon['value1'] = inputVal;
			screenJSon['value2'] = inputVal2;
			screenJSon['cond'] = selectCount;
			screenJSon['is_fixed'] = isNoemty($(this).attr('is_fixed'));
			screenJSon['field_name'] = isNoemty($(this).children('h5').find('span').text());
			screenJSon['field_type'] = fieldType;			
			screenJSon['is_choic'] = isNoemty($(this).attr('is_choic'));
			screenJSon['order_index'] = isNoemty($(this).attr('order_index'));
			screenJSon['condition_table'] = isNoemty($(this).attr('condition_table'));
			screenJSon['parent_metadata'] = isNoemty($(this).attr('parent_metadata'));
			screenJSon['valfield'] = isNoemty($(this).attr('valfield'));
			screenJSon['dynamic_field'] = isNoemty($(this).attr('dynamic_field'));
			screenJSon['tips'] = tips;
			ruleScreenJson.push(screenJSon);
			//getconditionsAttr($(this),ruleScreenJson,isNumber);		
		})
		param = {
			'tables' : tableJson,
			'groups' : definJson,
			'fields' : outAttrJson,
			'conditions' : ruleScreenJson
		}
		//特殊限制
		var special = $('.special label input[type="checkbox"]');
		var firstVal = '';
		$.each(special, function(index, el) {
			var attrVal = $(this).attr('name');
			if ($(this).is(':checked')) {
				firstVal = 'true';
				param[attrVal] = firstVal;
			} else {
				firstVal = 'false';
				param[attrVal] = firstVal;
			}
		});
		
		//判断没有输出属性
		if (outAttrJson == '') {
			layer.alert('输出属性不能为空！');
			return false;
		}
		//判断有维度输出属性没有求均值的判断		
		var iss = true;
		$.each(outAttr, function(index, el) {			
			var funcVal = $(this).attr('func');
			var is_compute = $(this).attr('is_compute');
			//console.log(funcVal+'////')
			if(is_compute != 'true'){
				if (definJson != '') {
					if (funcVal == '' || funcVal == 'undefined' || funcVal == undefined || funcVal == null || funcVal == 'null') {
						iss = false;
					}
				}
			}			
		})
		if (!iss) {				
			layer.alert('有输出属性未做聚合计算！');			
			return false;
		}
		//判断别名不能跟字段名相同
		var attrField = outAttr.children('span');
		var isAlias = true;
		$.each(outAttr, function(index, el) {
			var attrAlias = $(this).attr('alias');
			$.each(attrField, function(index, el) {
				var attrAliasText = $(this).text();
				if (attrAlias == attrAliasText) {
					isAlias = false;
				}
			})
		})
		if (!isAlias) {
			layer.alert('别名不能与输出属性字段相同！')
			return false;
		}
		//判断输出属性有一个字段有聚合运算所有字段必须选聚合运算		
		var funcount = 0;
		var isHavFunc = false;
		$.each(outAttr, function(index, el) {
			var funcVal = $(this).attr('func');
			var is_compute = $(this).attr('is_compute');
			if(is_compute != 'true'){
				if (funcVal != '' && funcVal != 'undefinde' && funcVal != 'null' && funcVal != null && funcVal != undefined) {
					isHavFunc = true;
					funcount++;
				}
			}
			
		})
		var isOper = false;
		var funNum = $('#outAttr li[is_compute="false"]');
		if (isHavFunc) {
			if (!(funcount == funNum.length)) {
				isOper = true;
			}
		}
		if (isOper) {
			//console.log('***!!!!');
			layer.alert('有输出属性未做聚合计算！')
			return false;
		}		
		var treeObj = $('#treeObj > li');
		var metadataId = treeObj.eq(0).attr('parent_code');
		param['metadata_id'] = metadataId;
		$('#metadata_id').val(metadataId);		
		saveDilog(obj, param);
	}
	//保存的弹窗
	function saveDilog(obj, param) {
		//console.log(JSON.stringify(param)+'保存参数')
		layer.open({
			title : '保存模板',
			type : 1,
			area : [ 'auto', 'auto' ],
			btn : [ '确定', '取消' ],
			btnAlign : 'c',
			content : obj,
			yes : function(index, layero) {
				var metadataIdVal = $.trim($('#metadata_id').val());
				var queryCode = $.trim($('#query_code').val());
				var queryName = $.trim($('#query_name').val());
				var queryDes = $.trim($('#query_des').val());
				var queryType = $.trim($('#query_type').val());
				var queryTypeName = $('#query_type option:selected').text();				
				if ($.trim(queryName) == '') {
					layer.alert('请输入查询名称！');
					return false;
				}
				if ($.trim(queryTypeName) == '') {
					layer.alert('请输入查询类型！')
					return false;
				}
				if (templateId != '') {
					param['id'] = templateId;
				}
				param['metadata_id'] = metadataIdVal;
				param['query_code'] = queryCode;
				param['query_type_name'] = queryTypeName;
				param['query_name'] = queryName;
				param['query_des'] = queryDes;
				param['query_type'] = queryType;
				//console.log(JSON.stringify(param)+'保存----');
				var params = {
					"param" : JSON.stringify(param)
				};
				ajaxSendload('/bdp-web/easyQuery/definitionSave', params, function(data) {	
					if(data.ret_code == '0'){
						//layer.alert('保存成功！',function(){
						window.location.href = '/bdp-web/flexiblequery/flexiblequery-index.html';
						//});						
					}else{
						layer.alert('保存失败！');
					}
				});
				layer.close(index); //如果设定了yes回调，需进行手工关闭

			},
			cancel : function(index, layero) {
				layer.close(index)
			}
		});
	}

	//全选字段
	$(document).on('click', '.rule-con > span > i', function() {
		var obj = $(this);
		var treeObjNum = $('#treeObj > li');		
		if(treeObjNum.length =='0'){
			layer.alert('请先选择业务对象！');
			return false;
		}else{
			selectLayer(obj);
		}

	})
	//全选弹出函数
	//全选弹出函数
	function selectLayer(obj) {
		var __id = obj.parents('.rule-con').children('.rule-con-list').children('ul').attr("id"); //选择字段的字段要显示的区域
		var boolVal = false;
		if (__id == 'outAttr') {
			boolVal = true;
		}
		var viewField = document.getElementById(__id);
		//获取原有维度、输出属相已选择的字段
		var viewFieldList = obj.parents('.rule-con').children('.rule-con-list').children('ul').children('li');
		var viewFieldJson = [];		
		var modelJSON = {'title_name':'',
						'metadata_id':'',
						'name':'',
						'alias':'',
						'func':'',
						'title_name':'',
						'field':'',
						'is_total_field':'',
						'data_type':'',
						'field_type':'',
						'order_type':'',
						'order_index':'',
						'show_form':'',
						'field_unit_limit':'',
						'field_decimal_point':'',
						'is_default':'',
						'is_compute':'',
						'compute_expression':'',
						'parent_metadata':'',
						'table_code':'',
						'field_table':'',
						'group_table':'',
						'column_type':''
						
					};
		$.each(viewFieldList, function(index, el) {				
			var temp=getOnjAttr($(this),modelJSON);
			temp['field_eval'] = $(this).children('b').text();	
			viewFieldJson.push(temp);
			
		});
		//console.log(JSON.stringify(viewFieldJson) + '#$$3333$')
		//弹窗的分组字段
		$('.easy-select-filed > span input[type="checkbox"]').prop('checked', false);
		var content = $('#selectAllFiled');
		var selectAllCon = $('.selectAllCon');
		selectAllCon.html('');
		var choseTree = $('#treeObj').children('li');
		var param = {
				'table':''
		};
		$.each(choseTree,function(index,item){
			var val = $(this).attr('metadata_id');
			if(param.table == ''){
				param.table = val;
			}else{
				param.table += ','+ val;
			}	
		})
		//console.log(JSON.stringify(param)+'!!!')
		ajaxSend('/bdp-web/metadata/selectObjAndPropertie', param, function(data){
			var list = data.list;
			$.each(list,function(index,el){
				var divCon = $('<div class="easy-select-filed"></div>');
				selectAllCon.append(divCon);
				var spanHtml = $('<span><label><input type="checkbox"><i class="fa fa-file-text"></i> '+el.metadata_name+'</label></span>');
				divCon.append(spanHtml);
				var divChild = $('<div></div>');
				divCon.append(divChild);
				var columns = el.columns;				
				$.each(columns, function(indexcol, elcol) {
					var isCheck = '';
					$.each(viewFieldJson, function(indexvie, elvie) {
						if (elcol.column_name == elvie.title_name && el.metadata_id ==  elvie.parent_metadata) {
							isCheck = 'checked';
						}
					})
					var type = elcol.data_type;			
					var div = $('<div class="easy-tab-item"></div>');
					var label = $('<label><input type="checkbox" ' + isCheck + '>' + elcol.column_name + '</label>');
					div.append(label);
					div.attr({
						'metadata_id' : elcol.metadata_id,
						'name' : elcol.column_code,
						'alias' : '',
						'title_name' : elcol.column_name,
						'is_total_field' : '0',
						'data_type' : type,
						'order_index' : elcol.order_index,
						'is_default' : elcol.is_default,
						'is_compute':'false',
						'parent_metadata':el.metadata_id,
						'table_code':el.table_code,
						'column_type':elcol.column_type
					});
					divChild.append(div);
				})
			})
		});		
		//全选的回显
		var selectAllVal = $('.easy-select-filed .easy-tab-item').find('input[type="checkbox"]');
		$.each(selectAllVal, function(index, el) {
			var allSelect = $(this).parents('.easy-select-filed').children('span').find('input[type="checkbox"]');
			if ($(this).is(':checked')) {
				allSelect.prop('checked', true);
			} else {
				allSelect.prop('checked', false);
			}
		})
		//console.log(JSON.stringify(viewFiledJson)+'%%%');
		layer.open({
			type : 1,
			area : [ '860px', '600px' ],
			btn : [ '确定', '取消' ],
			btnAlign : 'c',
			content : content,
			yes : function(index, layero) {
				if(boolVal == false){
					$(viewField).empty();
				}else{
					$(viewField).children('li[is_compute="false"]').remove();
				}				
				var selectAllVal = $('.easy-select-filed .easy-tab-item').find('input[type="checkbox"]:checked');
				$.each(selectAllVal, function(index, el) {
					var text = $(this).parent('label').text();
					var metadataId = isNoemty($(this).parents('.easy-tab-item').attr('metadata_id'));
					var title_name = isNoemty($(this).parents('.easy-tab-item').attr('title_name'));
					var is_total_field = isNoemty($(this).parents('.easy-tab-item').attr('is_total_field'));
					var aliast = isNoemty($(this).parents('.easy-tab-item').attr('alias'));
					var func = isNoemty($(this).parents('.easy-tab-item').attr('func'));					
					var name = isNoemty($(this).parents('.easy-tab-item').attr('name'));
					var data_type = isNoemty($(this).parents('.easy-tab-item').attr('data_type'));
					var order_index = isNoemty($(this).parents('.easy-tab-item').attr('order_index'));
					var is_compute = isNoemty($(this).parents('.easy-tab-item').attr('is_compute'));
					var compute_expression = isNoemty($(this).parents('.easy-tab-item').attr('compute_expression'));
					var parent_metadata = isNoemty($(this).parents('.easy-tab-item').attr('parent_metadata'));
					var table_code = isNoemty($(this).parents('.easy-tab-item').attr('table_code'));
					var field_table = isNoemty($(this).parents('.easy-tab-item').attr('table_code'));
					var column_type = isNoemty($(this).parents('.easy-tab-item').attr('column_type'));
					var objAttr=getOnjAttr($(this),modelJSON);
					
					var isHav = false;
					$.each(viewFieldJson, function(indexLi, elLi) {
						//console.log(JSON.stringify(elLi)+'@#@###')
						var titleName = elLi.title_name;
						if (text == titleName) {
							isHav = true;
							objAttr = {
								flag : boolVal,
								tag : '1',
								text : isNoemty(elLi.title_name),
								metadataId : isNoemty(elLi.metadata_id),
								titleName : isNoemty(elLi.title_name),
								isTotalField : isNoemty(elLi.is_total_field),
								alias : isNoemty(elLi.alias),
								field_eval : isNoemty(elLi.field_eval),
								func : isNoemty(elLi.func),
								field : isNoemty(elLi.field),
								name : isNoemty(elLi.name),
								is_default:isNoemty(elLi.is_default),
								dataType : isNoemty(elLi.field_type),
								orderType : isNoemty(elLi.order_type),
								orderIndex : isNoemty(elLi.order_index),
								show_form : isNoemty(elLi.show_form),
								field_decimal_point : isNoemty(elLi.field_decimal_point),
								field_unit_limit : isNoemty(elLi.field_unit_limit),
								is_compute:isNoemty(elLi.is_compute),
								compute_expression:isNoemty(elLi.compute_expression),
								parent_metadata:isNoemty(elLi.parent_metadata),
								group_table:isNoemty(elLi.table_code),
								field_table:isNoemty(elLi.table_code),
								table_code:isNoemty(elLi.table_code),
								column_type:isNoemty(elLi.column_type)
							};
						}						
					});
					//console.log(JSON.stringify(objAttr)+'%%%')
					if (!isHav) {
						objAttr = {
							flag : boolVal,
							tag : '1',
							text : text,
							metadataId : metadataId,
							titleName : title_name,
							isTotalField : is_total_field,
							alias : aliast,
							func : func,
							field : name,
							name : name,
							dataType : data_type,
							orderIndex : order_index,
							is_compute:is_compute,
							parent_metadata:parent_metadata,
							table_code:table_code,
							group_table:table_code,
							field_table:table_code,
							table_code:table_code,
							column_type:column_type
						};
					}					
					creatLi(objAttr, viewField);
					$('#outAttr > li').arrangeable();
					sortIndex(viewField);
					fullScreen();
				})
				if (boolVal) {
					creatTemplateTable(viewField);
				}

				layer.close(index); //如果设定了yes回调，需进行手工关闭
			},
			cancel : function(index, layero) {
				layer.close(index)
			}
		});
	}
	//返回
	$('#btnback').click(function() {
		layer.confirm('返回后修改的模板内容将不保存', {
			btn : [ '确定', '取消' ] //按钮
		}, function() {
			window.location.href = '/bdp-web/flexiblequery/flexiblequery-index.html';
		}, function() {});
	})
	
	//高级选项
	$('.special input[type="checkbox"]').click(function(){		
		var sib = $(this).parent('label').siblings('label').children('input[type="checkbox"]');
		if($(this).is(':checked')){			
			sib.prop('disabled',true);
		}else{
			sib.prop('disabled',false);
		}
	})
	
})
//设置别名
$(document).on('click', '.editset', function() {
	$('#setAliaszInput').val('');
	var thisVal = $(this).parents('.attrDowm').parent('li').attr('alias');
	var fieldLi = $(this).parents('.attrDowm').parent('li').attr('field');
	var outAttrAlias = $('#outAttr > li[is_compute="true"]');
	var isAliasTrue = false;
	$.each(outAttrAlias,function(index,el){
		var computeExpression = $(this).attr('compute_expression');
		var thisValField = '{'+thisVal+'}';
		//console.log(thisValField+'@@@'+computeExpression);
		if(computeExpression.indexOf(thisValField) >= 0 || computeExpression.indexOf(fieldLi) >= 0){
			//console.log('相同的')
			isAliasTrue = true;
		}		
	})
	console.log(isAliasTrue)
	if(isAliasTrue == true){
		layer.alert('该字段已用于计算字段不能更改别名！');
		return false;
	}
	var inputBox = $('#setAliasz');
	$('#setAliaszInput').val(thisVal);
	var parentLiVal = $(this).parents('.attrDowm').parent('li');
	var getParentId = $(this).parents('.attrDowm').parent('li').parent('ul').attr('id');
	var ParentId = document.getElementById(getParentId);
	
	//console.log(getParentId+'222');
	layer.open({
		title : '设置别名',
		type : 1,
		area : [ 'auto', 'auto' ],
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		content : inputBox,
		yes : function(index, layero) {
			parentLiVal.children('strong').remove();
			var val = $('#setAliaszInput').val();
			var isLike = false;
			if (val != '') {
				var outAttrLi = $('.rule-con-list').children('ul').children('li');
				$.each(outAttrLi, function(index, el) {
					var alias = $(this).attr('alias');
					var text = $(this).children('span').text();
					if (val == alias || val == text) {
						isLike = true;
					}
				})
				if(val == thisVal){
					isLike = false;
				}
				if (isLike) {
					layer.alert('别名不能相同，并且不能与维度名称、输出属性名称相同！');
					return false;
				}
				if (reg_check_val(val)) {
					parentLiVal.attr('alias', val);
					parentLiVal.append('<strong>[' + val + ']</strong>');
					//展示输出字段
					creatTemplateTable(ParentId);
				}else{
					parentLiVal.attr('alias', '');
				}
			} else {
				parentLiVal.attr('alias', '');
				//展示输出字段
				creatTemplateTable(ParentId);
			}
			layer.close(index); //如果设定了yes回调，需进行手工关闭

		},
		cancel : function(index, layero) {
			layer.close(index)
		}
	});
})
//设置字段名称
$(document).on('click', '.fieldformat', function() {	
	var fieldformat = $('#fieldformat');
	var curElem = $(this);
	var showForm = curElem.parents('.attrDowm').parent('li').attr('show_form'); //千分位
	var unitlimit = curElem.parents('.attrDowm').parent('li').attr('field_unit_limit'); //是否启用单位
	var decimalPoint = curElem.parents('.attrDowm').parent('li').attr('field_decimal_point'); //小数点位数
	var objAttr = {
		showForm:showForm,
		unitlimit:unitlimit,
		decimalPoint:decimalPoint
	}
	defaultVal(fieldformat,curElem,objAttr);	
	var parentLiVal = curElem.parents('.attrDowm').parent('li');
	layer.open({
		title : '设置字段格式',
		type : 1,
		area : [ 'auto', 'auto' ],
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		content : fieldformat,
		yes : function(index, layero) {				
			var show_form_flag = false; //是否勾选千分位，都选则必须输入小数点
			var checkList = $('#fieldformat').find('input[type="checkbox"]');
			$.each(checkList, function() {
				var name = $(this).attr('name');
				if ($(this).is(':checked')) {
					if (name == "show_form") {
						parentLiVal.attr(name, 'thousand');
						show_form_flag = true;
					} else {
						parentLiVal.attr(name, 'true');
					}
				} else {
					parentLiVal.attr(name, 'false');
				}
			})
			var checkInput = $('#fieldformat').find('input[type="text"]');
			var pointVal = $.trim(checkInput.val());
			if (show_form_flag && pointVal == '') {
				pointVal = '';		
				layer.alert('请输入0~6数值！');
				return false;
			}
			if (pointVal > 6) {		
				layer.alert('只能入0~6数值！');
				return false;
			}
			pointVal = pointVal;			
			parentLiVal.attr('field_decimal_point', pointVal);
			//setThoud(parentLiVal,checkList);
			layer.close(index); //如果设定了yes回调，需进行手工关闭
		},
		cancel : function(index, layero) {
			layer.close(index)
		}
	});
})
function defaultVal(obj,curObj,objAttr){
	var formatInput = obj.find('input[name="field_decimal_point"]'); //小数点位数
	var formatUnit = obj.find('input[name="field_unit_limit"]'); //是否启用单位
	var formatThoud = obj.find('input[name="show_form"]'); //千分位
	formatInput.val('');
	formatUnit.prop('checked', false);
	formatThoud.prop('checked', false);
	var fieldformat = $('#fieldformat');
	if (objAttr.decimalPoint != '' && objAttr.decimalPoint != 'undefined' && objAttr.decimalPoint != null && objAttr.decimalPoint != 'null') {
		formatInput.val(objAttr.decimalPoint); //小数点位数
	} else {
		formatInput.val(''); //小数点位数
	}
	if (objAttr.unitlimit == 'true') {
		formatUnit.prop('checked', true); //是否启用单位
	}
	if (objAttr.showForm == 'thousand') {
		formatThoud.prop('checked', true); //千分位
	}
}
function setThoud(paren,obj){	
	var show_form_flag = false; //是否勾选千分位，都选则必须输入小数点
	var checkList = obj.find('input[type="checkbox"]');
	$.each(checkList, function() {
		var name = $(this).attr('name');
		if ($(this).is(':checked')) {
			if (name == "show_form") {
				paren.attr(name, 'thousand');
				show_form_flag = true;
			} else {
				paren.attr(name, 'true');
			}

		} else {
			paren.attr(name, 'false');
		}
	})
	var checkInput = obj.find('input[type="text"]');
	var pointVal = $.trim(checkInput.val());
	if (show_form_flag && pointVal == '') {
		pointVal = '';		
		layer.alert('请输入0~6数值！');
		return false;

	}
	if (pointVal > 6) {		
		layer.alert('只能入0~6数值！');
		return false;
	}
	pointVal = pointVal;
	paren.attr('field_decimal_point', pointVal);
}


//筛选器属性弹框
$(document).on('click','.editScreen',function(){
	$('#screenData').html('');
	var _this = $(this);
	var paren = $(this).parent('div');
	var fieldType = paren.attr('field_type');
	var screenformat = $('#screenformat');
	var screenData = $('#screenData');	
	var text = $(this).parent().children('h5').children('span').text();		
	var oldVal = isNoemty($(this).parent().attr('is_choic'));	
	var field1 = isNoemty($(this).parent().attr('field1'));	
	var value1 = isNoemty($(this).parent().attr('value1'));
	var value2 = isNoemty($(this).parent().attr('value2'));
	var cond = isNoemty($(this).parent().attr('cond'));
	var valfield = isNoemty($(this).parent().attr('valfield'));
	var dynamic_field = isNoemty($(this).parent().attr('dynamic_field'));
	var is_fixed = $(this).parent().attr('is_fixed');	
	var tips = isNoemty($(this).parent().attr('tips'));
	if((isNoemty(is_fixed)) ==''){
		is_fixed = '0'
	}
	var is_fixedCheck = ''
	if(is_fixed == '1'){
		is_fixedCheck ='checked';
	}
	var isCheck = '';
	var isNone = 'style="display:none"';
	var ischeckDown = 'style="display:none"';
	if(isNoemty(dynamic_field) !=''){
		isCheck = 'selected';
		isNone = 'style="display:inline-block"';
		ischeckDown = 'style="display:none"';
	}else{
		isNone = 'style="display:none"';
		ischeckDown = 'style="display:inline-block"';
	}
	var defalutHtml = $('<select class="form-control widthselect dynamicSelect" name="dynamicSelect">'
			+'<option>静态</option>'
			+'<option '+isCheck+'>动态</option>'
			+'</select>');
	var deViewField = $('<tr><th>是否隐藏：</th><td><label><input type="checkbox" name="is_fixed" value="'+is_fixed+'" '+is_fixedCheck+'></label></td>></tr>')
	var tipsHtml = $('<tr><th>输入框提示语：</th><td><input type="text" class="form-control" name="tips" value="'+tips+'"></td></tr>');		
	if(fieldType == 'pk'){
		//Pk
		screenData.append(deViewField);
		$.each(pkJson,function(index,el){	
			var trHtml = $('<tr></tr>');
			screenData.append(trHtml);
			var thHtml = $('<th>'+el.field+'：</th>');
			trHtml.append(thHtml);
			var valCheck = '';
			var isCheck = '';
			if(oldVal != el.default_value && oldVal !=''){
				valCheck = oldVal;
				isCheck = 'checked';
			}else{
				valCheck = el.default_value;
				isCheck = '';
			}
			if(el.texttype == 'checkbox'){
				inputHtml = '<td><label><input type="checkbox" name="'+el.attr+'" value="'+valCheck+'" '+isCheck+'>'+el.label+'</label></td>'
			}
			trHtml.append(inputHtml);
		})		
		var trHtml = $('<tr></tr>');
		screenData.append(trHtml);
		var thHtml = '<th>'+text+'：</th>';
		trHtml.append(thHtml);
		var tdHtml = $('<td></td>');
		trHtml.append(tdHtml);
		var select = '<select class="form-control widthselect" fieldtype="'+fieldType+'" name="cond" value="'+cond+'"><option value="in">包含</option></select>';
		tdHtml.append(select);
		tdHtml.append(defalutHtml);
		var checkDown = $('<input class="form-control checkDown value1" name="value1" autocomplete="off" tag="'+value1+'" value="'+valfield+'" '+ischeckDown+'>');
		tdHtml.append(checkDown);			
		//外键pk
		var metadataIdVal = {
			'metadata_id' : ''
		};
		var metadataIdList = paren.attr('metadata_id');
		var isChoicVal = paren.attr('is_choic');
		if (metadataIdVal.metadata_id == '') {
			metadataIdVal.metadata_id = metadataIdList;
		} else {
			metadataIdVal.metadata_id += ',' + metadataIdList;
		}
		ajaxSend('/bdp-web/metadata/selectMetadataPK', metadataIdVal, function(pklist) {
			var checkListData = pklist;
			var listCheckJson = checkListData[metadataIdList];
			var is_choic = true;
			if (isChoicVal == '1') {
				is_choic=false;
			}
			checkDown.hsCheckData({
				isShowCheckBox : is_choic, //默认为false
				selectOnceText : null,
				selectVal : null,
				selectText : null,
				selectAlltVal : null,
				data : listCheckJson
			})
		})
		var checkInput = $('<select class="form-control dynamic_field" name="dynamic_field" '+isNone+' value="'+dynamic_field+'">'
				+'<option value=""></option>'
				+'<option value="unit_id" '+isCheck+'>当前机构号</option>'
				+'</select>');
		tdHtml.append(checkInput);
		screenData.append(tipsHtml);//添加提示语
	}else if(fieldType == 'number' || fieldType == 'decimal' || fieldType =='numeric'){
			//数值
			screenData.append(deViewField);
			var trHtml = $('<tr></tr>');
			screenData.append(trHtml);
			var thHtml = '<th>'+text+'：</th>';
			trHtml.append(thHtml);
			var tdHtml = $('<td></td>');
			trHtml.append(tdHtml);
			var select = $('<select class="form-control widthselect fieldAttr" name="cond" value="'+cond+'"></select> ');
			//console.log(cond+'开始的默认值')
			$.each(datatimeJson,function(index,el){
				var isSelete = '';
				if(cond == el.value){
					isSelete = 'selected';
				}			
				var opt = $('<option value="'+el.value+'" '+isSelete+'>'+el.label+'</option>');
				select.append(opt);
			})
			tdHtml.append(select);	
			
			tdHtml.append('<input type="text" class="form-control value1" name="value1" autocomplete="off" value="'+value1+'">');
			if(cond == 'between'){
				tdHtml.append('<b>~</b><input type="text" class="form-control value2" autocomplete="off" name="value2" value="'+value2+'">');
			}
			screenData.append(tipsHtml);//添加提示语
		}else if(fieldType == 'dateTime'){
		//日期
		screenData.append(deViewField);
		var trHtml = $('<tr></tr>');
		screenData.append(trHtml);
		var thHtml = '<th>'+text+'：</th>';
		trHtml.append(thHtml);
		var tdHtml = $('<td></td>');
		trHtml.append(tdHtml);
		var select = $('<select class="form-control widthselect fieldAttr" fieldtype="'+fieldType+'" name="cond" value="'+cond+'"></select> ');
		$.each(datatimeJson,function(index,el){
			var isSelete = '';
			if(cond == el.value){
				isSelete = 'selected';
			}			
			var opt = $('<option value="'+el.value+'" '+isSelete+'>'+el.label+'</option>');			
			select.append(opt);
			
		})
		tdHtml.append(select);	
		tdHtml.append(defalutHtml);
		select.find('option[value="in"]').remove();
		select.find('option[value="notin"]').remove();
		var selectList = $('<select class="form-control dynamic_field" name="dynamic_field" '+isNone+' value="'+dynamic_field+'">'
				+'<option value=""></option>'
				+'<option value="early_month_start">上月初</option>'
				+'<option value="early_month_end">上月末</option>'
				+'<option value="month_start">月初</option>'
				+'<option value="month_end">月末</option>'
				+'<option value="season_start">季初</option>'
				+'<option value="season_end">季末</option>'
				+'<option value="year_start">年初</option>'
				+'<option value="year_end">年末</option>'
				+'</select>');		
		tdHtml.append(selectList);	
		selectList.find('option[value="'+dynamic_field+'"]').prop('selected',true);
		if(dynamic_field !=''){
			select.find('option[value !="="]').hide();
		}
		tdHtml.append('<input type="text" class="form-control value1 datepicker dateStar" autocomplete="off" name="value1" value="'+value1+'" '+ischeckDown+'>');
		if(cond == 'between'){
			tdHtml.append('<b>~</b><input type="text" class="form-control value2 datepicker dateEnd" autocomplete="off" name="value2" value="'+value2+'">');
		}
		intDate();		
		screenData.append(tipsHtml);//添加提示语
	}else{
		//字符
		screenData.append(deViewField);
		var trHtml = $('<tr></tr>');
		screenData.append(trHtml);
		var thHtml = '<th>'+text+'：</th>';
		trHtml.append(thHtml);
		var tdHtml = $('<td></td>');
		trHtml.append(tdHtml);
		var select = $('<select class="form-control widthselect fieldAttr" fieldtype="'+fieldType+'" name="cond" value="'+cond+'"></select> ');
		$.each(charJson,function(index,el){
			var isSelete = '';
			if(cond == el.value){
				isSelete = 'selected'
			}
			var opt = $('<option value="'+el.value+'" '+isSelete+'>'+el.label+'</option>');
			select.append(opt);
		})
		tdHtml.append(select);	
		tdHtml.append('<input type="text" class="form-control value1" autocomplete="off" fieldtype="'+fieldType+'" name="value1" value="'+value1+'">');		
		screenData.append(tipsHtml);//添加提示语
	}
	layer.open({
		title : '筛选器设置',
		type : 1,
		area : [ '755px', '320px' ],
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		scrollbar: false, // 父页面 滚动条 禁止  
		content : screenformat,
		yes : function(index, layero) {
			//多选
			var checkList = $('#screenData').find('input[type="checkbox"]');
			$.each(checkList,function(){
				var name = $(this).attr('name');
				if($(this).is(':checked')){						
					$(this).val('1');
					_this.parent().attr(name,$(this).val());
				}else{
					$(this).val('0');
					_this.parent().attr(name,$(this).val());
				}
			})
			var textvalue1 = $.trim($('#screenData').find('.value1').val());
			var textvalue2 = $.trim($('#screenData').find('.value2').val());
			var textTag = $('#screenData').find('.value1').attr('tag');	
			if(isNoemty(textTag) !=''){
				_this.parent().attr('value1',textTag);
				_this.parent().attr('valfield',textvalue1);
				_this.parent().attr('value2','');
			}else{
				_this.parent().attr('value1',textvalue1);
				_this.parent().attr('value2',textvalue2);
				$('#screenData').find('.value1').attr('tag','');
				_this.parent().attr('valfield','');
			}				
			var selectList = $('#screenData').find('select[name="cond"]');
			$.each(selectList,function(){
				var name = $(this).attr('name');
				if($(this).val() != 'between'){
					_this.parent().attr('value2','');
					$('#screenData').find('.value2').val('');
				}
				_this.parent().attr(name,$(this).val());
			})
			var selectDynamic = $('#screenData').find('select[name="dynamic_field"]');
			var selectDynamicVal = selectDynamic.val();
			if(isNoemty(selectDynamicVal) !=''){
				_this.parent().attr('dynamic_field',selectDynamicVal);				
			}else{
				_this.parent().attr('dynamic_field','');
			}
			
			var val = _this.parent().attr('value1');			
			var val2 = _this.parent().attr('value2');
			if(val2 !=''){				
				if (parseInt(val) - parseInt(val2) > 0) {
					layer.alert('输入的 数值必须大于前一位！');
					return false;
				}
			}
			//提示语
			var tipsVal = $.trim($('#screenData').find('input[name="tips"]').val());
			_this.parent().attr('tips',tipsVal);
			layer.close(index); //如果设定了yes回调，需进行手工关闭

		},
		cancel : function(index, layero) {
			layer.close(index)
		}
	});
})
$(document).on('change','.fieldAttr',function(){
	var fieldtype = $(this).attr('fieldtype');
	if($(this).val() == 'between'){		
		if(fieldtype == 'dateTime'){
			$(this).parent().append(' <b>~</b>');
			$(this).parent().append('<input type="text" class="form-control value2 datepicker dateEnd" autocomplete="off" name="value2">');
			intDate();
		}else{
			$(this).parent().append(' <b>~</b>');
			$(this).parent().append('<input type="text" class="form-control value2" name="value2" autocomplete="off">');
		}		
	}else{
		$(this).parent().find('b').remove();
		$(this).parent().find('.value2').remove();
	}
})
//静态跟动态切换
$(document).on('change','.dynamicSelect',function(){
	var field_type= $(this).siblings('select[name="cond"]').attr('fieldtype');	
	var text = $(this).val();
	var sib = $(this).siblings('.dynamic_field');
	var value1 = $(this).siblings('.value1');
	var value2 = $(this).siblings('.value2');
	var bHtml = $(this).siblings('b');
	if(text == '静态'){
		if(field_type == 'pk'){
			sib.hide();
			sib.val('');
			value1.show();
		}else if(field_type == 'dateTime'){
			sib.hide();
			sib.val('');
			value1.show();
			value2.show();
			bHtml.show();
			$(this).siblings('select[name="cond"]').find('option').show();
		}		
	}else if(text == '动态'){
		if(field_type == 'pk'){
			value1.hide();
			value1.val('');
			value1.attr('tag','');
			sib.show();	
		}else if(field_type == 'dateTime'){
			value1.hide();
			value1.val('');
			value2.hide();
			value2.val('');
			bHtml.hide();
			sib.show();	
			$(this).siblings('select[name="cond"]').find('option[value !="="]').hide();
			$(this).siblings('select[name="cond"]').find('option[value="="]').prop('selected',true);
		}
	}
})

//日期区间选框值
$(document).on('click', '.dateStar', function() {
	var sibData = $(this).parent().find('.dateEnd');
	$(this).datetimepicker("setEndDate", sibData.val())
});
$(document).on('click', '.dateEnd', function() {
	var sibData = $(this).parent('').find('.dateStar');
	$(this).datetimepicker("setStartDate", sibData.val())
});
//判断第二个输入值不能小于第二个
$(document).on('blur', '.value1', function() {
	var val = $(this).val();
	var val2 = $(this).siblings('.value2').val();
	if (parseInt(val) - parseInt(val2) > 0) {
		layer.alert('输入的 数值必须大于前一位！');
		return false;
	}
})

//计算字段弹窗
$(document).on('click','.computField',function(){
	var outAttrLiNum = $('#outAttr > li');
	if(outAttrLiNum.length == '0'){
		layer.alert('请先选择输出属性字段！');
	}else{
		setcompField($(this));		
	}
	
})
function setcompField(obj){
	$('#setFildeType').find('input[name="field_type"]').removeAttr("checked");
	var field_type = isNoemty(obj.attr('field_type'));	
	//console.log(obj.attr('field_type')+'计算字段的字段类型');
	var setFildeformat = $('#setFildeformat');
	var showForm = isNoemty(obj.attr('show_form')); //千分位
	var unitlimit = isNoemty(obj.attr('field_unit_limit')); //是否启用单位
	var decimalPoint = isNoemty(obj.attr('field_decimal_point')); //小数点位数
	var objAttr = {
		showForm:showForm,
		unitlimit:unitlimit,
		decimalPoint:decimalPoint
	}
	defaultVal(setFildeformat,obj,objAttr);	
	var compute_expression = isNoemty(obj.attr('compute_expression'));
	var is_compute = isNoemty(obj.attr('is_compute'));
	var nameattr = isNoemty(obj.attr('name'));	
	$('#aliasTitle').val(nameattr);
	$('#textFormula').val(compute_expression);
	$("input[name=field_type][value="+field_type+"]").prop("checked",true);//字段类型
	var outAttrLi = $('#outAttr > li');
	var comp_iterm = $('#comp_iterm');
	comp_iterm.html('');
	$.each(outAttrLi,function(){
		var alias = $(this).attr('alias');
		var func = isNoemty($(this).attr('func'));
		var title_name = $(this).attr('title_name');
		var name = $(this).attr('name');
		var field_type = $(this).attr('field_type');
		var is_compute = $(this).attr('is_compute');
		var text = '';	
		var nameVal = '';
		if(isNoemty(alias) !=''){
			text = alias;
		}else{
			text = title_name;
		}
		if(isNoemty(alias) !=''){
			nameVal = alias;
		}else{
			nameVal = name;
		}
		if(field_type == 'number' || field_type == 'decimal' || field_type =='numeric' || func !=''){
			if(!is_compute || is_compute!='true'){//通过计算字段标识来判断
			    if(isNoemty(title_name) !=''){
					comp_iterm.append('<li name="'+nameVal+'" title="'+nameVal+'"><span>'+text+'</span></li>');
				}
			}	
		}
	})
	var main = $('#compField');
	layer.open({
		title : '计算字段',
		type : 1,
		area : [ 'auto', 'auto' ],
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		content : main,
		yes : function(index, layero) {
			var isDatrue = false;
			var checkList = $('#setFildeformat');
			var aliasTitle = $.trim($('#aliasTitle').val());
			var textFormula = $.trim($('#textFormula').val());
			var field_type = $("input[type='radio']:checked").val();			
			var isLike = false;
			var isTrue = false;
			if(aliasTitle != ''){
				var outAttrLi = $('.rule-con-list').children('ul').children('li');
				$.each(outAttrLi,function(){
					var alias = $(this).attr('alias');
					var text = $(this).children('span').text();
					if (aliasTitle == alias || aliasTitle == text) {
						isLike = true;
					}
					if(aliasTitle == obj.children('span').text()){
						isLike = false;
					}
				})				
				if (!reg_check_val(aliasTitle)) {
					isTrue = true;
				}
			}else{
				layer.alert('请输入别名！');
				return false;
			}
			if (isTrue) {
				layer.alert('别名只能以字母、中文或者下划线开头！');
				return false;
			}
			if (isLike) {
				layer.alert('别名不能相同，并且不能与维度名称、输出属性名称相同！');
				return false;
			}
			if(textFormula == ''){
				layer.alert('请输入计算公式！');
				return false;
			}
			if(field_type == '' || field_type == 'undefined' || field_type == undefined){
				layer.alert('请选择字段类型！');
				return false;
			}
			var isDatatrue = false;
			if(is_compute == 'true'){
				obj.attr('compute_expression',textFormula);
				obj.attr('is_compute','true');
				obj.attr('name',aliasTitle);
				obj.children('span').text(aliasTitle);	
				obj.attr('title_name',aliasTitle);
				obj.attr('field',aliasTitle);
				obj.attr('show_form',objAttr.showForm);
				obj.attr('field_unit_limit',objAttr.unitlimit);
				obj.attr('field_decimal_point',objAttr.decimalPoint);
				obj.attr('field_type',field_type);
				var show_form_flag = false; //是否勾选千分位，都选则必须输入小数点
				var checkList = $('#setFildeformat').find('input[type="checkbox"]');
				$.each(checkList, function() {
					var name = $(this).attr('name');
					if ($(this).is(':checked')) {
						if (name == "show_form") {
							obj.attr(name, 'thousand');
							show_form_flag = true;
						} else {
							obj.attr(name, 'true');
						}

					} else {
						obj.attr(name, 'false');
					}
				})
				var checkInput = $('#setFildeformat').find('input[type="text"]');
				var pointVal = $.trim(checkInput.val());
				if (show_form_flag && pointVal == '') {
					pointVal = '';	
					isDatatrue = true;
				}
				if (pointVal > 6) {	
					isDatatrue = true;
				}
				var field_type = $("input[type='radio']:checked").val();
				pointVal = pointVal;				
				obj.attr('field_decimal_point', pointVal);
				//setThoud(obj,checkList);
			}else{
				var liHtml = $('<li>'
						+'<span>'+aliasTitle+'</span><em class="fa fa-angle-down"></em><i class="fa fa-close"></i></li>');
				liHtml.attr({
					'compute_expression':textFormula,
					'is_compute':'true',
					'name':aliasTitle,
					'title_name':aliasTitle,
					'field':aliasTitle,
					'field_type':field_type
				})
				var show_form_flag = false; //是否勾选千分位，都选则必须输入小数点
				var checkList = $('#setFildeformat').find('input[type="checkbox"]');
				$.each(checkList, function() {
					var name = $(this).attr('name');
					if ($(this).is(':checked')) {
						if (name == "show_form") {
							liHtml.attr(name, 'thousand');
							show_form_flag = true;
						} else {
							liHtml.attr(name, 'true');
						}

					} else {
						liHtml.attr(name, 'false');
					}
				})
				var checkInput = $('#setFildeformat').find('input[type="text"]');
				var pointVal = $.trim(checkInput.val());
				if (show_form_flag && pointVal == '') {					
					isDatatrue = true;
				}
				if (pointVal > 6) {	
					isDatatrue = true;
				}
				pointVal = pointVal;
				liHtml.attr('field_decimal_point', pointVal);
				//setThoud(liHtml,checkList,isDatrue);
				$('#outAttr').append(liHtml);
			}
			creatTemplateTable($('#outAttr'));
			if(isDatatrue){
				layer.alert('只能入0~6数值！');
				return false;
			}
			if(isDatrue){
				layer.alert('请输入0~6数值！');
				return false;
			}			
			layer.close(index); //如果设定了yes回调，需进行手工关闭
		},
		cancel : function(index, layero) {
			layer.close(index)
		}
	});
}
$(document).on('click','#outAttr > li[is_compute="true"] > i',function(event){
	event.stopPropagation();
	$(this).parent('li').remove();
})
$(document).on('click','#outAttr > li[is_compute="true"]',function(event){
	event.preventDefault();
	setcompField($(this));
	
})
$(document).on('click','#comp_iterm > li > span',function(){	
	var textFormula = $('#textFormula');
	var textFormulaTag = $('#textFormulaTag');
	var val = $(this).text();
	var name = $(this).parent().attr('name');
	InsertString('textFormula', '{'+name+'}');	
})
function InsertString(tbid, str){
    var tb = document.getElementById(tbid);
    tb.focus();
    if (document.all){
        var r = document.selection.createRange();
        document.selection.empty();
        r.text = str;
        r.collapse();
        r.select();
    }
    else{
        var newstart = tb.selectionStart+str.length;
        tb.value=tb.value.substr(0,tb.selectionStart)+str+tb.value.substring(tb.selectionEnd);
        tb.selectionStart = newstart;
        tb.selectionEnd = newstart;
    }
}
//计算字段搜索
$(document).on('keydown','.search_comp',function(event){
	event = event || window.event;
    if (event.keyCode == 13) {
    	comField();
    }
})
function comField() {
	var tableNavLi = $('#comp_iterm li');
	var tableInputVal = $.trim($('.search_comp').val());
	$.each(tableNavLi, function(index, el) {
		var text = $(this).children('span').text();
		$(this).show();		
		if (tableInputVal == '') {
			tableNavLi.show();
		}
		if (text.indexOf(tableInputVal) >= 0) {
			$(this).parent('li').show();			
		} else {
			$(this).hide();
		}
	})
}

function defaultRadioVal(name,value){	
	if(value!=''){
		//console.log("@@@@@@@")
		
	}
}


