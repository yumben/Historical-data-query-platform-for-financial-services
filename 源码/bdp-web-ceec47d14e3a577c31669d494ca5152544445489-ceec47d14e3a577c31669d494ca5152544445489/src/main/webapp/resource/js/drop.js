// JavaScript Document

	
	var del = [];	
	var strAttr = "";
	var strDfine = "";
	//统计对象属性下拉函数拼接	
	//var creamElm = once(function(){	
	function creamElm(){
		strAttr = "";
		for(var i=0;i<menujson.length;i++){
			var item = menujson[i];
			if(item.childMenus == ""){
				strAttr += "<li id='node-"+item.id+"' value="+item.value+" classid="+item.classId+" code="+item.code+"><em>"+item.name+"</em></li>";
			}else{
				strAttr += "<li id='node-"+item.id+"' value="+item.value+" classid="+item.classId+" code="+item.code+"><em><i class='icon-angle-right'></i>"+item.name+"</em>";
				
				var childMenu= item.childMenus;
				
				if(childMenu != ""){
					intMenu(childMenu);
				}
			}
		}
		return strAttr;
	}
    //});
	
	function intMenu(childMenu){
		var menu = childMenu;
		strAttr +="<ul>"	
		for(var j=0;j<childMenu.length;j++){ 
		     var menuchild = childMenu[j];
		     if(menuchild.childMenus !=""){
				 strAttr += "<li id='node-"+childMenu[j].id+"' value="+childMenu[j].value+" code="+childMenu[j].code+" classid="+childMenu[j].classId+"><em><i class='icon-angle-right'></i>"+childMenu[j].name+"</em>";
				 intMenu(menuchild.childMenus)
				  strAttr += "</li>";
			 }else{
				 strAttr += "<li id='node-"+childMenu[j].id+"' value="+childMenu[j].value+" code="+childMenu[j].code+" classid="+childMenu[j].classId+"><em>"+childMenu[j].name+"</em></li>";
			 }
		}	
		 strAttr +="</ul></li>"	
	};
	
	var typeAttr = "";
	function initIndexType(){
		typeAttr = "";
		for(var i=0;i<indexTypeJson.length;i++){
			var item = indexTypeJson[i];
			if(item.childMenus == "" || item.childMenus == null){
				typeAttr += "<li id='"+item.id+"' value="+item.value+" classid="+item.classId+" code="+item.code+"><em>"+item.name+"</em></li>";
			}else{
				typeAttr += "<li id='"+item.id+"' value="+item.value+" classid="+item.classId+" code="+item.code+"><em><i class='icon-angle-right'></i>"+item.name+"</em>";
				
				var childMenu= item.childMenus;
				
				if(childMenu != "" && childMenu != null){
					intTypeMenu(childMenu);
				}
			}
		}
		return typeAttr;
	};
	
	function intTypeMenu(childMenu){
		var menu = childMenu;
		typeAttr +="<ul>";	
		for(var j=0;j<childMenu.length;j++){ 
		     var menuchild = childMenu[j];
		     if(menuchild.childMenus !=""){
		    	 typeAttr += "<li id='"+childMenu[j].id+"' value="+childMenu[j].value+" code="+childMenu[j].code+" classid="+childMenu[j].classId+"><em><i class='icon-angle-right'></i>"+childMenu[j].name+"</em>";
		    	 intTypeMenu(menuchild.childMenus);
				 typeAttr += "</li>";
			 }else{
				 typeAttr += "<li id='"+childMenu[j].id+"' value="+childMenu[j].value+" code="+childMenu[j].code+" classid="+childMenu[j].classId+"><em>"+childMenu[j].name+"</em></li>";
			 }
		}	
		typeAttr +="</ul></li>";
	};
	
   //统计维度下拉函数拼接
	//dfinejson =
	function creamDfine(){
		strDfine = "";
		var setUp = {"id":"1","name":"设置字段","parentId":"0","childMenus":""};
		dfinejson.push(setUp);
		var sort = {"name":"排序","flag":"order","parentId":"0","childMenus":[
		               {"name":"默认","value":"desc","parentId":"2","childMenus":""},
		               {"name":"升序","value":"asc","parentId":"2","childMenus":""},
		               {"name":"降序","value":"desc","parentId":"2","childMenus":""},
		               {"name":"自定义","parentId":"2","childMenus":""}
		           ]};
		dfinejson.push(sort);
			
		for(var i=0;i<dfinejson.length;i++){
			var item = dfinejson[i];
			if(item.childMenus == ""){
				strDfine += "<li id='dfine-"+item.id+"' code="+item.code+" classid="+item.classId+" flag="+item.flag+"><em>"+item.name+"</em></li>";
			}else{
				strDfine += "<li id='dfine-"+item.id+"' code="+item.code+" classid="+item.classId+" flag="+item.flag+"><em><i class='icon-angle-right'></i>"+item.name+"</em>";
				
				var childMenu= item.childMenus;
				
				if(childMenu != ""){
					dfineMenu(childMenu);
				}
			}
		}
		return strDfine;
	}
	//console.log(creamDfine());
	//}
		
	function dfineMenu(childMenu){
		var menu = childMenu;
		strDfine +="<ul>"	
		for(var j=0;j<childMenu.length;j++){ 
		     var menuchild = childMenu[j];
		     if(menuchild.childMenus !=""){
				 strDfine += "<li id='dfine-"+childMenu[j].id+"'"+" code="+childMenu[j].code+" classid="+childMenu[j].classId+"><em><i class='icon-angle-right'></i>"+childMenu[j].name+"</em>";
				 dfineMenu(menuchild.childMenus)
				  strAttr += "</li>";
			 }else{
				 strDfine += "<li id='dfine-"+childMenu[j].id+"'"+" code="+childMenu[j].code+" classid="+childMenu[j].classId+"  value="+childMenu[j].value+"><em>"+childMenu[j].name+"</em></li>";
			 }
		}	
		 strDfine +="</ul></li>"		 	
	}
	
	//函数只执行一次	
	function once(fn, context) { 
		var result; 
		return function() { 
			if(fn) {
				result = fn.apply(context || this, arguments);
				fn = null;
			}
			return result;
		};
	}	
	
	
	
	 


