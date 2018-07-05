(function ($) {
//$(function(){
    $.fn.hsCheckData = function (options) {
        var defaults = {
            isShowCheckBox: false,            
            selectOnceText:null,
            selectVal:null,
            selectText:null,
            selectAlltVal:null,
            data: null
        };
        var opts = $.extend(defaults, options);        
        $(this).click(function(event) {        	
			//console.time();
        	var inputDiv = $(this);        	
        	if(inputDiv.val() == ''){
        		inputDiv.attr('tag','')
        	}        
        	event.stopPropagation();   
        	var tag = inputDiv.attr('tag');
        	var tagArr = tag.split(',');
        	var jsonTag = {};
        	for(var i=0;i<tagArr.length;i++){
        		jsonTag[tagArr[i]]='0';
        	}
        	//console.log(JSON.stringify(jsonTag)+'打印的值');
        	if($('.checkCon').length >0){
        		$('.checkCon').remove();
        	}
        	var mainHtml = '<div class="checkCon" style="display:none">';
        	//确定按钮
        	var btnHtml = sureBtn();
        	//加载数据
        	var dataList = getJsonHtml(opts.data,jsonTag);        	
        	mainHtml += btnHtml += dataList += '</div>';  
        	$('body').find('.checkCon').html('');
        	$('body').append(mainHtml);
        	//设置悬浮窗的位置跟大小        	
        	pos(inputDiv); 	
        	
            //点击展开菜单
            $('#checkListLi li p > i').click(function(event){
            	event.stopPropagation();
            	toggleShow($(this));
            })
            $('#checkListLi li h5 > i').click(function(event){
            	event.stopPropagation();
            	toggleShow($(this));
            })			
			//点击确定
			$('.savaSure').click(function() {
				saveSure($(this));
				setText(inputDiv);
			});
			//全选
			$('.allSelect').click(function() {
				mulitFun($(this));
			});
			//反选
			$('.reverseSelect').click(function() {
				reverseFun($(this));				
			})
			//点击弹出div不隐藏
			$('.checkCon').click(function(event) {
				event.stopPropagation();
			});
			 //点击空白选择框消失
			$(document).on("click",function(e){
				var target = $(e.target);
				if(target.closest(".checkCon").length == 0){
					$('.checkCon').remove();
				}
			})
			 //树形单选
			 $('#checkListLi li > p').click(function(){
				 onceSelect($(this),inputDiv);
			 });
			 //多选选择父级子集选中
			 $('#checkListLi').find('h5 input[type="checkbox"]').click(function(){
				 cloudSelect($(this));
			 })
        });
        
        $(this).on("keyup",function(event){
        	event.stopPropagation();
        	var inputDiv = $(this);
        	selectCheckInput($(this),true);		
	    	//点击展开菜单
	        $('#checkListLi li p > i').click(function(event){
	        	event.stopPropagation();
	        	toggleShow($(this));
	        })
	        $('#checkListLi li h5 > i').click(function(event){
	        	event.stopPropagation();
	        	toggleShow($(this));
	        })			
			//点击确定
			$('.savaSure').click(function() {
				saveSure($(this));
				setText(inputDiv);
				$('.checkCon').remove();
			});
	      //全选
			$('.allSelect').click(function() {
				mulitFun($(this));
			});
			//反选
			$('.reverseSelect').click(function() {
				reverseFun($(this));				
			})
	
			//点击弹出div不隐藏
			$('.checkCon').click(function(event) {
				event.stopPropagation();
			});
			 //点击空白选择框消失
			$(document).on("click",function(e){
				var target = $(e.target);
				if(target.closest(".checkCon").length == 0){
					$('.checkCon').remove();
				}
			})			 
			 //树形单选
			 $('.checkListLi li > p').click(function(){
				 onceSelect($(this),inputDiv);
			 });
			 //多选选择父级子集选中
			 $('.checkListLi').find('h5 input[type="checkbox"]').click(function(){
				 cloudSelect($(this));
			 })
        });
        //给文本框赋值
        function setText(obj){
        	if(opts.isShowCheckBox){
        		obj.attr('tag',opts.selectVal);
        		obj.val(opts.selectOnceText);        		
        	}else{
        		obj.val(opts.selectText);
        		obj.attr('tag',opts.selectAlltVal);        		
        	}
        }
        //搜索框html
        function selectInput(){
        	var html = '<div class="inputSelect">';
	        	html += '<input type="text" class="form-control inputCheck">';
	        	html += '</div>';
	        	return html;
        }
        //数据html
        function getJsonHtml(dataList,jsonTag){        	
        	var htmlList = '<div class="checkList"><ul class="checkListLi" id="checkListLi">';
        		htmlList += getaJson(dataList,jsonTag);
        		htmlList += '</ul></div>'; 
        		return htmlList;    		
        }
        //解析json数据 
        function getaJson(dataList,jsonTag){        	
        	var selectHtml = ''; 
        	if(dataList !=null && dataList !='null' && dataList !=''){        		
        		for(var i=0;i<dataList.length;i++){
        			var cabckVal ='';
    				var item = dataList[i];
    				var isCheck = '';
    				if(jsonTag !=null && jsonTag !='null' && jsonTag !='' && jsonTag !='undefined' && jsonTag !=undefined){	
    					cabckVal = isNoemty(jsonTag[item.value]);
        				if(cabckVal !=''){
        					isCheck = 'checked';
        				} 
    				}		
    				//判断childrens是否存在
    				if(item.childrens){
    					if(opts.isShowCheckBox){
    						if(item.childrens == ''){
    							selectHtml += '<li id="'+item.value+'"><p>'+item.label+'</p></li>';
    						}else{
    							selectHtml += '<li id="'+item.value+'"><p><i class="fa fa-plus-square"></i>'+item.label+'</p>';			
    							var childMenu= item.childrens;			
    							if(childMenu != ''){
    								intMenu(childMenu);
    							}
    						}
    					}else{						
    						if(item.childrens == ''){
    							selectHtml += '<li id="'+item.value+'"><h5><label><input type="checkbox" '+isCheck+'>'+item.label+'</label></h5></li>';
    						}else{
    							selectHtml += '<li id="'+item.value+'"><h5><i class="fa fa-plus-square"></i><label><input type="checkbox" '+isCheck+'>'+item.label+'</label></h5>';			
    							var childMenu= item.childrens;			
    							if(childMenu != ''){
    								intMenu(childMenu);
    							}
    						}
    					}
    				}else{
    					if(opts.isShowCheckBox){
    						selectHtml += '<li id="'+item.value+'"><p>'+item.label+'</p></li>';
    					}else{
    						selectHtml += '<li id="'+item.value+'"><h5><label><input type="checkbox" '+isCheck+'>'+item.label+'</label></h5></li>';
    					}
    				}
    			}
        	}		
			return selectHtml
			function intMenu(childMenu){
				var menu = childMenu;
				selectHtml +='<ul>';
				//判断childMenu是否存在
				if(childMenu){
					for(var j=0;j<childMenu.length;j++){ 
						var cabckValChild = '';
					     var menuchild = childMenu[j];
					     var isCheckChild = '';	
					     if(jsonTag !=null && jsonTag !='null' && jsonTag !='' && jsonTag !='undefined' && jsonTag !=undefined){	
					    	cabckValChild = isNoemty(jsonTag[childMenu[j].value]);    				
		    				if(cabckValChild !=''){
		    					isCheckChild = 'checked';
		    				}
					     }					     	
					    /* if(tagData !=null && tagData !='null' && tagData !=''){					    	 
							for(var k=0;k<tagData.length;k++){								
								if(childMenu[j].value == tagData[k]){
									isCheckChild = 'checked';
								}
							}
					     }*/
					     //判断childMenu是否存在
					     if(menuchild.childrens){
					     	if(opts.isShowCheckBox){
					     		if(menuchild.childrens !=''){
									 selectHtml += '<li id="'+childMenu[j].value+'"><p><i class="fa fa-plus-square"></i>'+childMenu[j].label+'</p>';
									 intMenu(menuchild.childrens)
									  //selectHtml += '</li>';
								 }else{
									 selectHtml += '<li id="'+childMenu[j].value+'"><p>'+childMenu[j].label+'</p></li>';
								 }
					     	}else{
					     		if(menuchild.childrens !=''){
									 selectHtml += '<li id="'+childMenu[j].value+'"><h5><i class="fa fa-plus-square"></i><label><input type="checkbox" '+isCheckChild+'>'+childMenu[j].label+'</label></h5>';
									 intMenu(menuchild.childrens)
									 // selectHtml += '</li>';
								 }else{
									 selectHtml += '<li id="'+childMenu[j].value+'"><h5><label><input type="checkbox" '+isCheckChild+'>'+childMenu[j].label+'</label></h5></li>';
								 }
					     	}
							}else{
								if(opts.isShowCheckBox){
									selectHtml += '<li id="'+childMenu[j].value+'"><p>'+childMenu[j].label+'</p></li>';
								}else{
									selectHtml += '<li id="'+childMenu[j].value+'"><h5><label><input type="checkbox" style="margin-left:16px;" '+isCheckChild+'>'+childMenu[j].label+'</label></h5></li>';
								}
							}
					}
				}
				selectHtml +='</ul></li>';	
			}
		}

		//确定按钮html
		function sureBtn(){
			var btnHtml ='';
			if(opts.isShowCheckBox){
				return btnHtml;
			}else{
				btnHtml += '<div class="checkBtn">';
				btnHtml += '<label class="allSelect"><input type="checkbox" value="0">全选</label>';
				btnHtml += '<label class="reverseSelect"><input type="checkbox" value="0">反选</label>';
				btnHtml += '<button class="btn btn-info savaSure">确定</button>';
				btnHtml += '</div>';
				return btnHtml;
			}
			
		}

		//点击展开收缩子集
		function toggleShow(obj){
			var ul = obj.parent().siblings('ul');
			if(ul.is(':hidden')){
				ul.show();
				obj.addClass('fa-minus-square').removeClass('fa-plus-square');
			}else{
				ul.hide();
				obj.addClass('fa-plus-square').removeClass('fa-minus-square');
			}
		}

		//文本框直接搜素
		function selectCheckInput(obj,isMinus){		
			var inputVal = $.trim(obj.val());
			//var checkList ='';	
			if(opts.isShowCheckBox){
				var newdata=selectTree(opts.data,inputVal);
				var mainHtml = '<div class="checkCon">';
	        	//确定按钮
	        	//var btnHtml = sureBtn();
	        	//加载数据
	        	var dataList = getJsonHtml(newdata);        	
	        	mainHtml += dataList += '</div>';  
	        	$('body').find('.checkCon').html('');
	        	$('body').append(mainHtml);
	        	//设置悬浮窗的位置跟大小        	
	        	pos(obj);
	        	//$('.checkCon').remove();
			}else{
				var newdata=selectTree(opts.data,inputVal);
				var mainHtml = '<div class="checkCon">';
	        	//确定按钮
	        	var btnHtml = sureBtn();
	        	//加载数据
	        	var dataList = getJsonHtml(newdata);        	
	        	mainHtml += btnHtml += dataList += '</div>';  
	        	$('body').find('.checkCon').html('');
	        	$('body').append(mainHtml);
	        	//设置悬浮窗的位置跟大小        	
	        	pos(obj);
			}
			if(isMinus){
				$('.checkCon').find('ul').show();
	        	$('.checkCon').find('i').removeClass('fa-plus-square').addClass('fa-minus-square');
			}			
		}
	   //搜索确定按钮
		function saveSure(obj){
				var checkList = obj.parent('.checkBtn').siblings('.checkList').children('#checkListLi').find('li > h5 > label');
				var textArr =[];
				var textVal = [];
				opts.selectText = textArr;
				opts.selectAlltVal = textVal;
				$.each(checkList,function(index,el){
					var text = $(this).text();
					var check = $(this).children('input[type="checkbox"]');
					var val = $(this).closest('li').attr('id');
					if(check.is(':checked')){
						textArr.push(text);
						textVal.push(val);
					}
				})
				if(textVal == ''){
					alert('请选择');
				}else{
					obj.parents('.checkCon').remove();
				}
		}

		//反选函数
		function reverseCheckFun(list){
			$.each(list,function(index,el){
				if($(this).is(':checked')){
					$(this).prop('checked',false);
				}else{
					$(this).prop('checked',true);
				}
			})	
		}
		
		//div定位
		function pos(obj){
			 var width = obj.outerWidth()+115;
            var height = obj.outerHeight();
            var x = obj.offset().top;
            var y = obj.offset().left;
            $('.checkCon').css({
            	"width": (width),
            	"left": y,
            	"top": (x + height),
            	"display":"block"
            });
            
		}
		//属性单选
		function onceSelect(obj,curObj){
			var oneText = obj.text();
			//console.log(oneText+'@@@')			
		 	var oneVal = obj.parent('li').attr('id');
		 		opts.selectOnceText = oneText;
		 		opts.selectVal = oneVal;
		 		setText(curObj);
		 		$(document).find('.checkCon').remove();
		 		obj.siblings('i.closeInput').show();
		}
		//多选的展开
		function cloudSelect(obj){
			var sib = obj.parents('h5').siblings('ul').find('input[type="checkbox"]');
			var sibparen = obj.parents('h5').siblings('ul').parents('li').children('h5').find('input[type="checkbox"]');
			 if(obj.is(':checked')){
				 sib.prop('checked',true);				 
			 }else{
				 sib.prop('checked',false); 
				 sibparen.prop('checked',false);				 
			 }
		}
		//多选
		function mulitFun(obj){
			$('.reverseSelect').find('input[type="checkbox"]').prop('checked',false);
			$('.reverseSelect').find('input[type="checkbox"]').val('0');
			var allCheck = obj.parents('.checkBtn').siblings('.checkList').find('input[type="checkbox"]');
			var child = obj.children('input[type="checkbox"]');
			var childVal = child.val();
			if(childVal == '1'){
				child.val('0');
				child.prop('checked',false);
				allCheck.prop('checked',false);					
			}else if(childVal == '0'){					
				allCheck.prop('checked',true);
				child.prop('checked',true);
				child.val('1');
			}
		}
		//反选
		function reverseFun(obj){
			$('.allSelect').find('input[type="checkbox"]').prop('checked',false);
			$('.allSelect').find('input[type="checkbox"]').val('0');
			var reverseCheck = obj.parents('.checkBtn').siblings('.checkList').find('input[type="checkbox"]');
			var child = obj.children('input[type="checkbox"]');
			var childVal = child.val();
			if(childVal == '1'){
				child.val('0');
				reverseCheckFun(reverseCheck);	
			}else if(childVal == '0'){
				child.val('1');
				reverseCheckFun(reverseCheck);
			}
		}
		//回显选中的字段
		function backVal(obj){
			//回显选中的字段
        	var tagVal = obj.attr('tag');
        	if(!opts.isShowCheckBox && tagVal.length > 0){
        		var checkList = $('#checkListLi');
        		var checkListLi = checkList.find('li'); 
        		var tagArr = tagVal.split(',');
        		//console.log('@@@@')
        		//console.time(); 
        		for(var i=0;i<tagArr.length;i++){
        			var oldVal = tagArr[i];
        			$.each(checkListLi,function(){
        				var val = $(this).attr('id');
        				if(oldVal == val){
        					$(this).children('h5').find('input[type="checkbox"]').prop('checked',true);
        				}
        			})
        		}
        		//console.timeEnd(); 
        	}
		}
		//var checkHtmlDete = document.getElementsByClassName('checkCon');
		$('#checkCon').remove();
    };
})(jQuery);
//})

