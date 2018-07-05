fullScreen();
$(window).resize(function(){
	fullScreen();
})
//输出属性下拉
var dimenJson = [
                 		{'code':'setdefaule','label':'默认输出','child':[]},
						{'code':'set','label':'设置别名','child':[]},
						{'code':'fieldformat','label':'设置字段格式','child':[]},
						{'code':'sort','label':'计算','child':[
								{'code':'sum','label':'求和','child':[]},
								{'code':'avg','label':'平均','child':[]},
								{'code':'count','label':'计数','child':[]},
								{'code':'notempty','label':'非空去重计数','child':[]},
								{'code':'nonemptycount','label':'非空计数','child':[]},
								{'code':'count_distinct','label':'去重计数','child':[]},
								{'code':'cancel','label':'取消','child':[]},
								{'code':'cancelAll','label':'全部取消','child':[]}
							]
						},
						{'code':'order','label':'排序','child':[
								{'code':'asc','label':'升序','child':[]},
								{'code':'desc','label':'降序','child':[]}
							]
						},
						{'code':'isTotal','label':'合计','child':[]}
					]
//维度下拉
var dimenList = [
               {'code':'setdefaule','label':'默认选中','child':[]}
			]
//筛选器字符类型
var charJson = [
                  	{"label":"等于","value":"="},
                  	{"label":"不等于","value":"!="},
                  	{"label":"匹配","value":"like"},
                  	{"label":"开头包含","value":"start"},
                  	{"label":"结尾包含","value":"end"},
                  	{"label":"包含","value":"in"},
                  	{"label":"不包含","value":"notin"}
                  ];
//日期，浮点，数值
var datatimeJson= [                
            	{"label":"等于","value":"="},
            	{"label":"不等于","value":"!="},
            	{"label":"大于","value":">"},
            	{"label":"小于","value":"<"},
            	{"label":"大于等于","value":">="},
            	{"label":"小于等于","value":"<="}, 
            	{"label":"区间","value":"between"},
            	{"label":"包含","value":"in"},
             	{"label":"不包含","value":"notin"}
            ];

//筛选器pk
var pkJson = [
				{
					"attr":"is_choic",
					"default_value": "0",
					"label": "多选",
					"field": "是否多选",
					"texttype": "checkbox"
				}
           ]

