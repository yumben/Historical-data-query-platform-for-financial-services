//查询定义满屏显示
function fullScreen(){
	var bodyHeight = $(window).height();//浏览器当前窗口文档的高度
	var sectionWrap = $('.section-wrap');//最外层div
	var sectionWrapHeight = $('.section-wrap').outerHeight();
	var sectionMain = $('.section-main');//内层div
	var sectionMainHeight = $('.section-main').outerHeight();
	var ruleTitle = $('.rule-title');//右边内容标题
	var ruleTitleHeight = $('.rule-title').outerHeight();
	var rule = $('.rule');//条件区
	var ruleHeight = $('.rule').outerHeight();
	var ruleContent = $('.rule-content');//展示字段去高度
	var mainHeight = bodyHeight - 76;
	sectionWrap.height(mainHeight);
	sectionMain.height(mainHeight);
	$('.centerContent').outerHeight(mainHeight);
	ruleContent.outerHeight($('.centerContent').outerHeight()-ruleTitleHeight-ruleHeight);
}
//输出属性下拉
var dimenJson = [
						{'code':'set','label':'设置别名','child':[]},						
						{'code':'sort','label':'计算','child':[
								{'code':'sum','label':'求和','child':[]},
								{'code':'avg','label':'平均','child':[]},
								{'code':'count','label':'计数','child':[]},
								{'code':'count_distinct','label':'去重计数','child':[]},
								{'code':'cancel','label':'取消','child':[]},
								{'code':'cancelAll','label':'全部取消','child':[]}
							]
						}
					]
//维度下拉
var dimenList = [
               {'code':'setSection','label':'分段统计','child':[]}
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
//分段计算
var sectionSelect =[
                	{"label":"等于","value":"="},
                	{"label":"不等于","value":"!="},
                	{"label":"大于","value":">"},
                	{"label":"小于","value":"<"},
                	{"label":"大于等于","value":">="},
                	{"label":"小于等于","value":"<="},
                	{"label":"区间","value":"between"},
                	{"label":"包含","value":"in"},
                 	{"label":"不包含","value":"notin"}
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
			li.setAttribute('parent_metadata',isNoemty(objAttr.parent_metadata));
			//console.log()
			if(objAttr.flag == true){
				li.setAttribute('field_table',objAttr.field_table);
				//console.log(isNoemty(objAttr.field_table)+'8989')
				li.setAttribute('title','字段名：'+objAttr.name+'\n数据类型：'+objAttr.dataType+'\n表名：'+objAttr.field_table);
			}else{
				li.setAttribute('group_table',isNoemty(objAttr.group_table));
			}
			li.setAttribute('param',isNoemty(objAttr.param));
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
		    		//creatTemplateTable(obj);
		    		sortIndex(obj);
		    	}	
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
			if(isNoemty(objAttr.alias) !=''){
				var strong = document.createElement('strong');
					strong.innerText = '['+objAttr.alias+']';
					li.appendChild(strong);
			}
			if(objAttr.show_form && objAttr.show_form =='thousand'){				
				var oDiv = document.createElement('div');
				oDiv.className = 'thousand';
				//oDiv.innerText = '(千分位)';
			    li.appendChild(oDiv);
			}			
			//维度、字段属性下拉菜单、展示字段
			if(objAttr.flag == true){
								
				attrDowm(li,dimenJson,objAttr.isTotalField,null);
				//creatTemplateTable(obj);			
			}else if(objAttr.flag == false){
				attrDowm(li,dimenList,null,null);
			}
			
	}
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
		objAttr.table_code = ev.dataTransfer.getData('table_code');
		objAttr.parent_metadata = ev.dataTransfer.getData('parent_metadata');
		objAttr.condition_table = ev.dataTransfer.getData('table_code');
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
			oDiv.setAttribute('condition_table',isNoemty(objAttr.table_code));
			oDiv.setAttribute('parent_metadata',isNoemty(objAttr.parent_metadata));
			oDiv.setAttribute('condition_table',isNoemty(objAttr.condition_table));
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
		if(is_default == 'true'){			
			parent.attr('is_default','false');	
			parent.find('b').remove();
			$(this).text('默认选中');
		}else{
			parent.attr('is_default','true');			
			parent.children('i').after('<b class="fa fa-check"></b>');
			$(this).text('取消默认选中');
		}
	}else if(code =='setSection'){
		sectionFun($(this));
	}else{
		//聚合运算
		parent.find('b').remove();
		parent.children('i').after('<b>('+text+')</b>');
		parent.attr('func',funcVal);
	}
})
