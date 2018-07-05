fullScreen();
$(window).resize(function(){
	fullScreen();
})
$(function() {	
	var templateId = GetQueryString("id");
	var param = {
			'echart_template_id' : templateId,
	}
	//判断是修改还是新增
	if (templateId) {
		//alert('修改')		
		ajaxSendload('/bdp-web/easyChart/selectTemplateObj', param, editSucess);
	}else{
		calFunTable();
	}
	//维度释放生成
	dimen.ondragover = function(ev) {
		ev.preventDefault();
	}
	dimen.ondrop = function(ev) {
		ev.preventDefault();		
		if(dimenLi.length == '0'){
			dragRelease(ev, groupsDrop, dimen);
		}else{
			layer.alert('维度只能拖入一个字段！');
		}
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
		console.log(JSON.stringify(data)+'图表回显修改')
		var list = data.resultJson;		
		var querytemplate = list.querytemplate;
		$('#metadata_id').val(querytemplate.metadata_id);
		$('#chart_code').val(querytemplate.chart_code);
		$('#chart_name').val(querytemplate.chart_name);
		$('#chart_des').val(querytemplate.chart_des);
		$('#chart_catalog_id').val(querytemplate.chart_catalog_id);	
		//表
		var tables = querytemplate.tables;		
		if(tables && tables.length > 0){
			$.each(tables, function(index, el) {
				var objAttr = tablesBack(el);				
				var newLi = $('<li>'+ el.table_title +'<i class="fa fa-close"></i></li>').attr(objAttr);
				$('#treeObj').append(newLi);

			})
		}		
		//触发“查询表”按钮
		calFunTable();
		calFunField();		
		//维度
		var groups = querytemplate.groups;
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
		//console.log(JSON.stringify(conditions)+'^&^&^---&')
		if (conditions) {
			if (conditions.length > 0) {
				$.each(conditions, function(index, el) {
					var objAttr = conditionsBack(el);					
					creatScreenHtml(objAttr, ruleScreen);
				})
			}
		}
		//图表回显
		var charttypeLi = $('#charttype').find('li');		
		$.each(charttypeLi,function(){			
			var type = $(this).attr('type');
			var charttypename = $(this).attr('title');
			var chartVal = $(this).attr('value');
			if(type ==querytemplate.chart_type &&　charttypename == querytemplate.chart_type_name){				
				$(this).addClass('current').siblings('li').removeClass('current');								
				var chartTrue = true;
				getSaveData(querytemplate, null,chartTrue,chartVal);
			}
		})
	}
	
	//保存
	$('#btnsave').click(function() {		
		var showBox = $('#saveEasy');
		var param = {};
		getSaveData(param, showBox,null);
	})
	//保存的参数获取
	function getSaveData(param, obj,chartTrue,chartVal) {
		var outAttrLi = $('#outAttr');
		sortIndex(outAttrLi);
		//获取表属性		
		var tableJson = [];
		var tableLi = $('#treeObj>li');
		tableLi.each(function(){
			getTablesAttr($(this),tableJson);			
		})
		//console.log(JSON.stringify(tableJson)+'~~~')
		//获取维度字段
		var definJson = [];
		var dimen = $('#dimen > li');
		dimen.each(function() {
			getGroupsAttr($(this),definJson);			
		})
		//判断没有维度字段
		if (definJson == '') {
			layer.alert('维度不能为空！');
			return false;
		}
		//获取输出属性字段
		var outAttrJson = [];
		var outAttr = $('#outAttr > li');
		outAttr.each(function() {			
			getfieldsAttr($(this),outAttrJson);
		})
		//判输出属性跟维度是否相等
		var isCompare = false;
		//console.log(JSON.stringify(definJson)+'~!!!!'+JSON.stringify(outAttrJson))
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
		//判断没有输出属性
		if (outAttrJson == '') {
			layer.alert('输出属性不能为空！');
			return false;
		}
		//判断有维度输出属性没有求均值的判断		
		var iss = true;
		$.each(outAttr, function(index, el) {
			var funcVal = $(this).attr('func');
			if (definJson != '') {				
				if (funcVal == '' || funcVal == 'undefined' || funcVal == undefined || funcVal == null || funcVal == 'null') {
					iss = false;
				}
			}
		})
		if (!iss) {
			layer.alert('有输出属性未做聚合计算！')
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
			if (isNoemty(funcVal) != '') {
				isHavFunc = true;
				funcount++;
			}
		})
		var isOper = false;
		if (isHavFunc) {
			if (!(funcount == outAttr.length)) {
				isOper = true;
			}
		}
		if (isOper) {
			layer.alert('有输出属性未做聚合计算！')
			return false;
		}
		
		//获取筛选条件字段
		var ruleScreenJson = [];
		var ruleScreen = $('#ruleScreen > div');		
		var isNumber = false;				
		ruleScreen.each(function() {
			getconditionsAttr($(this),ruleScreenJson,isNumber);
		});
		if (isNumber) {
			layer.alert('数值型的筛选器的条件必须输入数字');
			return false;
		}		
		param['tables'] = tableJson;
		param['groups'] = definJson;
		param['fields'] = outAttrJson;
		param['conditions'] = ruleScreenJson;	
		var treeObj = $('#treeObj > li');
		var metadataId = treeObj.eq(0).attr('parent_code');
		param['metadata_id'] = metadataId;
		$('#metadata_id').val(metadataId);			
		if(chartTrue == true){
			console.log(JSON.stringify(param)+'图表参数')
			selectChart(param,chartVal);
			//console.log('图表类型走的');
		}else{			
			//读取图表类型
			var charttype = $('#charttype >li');
			$.each(charttype,function(){
				if($(this).hasClass('current')){
					param['chart_type'] = isNoemty($(this).attr('type'));
					param['chart_type_name'] = isNoemty($(this).attr('title'));
					param['chart_type_value'] = isNoemty($(this).attr('value'));
				}
			})
			console.log(JSON.stringify(param)+'保存的参数')
			saveDilog(obj, param);
		}		
	}
	//保存的弹窗
	function saveDilog(obj, param) {
		console.log(JSON.stringify(param)+'保存参数999')
		layer.open({
			title : '保存模板',
			type : 1,
			area : [ 'auto', 'auto' ],
			btn : [ '确定', '取消' ],
			btnAlign : 'c',
			content : obj,
			yes : function(index, layero) {
				var metadataIdVal = $('#metadata_id').val();
				var chartCode = $('#chart_code').val();
				var chartName = $('#chart_name').val();
				var chartDes = $('#chart_des').val();
				var chart_catalog_id = $('#chart_catalog_id option:selected').val();
				if ($.trim(chartName) == '') {
					layer.alert('请输入图表名称！');
					return false;
				}
				var url="/add";
				if (isNoemty(templateId) != '') {
					param['id'] = templateId;
					url = "/edit";
				}
				//console.log(JSON.stringify(url)+'保存路径')
				param['metadata_id'] = metadataIdVal;
				param['chart_code'] = chartCode;				
				param['chart_name'] = chartName;
				param['chart_catalog_id'] = chart_catalog_id;
				param['chart_des'] = chartDes;				
				var params = {
					"param" : JSON.stringify(param)
				};
				console.log(JSON.stringify(param)+'保存参数');
				ajaxSendload('/bdp-web/easyChart'+url, params, function(data) {
					console.log(JSON.stringify(data)+'@@@')
					if(data.ret_code == '0'){
						layer.alert('保存成功！');
						window.location.href = '/bdp-web/easychart/easychart-index.html';
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
	
	//图表查数据
	function selectChart(param,chartVal){
		var params = {
				"param" : JSON.stringify(param)
			};
		//console.log(JSON.stringify(param)+'雷达图表的切换')
		$('#chartType').html('');
		var myChart1 = echarts.init(document.getElementById('chartType'));
		ajaxSendload('/bdp-web/easyChart/tempQuery', params, function(data) {
			//console.log(JSON.stringify(data)+'图标返回')
			chartBack(chartVal,myChart1,data);				    	
	    })
	}
	//图标类型的选择
	$('#charttype > li').click(function(){	
		$(this).addClass('current').siblings().removeClass('current');
		var chartTrue = true;
		var chart_type = $(this).attr('type');
		var chartVal = $(this).attr('value');
		var chart_type_name = $(this).attr('title');
		var chart_type_value = $(this).attr('value');
		var outAttrLi = $('#outAttr > li');	
		var numTrue = false;
		if(chartVal == 'pie-chart'){
			if(outAttrLi.length > 1){
				numTrue =true;
			}
		}
		if(numTrue){
			layer.alert('饼状图输出字段只能选择一个！');
			return false;
		}
		var showBox = $('#saveEasy');
		var param = {};
		param['chart_type'] = chart_type;
		param['chart_type_name'] = chart_type_name;
		param['chart_type_value'] = isNoemty($(this).attr('value'));		
		getSaveData(param, null,chartTrue,chartVal);		
	})
	
	//返回
	$('#btnback').click(function() {
		layer.confirm('返回后修改的模板内容将不保存', {
			btn : [ '确定', '取消' ] //按钮
		}, function() {
			window.location.href = '/bdp-web/easychart/easychart-index.html';
		}, function() {});
	})
		
})
//设置别名
$(document).on('click', '.editset', function() {
	$('#setAliaszInput').val('');
	var thisVal = $(this).parents('.attrDowm').parent('li').attr('alias');
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
				}else{
					parentLiVal.attr('alias', '');
				}
			} else {
				parentLiVal.attr('alias', '');				
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
	var formatInput = $('#fieldformat').find('input[name="field_decimal_point"]'); //小数点位数
	var formatUnit = $('#fieldformat').find('input[name="field_unit_limit"]'); //是否启用单位
	var formatThoud = $('#fieldformat').find('input[name="show_form"]'); //千分位
	formatInput.val('');
	formatUnit.prop('checked', false);
	formatThoud.prop('checked', false);
	var fieldformat = $('#fieldformat');
	var showForm = $(this).parents('.attrDowm').parent('li').attr('show_form'); //千分位
	var unitlimit = $(this).parents('.attrDowm').parent('li').attr('field_unit_limit'); //是否启用单位
	var decimalPoint = $(this).parents('.attrDowm').parent('li').attr('field_decimal_point'); //小数点位数	
	if (decimalPoint != '' && decimalPoint != 'undefined' && decimalPoint != null && decimalPoint != 'null') {
		formatInput.val(decimalPoint); //小数点位数
	} else {
		formatInput.val(''); //小数点位数
	}
	if (unitlimit == 'true') {
		formatUnit.prop('checked', true); //是否启用单位
	}
	if (showForm == 'thousand') {
		formatThoud.prop('checked', true); //千分位
	}
	var parentLiVal = $(this).parents('.attrDowm').parent('li');
	var getParentId = $(this).parents('.attrDowm').parent('li').parent('ul').attr('id');
	var ParentId = document.getElementById(getParentId);
	//console.log(getParentId+'222');
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
				layer.alert('请输入0~6数值！');
				return false;

			}
			if (pointVal > 6) {
				layer.alert('只能入0~6数值！');
				return false;
			}
			parentLiVal.attr('field_decimal_point', pointVal);
			layer.close(index); //如果设定了yes回调，需进行手工关闭

		},
		cancel : function(index, layero) {
			layer.close(index)
		}
	});
})

//筛选器属性弹框
$(document).on('click','.editScreen',function(){
	$('#screenData').html('');
	var _this = $(this);
	var paren = $(this).parent('div');
	var fieldType = isNoemty(paren.attr('field_type'));
	var screenformat = $('#screenformat');
	var screenData = $('#screenData');	
	var text = isNoemty($(this).parent().children('h5').children('span').text());		
	var oldVal = isNoemty($(this).parent().attr('is_choic'));	
	var field1 = isNoemty($(this).parent().attr('field1'));	
	var value1 = isNoemty($(this).parent().attr('value1'));
	var value2 = isNoemty($(this).parent().attr('value2'));
	var cond = isNoemty($(this).parent().attr('cond'));
	var valfield = isNoemty($(this).parent().attr('valfield'));
	var dynamic_field = isNoemty($(this).parent().attr('dynamic_field'));
	var is_fixed = isNoemty($(this).parent().attr('is_fixed'));	
	if(is_fixed == ''){
		is_fixed = '0'
	}
	var is_fixedCheck = ''
	if(is_fixed == '1'){
		is_fixedCheck ='checked';
	}
	var isCheck = '';	
	if(fieldType == 'pk'){
		//Pk		
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
		var checkDown = $('<input class="form-control checkDown value1" name="value1" autocomplete="off" tag="'+value1+'" value="'+valfield+'">');
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
	}else if(fieldType == 'number' || fieldType == 'decimal' || fieldType =='numeric'){
		//数值
			var trHtml = $('<tr></tr>');
			screenData.append(trHtml);
			var thHtml = '<th>'+text+'：</th>';
			trHtml.append(thHtml);
			var tdHtml = $('<td></td>');
			trHtml.append(tdHtml);
			var select = $('<select class="form-control widthselect fieldAttr" name="cond" value="'+cond+'"></select> ');
			$.each(datatimeJson,function(index,el){
				var isSelete = '';
				if(cond == el.value){
					isSelete = 'selected';
				}
				var opt = $('<option value="'+el.value+'" '+isSelete+'>'+el.label+'</option>');
				select.append(opt);
			})
			tdHtml.append(select);	
			tdHtml.append('<input type="text" class="form-control value1" autocomplete="off" name="value1" value="'+value1+'">');
			if(cond == 'between'){
				tdHtml.append('<b>~</b><input type="text" class="form-control value2" autocomplete="off" name="value2" value="'+value2+'">');
			}
		}else if(fieldType == 'dateTime'){
		//日期
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
		tdHtml.append('<input type="text" class="form-control value1 datepicker dateStar" name="value1" autocomplete="off" value="'+value1+'">');
		if(cond == 'between'){
			tdHtml.append('<b>~</b><input type="text" class="form-control value2 datepicker dateEnd" name="value2" autocomplete="off" value="'+value2+'">');
		}
		intDate();
	}else{
		//字符
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
	
	}
	layer.open({
		title : '筛选器设置',
		type : 1,
		area : [ '700px', '280px' ],
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
			/*if(isNoemty(textvalue1) !=''){
				if(isNoemty(textTag) !=''){
					_this.parent().attr('value1',textTag);
					_this.parent().attr('valfield',textvalue1);
				}else{
					_this.parent().attr('value1',textvalue1);				
				}
			}else{				
				$('#screenData').find('.value1').attr('tag','');
				$('#screenData').find('.value1').val('');
				_this.parent().attr('value1','');
				_this.parent().attr('valfield','');
			}
			if(isNoemty(textvalue1) != ''){
				_this.parent().attr('value2',textvalue2);
			}else{
				_this.parent().attr('value2','');
				$('#screenData').find('.value2').val('');
			}*/						
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



//分段计算
function sectionFun(obj){
	var paren = obj.parents('.attrDowm').parent('li');
	var fieldType = paren.attr('field_type');
	if(fieldType !='varchar'){
		var field1 = paren.attr('field');
		var param = isNoemty(paren.attr('param'));
		var sectioncon = $('.sectioncon > div');
		sectioncon.html('');
		var listHtml = $('<div class="section-item">'
							+'<i class="fa fa-plus addList" fieldType="'+fieldType+'"></i>'
							+'<i class="fa fa-trash-o delectList"></i>'
							+'<p><select class="form-control" name="cond"></select>'
							+'<input class="form-control" name="value1" autocomplete="off">'
							+'</p>'
							+'<div>'
							+'<span>分段结果</span>'
							+'<input class="form-control" name="field" autocomplete="off">'
							+'</div></div>');
		 if(isNoemty(param) !=''){	 	
			param = param.substring(1,param.length-1);
			var start = '';
			var end = '';
			var objArray = [];
			var strArray = [];
			while (param.indexOf('{') != -1){
				start = param.indexOf('{');
				end = param.indexOf('}')+1;
				var obj = param.substring(start,end);
				objArray.push(obj);
				param = param.replace(obj+',','');
			}		
			strArray = param.split(',');			
			for(var i = 0 ; i<objArray.length;i++ ){
				var text = strArray[i];
				var obj = JSON.parse(objArray[i]);	
				text = text.replace(/"/g,'');
				var div = $('<div class="section-item"><i class="fa fa-plus addList">'
						+'</i><i class="fa fa-trash-o delectList"></i><p></p><div><span>分段结果</span></div></div>');
				var  select = $('<select class="form-control" name="cond"></select>');			
				$.each(sectionSelect,function(index,el){
					var option = '<option value="'+el.value+'">'+el.label+'</option>';
					select.append(option);
				})
				select.val(obj.cond);
				var input = $('<input class="form-control" name="value1" autocomplete="off" value="'+obj.value1+'">');
				var input01 = $('<input class="form-control" name="field" autocomplete="off" value="'+text+'">');
				div.find('p').append(select);
				div.find('p').append(input);
				div.find('p').append('<b>~</b><input class="form-control" name="value2" autocomplete="off"" value="'+obj.value2+'">');
				if(obj.cond == 'between'){
					div.children('p').find('b').show();
					div.children('p').find('input[name="value2"]').show();
				}else{
					div.children('p').find('b').hide();
					div.children('p').find('input[name="value2"]').hide();
				}
				div.find('div').append(input01);
				sectioncon.append(div);
			}
			$('.section-item').eq(0).find('i.delectList').hide();
		}else{	
			$.each(sectionSelect,function(index,el){
				var option = '<option value="'+el.value+'">'+el.label+'</option>';			
				listHtml.find('select[name="cond"]').append(option);				
			})
			listHtml.find('p').append('<b>~</b><input class="form-control" name="value2" autocomplete="off" value="">');
			listHtml.find('select[name="cond"]').val('between');
			listHtml.find('i.delectList').hide();
			sectioncon.append(listHtml);
		}
		if(fieldType =='dateTime'){
			console.log('日期');
			$('.section-item').find('input[name="value1"]').addClass('datepicker dateStar');
			$('.section-item').find('input[name="value2"]').addClass('datepicker dateEnd');
			intDate();
		}
		var main = $('#section');	
		//var cond
		layer.open({
			title : '分段计算',
			type : 1,
			area : [ 'auto', 'auto' ],
			btn : [ '确定', '取消' ],
			btnAlign : 'c',
			content : main,
			yes : function(index, layero) {
				var sectionItem =$('.section-item');
				var listArr = [];
				var isSize = false;
				var isValue = false;
				$.each(sectionItem,function(index,el){
					var value1 = $.trim($(this).find('input[name="value1"]').val());
					var value2 = $.trim($(this).find('input[name="value2"]').val());
					var field = $.trim($(this).find('input[name="field"]').val());
					var cond = $(this).find('select[name="cond"]').val();
					if(cond == 'between'){
						if (parseInt(value1) - parseInt(value2) > 0) {
							isSize = true;
						}
						if(value1 =='' || value2 =='' || field ==''){
							console.log('30')
							isValue = true;
						}
						if(value1 =='' && value2 =='' && field ==''){
							console.log('3')
							isValue = false;
						}
					}else{
						if(value1 =='' || field ==''){
							console.log('20')
							isValue = true;
						}
						if(value1 =='' && field ==''){
							console.log('2')
							isValue = false;
						}
					}
					
				});
				if (isSize) {
					layer.alert('输入的 数值必须大于前一位！');
					return false;
				}
				if(isValue){					
					layer.alert('请输入值！');
					return false;
				}
				$.each(sectionItem,function(index,el){
					var listJson = {};
					var value1 = $.trim($(this).find('input[name="value1"]').val());
					var value2 = $.trim($(this).find('input[name="value2"]').val());
					var cond = $(this).find('select[name="cond"]').val();
					var field = $.trim($(this).find('input[name="field"]').val());
					listJson['value1'] = value1;
					listJson['value2'] = value2;
					listJson['cond'] = cond;
					listJson['field1'] = field1;
					listArr.push(listJson);
					listArr.push(field);
				})				
				listArr.push('其他');
				//console.log(JSON.stringify(listArr)+'其他的值');
				paren.attr('param',JSON.stringify(listArr));
				layer.close(index); //如果设定了yes回调，需进行手工关闭

			},
			cancel : function(index, layero) {
				layer.close(index)
			}
		});
	}else{
		layer.alert('字符型字段不能做分段统计!');
	}
	
	
}
$(document).on('change','.section-item select[name="cond"]',function(){	
	var val = $(this).val();
	var pLen = $(this).parent('p').find('b');
	console.log(pLen.length+'~!!!!')
	if(val == 'between'){
		$(this).parent('p').find('b').show();
		$(this).parent('p').find('input[name="value2"]').show();						
	}else{
		$(this).parent('p').find('b').hide();
		$(this).parent('p').find('input[name="value2"]').hide();
		$(this).parent('p').find('input[name="value2"]').val('');
	}
})

//分段计算增加删除
$(document).on('click','.section-item > i.addList',function(){
	var fieldType = $(this).attr('fieldType');
	var listHtml = $('<div class="section-item">'
			+'<i class="fa fa-plus addList" fieldType="'+fieldType+'"></i>'
			+'<i class="fa fa-trash-o delectList"></i>'
			+'<p><select class="form-control" name="cond"></select>'
			+'<input class="form-control" name="value1" autocomplete="off">'
			+'</p>'
			+'<div>'
			+'<span>分段结果</span>'
			+'<input class="form-control" name="field" autocomplete="off">'
			+'</div></div>');
	$.each(sectionSelect,function(index,el){
		var option = '<option value="'+el.value+'">'+el.label+'</option>';			
		listHtml.find('select[name="cond"]').append(option);				
	})
	listHtml.find('p').append('<b>~</b><input class="form-control" name="value2" autocomplete="off" value="">');
	listHtml.find('select[name="cond"]').val('between');	
	listHtml.find('input').val('');	
	$(this).parent().parent().append(listHtml);
	if(fieldType =='dateTime'){
		$('.section-item').find('input[name="value1"]').addClass('datepicker dateStar');
		$('.section-item').find('input[name="value2"]').addClass('datepicker dateEnd');
		intDate();
	}
})
$(document).on('click','.section-item > i.delectList',function(){
	var sectionItem = $('.section-item');
	if(sectionItem.length > 1){
		$(this).parent().remove();		
	}else{
		sectionItem.find('i.delectList').hide();
	}
})
//日期区间选框值
$(document).on('click', '.dateStar', function() {
	var sibData = $(this).siblings('.dateEnd');
	$(this).datetimepicker("setEndDate", sibData.val())
});
$(document).on('click', '.dateEnd', function() {
	var sibData = $(this).siblings('.dateStar');
	$(this).datetimepicker("setStartDate", sibData.val())
});

