//菜单展开收缩
$(document).on('click','.perList li > h5 > i',function(){
	var sibling = $(this).parent('h5').siblings('ul');
	if(sibling.is(':hidden')){
		sibling.show();
		$(this).addClass('fa-minus-square').removeClass('fa-plus-square');
	}else{
		sibling.hide();
		$(this).addClass('fa-plus-square').removeClass('fa-minus-square');
	}
})
//点击选框最后一级父级勾选
$(document).on('click','.perList li > ul li input[type="checkbox"]',function(){
	var _this = $(this);
	var parentInput = _this.parents('ul').siblings('h5').find('input[type="checkbox"]');
	var sibling = _this.parent('label').parent('li').siblings('li').find('input[type="checkbox"]');
	if(_this.is(':checked')){
		parentInput.prop('checked',true);
	}else{
		var isTrue = false;
		$.each(sibling,function(index,el){		
			if($(this).is(':checked')){
				isTrue = true;
			}
		})
		if(isTrue){
			parentInput.prop('checked',true);
		}else{
			parentInput.prop('checked',false);
		}
	}
})
//点击父级子集全选
$(document).on('click','.perList li > h5 input[type="checkbox"]',function(){
	var child = $(this).parents('h5').siblings('ul').find('input[type="checkbox"]');
	if($(this).is(':checked')){
		child.prop('checked',true);
	}else{
		child.prop('checked',false);
	}
})
$(function(){
	//弹窗搜索
	$('#setAuthBtn').click(function(){		
		authSearch();
	});
	$('#setAuthInput').keydown(function(event){
		if(event.keyCode == 13) {
			authSearch();
		}
	})	
	function authSearch(){
		var tableNavLi = $('.perList > li > h5').find('label');
		var tableInputVal = $.trim($('#setAuthInput').val());
		$.each(tableNavLi,function(index,el){			
			var LiText = $(this).text();
			if(tableInputVal == ''){
				$(this).parents('li').show();				
			}
			if(LiText.indexOf(tableInputVal) >=0){
				$(this).parents('li').show();
			}else{
				$(this).parents('li').hide();
			}
		})
	}	
	$('#allAuthCheck').click(function(){
		var perList = $('.perList>li');
		var _this = $(this);
		var isTrue = false;
		$.each(perList,function(){				
			var child =$(this).find('input[type="checkbox"]'); 
			var childCheck = $(this).children('ul').find('input[type="checkbox"]');
			if($(this).is(':hidden')){				
				isTrue = true;
			}
		})
		if(_this.is(':checked')){
			$.each(perList,function(){				
				var child =$(this).find('input[type="checkbox"]'); 
				var childCheck = $(this).children('ul').find('input[type="checkbox"]');
				if($(this).is(':hidden')){
					isTrue = true;
				}else{
					//console.log('显示的');
					if($(this).is(':checked')){
						$(this).prop('checked',false);
						child.prop('checked',false);
						childCheck.prop('checked',false);
					}else{
						$(this).prop('checked',true);
						child.prop('checked',true);
						childCheck.prop('checked',true);
					}
				}
			})
		}else{
			if(isTrue){				
				$.each(perList,function(){				
					var child =$(this).find('input[type="checkbox"]'); 
					var childCheck = $(this).children('ul').find('input[type="checkbox"]');
					if($(this).is(':hidden')){
					}else{						
						if($(this).is(':checked')){
							$(this).prop('checked',true);
							child.prop('checked',true);
							childCheck.prop('checked',true);
						}else{
							$(this).prop('checked',false);
							child.prop('checked',false);
							childCheck.prop('checked',false);
						}
					}
				})
			}else{
				console.log('000')
				$('.perList li').find('input[type="checkbox"]').prop('checked',false);
			}
			
		}
	})
})

var menuCode = GetQueryString("menuCode");
function authHref(){
	window.location.href = '/bdp-web/sys/auth.html?menuCode=' + menuCode;
}
function roleHref(){
	window.location.href = '/bdp-web/sys/role.html?menuCode=' + menuCode;
}