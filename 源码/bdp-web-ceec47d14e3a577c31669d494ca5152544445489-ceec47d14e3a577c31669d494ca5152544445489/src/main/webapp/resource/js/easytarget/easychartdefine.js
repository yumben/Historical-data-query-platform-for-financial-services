var treeList = [];// 定义树的总个数
var treeObj2Name = "";
fullScreen();
$(window).resize(function() {
	fullScreen();
})
var menuCode = GetQueryString("menuCode");
$(function() {
	var templateId = GetQueryString("id");
	var param = {
		'id' : templateId,
	}
	//console.log(templateId+'修改回显')	
	// 计算对象
	var treeObj = $('#treeObj')[0];
	var treeObj2 = $('#treeObj2')[0];
	// 判断是修改还是新增
	if (templateId) {
		// alert('修改')		
		ajaxSendload('/bdp-web/easyTarget/selectObj', param, editSucess);
		calFunTable();
	} else {
		calFunTable();
	}
	// 计算对象
	var targetEleLi, newAttr;
	var tableMenuDrop = document.getElementById('tableMenu');
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

	treeObj2.ondragover = function(ev) {
		ev.preventDefault();
	}

	treeObj.ondrop = function(ev) {
		ev.preventDefault();
		var oDiv = document.createElement('div');
		var newEleLi = targetEleLi.cloneNode(true);
		var metadata_id = $(newEleLi)[0].getAttribute('metadata_id');
		var tableNameLi = $(newEleLi)[0].getAttribute('table_name');
		if ($(newEleLi)[0].getAttribute('tag')) {
			layer.alert('字段不能放置到“业务对象”！');
			return false;
		}
		var clickI = '<i class="fa fa-close"> </i>';

		$(newEleLi).append(clickI);
		var treeName = metadata_id;
		if (isExistTreeList(treeName)) {
			layer.alert('业务对象已存在！');
			return false;
		}
		if (treeObj2Name == treeName) {
			layer.alert('已存在指标结果中！');
			return false;
		}
		treeList.push(treeName);
		console.log(treeList+'!!~!~~~')
		$(oDiv).append(newEleLi);
		$(this).append(oDiv);
		var choseTree = $('#treeObj');
		sortIndex(choseTree);
		//console.log('字段的返回')
		var choseTreeLi = choseTree.children('div').children('li');
		calFunField(choseTreeLi,$(oDiv),tableNameLi);		
	}

	treeObj2.ondrop = function(ev) {
		ev.preventDefault();
		var oDiv = document.createElement('div');
		var newEleLi = targetEleLi.cloneNode(true);
		var metadata_id = $(newEleLi)[0].getAttribute('metadata_id');
		var tableNameLi = $(newEleLi)[0].getAttribute('table_name');
		if ($(newEleLi)[0].getAttribute('tag')) {
			layer.alert('字段不能放置到“指标结果”！');
			return false;
		}
		var clickI = '<i class="fa fa-close"> </i>';
		$(newEleLi).append(clickI);
		var treeName = metadata_id;
		if (isExistTreeList(treeName)) {
			layer.alert('已存在业务对象中！');
			return false;
		}
		if ($("#treeObj2 li").length >= 1) {
			layer.alert('指标结果无法添加多个！');
			return false;
		}
		treeObj2Name = treeName;
		//$(ev.target).append(newEleLi);
		$(oDiv).append(newEleLi);
		$(this).append(oDiv);
		var choseTree = $('#treeObj2');
		sortIndex(choseTree);
		var choseTreeLi = choseTree.children('div').children('li');
		calFunField(choseTreeLi,$(oDiv),tableNameLi);
	}

	function calFunField(list,obj,val){
		var param = {
				'table':''
		};
		$.each(list,function(){
			var metadata_id = $(this).attr('metadata_id');
			if(param.table == ''){
				param.table = metadata_id;
			}else{
				param.table += ','+ metadata_id;
			}
		})				
		ajaxSend('/bdp-web/metadata/selectObjAndPropertie', param, function(data){
			//console.log(JSON.stringify(data)+'返回的表字段')
			var list = data.list;
			var div = $('<div class="list-item"></div>');			
			obj.append(div);
			$.each(list,function(index,el){					
				var tableName =  el.table_code;				
				if(val == tableName){
					//console.log('320')
					//console.log(JSON.stringify(list)+'!!55')
					var columns = el.columns;
					$.each(columns,function(o,i){
						var span = $('<span>'+i.metadata_name+'</span>');
						span.attr({							
							'metadata_id' : i.metadata_id													
						})
						div.append(span);
					})
				}
			})
		});
	}
	
	// 点击已选择的表删除
	$(document).on('click', '#treeObj > div > li>i', function() {
		delTreeObjLi($(this));// 业务对象
	});

	// 点击已选择的表删除
	$(document).on('click', '#treeObj2 > div >li>i', function() {
		delTreeObjLi($(this));// 指标结果
		treeObj2Name = "";
	});

	// 拖出关联表请求关联表
	function calFunTable() {
		var choseTree = $('#treeObj').find('li');
		var param = {
			'param' : ''
		};		
		ajaxSend('/bdp-web/metadata/selectRelationMetadataJson', param,
				successTableLeftDrop);		
	}
	// 删除表li时执行
	function delTreeObjLi(a) {
		var orderIndex = a.parent().attr('order_index');
		var parentId = a.parent().attr('metadata_id');
		var parensib = a.parent().siblings();
		(a.parent().parent()).remove();
		delTreeList(parentId);
	}
	// 修改回显回调函数
	function editSucess(data) {		
		console.log(JSON.stringify(data)+'回显修改')
		$('#target_name').val(data.target_name);
		$('#target_catalog_id').val(data.target_catalog_id);
		$('#target_template_id').val(data.target_template_id);
		$('#target_desc').val(data.target_desc);
		$('#target_code').val(data.target_code);
		var targetSourceObj = data.targetSourceObj;
		var targetResultObj = data.targetResultObj;
		var treeObj = $('#treeObj');
		var treeObj2 = $('#treeObj2');
		var isTrue = true;
		var isFalse = false;
		backTableField(targetSourceObj,treeObj,isTrue);
		backTableField(targetResultObj,treeObj2,isFalse);
		console.log(treeList+'一个的'+treeObj2Name)
	}
	//修改回显的表名跟字段函数
	function backTableField(data,obj,val){
		$.each(data,function(index,el){			
			var div = $('<div></div>');
			var li = $('<li>'+el.table_name+'<i class="fa fa-close"></i></li>');
			li.attr({
				'fieldname':el.table_name,
				'metadata_id':el.metadata_id,
				'table_name':el.table_code
			})
			//var treeName = li.html();
			if(val == true){
				treeList.push(el.metadata_id);
				console.log(treeList+'数组中的字段')
			}else{
				treeObj2Name = el.metadata_id;
				console.log(treeObj2Name+'字段')
			}
			div.append(li);
			obj.append(div);
			calFunField(li,div,el.table_code);
		})
		
	}

	// 表回调函数
	function successTableLeftDrop(data) {
		$('#tableMenu').html('');
		var tableMenu = data.list;
		if (tableMenu != '' && tableMenu != null && tableMenu != 'null'
				&& tableMenu != undefined && tableMenu != 'undefined') {
			var showBox = '';
			showBox += '<ul id="treeListId" class="tableNav leftTableNav">';
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
						showBox += '<li fieldName="' + metadataName
								+ '" metadata_id="' + metadataId
								+ '" relationMeta="' + relationMeta
								+ '" beRelationMeta="' + beRelationMeta
								+ '" value="' + metadataCode
								+ '" draggable="true">' + metadataName
								+ '</li>';
						numList++;
					} else {
						showBox += '<li fieldName="' + metadataName
								+ '" metadata_code="' + parenCode
								+ '"><h5><i class="fa fa-plus-square"></i> '
								+ metadataName + '</h5><ul>';
						$.each(child, function(indexChild, elChilid) {
							showBox += '<li fieldName="'
									+ elChilid.metadataName + '" table_name="'
									+ elChilid.property.table_name
									+ '" parent_code="' + parenCode
									+ '" metadata_id="' + elChilid.metadataId
									+ '" value="' + elChilid.metadataCode
									+ '" relationMeta="'
									+ elChilid.relationMeta
									+ '" beRelationMeta="'
									+ elChilid.beRelationMeta
									+ '" draggable="true">'
									+ elChilid.metadataName + '</li>';
						})
						showBox += '</ul></li>';
					}
				}
			})
			showBox += '</ul>';
			$('#tableMenu').append(showBox);
		}
	}

	// 保存
	$('#btnsave').click(function() {
		if ($("#treeObj li").length == 0) {
			layer.alert('请先添加业务对象！');
			return false;
		}

		if ($("#treeObj2 li").length == 0) {
			layer.alert('请先添加指标结果！');
			return false;
		}

		var showBox = $('#saveEasy');
		var param = {};
		saveDilog(showBox, param);
	});
	// 保存的弹窗
	function saveDilog(obj, param) {
		// console.log(JSON.stringify(param)+'保存参数')
		layer
				.open({
					title : '保存模板',
					type : 1,
					area : [ 'auto', 'auto' ],
					btn : [ '确定', '取消' ],
					btnAlign : 'c',
					content : obj,
					yes : function(index, layero) {
						var targetName = $.trim($("#target_name").val());
						var url="/add";
						//console.log(templateId+'@###')
						if (templateId != '' && templateId != 'null' && templateId != null && templateId != undefined && templateId != 'undefined') {
							param['id'] = templateId;
							url = "/edit";
						}
						//console.log(url+'新增还是修改的')						
						if (targetName != '' && targetName != null
								&& targetName != 'null'
								&& targetName != undefined
								&& targetName != 'undefined') {

							var targetCatalogId = $
									.trim($("#target_catalog_id").val());
							var targetDesc = $.trim($("#target_desc").val());
							var targetTemplateId =$.trim($('#target_template_id').val());
							var targetCode =$.trim($('#target_code').val());
							var json = '{"target_name": "' + targetName
									+ '","target_catalog_id": "'
									+ targetCatalogId + '","target_template_id":"'
									+targetTemplateId+'","target_code":"'
									+targetCode+'","target_desc": "'
									+ targetDesc + '","targetSourceObj": ['
									+ geyJson("treeObj")
									+ '],"targetResultObj": ['
									+ geyJson("treeObj2") + ']}';

							var params = {
								"param" : json
							};

							layer.close(index);
							console.log(json+'保存的参数999')
							ajaxSendload('/bdp-web/easyTarget'+url, params,
									function(data) {
								         console.log(data);
										if (data.ret_code == '0') {
											$("#target_name").val("");
											$("#target_desc").val("");
											layer.alert('保存成功！');
											window.location.href = '/bdp-web/easytarget/easytarget-index.html'
											// 如果设定了yes回调，需进行手工关闭
										} else {
											layer.alert('保存失败！');
										}
									});

						} else {

							layer.alert('请输入指标名称！');
						}

					},
					cancel : function(index, layero) {
						layer.close(index)
					}
				});
	}

	// 返回
	$('#btnback')
			.click(
					function() {
						layer
								.confirm(
										'返回后修改的模板内容将不保存',
										{
											btn : [ '确定', '取消' ]
										// 按钮
										},
										function() {
											window.location.href = '/bdp-web/easytarget/easytarget-index.html';
										}, function() {
										});
					})

	$('#tableSearchInput').keydown(function(event) {
		console.log(event.keyCode);
		if (event.keyCode == 13) {
			tableSearch();
		}
	})

	// 清空选择的维度，字段属性，筛选器
	$(document).on(
			'click',
			'.rule-con > b.clearAttr',
			function() {
				var parentId = $(this).parents('.rule-con').children(
						'.rule-con-list').children('ul').attr('id');
				if (parentId == 'treeObj') {
					$(this).parents('.rule-con').children('.rule-con-list')
							.children('ul').empty();
					console.log(treeList+'删除业务对象')
					treeList.length =0;
					//treeList.splice(0,treeList.length);			
				} else {
					$(this).parents('.rule-con').children('.rule-con-list')
							.children('ul').empty();
					treeObj2Name = "";
				}
			})
	

})
//查询定义排序
function sortIndex(obj){
	//字段释放增加排序
	if(obj){
		$obj = $(obj);
		var child = $obj.children('li');
		var num = 1;
		$.each(child,function(index,el){
			$(this).attr('order_index',num++);
		})
	}
}