$(function(){
	$(document).on("click",".vallist li > h5",function(){
		var that = $(this);
		if(that.siblings("ul").is(":hidden")){
			that.siblings("ul").slideDown();
			that.children("i").addClass("fa-chevron-down").removeClass("fa-chevron-right");
		}else{
			that.siblings("ul").slideUp();
			that.children("i").addClass("fa-chevron-right").removeClass("fa-chevron-down");
		}
		
	})

    //统计对象属性下拉菜单显示隐藏
    $(document).on("click",".defin-r-lsit01 > div > ul > li > b",function(event){	
	     event.stopPropagation();	
		var sibling = $(this).siblings("ul");
		if(sibling.is(":hidden")){
			$(".defin-r-lsit01 > div > ul > li > ul").slideUp();
			sibling.slideDown();
		}else{			
			sibling.slideUp();
		}
	});
	$(document).on("mouseover",".attrshow li em",function(){
		//event.stopPropagation();
		var child = $(this).siblings("ul");	
		  $(".attrshow li ul").hide();
		if(child.is(":hidden")){
			child.slideDown();			
		}else{
			child.hide();
		}
	})
	$(document).on("mouseover",".attrshow li ul",function(){
		$(this).show();
	})
	
	$(document).on("mouseover",".attrshow01 li em",function(){
		//event.stopPropagation();
		var child = $(this).siblings("ul");	
		  $(".attrshow01 li ul").hide();
		if(child.is(":hidden")){
			child.slideDown();			
		}else{
			child.hide();
		}
	})
	$(document).on("mouseover",".attrshow01 li ul",function(){
		$(this).show();
	})
	
	
	$(document).on("click",".defin-r-lsit01 > div > ul > li > b >i",function(){
		$(this).closest("li").remove();
	})
	
	$(document).click(function(){
		$(".defin-r-lsit01 > div > ul > li > ul").hide();	
	});
		
	//筛选器展开收缩
	$(document).on("click","#Filter li span",function(event){
		event.preventDefault();
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
	$(document).on("click","#Filter li span b",function(event){
		event.stopPropagation();
		$(this).closest("li").remove();
	})
})

































