$(function() {
	var _id = GetQueryString('id');
	var dataid = GetQueryString('dataid');
	var Param = {
		"id" : _id,
		"dataid" : dataid
	}
	var frameForm = $('#frameForm');
	var formData = '';
	var relationVal = [];//存储外键字典
	ajaxSend('/bdp-web/viewTableCofig/selectObj', Param, callBackFun);
	function callBackFun(data) {
		var title = data.viewTitle; //标题
		formData = data.columns;
		var isInsert = data.isInsert; //是否可新增
		var isEdit = data.isEdit; //是否可编辑	
		var titleHtml = ''; //对象标题拼接 html
		relaKey(formData,relationVal);//存储外键字典数据
		if (title != null) { //标题是否存在
			titleHtml += '<h3>' + title + '</h3>';
		}
		var formHTML = $('<div class="text-form formList" id="formList">' + titleHtml + '</div>');
		frameForm.append(formHTML);
		var formObj = $('#formList');
		//字段拼接
		if(isNoEmpty(dataid)){//编辑			
			var param1 = {
				'id' : dataid
			};
			var json = {
				"json" : JSON.stringify(param1)
			}
			var jsonVal = {};
			ajaxSend('/bdp-web/viewTableCofig/selectDataObj/' + _id, json, function(data) {
				var obj = data.list[0];				
				var numCon = '';
				//分组的个数
				var viewGroupArry = [];
				groupArray(formData,viewGroupArry);
				for (var i = 0; i < viewGroupArry.length; i++) {					
					numCon = viewGroupArry[i];
					var subCon = '<h5 class="subTitle"><span>'+ numCon + '</span><i class="fa fa-angle-double-down"></i></h5><div class="subCon" id="subCon_' + numCon + '"></div>';
					formObj.append(subCon);					
					$.each(formData, function(index, el) {							
						var viewGroup = el.viewGroup;//分组
						var filed = el.columnName;//中文字段名
						var val = el.value;
						var textType = el.columnType;//显示类型
						var name = el.columnCode;//显示的key值
						var isEdit = el.isEdit;//是否可以修改
						var edidVal = obj[name];//key对应的值	
						var isPk = el.isPk;//是否是主键												
						if (isPk == '1') {
							jsonVal[name] = dataid;
						}
						if(edidVal == null){
							edidVal = '';
						}
						if (isEdit == '1' || isPk == '1') {	
							if (viewGroup == numCon) {
								var subObj = $('#subCon_' + numCon);
								createHtml(textType, subObj, filed, name,edidVal,isPk,null,relationVal);								
							}
						}
					});
				}
				//判断分组的div是否为空，为空就隐藏
				var subConList = $('.subCon');
				divEmpty(subConList);
			});
			var btn = '<div class="formBtnList"><button type="button" class="btn btn-primary" id="savaBtnEdit"><i class="fa fa-save"></i> 提交</button><button type="button" class="btn btn-default" id="backBtn"><i class="fa fa-mail-reply-all"></i> 返回</button></div>';
			formObj.append(btn);
		} else {//新增
			var numCon = ''
			//分组的个数
			var viewGroupArry = [];
			groupArray(formData,viewGroupArry);			
			for (var i = 0; i < viewGroupArry.length; i++) {
				numCon = viewGroupArry[i];
				var subCon = '<h5 class="subTitle"><span>' + numCon + '</span><i class="fa fa-angle-double-down"></i></h5><div class="subCon" id="subCon_' + numCon + '"></div>';
				formObj.append(subCon);
				$.each(formData, function(index, el) {
					console.log(JSON.stringify(el) + '000')
					var viewGroup = el.viewGroup; //分组
					var columnCode = el.columnCode; //字段名
					var columnName = el.columnName; //字段中文名	
					var columnType = el.columnType; //文本显示类型							
					var isInsertChild = el.isInsert; //是否新增字段
					var isEditChild = el.isEdit; //是否可编辑字段					
					if (isInsertChild == '1') {
						if (viewGroup == numCon) {
							var subObj = $('#subCon_' + numCon);
							//console.log(JSON.stringify(relationVal)+'rty')	
							createHtml(columnType, subObj, columnName, columnCode,null,null,null,relationVal);
						}
					}
				})
			}
			//判断分组的div是否为空，为空就隐藏
			var subConList = $('.subCon');
			divEmpty(subConList);
			var btn = '<div class="formBtnList"><button type="button" class="btn btn-primary" id="savaBtn"><i class="fa fa-save"></i> 提交</button><button type="button" class="btn btn-default" id="backBtn"><i class="fa fa-mail-reply-all"></i> 返回</button></div>';
			formObj.append(btn);
		}
	}
	//新增保存
	$(document).on('click', '#savaBtn', function() {
		var formList = $('#formList');
		var jsonVal = {};
		var text,radio,checkbox,select,area,password;
		getForm(formList, text, radio, checkbox, select, area,password, jsonVal);
		var paramAdd = {
			"json" : JSON.stringify(jsonVal)
		};
		console.log(JSON.stringify(jsonVal) + '000')
		ajaxSend('/bdp-web/viewTableCofig/addData/' + _id, paramAdd, function(data) {
			console.log(JSON.stringify(data) + '000')
			layer.msg(data.msg);
			window.location.href = '/bdp-web/PageConfig/index.html?id=' + _id;
		});
	})
	//编辑保存
	$(document).on('click', '#savaBtnEdit', function() {
		var formList = $('#formList');
		var jsonVal = {};
		var text,radio,checkbox,select,area,password;
		getForm(formList, text, radio, checkbox, select, area,password, jsonVal);
		var json1 = {
			"json" : JSON.stringify(jsonVal)
		};
		console.log(JSON.stringify(jsonVal)+'编辑保存')
		ajaxSend('/bdp-web/viewTableCofig/updateData/' + _id, json1, function(data) {
			layer.msg(data.msg);
			window.location.href = '/bdp-web/PageConfig/index.html?id=' + _id;
		});
	})

	//返回
	$(document).on('click', '#backBtn', function() {
		window.location.href = '/bdp-web/PageConfig/index.html?id=' + _id;
	})

})

//保存字典跟外键值
function relaKey(data,json){
	$.each(data,function(index,el){
		var columnType = el.columnType; //文本显示类型
		var columnCode = el.columnCode; //字段名
		var relationType = el.relation_type; //外键
		var relationTable = el.relation_table; //外键表
		var relationField = el.relation_field; //外键字段名
		//console.log(columnType+'UUU')
		if(columnType == 'radio' || columnType == 'select' || columnType == 'checked'){
			var param = {
					'relation_type':relationType,
					'relation_table':relationTable,
					'relation_field':relationField
			}
			ajaxSend('/bdp-web/dictionaryControll/getDictionary',param, function(data){					
				var list = data.results;			
				var jsonVal ={};
				jsonVal[columnCode]=list;
				json.push(jsonVal);
			})
		}
	})
}

//数组分组
function groupArray(data,arry){	
	$.each(data, function(index, el) {
		var viewGroup = el.viewGroup; //分组
		if (arry.indexOf(viewGroup) == -1) {
			arry.push(viewGroup);
		}
	})
}