//灵活查询定义界面选择表的多级菜单展开收缩
$(document).on('click','.tableNav li > h5',function(){
	var sibling = $(this).siblings('ul');
	var paren = $(this).parent('li').siblings('li').children('ul');	
		paren.hide();
		$(this).parent('li').siblings('li').children('h5').find('i').addClass('fa-plus-square').removeClass('fa-minus-square');
	if(sibling.is(':hidden')){
		sibling.show();
		$(this).children('i').addClass('fa-minus-square').removeClass('fa-plus-square');
	}else{
		sibling.hide();
		$(this).children('i').addClass('fa-plus-square').removeClass('fa-minus-square');
	}
})

// 选择表搜索
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
// 判断业务对象是否存在
function isExistTreeList(treeName) {
	//console.log(treeList+'存在的')
	var boolean = false;
	for (var i = 0; i < treeList.length; i++) {
		if (treeName == treeList[i]) {
			boolean = true;
			break;
		}
	}
	return boolean;
}

// 删除存在的业务对象
function delTreeList(treeName) {
	//console.log(treeList+'存在的业务对象')
	for (var i = 0; i < treeList.length; i++) {
		if (treeName == treeList[i]) {
			treeList.splice(i, 1)
			break;
		}
	}
}

// 取json
function geyJson(id) {
	var json = ""
	$("#" + id + " li").each(function() {
		json += '{"metadata_id":"' + $(this).attr("metadata_id") + '","table_name":"' + $(this).attr("fieldname") + '","table_code":"' + $(this).attr("table_name") + '"},';		
	});
	if (json != "") {
		json = json.substring(0, json.length - 1);
	}

	return json;
}