//生成的维度、输出属相标签
function creatLi(objAttr,obj){
	if(objAttr.tag == '1'){		
		var li = document.createElement('li');
			obj.appendChild(li);
			li.setAttribute('metadata_id',isNoemty(objAttr.metadataId));
			li.setAttribute('name',isNoemty(objAttr.name));
			li.setAttribute('title_name',isNoemty(objAttr.titleName));
			li.setAttribute('is_total_field',isNoemty(objAttr.isTotalField));
			li.setAttribute('alias',isNoemty(objAttr.alias));
			li.setAttribute('func',isNoemty(objAttr.func));
			li.setAttribute('field',isNoemty(objAttr.field));
			li.setAttribute('field_type',isNoemty(objAttr.dataType));
			li.setAttribute('order_index',isNoemty(objAttr.orderIndex));
			li.setAttribute('order_type',isNoemty(objAttr.orderType));
			li.setAttribute('show_form',isNoemty(objAttr.show_form));
			li.setAttribute('field_decimal_point',isNoemty(objAttr.field_decimal_point));
			li.setAttribute('field_unit_limit',isNoemty(objAttr.field_unit_limit));
			li.setAttribute('is_default',isNoemty(objAttr.is_default));
			li.setAttribute('is_compute',isNoemty(objAttr.is_compute));
			li.setAttribute('compute_expression',isNoemty(objAttr.compute_expression));
			li.setAttribute('parent_metadata',isNoemty(objAttr.parent_metadata));	
			li.setAttribute('table_code',isNoemty(objAttr.table_code));
			li.setAttribute('column_type',isNoemty(objAttr.column_type));			
			if(objAttr.flag == true){	
				//console.log(objAttr.field_table+'输出属性的')
				li.setAttribute('field_table',isNoemty(objAttr.field_table));
				li.setAttribute('title','字段名：'+objAttr.name+'\n数据类型：'+objAttr.dataType+'\n表名：'+objAttr.field_table);
			}else{				
				li.setAttribute('group_table',isNoemty(objAttr.group_table));
				li.setAttribute('title','字段名：'+objAttr.name+'\n数据类型：'+objAttr.dataType+'\n表名：'+objAttr.group_table);
			}
		var span = 	document.createElement('span');
		    span.innerText = objAttr.text;	
		    li.appendChild(span);
		var em = document.createElement('em');
			em.className = 'fa fa-angle-down';
			li.appendChild(em);
		var liClose = document.createElement('i');
		    liClose.className = 'fa fa-close';		    	
			li.appendChild(liClose);
			liClose.onclick = function(){
		    	deletAttr(this);
		    	if(objAttr.flag == true){
		    		creatTemplateTable(obj);
		    		sortIndex(obj);
		    	}	
			}
			if(objAttr.is_default == 'true'){
				var bHtml = document.createElement('div');
				bHtml.className = 'fa fa-check is_default';
			    li.appendChild(bHtml);
			}
			if(objAttr.orderType){
				if(objAttr.orderType == 'asc'){
					var strongHtml = document.createElement('p');
						strongHtml.innerText = '(升序)';
					    li.appendChild(strongHtml);
				}else if(objAttr.orderType == 'desc'){
					var strongHtml = document.createElement('p');
						strongHtml.innerText = '(降序)';
					    li.appendChild(strongHtml);
				}				
			}
			if(objAttr.field_eval){
				var b = document.createElement('b');
					b.innerText = objAttr.field_eval;
				    li.appendChild(b);
			}
			if(objAttr.alias !='undefined' && objAttr.alias != undefined && objAttr.alias !='' && objAttr.alias !='null' && objAttr.alias !=null){
				var strong = document.createElement('strong');
					strong.innerText = '['+objAttr.alias+']';
					li.appendChild(strong);
			}
			if(objAttr.show_form && objAttr.show_form =='thousand'){				
				var oDiv = document.createElement('div');
				oDiv.className = 'thousand';
			    li.appendChild(oDiv);
			}			
			//维度、字段属性下拉菜单、展示字段
			if(objAttr.flag == true &&　objAttr.is_compute != 'true'){
				li.setAttribute('is_compute',objAttr.is_compute);
				attrDowm(li,dimenJson,objAttr.isTotalField,null);
				creatTemplateTable(obj);			
			}else if(objAttr.flag == false){
				attrDowm(li,dimenList,null,objAttr.is_default);				
			}
			
	}
}
//展示字段函数
function creatTemplateTable(obj){	
	$('#ruleDetailList').html('');
	 var li = $(obj).children('li');
	 if(li.length <0) $('#ruleDetailList').html('');
	 $.each(li,function(index,el){
		 var is_compute = $(this).attr('is_compute');
		 var parent_metadata = $(this).attr('parent_metadata');
		 if(is_compute !='true'){
			 var aliasText = $(this).children('strong').text();
			 var funcText = $(this).children('b').text();
			 var titleName = $(this).attr('title_name');
			 $('#ruleDetailList').append('<span parent_metadata="'+parent_metadata+'">'+titleName+aliasText+funcText+'</span>'); 
		 }
		 
	 })
}
//筛选器生成元素标签
function dragScreen(ev,objAttr,obj){
		objAttr.text = ev.dataTransfer.getData('text');
		objAttr.metadataId = ev.dataTransfer.getData('metadataId');
		objAttr.field1 = ev.dataTransfer.getData('name');
		objAttr.value1 = ev.dataTransfer.getData('value1');
		objAttr.value2 = ev.dataTransfer.getData('value2');
		objAttr.cond = ev.dataTransfer.getData('cond');
		objAttr.field1 = ev.dataTransfer.getData('field1');
		objAttr.dataType = ev.dataTransfer.getData('dataType');
		objAttr.isFixed = '0';
		objAttr.tag = ev.dataTransfer.getData('tag');
		objAttr.is_choic = ev.dataTransfer.getData('is_choic');	
		objAttr.orderIndex = ev.dataTransfer.getData('order_index');
		objAttr.parent_metadata = ev.dataTransfer.getData('parent_metadata');
		objAttr.condition_table = ev.dataTransfer.getData('table_code');
		objAttr.tips = ev.dataTransfer.getData('tips');
		creatScreenHtml(objAttr,obj);
}
function creatScreenHtml(objAttr,obj){
	if(objAttr.tag =='1'){
		var oDiv = document.createElement('div');
			obj.appendChild(oDiv);		
			oDiv.setAttribute('metadata_id',isNoemty(objAttr.metadataId));
			oDiv.setAttribute('field1',isNoemty(objAttr.field1));
			oDiv.setAttribute('value1',isNoemty(objAttr.value1));
			oDiv.setAttribute('value2',isNoemty(objAttr.value2));
			oDiv.setAttribute('cond',isNoemty(objAttr.cond));
			oDiv.setAttribute('is_fixed',isNoemty(objAttr.isFixed));
			oDiv.setAttribute('field_type',isNoemty(objAttr.dataType));
			oDiv.setAttribute('is_choic',isNoemty(objAttr.is_choic));
			oDiv.setAttribute('order_index',isNoemty(objAttr.orderIndex));
			oDiv.setAttribute('valfield',isNoemty(objAttr.valfield));
			oDiv.setAttribute('dynamic_field',isNoemty(objAttr.dynamic_field));
			oDiv.setAttribute('parent_metadata',isNoemty(objAttr.parent_metadata));
			oDiv.setAttribute('condition_table',isNoemty(objAttr.condition_table));
			oDiv.setAttribute('valfield',isNoemty(objAttr.valfield));
			oDiv.setAttribute('dynamic_field',isNoemty(objAttr.dynamic_field));
			oDiv.setAttribute('tips',isNoemty(objAttr.tips));
		var h5 = document.createElement('h5');		
			oDiv.appendChild(h5);
		var span = 	document.createElement('span');
		    span.innerText = objAttr.text;	
		    h5.appendChild(span);
    	var b = document.createElement('b');
			b.className = 'fa fa-cog editScreen';
			oDiv.appendChild(b);		    			
		var liClose = document.createElement('i');
		    liClose.className = 'fa fa-close';		    	
			oDiv.appendChild(liClose);
			liClose.onclick = function(){
			    	deletAttr(this);
			    	sortIndexScreen(obj);
			}			
	}	
}

