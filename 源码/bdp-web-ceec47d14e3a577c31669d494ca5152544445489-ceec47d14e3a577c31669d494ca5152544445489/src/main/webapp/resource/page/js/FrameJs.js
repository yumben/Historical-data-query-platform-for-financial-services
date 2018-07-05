var curPage = 1; //当前页码
var pageSize = 10; //每页显示数
var total = 0;//总记录数
var totalPage = 0; //总页数

var framebox = $('.framebox');
framebox.html(''); 
var framData = ''; //存储结构数据
var tabledata = {};
var relationVal = [];//存储外键字典
var _id = GetQueryString('id');
var paramFram = {
	"id" : _id
};
ajaxSend('/bdp-web/viewTableCofig/selectObj', paramFram, callBackFun);
//回调函数
function callBackFun(data) {
	tabledata = data;
	framData = data.columns;
	var type = data.viewType; //展示类型
	var id = data.table_id; //标签ID
	var title = data.viewTitle; //标题
	var pkField = data.pkField; //主键
	var isInsert = data.isInsert; //是否新增操作
	var isEdit = data.isEdit; //是否编辑操作
	var isSearch = data.isSearch; //是否有查询条件
	var isDelete = data.isDelete; // 是否有删除操作	
	var titleHtml = ''; //对象标题拼接 html
	if (title != null) { //标题是否存在
		titleHtml += '<h3>' + title + '</h3>';
	}
	//判断type，table：表格；form:表单	
	if (type == 'table') {
		var tableDiv = $('<div class="table-form" id="' + id + '">' + titleHtml + '</div>');
		$(framebox).append(tableDiv);		
		//表格
		var table = $('<div class="tableScroll"><table id="table_" class="table table-bordered table-hover table-striped"><thead><tr></tr></thead><tbody></tbody></table></div>');
		tableDiv.append(table);
		var thead = table.find('thead > tr');
		var tbody = table.find('tbody');
		theadHtml(thead);
		tbodyHtml(tbody);		
		//是否有查询条件	
		var searchHTML = $('<div class="text-search"></div>');
		if (isSearch == '1') {			
			searchHTML.insertBefore(table);
			searchFun(framData, searchHTML, isInsert);			
		} else {
			if (isInsert == '1') {
				searchHTML.insertBefore(table);
				searchHTML.append('<div class="item"><button type="button" class="btn btn-warning addBtn"><i class="fa fa-plus-square-o"></i> 新增</button></div>');
			}
		}
	}
}
//表头结构
function theadHtml(obj) {
	var isEdit = tabledata.isEdit;
	var isDelete = tabledata.isDelete;
	//表头数据结构	
	$.each(framData, function(index, el) {
		var isView = el.isView;//字段是否显示
		var isType = el.columnType;//字段显示类型
		var relationType = el.relation_type; //外键
		var relationTable = el.relation_table; //外键表
		var relationField = el.relation_field; //外键字段名	
		var columnCode = el.columnCode;//字段名
		
		if (isView == '1') {
			obj.append('<th data-filed="' + columnCode + '">' + el.columnName + '</th>');
		}
		if(isType == 'radio' || isType == 'select' || isType == 'checked'){
			var param = {
					'relation_type':relationType,
					'relation_table':relationTable,
					'relation_field':relationField
			}
			ajaxSend('/bdp-web/dictionaryControll/getDictionary',param, function(data){					
				var list = data.results;			
				var jsonVal ={};
				jsonVal[columnCode]=list;
				relationVal.push(jsonVal);
			})				
		}
	});
	//编辑操作
	var opter = $('<th data-filed="opteration">操作</th>');
	if(isNoEmpty(framData)){	
		if (isEdit == '1' || isDelete == '1') {
			obj.append(opter);
		}
	}
}
//表数据
function tbodyHtml(obj, param) {	
	obj.html('');
	var isEditData = tabledata.isEdit;
	var isDeleteData = tabledata.isDelete;
	var param1 = {
		'json' : ''
	};
	if (param) {
		param1 = param;
	}
	param1.curPage = curPage;
	param1.pageSize = pageSize;
	ajaxSend('/bdp-web/viewTableCofig/selectData/' + _id, param1, function(data) {
		total = data.totalCount;
		totalPage = data.totalPage;
		if(isNoEmpty(data.list)){		
			$.each(data.list, function(index, el) {
				var tdRow = $('<tr></tr>');
				var __id = '';
				$.each(framData, function(indexTh, elTh) {
					var checkType = elTh.columnType;//显示类型					
					var filedKey = elTh.columnCode;//字段的key
					var tdValselect = ''; //单元格内容多个用“,”隔开 显示
					var tdValue = el[filedKey];//字段中文名，值
					var aLink = elTh.links; //数据是否有链接
					var isPk = elTh.isPk; //是否是主键
					var isView = elTh.isView; //是否显示					
					if (isPk == '1') {
						tdRow.attr('id', tdValue);
						__id = tdValue;
					}
					
					//判断是有多个值的时候
					if(isNoEmpty(tdValue)){
						if(checkType == 'checked'){
							for(var i=0; i<relationVal.length;i++){
								for(var key in relationVal[i]){
									var valList = relationVal[i][key];
									if(filedKey == key){
										$.each(valList, function(indexVal, elVal) {
											for (var i = 0; i < tdValue.length; i++) {
												if (elVal.value == tdValue[i]) {
													tdValselect += elVal.label + ',';
												}
											}
										})
										if (tdValselect.lastIndexOf(',') != -1) {
											tdValselect = tdValselect.substring(0, tdValselect.length - 1);
										} else {
											tdValselect = tdValselect;
										}
									}
								}
							}
						}else if(checkType == 'radio' || checkType == 'select'){
							for(var i=0; i<relationVal.length;i++){
								for(var key in relationVal[i]){
									var valList = relationVal[i][key];
									if(filedKey == key){
										$.each(valList, function(indexVal, elVal) {
											if (elVal.value == tdValue) {
												tdValselect = elVal.label;
											}
										})										
									}
								}
							}
						}else{
							tdValselect = tdValue;
						}
					}else{
						tdValselect = '';						
					}
					//是否有链接,是否显示
					if(isNoEmpty(aLink) && isView == '1'){
						tdRow.append('<td data-filed="' + filedKey + '"><a href="' + aLink + __id + '">' + tdValselect + '</a></td>');
					} else if (isView == '1') {
						tdRow.append('<td data-filed="' + filedKey + '">' + tdValselect + '</td>');
					}
				});
				//操作事件
				if(isEditData == '1' || isDeleteData == '1'){
					var opterList = $('<td data-filed="opteration"></td>');
					if (isEditData == '1') {
						opterList.append('<a class="editTable">编辑</a>');
					}
					if (isDeleteData == '1') {
						opterList.append('<a class="deleteTable">删除</a>');
					}
					tdRow.append(opterList);
				}
				obj.append(tdRow);
			});
		} else {
			var len = $('#table_ > thead').find('th');
			var tdRowNo = $('<tr></tr>');
			tdRowNo.append('<td colspan="' + len.length + '"><p class="noList">没有相关数据</p></td>');
			obj.append(tdRowNo);
		}
		//分页
		$('.table-form').find('.page').remove();
		var pageDiv = $('<div class="page"></div>');
		$('.table-form').append(pageDiv);
		getPageBar();
	});
}
//查询模块
function searchFun(thData, obj, isInsert) {
	$.each(thData, function(index, el) {
		var isSearch = el.isSearch;//是否是查询字段
		var Type = el.columnType;//显示的文本类型
		var filed = el.columnName;//显示的中文名字
		var codeName = el.columnCode;//中文名对应的key值
		var IsPk = el.is_pk;//中文名对应的key值
		var val = '';
		if (isSearch == '1') {			
			createHtml(Type, obj, filed, codeName,val,IsPk,isSearch,relationVal);			
		}		
		
	});
	obj.append('<div class="item btnground"><button type="button" class="btn btn-primary selectBtn"><i class="fa fa-search"></i> 查询</button></div>');
	if (isInsert == '1') {
		obj.append('<div class="item btnground"><button type="button" class="btn btn-warning addBtn"><i class="fa fa-plus-square-o"></i> 新增</button></div>');
	}
}

