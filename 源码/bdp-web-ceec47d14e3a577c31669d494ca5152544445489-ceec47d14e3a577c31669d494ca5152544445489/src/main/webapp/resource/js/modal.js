$(function(){
	
	//全选
	$(".selectinput").click(function(){
		if($(this).val()==0){
			$(this).closest(".alltable").find("input[type='checkbox']").prop("checked",true);
			$(this).val(1);
		}else{
			$(this).closest(".alltable").find("input[type='checkbox']").prop("checked",false);
			$(this).val(0);
		}
	})

	//元模型左边菜单
	$(document).on("click","#nav-left h5>i",function(){
		var that = $(this);
		var parent = that.parent("h5");
		if(parent.siblings("ul").is(":hidden")){
			parent.addClass("current");
			parent.siblings("ul").slideDown();	
			parent.children("i").removeClass("glyphicon-plus").addClass("glyphicon-minus");		
		}else{
			parent.siblings("ul").slideUp();
			parent.children("i").removeClass("glyphicon-minus").addClass("glyphicon-plus");
			parent.removeClass("current");
		}
	})	
	$(document).on("click","#nav-left a",function(){
		if($(this).hasClass("current")){
			$(this).removeClass("current");
		}else{
			$("#nav-left a").removeClass("current");
			$(this).addClass("current").parent("li").siblings().children("a").removeClass("current");
		}
	})

	//右边tab切换
	$(document).on("click",".tab-right-menu > li",function(){	
		$(this).addClass("current").siblings().removeClass("current");
		var index = $(this).index();
		$(".tab-content > div").eq(index).show().siblings().hide();
	})
	$("#modelMeijue").click(function(){
		$(".tab-content > div").eq(3).show().siblings().hide();
	})
    //增加弹出
   $(document).on("click",".navShow h5>i",function(){
		var that = $(this);
		var parent = that.parent("h5");
		if(parent.siblings("ul").is(":hidden")){
			parent.addClass("current");
			parent.siblings("ul").slideDown();	
			parent.children("i").removeClass("fa-plus-square").addClass("fa-minus-square");		
		}else{
			parent.siblings("ul").slideUp();
			parent.children("i").removeClass("fa-minus-square").addClass("fa-plus-square");
			parent.removeClass("current");
		}
	})	
	$(document).on("click","#nav-left a",function(){
		if($(this).hasClass("current")){
			$(this).removeClass("current");
		}else{
			$("#nav-left a").removeClass("current");
			$(this).addClass("current").parent("li").siblings().children("a").removeClass("current");
		}
	})


    //元数据弹出目录选择
	$(".showMeta input[type='radio']").click(function(){		
		if($("#metaList").is(":checked")){
			$(".metdDiv").slideDown();
			$(".metdDiv1").slideUp();			
		}else{
			$(".metdDiv").slideUp();
			$(".metdDiv1").slideDown();
		}
		if($("#metaList1").is(":checked")){
			$(".metdDiv1").slideDown();
			$(".metdDiv").slideUp();			
		}else{
			$(".metdDiv1").slideUp();
			$(".metdDiv").slideDown();
		}		
	})
	$(".showMeta01 input[type='radio']").click(function(){
		if($("#metaList2").is(":checked")){
			$(".metdDiv2").slideDown();
			$(".metdDiv3").slideUp();			
		}else{
			$(".metdDiv2").slideUp();
			$(".metdDiv3").slideDown();
		}
		if($("#metaList3").is(":checked")){
			$(".metdDiv3").slideDown();
			$(".metdDiv2").slideUp();			
		}else{
			$(".metdDiv3").slideUp();
			$(".metdDiv2").slideDown();
		}		
	})
	
	
	//右边内容收缩展示
	$(".metadatarulid .metadatarulid-title span").click(function(){
		var parent = $(this).parents(".metadatarulid").children(".metadata-con");
		//alert(parent);
		if(parent.is(":hidden")){
			parent.slideDown();
			$(this).removeClass("fa-angle-double-down").addClass("fa-angle-double-up");
			//alert("1111");
		}else{
			//alert("222");
			parent.slideUp();
			$(this).removeClass("fa-angle-double-up").addClass("fa-angle-double-down");
		}
	})
	
	$(document).on("click",".navmenu li > p",function(){
		$(".navmenu li > p").removeClass("current");
	    $(this).addClass("current");	
	})
	$(document).on("click",".navmenu li > h5",function(){
		$(".navmenu li > h5").removeClass("current");
	    $(this).addClass("current");	
	})
	
	
	
	
	//筛选器展开收缩
	$(document).on("click","#Filter li span",function(){
		var child = $(this).parents("li").children("div");
		if(child.is(":hidden")){
			child.slideDown();
		}else{
			child.slideUp();
		}		
	})
	//数值筛选
	$(document).on("change","#numselsect",function(){
		//alert($(this).val());
		if($(this).val() == 1){
			$(".numinput1").show();
			$(".numinput2").hide();
		}else if($(this).val() == 2){			
			$(".numinput2").show();
			$(".numinput1").hide();
		}
	})
	
	//筛选器删除
	$(document).on("click","#Filter li span b",function(){
		$(this).closest("li").remove();
	})
	
	
	
	//模拟下拉选框
	$(document).on("click",".drowbtn",function(event){
		event.stopPropagation();
		var sibling = $(this).siblings("ul");
		if(sibling.is(":hidden")){
			$(".drownlist").hide();
			sibling.show();			
		}else{
			sibling.hide();	
		}		
	})
	$(document).on("click",".drowlist > li",function(){
		var childText = $(this).find("a").html();
		$(this).closest(".drow").find(".drowbtn")[0].firstChild.nodeValue = childText;
   	   	$(this).parent(".drowlist").hide();				
	})
	$(document).click(function(){
        $(".drowlist").hide();
	});
	
	//精确筛选
	$(document).on("click","#site-l-list li > span",function(){	
		var Val = $(this).attr("value");		
		if(Val == "0"){			
			var Text = $(this).siblings("em").html();
			var dictionaryId=$(this).attr("dictionaryId");
			var siteLi = $("#site-r-li");
			var Li = "<li id='diction' dictionId="+dictionaryId+" dictionName="+Text+" ><em >"+Text+"</em><span>删除</span></li>";				
			siteLi.prepend(Li);
			$(this).attr("value","1");
		}else{
			alert("该筛选项已存在！");
		}
	});		
	$(document).on("click","#allBtn",function(){		
		$("#site-l-list li > span").each(function(){
			var Val = $(this).attr("value");
			if(Val == "0"){
				var Text = $(this).siblings("em").html();
				var dictionaryId=$(this).attr("dictionaryId");
				var siteLi = $("#site-r-li");
				var Li = "<li id='diction' dictionId="+dictionaryId+" dictionName="+Text+" ><em >"+Text+"</em><span>删除</span></li>";				
				siteLi.prepend(Li);
				$(this).attr("value","1");			
			}
		})
	})
	$(document).on("click","#site-r-li li > span",function(){
		var textClear = $(this).siblings("em").html();
		var par = $(this).parent("li");
		$("#site-l-list li > em").each(function(){
			if(textClear == Texe){
				par.remove();
				$(this).siblings("span").attr("value","0");	
			}
		})		
	})	
	$(document).on("click","#clearbtnSite",function(){
		$("#site-r-li li").remove();
		$("#site-l-list li > span").attr("value","0");	
	})
	//筛选器切换
	$("#siteBtnground input[type='radio']").click(function(){		
		if($("#siteBtn1").is(":checked")){
			$("#site-r-li li").remove();
			$("#site-l-list li >span").attr("value","0");
			$("#siteDiv").slideDown();
			$("#siteDiv1").slideUp();
		}else{
			$("#siteDiv").slideUp();
			$("#siteDiv1").slideDown();			
		}
		if($("#siteBtn2").is(":checked")){
			$("#text1").val("");
			$("#siteDiv1").slideDown();
			$("#siteDiv").slideUp();
		}else{
			$("#siteDiv1").slideUp();
			$("#siteDiv").slideDown();
		}		
	})
	
	
	//点击分页滚动条回顶部
	$(document).on("click",".pagination li a",function(event){
		event.stopPropagation();
		$('html,body', window.parent.document).animate({
			scrollTop : 0
		}, 0);
	});	
	
	
	 //对象查询展开收缩
    $(".showbtnico").click(function(){
    	var obj = $(this).parent("h5").siblings(".obj-search-form");
    	if(obj.is(":hidden")){
    		obj.show();    	
    		$(this).removeClass("fa-angle-double-down").addClass("fa-angle-double-up");
    	}else{
    		obj.hide();
    		$(this).removeClass("fa-angle-double-up").addClass("fa-angle-double-down");
    	}
    });

    //增加过滤条件
    var count=0;
    $("#addObject").click(function(){
    	var str = '<div class="item display_term" name="check_filter">'
		    		+'<select class="form-control section-control" name="filterconn" disabled="disabled">'
		            +'<option value="and">and</option>'
		            +'<option value="or">or</option>'
		            +'</select>'
    		
    		          +'<span>业务对象：</span>'
    		          +'<select class="form-control" name="filter_business_object"  id="business'+count+'" onchange="selectbusiness('+count+')">'
    		           +'</select>'
    	              +'<span>过滤属性：</span>'
    	              +'<select class="form-control" name="filter_con" id="businesspro'+count+'"  onchange="filterPro('+count+')">'    	                
    	              +'</select>'
    	              +'<select class="form-control section-control" name="filter_conn" id="numselsects">'
    	                +'<option value="=">等于</option>'
    	                +'<option value="!=">不等于</option>'
    	                +'<option value=">">大于</option>'
    	                +'<option value="<">小于</option>'
    	                +'<option value=">=">大于等于</option>'
    	                +'<option value="<=">小于等于</option>'
    	                +'<option selected="" value="between">区间</option>'
    	                +'<option value="not between">不在区间</option>'
    	              +'</select>'
    	              +'<span class="search-item">'
	    	              +'<input type="text"  class="form-control input-interva input01" name="filter_star" id="star'+count+'">'
	    	              +'<em class="equal">~</em>'
	    	              +'<input type="text"  class="form-control input-interva input02" name="filter_end" id="end'+count+'">'
    	              +'</span>'
    	              +'<span class="close-form"  >删除</span>'
    	            +'</div>'
    	            
        $("#searchForm").append(str);
    	count=count+1;
    	initSelect();
    	
    });

    $(document).on('mousemove','.display_term', function() {
    	$(this).children('.close-form').show();
    });
	$(document).on('mouseout','.display_term', function() {

    	$(this).children('.close-form').hide();
    });

   
   $(document).on('click', '.close-form', function() {
   	   var length = $('.display_term').length;

   	   if(length > 1 ){
   	       $(this).parent('.display_term').remove();   
      
   	   }else{
   	   	  $(this).remove();
   	   } 
		
   });
    
   $(document).on('change','.section-control',function(){
   	  var val = $(this).val();
   	  var input = $(this).siblings('.search-item');
   	  //alert(input);
   	  var input01 = input.children('.input01');
   	  var input02 = input.children('.input02');
   	  var equal = input.children('.equal');
   	  var select03 = input.children('.select03');
   	  if(val == '=' || val == '!=' || val == '>'|| val == '<'||val == '>=' || val == '<='){
   	  	input01.val("");
   	    input02.val("");
   	    input01.show();
   	  	input02.hide();
   	  	equal.hide();
   	    select03.hide();
   	  }else if(val == 'between'|| val == 'not between'){
   		input01.val("");
   	    input02.val("");
   	    input01.show();
   	  	input02.show();
   	  	equal.show();
   	    select03.hide();
   	  }else if(val=='isnull'){
   		input01.val("");
   	    input02.val("");
   	    input01.hide();
   	    input02.hide();
	  	equal.hide();
	  	select03.show();
   	  }
   }) 
   
   //增加查询条件
   var num = 0;
   $(document).on("click","#addObject1",function(){
	     if(id==""||id==null){
	         alert("请选择业务对象");
	         return;
	      }
	       num ++;
	   var strHtml = '<div class="obj-search-block" name="check_msg">'			              
			               +'<div class="item">'
				               +'<span>业务对象：</span>'
				               +'<select class="form-control" name="msg_business_object" id="theme'+num+ '"onchange="selectMetada('+num+')">'
				                +'</select>'
			               +'</div>'
			               +'<div class="item">'
				               +'<span class="fl">查询属性：</span>'
				               +'<div class="downbox">'
					               +'<button type="button" class="form-control downinput" type="text" id="downinput'+num+'" name="msg_downinput">请选择属性</button>'
				                   +'<em class="glyphicon glyphicon-triangle-bottom"></em>'
				                   +'<ul class="downlist" name="msg_downlist" id="proo'+num+'">'+'</ul>'
			                   +'</div>'
		                   +'</div>'
		                   +'<span class="close-form01" id="'+num+'">删除</span>'
		                   +'<div style="clear:both"></div>'
	                   +'</div>';
	   
	   $("#searchtop").append(strHtml);
	   initSelects();
   })

   $(document).on('mousemove','.obj-search-block', function() {
   	$(this).children('.close-form01').show();
   });
	$(document).on('mouseout','.obj-search-block', function() {
   	$(this).children('.close-form01').hide();
   });
	var clickId=null;
	$(document).on('click', '.close-form01', function() {
		clickId =$(this).attr("id");
		initObject(clickId);
		var idss = $("#theme"+clickId+" option:selected").attr("id");
   	   var length = $('.obj-search-block').length;
   	   if(length > 1 ){
   	       $(this).parent('.obj-search-block').remove();           
   	   }else{
   	   	  $(this).remove();
   	   }
   	condinitsele();
    selectMetada2(clickId,idss);
   });
	
	
});





