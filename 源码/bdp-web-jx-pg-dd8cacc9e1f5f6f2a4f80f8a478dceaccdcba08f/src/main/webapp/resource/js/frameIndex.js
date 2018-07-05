$(function(){
	//满屏显示
	function indexFullScreen(){
		var bodyHeight = $(window).height();//浏览器当前窗口文档的高度
		var topBox = $('#topbox');//头部
		var wrap = $('#wrapFram');
		var main = $('#conFram');
		var title = $('#titleFram');
		var right = $('#rightFram');
		var conHeight = bodyHeight - topBox.outerHeight();
		if(conHeight < 300){
			$('#wrapFram').css('height','300px');
			$('#conFram').css('height','300px');
			var rightHeight = main.height() - 49;
			$('#rightFram').height(rightHeight);
			$('.contentFram').height(rightHeight);
		}else{
			$('#wrapFram').outerHeight(conHeight);
			$('#conFram').outerHeight(conHeight);
			var rightHeight = main.height() - 49;
			$('#rightFram').height(rightHeight);
			$('.contentFram').height(rightHeight);

		}
	}
	indexFullScreen();
	$(window).resize(function() {
	  indexFullScreen();
	});	
})
//侧边栏菜单
function asidMenu(data,obj){
	var selectHtml = ''; 
	for(var i=0;i<data.length;i++){
		var item = data[i];
		//判断childrens是否存在
		if(item.childrens){			
			if(item.childrens == ''){
				selectHtml += '<li id="'+item.id+'" value="'+item.value+'"><h5><i></i>'+item.label+'</h5></li>';
			}else{
				selectHtml += '<li id="'+item.id+'" value="'+item.value+'"><h5><i></i>'+item.label+'</h5>';			
				var childMenu= item.childrens;			
				if(childMenu != ''){
					intMenuAside(childMenu);
				}
			}
		}else{
			selectHtml += '<li id="'+item.id+'" value="'+item.value+'"><p>'+item.label+'</p></li>';
		}
	}			
	obj.append(selectHtml);
	function intMenuAside(childMenu){
		var menu = childMenu;
		selectHtml +='<ul>';
		//判断childMenu是否存在
		if(childMenu){
			for(var j=0;j<childMenu.length;j++){ 
			     var menuchild = childMenu[j];
			     //判断childMenu是否存在
			     if(menuchild.childrens){			     	
		     		if(menuchild.childrens !=''){
						 selectHtml += '<li id="'+childMenu[j].id+'" value="'+childMenu[j].value+'"><h5><i></i>'+childMenu[j].label+'</h5>';
						 intMenuAside(menuchild.childrens)
						  selectHtml += '</li>';
					 }else{
						 selectHtml += '<li id="'+childMenu[j].id+'" value="'+childMenu[j].value+'"><h5><i></i>'+childMenu[j].label+'</h5></li>';
					 }
				}else{
					selectHtml += '<li id="'+childMenu[j].id+'" value="'+childMenu[j].value+'"><p>'+childMenu[j].label+'</p></li>';					
				}
			}
		}
		selectHtml +='</ul></li>';	
	}
	
}

//侧边菜单展开收缩函数
function menuInt(obj){
	var ul = obj.siblings('ul');
	var icoImg = obj.children('i');
	if(icoImg.hasClass('current')){
		ul.slideUp();
		icoImg.removeClass('current');
	}else{
		ul.slideDown();
		obj.children('i').addClass('current');
	}
}

//点击侧边的菜单右边内容区域改变
function asideList(obj){
	var titleLabel = obj.parents('.asideFram').siblings('#conFram').find('#titleLabel');
	var child = titleLabel.children('li');
		child.removeClass('current');
	var liId = obj.parent('li').attr('id');
	var aText = obj.text();
	var isHas = false;	
	var rightFram = $('#rightFram');
	var rightHeight = rightFram.height();
	$.each(child,function(index,el){
		var labelChild = $(this).children('a');
		var labelUrl = labelChild.attr('id');
		if(liId == labelUrl){
			isHas = true;
			$(this).addClass('current')
			var framElem = rightFram.find('.contentFram');
			$.each(framElem,function(index,el){
				var framId = $(this).attr('id');
				if(liId == framId){
					$(this).show().siblings().hide();
				}
			})
		}
	})	
	if(!isHas){
		rightFram.find('.contentFram').hide();
		var li = $('<li class="current"><a id="'+liId+'" target="mainFrame">'+aText+'</a><i></i></li>');
		titleLabel.append(li);
		var framHtml = $('<div class="contentFram" id="'+liId+'">'
						+'<iframe class="mainFrame" name="mainFrame" width="100%" height="'+rightHeight+'" frameborder="0"></iframe>'
						+'</div>');
		framHtml.find('iframe').attr('src','easyquery.html?id='+liId);		
		rightFram.append(framHtml);
	}
}
//点击页签
function switchTab(obj){
	var aElem = obj.children('a');
	var aId = aElem.attr('id');
	var rightFram = $('#rightFram');
	var framElem = rightFram.find('.contentFram');
	showId(framElem,aId);
	obj.addClass('current').siblings().removeClass('current');
}
//删除页签
function deletTab(obj){	
	var nextSibling = obj.parent('li').next('li');
	var prevSibling = obj.parent('li').prev('li');	
	var deletId = obj.siblings('a').attr('id');
	var rightFram = $('#rightFram');
	var framElem = rightFram.find('.contentFram');	
	if(obj.parent('li').is('.current')){
		if(nextSibling.length > 0){
			nextSibling.addClass('current');
			var nextUrl = nextSibling.find('a').attr('id');
			showId(framElem,nextUrl);				
		}else if(prevSibling.length > 0){
			prevSibling.addClass('current');
			var prevUrl = prevSibling.find('a').attr('id');
			showId(framElem,prevUrl);
		}		
		removeId(framElem,deletId);	
		obj.parent('li').remove();
	}else{
		removeId(framElem,deletId);		
		obj.parent('li').remove();		
	}
	var iElem = $('#titleLabel li i');
	if(iElem.length == 0){
		rightFram.html('');
		$('.titleLabel').hide();
	}
}

function removeId(list,id){
	$.each(list,function(index,el){
		var framId = $(this).attr('id');
		if(id == framId){
			$(this).remove();
		}
	})
}
function showId(list,id){
	$.each(list,function(index,el){
		var framId = $(this).attr('id');
		if(id == framId){
			$(this).show().siblings().hide();
		}
	})
}