//删除点击事件
$(document).on('click', '.deleteTable', function() {
	var keyId = $(this).parents('tr').attr('id');	
	var param = {
		'id' : keyId
	};
	var json = {
		"json" : JSON.stringify(param)
	}	
	layer.open({
		type : 1,
		area : [ 'auto', 'auto' ],
		btn : [ '确定', '取消' ],
		btnAlign : 'c',
		content : '<div class="showbox">删除数据</div>',
		yes : function(index, layero) {
			layer.close(index); //如果设定了yes回调，需进行手工关闭
			ajaxSend('/bdp-web/viewTableCofig/delData/' + _id, json, function(data) {				
				var tbody = $("#table_").find('tbody');
				tbodyHtml(tbody);
			});
		},
		cancel : function(index, layero) {
			layer.close(index)
		}
	});
})

//查询点击事件
$(document).on('click', '.selectBtn', function() {
	var searchBtn = $(this).closest('.text-search');
	var jsonVal = {};
	var text,radio,checkbox,select,area;
	getForm(searchBtn, text, radio, checkbox, select, area, jsonVal);
	var param = {
		'json' : JSON.stringify(jsonVal)
	};
	var tbody = $("#table_").find('tbody');
	tbodyHtml(tbody, param);
})

//获取分页条
function getPageBar() {
	//页码大于最大页数
	if (curPage > totalPage)
		curPage = totalPage;
	//页码小于1
	if (curPage < 1)
		curPage = 1;
	
	pageStr = "<em id=\"totalVar\" totalVar=\"" + total + "\">共" + total + "条</em><em><b>" + curPage
		+ "</b>/" + totalPage + "</em>";
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
}

$(document).on('click', '.page span a', function() {
	var rel = $(this).attr("rel");
	if (rel) {
		curPage = rel;
		var tbody = $("#table_").find('tbody');
		tbodyHtml(tbody);
	}
})



//跳转界面新增
$(document).on('click', '.addBtn', function() {		
	window.location.href='/bdp-web/PageConfig/form.html?id='+_id;
})
//跳转界面编辑
$(document).on('click', '.editTable', function() {
	var dataId = $(this).parents('tr').attr('id');
	window.location.href='/bdp-web/PageConfig/form.html?id='+_id+'&dataid='+dataId;
})



