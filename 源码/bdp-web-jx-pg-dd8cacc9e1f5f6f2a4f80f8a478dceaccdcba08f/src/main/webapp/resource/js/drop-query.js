//拖拽
//$(function(){
	//表
	var businessObj = document.getElementById('businessObj');
	var businessObjEm = businessObj.getElementsByTagName('em');	
	var businessObjp = businessObj.getElementsByTagName('p');		
	var chiocSurface = document.getElementById('chiocSurface');
	var chiocSurfaceLi = chiocSurface.getElementsByTagName('li');
	
	//字段
	var queryAttr = document.getElementById('queryAttr01');
	var queryAttrEm = queryAttr.getElementsByTagName('strong');	
	var queryAttrp = queryAttr.getElementsByTagName('p');	
	var chiocFieldch = document.getElementById('chiocFieldch');
	var chiocFieldchLi = chiocFieldch.getElementsByTagName('li');
	//alert(chiocFieldchLi.length);
	//查询条件
	var queryfilterboxobj = document.getElementById('queryfilter');
	var queryfilterboxobjEm = queryAttr.getElementsByTagName('em');
	var queryfilterboxobjp = queryfilterboxobj.getElementsByTagName('p');

   
	//生成表格
	var headList = document.getElementById('headList');
	var headListth = document.getElementsByTagName('th');
	
	window.onload = function(){	
		//选择业务对象（表）
		buObjPro();
	};
	
	function  buObjPro(){
		for(var i=0;i<chiocSurfaceLi.length;i++){
			chiocSurfaceLi[i].ondragstart = function(ev){
				this.style.background = '#fff';			
				ev.dataTransfer.setData("name",this.innerText);	
				ev.dataTransfer.setData("val",this.getAttribute("value"));		
				ev.dataTransfer.setData("id",this.id);
				ev.dataTransfer.setData("code",this.getAttribute("code"));
			};
			chiocSurfaceLi[i].ondragend = function(ev){
				this.style.background ="none";					
			};
		}
		businessObj.ondragover = function(ev){
			ev.preventDefault();
		};		
		businessObj.ondrop = function(ev){
			ev.preventDefault();
			if(businessObjp.length > 0){
				for(var i=0;i<businessObjp.length;i++){					
					this.removeChild(businessObjp[i]);
				}				
			}		
			var sName = ev.dataTransfer.getData('name');
			var sVal = ev.dataTransfer.getData('val');		
			var id = ev.dataTransfer.getData('id');
			var code = ev.dataTransfer.getData('code');
			if(sVal == '0'){				
				addEmtext(sName,businessObj,id,null,code,null);
				refresh();
			}
		};		
		
	}
	
	//表名相同时不能增加
	function tableText(val){
		for(var i=0;i<businessObjEm.length;i++){
			if(businessObjEm[i].innerText == val){
				alert('此选项已经存在！');
			}
		}		
	}
	//删除函数
	function delEm(obj){
		$("#btn_search").val("");
		obj.parentNode.parentNode.removeChild(obj.parentNode);		
	}	

	//拖放生成的元素   
	function addEmtext(val,obj,id,parent,code,protype){ 
		var oEm = document.createElement('em');
		    oEm.innerHTML = val;
		    oEm.id=id;
		    if(parent!=null){
		    	oEm.setAttribute("parent",parent);
		    }
		    if(code!=null){
		    	oEm.setAttribute("code",code);
		    }
		    if(protype!=null){
		    	oEm.setAttribute("protype",protype);
		    }
		var oI = document.createElement('i');
		    oI.className = "icon-remove";
		    oEm.appendChild(oI);			
			obj.appendChild(oEm);
			
			
	}
	function refresh(){
		//刷新左侧业务对象表
		var listEmId=new Array();
		//获取所有em标签的id
		$("#businessObj em").each(function() {
			listEmId.push(  $(this).attr("id"));
	    });
		refreshBusinessObj(listEmId);
		//选择业务对象（表）
		buObjPro();
	}
	
	/*function clearHtml(obj,elem){
		obj.removeChild(elem);
		
	}*/
	

	//点击选择业务对象按钮
	$(document).on('click','#querybtnobj',function(){
/*		if($("#classIfication").children("option").text()=="无"){
			var op=$("<option id='calc_obj'>计算对象</option>");
			var opTwo=$("<option id='index' >指标</option>");
			$("#classIfication").children("option").remove();
			$("#classIfication").append(op);
			$("#classIfication").append(opTwo);
		}
		//获取选中的类型
		var id = $("#classIfication option:selected").attr("id");*/
	    
		//隐藏分组
        $(".disnone").hide();
		
		//显示所有业务对象
		var  params={"flag":"newBusinessObject"};
	    // initLeftMenu(params);
		refresh();
	     click_id = null;
	    $("#btn_search").val("");
		$('.nodetext').hide();
		$('#chiocSurface').show();
		$('#chiocFieldch').hide();
		$('#businessboxobj').show();
		$(this).addClass('businessObjpt');
	});

    $(document).on('click','#businessObj > em',function(){
    	//获取em的id    	
        var emId=$(this).attr("id");
        click_id = emId;
        $("#btn_search").val("");
        //点击后左侧出现属性
        businessObjPro(emId);
        
        //显示出分组
        $(".disnone").show();
        metaPraentId=emId;
        //initFun();
        chiocEm();
        releaseCondition();
        
        //alert($("#chiocFieldch li").length);
		$('.nodetext').hide();
		$('#chiocSurface').hide();
		$('#chiocFieldch').show();
		$('#queryAttrboxobj').show();	
		$('#queryfilterboxobj').show();	
	});

    var newFiledTemp= null;
    var id=null;
    function chiocEm(){
    	//选择输出属性
    	var  tempFlag=false;
    	for(var i=0;i<chiocFieldchLi.length;i++){
    		chiocFieldchLi[i].ondragstart = function(ev){
    			/*alert(2);
    			this.style.background = '#fff';			
    			ev.dataTransfer.setData("name",this.innerText);
    			ev.dataTransfer.setData("val",this.getAttribute("value"));	
    			ev.dataTransfer.setData("id",this.id);
    			ev.dataTransfer.setData("parent",this.getAttribute("parent"));
    			ev.dataTransfer.setData("code",this.getAttribute("code"));
    			ev.dataTransfer.setData("protype",this.getAttribute("protype"));
    			alert(this.getAttribute("newFiled"));
    			if(this.getAttribute("newFiled")=="yes"){
    				tempFlag=true;
    			}*/
    		};
    		chiocFieldchLi[i].ondragend = function(ev){
    			this.style.background ="none";
    		};
    	}
    	queryAttr.ondragover = function(ev){
    		ev.preventDefault();
    	};
    	queryAttr.ondrop = function(ev){
    		ev.preventDefault();
    		if((ev.dataTransfer.getData('isgroupField')=="null"||ev.dataTransfer.getData('isgroupField')=="undefined")&&(ev.dataTransfer.getData('iscalculatefield')=="null"||ev.dataTransfer.getData('iscalculatefield')=="undefined")){
    			newFiledTemp="null";
    		}else{
    			newFiledTemp="";
    		}
    		//alert(111);
       	  // 	if(ev.dataTransfer.getData('newFiled')=="yes"){//判断是否是新增计算字段或者分组字段(是就不加载计算组件)
       	   	   // tempFlag=false;
       	   		
       	  //	} else{
    	    var params = {"flag":"calculateIndexRule"};
    		//ajaxSend("/pms-web/ajax/ajaxQueryIndexAction",params,initAssembly);
    	    ajaxSend("/bdp-web/metaqueryTemplateDefinitionController/calculateIndexRule",params,initAssembly);
       	  //	}
    		if(queryAttrp.length > 0){
				for(var i=0;i<queryAttrp.length;i++){
					this.removeChild(queryAttrp[i]);
				}				
	     	}
    		var sName = ev.dataTransfer.getData('name');
    		var sVal = ev.dataTransfer.getData('val');			
    		 id = ev.dataTransfer.getData('id');	
    		var parent = ev.dataTransfer.getData('parent');			
    		var code = ev.dataTransfer.getData('code');
    		var protype = ev.dataTransfer.getData('protype');	
    		if(sVal == '1'){
    			
    			var oEm = document.createElement('em');
    			    //oEm.innerHTML = sName;
    			    oEm.id = id;
    			    oEm.setAttribute("parent",parent);
    			    oEm.setAttribute("code",code);
    			    oEm.setAttribute("comcode","");
    			    oEm.setAttribute("protype",protype);
    			
    			var oB = document.createElement("strong");
    		          oB.innerHTML = sName;     
    		          oEm.appendChild(oB);
    		          
    		          
    			var oE = document.createElement("em");
    		          oE.className = "icon-angle-down";
    		          oB.appendChild(oE);
    			    
    			var oI = document.createElement('i');
    			    oI.className = "icon-remove";			    
    			    oB.appendChild(oI);			
    				queryAttr.appendChild(oEm);
    			    addTh();
    			/*oI.onclick = function(){
    			    	delEm(this);	
    			    	addTh();
    			};*/
    			 // $(".attrshow01").empty();
    			   var oUl = document.createElement("ul"); 
    	           oUl.className = "attrshow01";
    	           oUl.innerHTML = creamElm();
    	           //oLi.appendChild(oUl);
    	          // objattr.appendChild(oLi);
    	           oEm.appendChild(oUl);
    		}
    	};

    	
    		  
    	
    	
    }
    //释放条件生成
   function  releaseCondition(){
	   var  tempFlag=false;
   	for(var i=0;i<chiocFieldchLi.length;i++){
		chiocFieldchLi[i].ondragstart = function(ev){
			this.style.background = '#fff';		
			ev.dataTransfer.setData("name",this.innerText);
			ev.dataTransfer.setData("val",this.getAttribute("value"));	
			ev.dataTransfer.setData("id",this.id);
			ev.dataTransfer.setData("parent",this.getAttribute("parent"));
			ev.dataTransfer.setData("code",this.getAttribute("code"));
			ev.dataTransfer.setData("protype",this.getAttribute("protype"));
			ev.dataTransfer.setData("isgroupfield",this.getAttribute("isgroupfield"));
			ev.dataTransfer.setData("iscalculatefield",this.getAttribute("iscalculatefield"));
			if(this.getAttribute("isgroupfield")=="yes"||this.getAttribute("iscalculatefield")=="yes"){
				tempFlag=true;
			}
		};
		
		chiocFieldchLi[i].ondragend = function(ev){
			this.style.background ="none";
		};
	}

	 //查询条件释放生成    
       queryfilterboxobj.ondragover = function(ev){
   		ev.preventDefault();
   	};
   	queryfilterboxobj.ondrop = function(ev){
   		ev.preventDefault();
   		if(queryfilterboxobjp.length > 0){
				for(var i=0;i<queryfilterboxobjp.length;i++){
					this.removeChild(queryfilterboxobjp[i]);
				}
				
			}
   		var sName = ev.dataTransfer.getData('name');
   		var sVal = ev.dataTransfer.getData('val');		
   		var id = ev.dataTransfer.getData('id');	
   		var code = ev.dataTransfer.getData('code');
   		var protype = ev.dataTransfer.getData('protype');
   		var temp=false;
   	   	if(tempFlag){//判断是否是新增计算字段或者分组字段（不能拖拽到条件中）
   	   	    tempFlag=false;
   	   		return;
   	   	}  
			$("#queryfilter em i").each(function(){
				if($(this).parent('em').attr("id")==id){
					alert("已选择了该属性！");
					temp=true;
				}
			});
			if(temp==true){
				temp=false;
				return;
			}
   		var parent = ev.dataTransfer.getData('parent');	
   		if(sVal == '1'){
   			addEmtext(sName,queryfilterboxobj,id,parent,code,protype);	
   		}
   	};
    	
    }
    
    
   $(document).on("click","#chiocSurface h5",function(){
		//var that = $(this);
		//var parent = $(this).parent("h5");
		if($(this).siblings("ul").is(":hidden")){
			$(this).addClass("current");
			$(this).siblings("ul").slideDown();	
			$(this).children("i").removeClass("fa-plus-square").addClass("fa-minus-square");		
		}else{
			$(this).siblings("ul").slideUp();
			$(this).children("i").removeClass("fa-minus-square").addClass("fa-plus-square");
			$(this).removeClass("current");
		}
	});	
  
   $(document).on("click","#businessObj > em >i",function(event){
	   event.stopPropagation();
	   //$(this).parent('em').remove();
	   //删除id
	   var id=$(this).parent('em').attr("id");
		/*$("#queryfilter em").each(function(){
			var ids=$(this).attr("id");
			alert(ids);
			if($(this).attr("parent")==id){
				alert($(this).attr("parent")+"::::"+id);
				//$("#"+ids).children('i').trigger("click");
				$("#"+ids).remove();
			}
		});*/
	   //前置集合
	   var beforeList=new Array();
	   //后置集合
	   var afterList=new Array();
	   //所有
	   var allList=new Array();
	   var flag=false;
	   $("#businessObj em ").each(function(){
		   allList.push($(this).attr("id"));
			   if($(this).attr("id")==id){
	         	  flag=true;
	         	  return true;
	           }
              if(flag==true){
            	  afterList.push($(this).attr("id"));
              }else if($(this).attr("id")!=id){
            	  beforeList.push($(this).attr("id"));
              } 
		});
       // alert(beforeList);
       // alert(afterList);
	   
	   //关联删除
	   //relationDelete(beforeList,afterList,allList,id);

	   
		$("#queryfilter em i").each(function(){
			if($(this).parent('em').attr("parent")==id){
				delEm(this);
			}
			if(afterList!=null){
			for ( var int = 0; int < afterList.length; int++) {
				if($(this).parent('em').attr("parent")==afterList[int]){
					delEm(this);
				}
			}
			}
		});
		$("#queryAttr01  em strong i").each(function(){
			if($(this).parent('strong').parent('em').attr("parent")==id){
				 $(this).closest("em").remove();
				//delEm(this);
				addTh();
			}
			if(afterList!=null){
			for ( var int = 0; int < afterList.length; int++) {
				if($(this).parent('strong').parent('em').attr("parent")==afterList[int]){
					 $(this).closest("em").remove();
					//delEm(this);
					addTh();
				}
			 }
			}
		});
	   
	   delEm(this);
	   $("#businessObj em i").each(function(){
		   if(afterList!=null){
			   for ( var int = 0; int < afterList.length; int++) {
				   if($(this).parent('em').attr("id")==afterList[int]){
						delEm(this);
					 }
				   }
		   }
		   
	   });
       //隐藏分组
       $(".disnone").hide();
       
	   //alert("1");
   	   //刷新左侧业务对象表
		var listEmId=new Array();
		//alert(listEmId);
		//获取所有em标签的id
		$("#businessObj em").each(function() {
			listEmId.push(  $(this).attr("id"));
	    });
		refreshBusinessObj(listEmId);
		buObjPro();
		
		
		
   });
   
   $(document).on("click","#queryfilter > em >i",function(event){
	   event.stopPropagation();
	   //$(this).parent('em').remove();
	   delEm(this);

   });
   $(document).on("click","#queryAttr01 > em  > strong >i",function(event){
	   event.stopPropagation();
	   //$(this).parent('em').remove();
	   //delEm(this);
	   $(this).closest("em").remove();
	   addTh();
   });

      
