$(function(){	
	//数据列表初始化
	var _json4Table = new $.json4Table({
		action : "/bdp-web/userManager/userQuery",
		formatter : operateFormatter
	});
	var params = {};
	_json4Table.drewTable(params);
	
	//操作
	function operateFormatter(id) {
		return [
		    '<a class="edit" href="javascript:setAuth(\'' + id
			+ '\')" title="权限设置">',
			'<i class="glyphicon glyphicon-cog">权限设置</i>',
			'</a>  ']
			.join('');
	}
	
	$('#btn-search').click(function() {
		var param = {'user_cn_name':$('#user_cn_name').val()};
		var params = {'param':JSON.stringify(param)};
		_json4Table.drewTable(params);
	});
})
//权限设置函数
var userId = '';
var privilegeMasterType ='';
function setAuth(id){
	userId = id;
	privilegeMasterType ='1';
	var param = {
			'privilege_master_id':id
	}
	ajaxSend('/bdp-web/authorization/authQuery',param,callBack);	
}

function callBack(data){
	$('#setAuthInput').val('');
	var queryObj = data.objectList;
	ajaxSend('/bdp-web/authorization/resourceQuery',{"param":"{}"},function(resource){
		var obj = resource.results;
		var perList = $('.perList');
		perList.html('');
		$.each(obj,function(index,el){
			var resourceOperation =el.resource_operation;//操作
			var resId = el.resource_id;
			var isCheck = '';
			var isOpterCheck = '';
			$.each(queryObj,function(j,k){
				var queryId = k.resource_id;				
				if(resId == queryId){
					var authPrivilegeOperationList = k.authPrivilegeOperationList;
					isCheck = 'checked';
					if(authPrivilegeOperationList.length>0){
						isOpterCheck = 'checked';
					}
					$.each(resourceOperation,function(o,t){
						var istrue='';
						$.each(authPrivilegeOperationList,function(i,y){
							if(t.operation_id == y.operation_id){	
								t.istrue='1';
							}
						});		
					});
				}
			})
			var li = $('<li><h5><i class="fa fa-plus-square"></i>'
					+'<label><input type="checkbox" '+isCheck+'>'+el.resource_name+'</label></h5>'
					+'<ul></ul></li>');
			li.attr({
				'resource_url':el.resource_url,
				'resource_code':el.resource_code,
				'resource_type':el.resource_type,
				'resource_id':el.resource_id			
			})
			var childOpt = $('<li><h5><i class="fa fa-plus-square"></i>'
					+'<label><input type="checkbox" '+isOpterCheck+'>操作</label></h5></li>');//操作
			if(resourceOperation.length > 0){
				li.children('ul').append(childOpt);
			}
			var ul = $('<ul></ul>');
			childOpt.append(ul);			
			$.each(resourceOperation,function(o,t){
				var isCheckList = '';
				if(t.istrue == '1'){
					isCheckList='checked';
				}
				var childLi = $('<li><label><input type="checkbox" '+isCheckList+'>'+t.operation_name+'</label></li>')
				childLi.attr({
					'id':t.id,					
					'resource_id':t.resource_id,
					'operation_id':t.operation_id
				})
				ul.append(childLi);				
			});				
			perList.append(li);
		})		
		var main = $('#setAuth');
		layer.open({
			type : 1,
			area : [ '500px', '480px' ],
			btn : [ '确定', '取消' ],
			btnAlign : 'c',
			content : main,
			yes : function(index, layero) {				
				//资源
				var authSource =[];
				var getLi = $('.perList > li > h5');
				$.each(getLi,function(o,j){	
					//操作
					var authOpter =[];
					//判断操作
					var childOpter = $(this).siblings('ul').children('li').children('ul').children('li');
					//资源判断
					var parenCheck = $(this).find('input[type="checkbox"]');
					var resource_id = $(this).parent('li').attr('resource_id');
					var resource_type = $(this).parent('li').attr('resource_type');
					var authSourceList ={};
					if(parenCheck.is(':checked')){
						authSourceList['privilege_master_type'] = privilegeMasterType;
						authSourceList['privilege_master_id'] = userId;
						authSourceList['resource_id'] = resource_id;
						authSourceList['privilege_type'] = '';						
						authSource.push(authSourceList);
						$.each(childOpter,function(index,el){							
							var _this = $(this);
							var authOpterList ={};
							var operationId = _this.attr('operation_id');
							var childLabel = _this.find('input[type="checkbox"]');
							if(childLabel.is(':checked')){
								authOpterList['resource_id'] = resource_id;
								authOpterList['operation_id'] = operationId;
								authOpterList['remarks'] = '';
								authOpter.push(authOpterList);							
							}
							authSourceList['authPrivilegeOperationList'] = authOpter;
						})
						
					}
										
				})				
				var param ={
					'privilege_master_id':userId,
					'authPrivilegeList':authSource
				};
				var params = {
						"param" : JSON.stringify(param)
					}
				//console.log(JSON.stringify(param)+'7777')
				ajaxSend('/bdp-web/authorization/authAdd',params,function(saveData){
					//console.log(JSON.stringify(saveData)+'333')					
				})
				layer.close(index); //如果设定了yes回调，需进行手工关闭
			},
			cancel : function(index, layero) {
				layer.close(index)
			}
		});
	});
}


