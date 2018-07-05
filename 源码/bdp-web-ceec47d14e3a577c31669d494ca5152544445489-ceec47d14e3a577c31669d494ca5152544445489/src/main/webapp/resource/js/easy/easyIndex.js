$(function(){
	var menuCode = GetQueryString("menuCode");
	switchHide();
	var param ={
			'id':menuCode
	}
	ajaxSendload('/bdp-web/easyQuery/selectTemplateTree', param, successCallBack);		
	function successCallBack(data){
		var list = data.list;	
		//console.log(JSON.stringify(list)+'权限菜单')
		var navList = $('.navList');
		navList.html('');
		asidMenu(list,navList);
	}
	//侧边菜单展开收缩
	$(document).on('click','.navList li > h5',function(){
		menuInt($(this));
	})
	//点击侧边的菜单右边内容区域改变
	$(document).on('click','.navList li > p',function(){
		$('.navList li > p').removeClass('current');
		$(this).addClass('current');
		asideList($(this));
		switchHide();
	})
	//点击页签
	$(document).on('click','#titleLabel > li',function(){		
		switchTab($(this));
		switchHide();
	})
	//删除页签
	$(document).on('click','#titleLabel > li > i',function(event){
		event.stopPropagation();
		deletTab($(this));		
	})	
	//页签标题
	function switchHide(){
		var titleLabel = $('#titleLabel li');
		if(titleLabel.length > 0){
			 $('.titleLabel').show();
		}else{
			 $('.titleLabel').hide();
		}
	}
	
	//菜单搜索	
	$('#select-search-btnMenuList').click(function(){		
		menuListSearch();
	});
	$('#searchMenuList').keydown(function(event){
		if(event.keyCode == 13) {
			menuListSearch();
		}
	})
	//选择表搜索
	function menuListSearch(){
		var tableNavLi = $('.navList').find('p');
		var tableInputVal = $.trim($('#searchMenuList').val());
		$.each(tableNavLi,function(index,el){			
			var LiText = $(this).text();
			if(tableInputVal == ''){
				$(this).parent('li').show();
				$('.navList').find('i').addClass('current');
			}
			if(LiText.indexOf(tableInputVal) >=0){
				$(this).parent('li').show();
				$(this).parent('li').parent('ul').show();
				$('.navList').find('i').addClass('current');
			}else{
				$(this).parent('li').hide();
				$('.navList').find('i').addClass('current');
			}
		})
	}
	
})