//点击选着聚合运算
$(document).on('click','.attrDowm li > span',function(){
	var paren = $(this).parents('.attrDowm').parents('ul').attr('id');
	var parent = $(this).parents('.attrDowm').parent('li');	
	$(this).parents('.attrDowm').hide();
	var text = $(this).text();	
	var funcVal = $(this).attr('value');
	var code = $(this).attr('value');
	var isTotalField = parent.attr('is_total_field');
	var is_default = parent.attr('is_default');
	//取消与全部取消
	if(code == 'cancel'){
		parent.find('b').remove();
		parent.attr('func','');
	}else if(code == 'cancelAll'){
		parent.find('b').remove();
		parent.attr('func','');
		parent.siblings('li').find('b').remove();
		parent.siblings('li').attr('func','');
	}else if(code == 'isTotal'){
		//合计与取消合计
		if(isTotalField == '0'){
			parent.attr('is_total_field','1');
			$(this).text('取消合计');
		}else if(isTotalField == '1'){
			parent.attr('is_total_field','0');
			$(this).text('合计');
		}
	}else if(code == 'asc'){
		parent.siblings('li').children('p').remove();
		parent.siblings('li').removeAttr('order_type');
		parent.children('p').remove();
		parent.children('i').before('<p>(升序)</p>');
		parent.attr('order_type','asc');
	}else if(code == 'desc'){
		parent.siblings('li').children('p').remove();
		parent.siblings('li').removeAttr('order_type');
		parent.children('p').remove();
		parent.children('i').before('<p>(降序)</p>');
		parent.attr('order_type','desc');
	}else if(code =='thousand'){
		parent.find('.thousand').remove();
		parent.append('<div class="thousand">('+text+')</div>');
		parent.attr('show_form','thousand');
	}else if(code =='cancelShowForm'){
		parent.find('.thousand').remove();
		parent.attr('show_form','');
	}else if(code =='setdefaule'){
		//console.log(paren+'@###')
		if(paren == 'outAttr'){
			if(is_default == 'true'){			
				parent.attr('is_default','false');	
				parent.find('div.is_default').remove();
				$(this).text('默认输出');
				//console.log('true进来的');
			}else{
				parent.attr('is_default','true');			
				parent.children('i').after('<div class="fa fa-check is_default"></div>');
				$(this).text('取消默认输出');
				//console.log('false进来的');
			}
		}else{
			if(is_default == 'true'){			
				parent.attr('is_default','false');	
				parent.find('div.is_default').remove();
				$(this).text('默认选中');
				//console.log('true进来的');
			}else{
				parent.attr('is_default','true');			
				parent.children('i').after('<div class="fa fa-check is_default"></div>');
				$(this).text('取消默认选中');
				//console.log('false进来的');
			}
		}
		
	}else{
		//聚合运算
		parent.find('b').remove();
		parent.children('i').after('<b>('+text+')</b>');
		parent.attr('func',funcVal);
	}	
	//展示输出字段
	if(paren == 'outAttr'){
		var getParentId = $(this).parents('.attrDowm').parent('li').parent('ul').attr('id');
		var ParentId = document.getElementById(getParentId);	
		creatTemplateTable(ParentId);
	}	
})

/*查询界面条件面板切换*/
$(document).on('click','#easyTabMenu li',function(){
	var index = $(this).index();
	$(this).addClass('current').siblings().removeClass('current');//easyTabCon
	var easyTabConList = $('#easyTabCon > div');
	var num = easyTabConList.eq(index);
	if(num.is(':hidden')){
		num.siblings().hide();
		num.show();
		$(this).children('i')
		$(this).siblings().children('i').addClass('fa-angle-double-down').removeClass('fa-angle-double-up');
		$(this).children('i').addClass('fa-angle-double-up').removeClass('fa-angle-double-down');		
	}else{
		num.hide();
		$(this).removeClass('current');
		$(this).children('i').addClass('fa-angle-double-down').removeClass('fa-angle-double-up');
	}
})