//});

//加载计算组件
var menujson =new Array();
function initAssembly(data){
	var assembly = data.assembly;
	var methodArray = data.tableArray;
	menujson=new Array();
	if(assembly != null){
		if(newFiledTemp=="null"){
		assembly.forEach(function(e,i){
			var assemblyObj = {"id":e.metadataId,"name":e.metadataName,"parentId":"0","value":e.metadataId,"code":e.metadataCode,"classId":e.classId};
			var method = new Array();
			var index = -4;
			if(methodArray[i] != null){
				methodArray[i].forEach(function(vo,j){
					if(j == methodArray[i].length-1){
						var methodObj = {"id":vo.metadataId,"name":vo.metadataName,"parentId":vo.parentMetadata,"value":vo.metadataId,
						"classId":vo.classId,"code":vo.metadataCode,"childMenus":""};
						method.push(methodObj);
						var more = {"id":index,"name":"更多","parentId":e.metadataId};
						var mix = {"id":index+1,"name":"最小值","parentId":index,"childMenus":""};
						var max = {"id":index+2,"name":"最大值","parentId":index,"childMenus":""};
						var array = new Array();
						array.push(mix);
						array.push(max);
						more.childMenus = array;
						method.push(more);
					}else {
						var methodObj = {"id":vo.metadataId,"name":vo.metadataName,"parentId":vo.parentMetadata,"value":vo.metadataId,
						"classId":vo.classId,"code":vo.metadataCode,"childMenus":""};
						method.push(methodObj);
					}
				});
			}
			assemblyObj.childMenus = method;
			menujson.push(assemblyObj);
		});
		
		var item1 = {"id":"-1","name":"数据格式设置","parentId":"0","childMenus":""};
		var item2 = {"id":"-2","name":"设置属性信息","parentId":"0","childMenus":""};
		var item3 = {"id":"-3","name":"取消","parentId":"0","childMenus":""};
		menujson.push(item1);
		menujson.push(item2);
		menujson.push(item3);
		}else{
			var item1 = {"id":"-1","name":"数据格式设置","parentId":"0","childMenus":""};
			var item2 = {"id":"-2","name":"设置属性信息","parentId":"0","childMenus":""};
			var item3 = {"id":"-3","name":"取消","parentId":"0","childMenus":""};
			menujson.push(item1);
			menujson.push(item2);
			menujson.push(item3);
		}
	}
}

   //统计对象属性下拉函数拼接
   var strAttr = "";
  var creamElm = function(){
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
  //}
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

	    
	
// 计算组件id
var calculateId = null;
// 计算组件类id
var calClassId = null;
// 计算组件名称
var calName = null;
// 计算组件代码
var calCode = null;
// 获取计算方法
var method = null;
var text=null;

$(document).on("click",".attrshow01 li ul li > em",function(){
var id=$(this).parent('li').parent('ul').parent('li').parent('ul').parent('em').attr("id");
var ids=$(this).parent('li').parent('ul').parent('li').parent('ul').parent('li').parent('ul').parent('em').attr("id");

var protype=$(this).parent('li').parent('ul').parent('li').parent('ul').parent('em').attr("protype");
var protypes=$(this).parent('li').parent('ul').parent('li').parent('ul').parent('li').parent('ul').parent('em').attr("protype");

var onecode= $(this).parent("li").attr("code");
//判断字段类型（字符型只能计数）
if(protype!=null&&protype!=""){
	protype=protype.charAt(protype.length - 1);
	if((protype==2||protype==1)&&onecode!="count"){
		alert("该属性只能计数");
		return;
	}
}else{
	protypes=protypes.charAt(protypes.length - 1);
	if(protypes==2||protypes==1&&onecode!="count"){
		alert("该属性只能计数");
		return;
	}
}

method = $(this).parent().attr("value");
var name = $(this).text();
calculateId = $(this).parent().parent().parent().val();
calClassId = $(this).parent().parent().parent().attr("classid");
calName = $(this).parent().parent().parent().children("em").text();
calCode = $(this).parent().parent().parent().attr("code");


/*$("#queryAttr01").children("em").each(function(e,i){
	var  chid= $(this).attr("id");
	if(id!=undefined){
	if(chid==id){
		$(this).attr("comcode",onecode);
		 text = $(this).children("strong").text().split("(");
		$(this).children("strong").text(text[0]+"("+name+")");
		$(this).children("strong").append($("<em class='icon-angle-down'></em>"));
		$(this).children("strong").append($("<i class='icon-remove'></i>"));
		addTh();
	    }
	}else{
		if(chid==ids){
			 text = $(this).children("strong").text().split("(");
			$(this).children("strong").text(text[0]+"("+name+")");
			$(this).children("strong").append($("<em class='icon-angle-down'></em>"));
			$(this).children("strong").append($("<i class='icon-remove'></i>"));
			addTh();
			}
	}
});*/
var text_one=$(this).parent('li').parent('ul').parent('li').parent('ul').parent('em').children("strong").text().split("(");
var text2_two=$(this).parent('li').parent('ul').parent('li').parent('ul').parent('li').parent('ul').parent('em').children("strong").text().split("(");

if(text_one!=""&&text_one!=null){
	$(this).parent('li').parent('ul').parent('li').parent('ul').parent('em').attr("comcode",onecode);
	$(this).parent('li').parent('ul').parent('li').parent('ul').parent('em').children("strong").text(text_one[0]+"("+name+")");
	$(this).parent('li').parent('ul').parent('li').parent('ul').parent('em').children("strong").append($("<em class='icon-angle-down'></em>"));
	$(this).parent('li').parent('ul').parent('li').parent('ul').parent('em').children("strong").append($("<i class='icon-remove'></i>"));
	addTh();
}else{
	$(this).parent('li').parent('ul').parent('li').parent('ul').parent('li').parent('ul').parent('em').attr("comcode",onecode);
	$(this).parent('li').parent('ul').parent('li').parent('ul').parent('li').parent('ul').parent('em').children("strong").text(text2_two[0]+"("+name+")");
	$(this).parent('li').parent('ul').parent('li').parent('ul').parent('li').parent('ul').parent('em').children("strong").append($("<em class='icon-angle-down'></em>"));
	$(this).parent('li').parent('ul').parent('li').parent('ul').parent('li').parent('ul').parent('em').children("strong").append($("<i class='icon-remove'></i>"));
	addTh();
}




});




		//统计对象属性下拉菜单显示隐藏
		$(document).on("click",".defin-r-lsit01 > div > em > strong",function(event){
		   event.stopPropagation(); 
		var sibling = $(this).siblings("ul");
		if(sibling.is(":hidden")){
		  $(".defin-r-lsit01 > div > em > ul").slideUp();
		  sibling.slideDown();
		}else{      
		  sibling.slideUp();
		}
		});
		
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
		$(document).click(function(){
		    $(".defin-r-lsit01 > div > em > ul").hide(); 
		  });




























