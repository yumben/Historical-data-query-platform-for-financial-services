function emptyRecords(speciFlag){
   //每次点击先清空上一次的记录
	if(speciFlag!="group"){
		$("#filedName").val("");
	}
	
	$("#numType").find("input").each(function(i, o) {
		$(this).val("");
	});
	$("#numType2").find("input").each(function(i, o) {
		$(this).val("");
	});
	var  length=$("#filed_time_num>div").length;
	if(length>1){
	   $("#filed_time_num>div").eq(0).nextAll().remove();
	   
	   $(".delico").remove();
	}
	$("#dateType").find("input").each(function(i, o) {
		$(this).val("");
	});
	var  length=$("#filed_time_dateType2>div").length;
	if(length>1){
	   $("#filed_time_dateType2>div").eq(0).nextAll().remove();
	   $(".delico").remove();
	}
	$("#dateType2").find("input").each(function(i, o) {
		$(this).val("");
	});
	var  length=$("#filed_time_dateType>div").length;
	if(length>1){
	   $("#filed_time_dateType>div").eq(0).nextAll().remove();
	   
	   $(".delico").remove();
	}
	$("#filed_time_dateType> div > select").val("1");
	$("#filed_time_dateType")
	$("#dateType3").find("input").each(function(i, o) {
		$(this).val("");
	});
	var  length=$("#filed_time_dateType3>div").length;
	if(length>1){
	   $("#filed_time_dateType3>div").eq(0).nextAll().remove();
	   $(".delico").remove();
	}
	$("#dateType4").find("input").each(function(i, o) {
		$(this).val("");
	});
	var  length=$("#filed_time_dateType4>div").length;
	if(length>1){
	   $("#filed_time_dateType4>div").eq(0).nextAll().remove();
	   $(".delico").remove();
	}
	$("#filed_time_dateType2> div > select").val("1");
	$("#filed_time_dateType3> div > select").val("1");
	$("#filed_time_dateType4> div > select").val("1");
	$("#filed_time_num> div > select").val("1");
}



function  groupFieldMange(metaPraentId,metadataId,flagRefresh){

	
	if(metadataId != null &&　checkFieldRealtion(metadataId) == "no"){
		alert("该字段正在被使用，无法修改");
		return;
	}
	
	if(metaPraentId == null || metaPraentId == ""){
		alert("请选择一个统计对象");
		return;
	}
	
    //清空上一次记录
    emptyRecords(null);
    initFun(metaPraentId);
    if(int==0){
 	   alert("该业务对象没有可以分组的字段");
 	   return;
    }
	var main = $("#showbox"); 
	layer.open({
		title:'新字段',
		closeBtn: 2, 
		type:1,
		area:['550px','410px'],
		shadeClass:true,
		content:main,
		btn:['确定','取消'],
		btnAlign:'c',
		closeBtn:1,
		scrollbar: false,
		success: function(layero, index){
		   //编辑回显
		   if(metadataId != null){
			   var params={"flag":"initEdit","metadataId":metadataId};
			  // ajaxSend("/pms-web/ajax/ajaxQueryStepStatistics",params, initEdit);
			   ajaxSend("/bdp-web/metaqueryGroupFieldController/initEditGroupField",params, initEdit);
		   }
		
		},
		yes: function(index, layero){
    		//按钮【确定】的回调
    		//var doc = $(layero).find("iframe")[0].contentWindow.document;
           
           //元数据部分所需参数
             var fieldName=$("#filedName").val();//将生成的元数据名称
             var isNum = fieldName.slice(0,1);

            if(fieldName==""||fieldName==undefined){
                alert("字段名称不能为空");
                return;
            }
            
            if(!isNaN(isNum)){
           	 alert("字段名称不能以数字开头");
           	 return; 
            }
            
            // 判断字段名称是否存在
    		var fieldFlag = true;
    		var checkParam = {"parentId":metaPraentId, "metadataId":metadataId, "fieldName":fieldName, "flag":"checkName"};
    		//ajaxSend("/pms-web/ajax/ajaxIndexFieldAction",{"parentId":metaPraentId, "metadataId":metadataId, "fieldName":filedName.trim(), "flag":"checkName"},function success(data){
    		ajaxSend("/bdp-web/calculateField/checkCalculateFieldNameAction",{"param" : JSON.stringify(checkParam)},function success(data){	
    		var proList = data.proList;
    			if(proList != null && proList.length > 0){
    				fieldFlag = false;
    			}
    		});
    		
    		if(fieldFlag == false){
    			alert("字段名称已存在");
    			return;
    		}
             var oldId=$("#downlist").children("em").attr("id"); //原元数据id
             var classId=$("#downlist").children("em").attr("classId"); //原元数据id
    		 var protype=$("#downlist").children("em").attr("protype");//数据类型
    		 var dateGroupMode_button= $("#dateGroupMode_button").children("em").attr("value");//日期分组方式
    		 var numGroupMode_button= $("#numGroupMode_button").children("em").attr("value");//数值分组方式
    		 var object = new Object();
             var jsonObject=null;
             var newJSONtext=null;
             var groupMode=null;
             if(protype!=""&&protype!=undefined){//先判断是否分组字段符合的是否为空
              var temp=protype.charAt(protype.length - 1);//获取数字部分
    		 if(temp=="1"){//日期
    		    if(dateGroupMode_button=="1"){//常规
    		    var data_normal_name=new Array();
    		    var data_normal_star=new Array();
    		    var data_normal_end=new Array();
    		    var  dataNormalNameNum=1;
    		    var  dataNormalStarNum=1;
    		    var  dataNormalEnd=1;
    		    var flagTemp=true;
    		         $("input[name='data_normal_name']").each(function() {
    		         if($(this).val()==""||$(this).val()==undefined){
    		                 alert("第"+dataNormalNameNum+"组"+"分组名称不能为空");
    		                 flagTemp=  false;
    		         }
    		              dataNormalNameNum++;
    		              data_normal_name.push($(this).val());
    		         });
    		         if(!flagTemp){ 
    		             return;
    		         }
    		         $("input[name='data_normal_star']").each(function() {
    		         if($(this).val()==""||$(this).val()==undefined){
    		                 alert("第"+dataNormalStarNum+"组"+"起始日期不能为空");
    		                 flagTemp=  false;
    		         }
    		         dataNormalStarNum++
    		              data_normal_star.push($(this).val());
    		         });
    		          if(!flagTemp){ 
    		             return;
    		         }
    		         $("input[name='data_normal_end']").each(function() {
    		         if($(this).val()==""||$(this).val()==undefined){
    		                 alert("第"+dataNormalEnd+"组"+"截至日期不能为空");
    		                 flagTemp=  false;
    		         }
    		         dataNormalEnd++
    		              data_normal_end.push($(this).val());
    		         });
					 if(!flagTemp){ 
    		             return;
    		         }
    		         var NoCoverageArea=$("#NoCoverageAreaData2").val();
    		         if(NoCoverageArea==""||NoCoverageArea==undefined){
    		             alert("未覆盖的范围不能为空");
    		             return;
    		         }
    		         for(var i=0;i<data_normal_star.length;i++){//判断开始时间不能大于结束时间
    		             var d1 = new Date(data_normal_star[i].replace(/\-/g, "\/"));  
						 var d2 = new Date(data_normal_end[i].replace(/\-/g, "\/"));  
						  if(data_normal_star[i]!=""&&data_normal_end[i]!=""&&d1 >d2)  
						 {  
						  alert("第"+(i+1)+"组开始时间不能大于结束时间！");  
						  return false;  
						 }
						//判断分组名称是否相同
    		             for(var j=i;j<data_normal_name.length;j++){
    		                 if(j==i){
    		                   continue;
    		                 }else if(data_normal_name[i]==data_normal_name[j]){
    		                    alert("第"+(i+1)+"组分组名称不能与第"+(j+1)+"组相同");
    		                    return;
    		                 }
    		             }
    		             if(data_normal_name[i]==NoCoverageArea){
    		            	 alert("第"+(i+1)+"组分组名称不能与未覆盖分组名称相同");
    		            	 return;
    		             }
						 
    		         }
    		         object["data_normal_name"]=data_normal_name;
    		         object["data_normal_star"]=data_normal_star;
    		         object["data_normal_end"]=data_normal_end;
    		         object["NoCoverageArea"]=NoCoverageArea;
    		         jsonObject={"object":object};
                     newJSONtext = JSON.stringify(jsonObject);
                     groupMode="日期-"+dateGroupMode_button;
    		    }else if(dateGroupMode_button=="2"){//年内分组
    		       var data_group_yearName =new  Array();
    		       var data_group_yearStar =new  Array();
    		       var data_group_yearEnd =new  Array();
    		       var data_group_yearStarTwo =new  Array();
    		       var data_group_yearEndTwo =new  Array();
    		       var flagTemp=true;
    		       var dataGroupYearNameNum=1;
    		        $("input[name='data_group_yearName']").each(function() {
    		        if($(this).val()==""||$(this).val()==undefined){
    		                 alert("第"+dataGroupYearNameNum+"组"+"分组名称不能为空");
    		                 flagTemp=  false;
    		         }
    		              dataGroupYearNameNum++
    		              data_group_yearName.push($(this).val());
    		         });
    		         if(!flagTemp){ 
    		             return;
    		         }
    		         $("select[name='data_group_yearStar']").each(function() {
    		              data_group_yearStar.push($(this).val());
    		         });
    		         $("select[name='data_group_yearEnd']").each(function() {
    		              data_group_yearEnd.push($(this).val());
    		         });
    		         $("select[name='data_group_yearStarTwo']").each(function() {
    		              data_group_yearStarTwo.push($(this).val());
    		         });
    		         $("select[name='data_group_yearEndTwo']").each(function() {
    		              data_group_yearEndTwo.push($(this).val());
    		         });
    		          var NoCoverageArea=$("#NoCoverageAreaData1").val();
    		          if(NoCoverageArea==""||NoCoverageArea==undefined){
    		             alert("未覆盖的范围不能为空");
    		             return;
    		         }

    		         for(var i=0;i<data_group_yearStar.length;i++){
    		             if(data_group_yearStar[i]!=""&&data_group_yearStarTwo[i]!=""&&parseInt(data_group_yearStar[i])>parseInt(data_group_yearStarTwo[i])){
    		                      //alert("第"+(i+1)+"组起始月不能大于截至月");
    		                      alert("第"+(i+1)+"组起始日期不能大于结束日期");
    		                      return;
    		             }else if(data_group_yearStar[i]!=""&&data_group_yearEnd[i]!=""&&parseInt(data_group_yearStar[i])==parseInt(data_group_yearStarTwo[i])){
    		                  if(data_group_yearEnd[i]!=""&&data_group_yearEndTwo[i]!=""&&parseInt(data_group_yearEnd[i])>parseInt(data_group_yearEndTwo[i])){
    		                     // alert("第"+(i+1)+"组起始月不能大于截至月");
    		                     alert("第"+(i+1)+"组起始日期不能大于结束日期");
    		                     return;
    		                  }
    		             }	
    		             //判断分组名称是否相同
    		             for(var j=i;j<data_group_yearName.length;j++){
    		                 if(j==i){
    		                   continue;
    		                 }else if(data_group_yearName[i]==data_group_yearName[j]){
    		                    alert("第"+(i+1)+"组分组名称不能与第"+(j+1)+"组相同");
    		                    return;
    		                 }
    		             }
    		             if(data_group_yearName[i]==NoCoverageArea){
    		            	 alert("第"+(i+1)+"组分组名称不能与未覆盖分组名称相同");
    		            	 return;
    		             }
    		             
    		         }
    		         object["data_group_yearName"]=data_group_yearName;
    		         object["data_group_yearStar"]=data_group_yearStar;
    		         object["data_group_yearEnd"]=data_group_yearEnd;
    		         object["data_group_yearStarTwo"]=data_group_yearStarTwo;
    		         object["data_group_yearEndTwo"]=data_group_yearEndTwo;
    		         object["NoCoverageArea"]=NoCoverageArea;
    		         jsonObject={"object":object};
    		         newJSONtext = JSON.stringify(jsonObject);
                     groupMode="日期-"+dateGroupMode_button;
    		    }else if(dateGroupMode_button=="3"){//月内分组
    		    	var data_group_monthName=new Array();
    		    	var data_group_monthStar=new Array();
    		    	var data_group_monthEnd=new Array();
    		    	var flagTemp=true;
    		    	var dataGroupMonthNameNum=1;
    		         $("input[name='data_group_monthName']").each(function() {
    		         if($(this).val()==""||$(this).val()==undefined){
    		                 alert("第"+dataGroupMonthNameNum+"组"+"分组名称不能为空");
    		                 flagTemp=  false;
    		         }
    		         dataGroupMonthNameNum++
    		              data_group_monthName.push($(this).val());
    		         });
    		         if(!flagTemp){ 
    		             return;
    		         }
    		         $("select[name='data_group_monthStar']").each(function() {
    		              data_group_monthStar.push($(this).val());
    		         });
    		         $("select[name='data_group_monthEnd']").each(function() {
    		              data_group_monthEnd.push($(this).val());
    		         });

    		         var NoCoverageArea=$("#NoCoverageAreaData3").val();
    		         if(NoCoverageArea==""||NoCoverageArea==undefined){
    		             alert("未覆盖的范围不能为空");
    		             return;
    		         }
    		         for(var i=0;i<data_group_monthStar.length;i++){
                          if(parseInt(data_group_monthStar[i])>parseInt(data_group_monthEnd[i])){
                              alert("第"+(i+1)+"组起始日期不能大于结束日期")
                              return;
                          }
                         //判断分组名称是否相同
    		             for(var j=i;j<data_group_monthName.length;j++){
    		                 if(j==i){
    		                   continue;
    		                 }else if(data_group_monthName[i]==data_group_monthName[j]){
    		                    alert("第"+(i+1)+"组分组名称不能与第"+(j+1)+"组相同");
    		                    return;
    		                 }
    		             }
    		             if(data_group_monthName[i]==NoCoverageArea){
    		            	 alert("第"+(i+1)+"组分组名称不能与未覆盖分组名称相同");
    		            	 return;
    		             }
                     }
    		         object["data_group_monthName"]=data_group_monthName;
    		         object["data_group_monthStar"]=data_group_monthStar;
    		         object["data_group_monthEnd"]=data_group_monthEnd;
    		         object["NoCoverageArea"]=NoCoverageArea;
    		         jsonObject={"object":object};
                    newJSONtext = JSON.stringify(jsonObject);
                    groupMode="日期-"+dateGroupMode_button;
    		    }else if(dateGroupMode_button=="4"){//周内分组
    		    	var data_group_weekName=new Array();
    		    	var data_group_weekStar=new Array();
    		    	var data_group_weekEnd=new Array();
    		    	var flagTemp=true;
    		    	var dataGroupWeekNameNum=1;
    		         $("input[name='data_group_weekName']").each(function() {
    		         if($(this).val()==""||$(this).val()==undefined){
    		                 alert("第"+dataGroupWeekNameNum+"组"+"分组名称不能为空");
    		                 flagTemp=  false;
    		         }
    		         dataGroupWeekNameNum++
    		              data_group_weekName.push($(this).val());
    		         });
    		         if(!flagTemp){ 
    		             return;
    		         }
    		         $("select[name='data_group_weekStar']").each(function() {
    		              data_group_weekStar.push($(this).val());
    		         });
    		         $("select[name='data_group_weekEnd']").each(function() {
    		              data_group_weekEnd.push($(this).val());
    		         });

    		         var NoCoverageArea=$("#NoCoverageAreaData4").val();
    		         if(NoCoverageArea==""||NoCoverageArea==undefined){
    		             alert("未覆盖的范围不能为空");
    		             return;
    		         }
    		        for(var i=0;i<data_group_weekStar.length;i++){
                          if(parseInt(data_group_weekStar[i])>parseInt(data_group_weekEnd[i])){
                              alert("第"+(i+1)+"组起始日期不能大于结束日期")
                              return;
                          }
                         //判断分组名称是否相同
    		             for(var j=i;j<data_group_weekName.length;j++){
    		                 if(j==i){
    		                   continue;
    		                 }else if(data_group_weekName[i]==data_group_weekName[j]){
    		                    alert("第"+(i+1)+"组分组名称不能与第"+(j+1)+"组相同");
    		                    return;
    		                 }
    		             }
    		             if(data_group_weekName[i]==NoCoverageArea){
    		            	 alert("第"+(i+1)+"组分组名称不能与未覆盖分组名称相同");
    		            	 return;
    		             }
                     }
    		         object["data_group_weekName"]=data_group_weekName;
    		         object["data_group_weekStar"]=data_group_weekStar;
    		         object["data_group_weekEnd"]=data_group_weekEnd;
    		         object["NoCoverageArea"]=NoCoverageArea;
    		         jsonObject={"object":object};
                    newJSONtext = JSON.stringify(jsonObject);
                    groupMode="日期-"+dateGroupMode_button;
    		    }
    		    
    		 }else if(temp=="3"){//数值
    		    if(numGroupMode_button=="1"){//固定步长
    		        var star_numType=$("#star_numType").val();//起始
    		        var end_numType=$("#end_numType").val();//终止
    		        var step_numType=$("#step_numType").val();//分组步长
    		        if(star_numType==""||star_numType==undefined){
    		            alert("起始不能为空");
    		            return;
    		        }else if(end_numType==""||end_numType==undefined){
    		            alert("终止不能为空");
    		            return;
    		        }else if(step_numType==""||step_numType==undefined){
    		            alert("分组步长");
    		            return;
    		        }
    		        if(parseInt(star_numType)>parseInt(end_numType)){
    		           alert("起始不能大于终止.");
    		           return;
    		        }
    		        if(parseInt(end_numType)<parseInt(step_numType)){
    		           alert("分组步长不能大于终止值");
    		           return;
    		        }
    		        object["star_numType"]=star_numType;
    		        object["end_numType"]=end_numType;
    		        object["step_numType"]=step_numType;
                    jsonObject={"object":object};
                    newJSONtext = JSON.stringify(jsonObject);
                    groupMode="数值-"+numGroupMode_button;
    		      }else if(numGroupMode_button=="2"){//自定义步长
    		    	 var text_group_name=new  Array();
		    		 var text_group_star=new  Array();
		    		 var select_symbol_star=new  Array();
		    		 var select_symbol_end=new  Array();
		    		 var text_group_end=new  Array();
		    		 var groupNameNum=1;
		    		 var groupStarNum=1;
		    		 var groupEndNum=1;
		    		 var flagTemp=true;
    		         $("input[name='text_group_name']").each(function() {
    		              if($(this).val()==""||$(this).val()==undefined){
    		                  alert("第"+groupNameNum+"组"+"分组名称不能为空");
    		                  flagTemp=  false;
    		              }
    		              text_group_name.push($(this).val());
    		              groupNameNum++;
    		         });
    		         if(!flagTemp){ 
    		             return;
    		         }
    		         $("input[name='text_group_star']").each(function() {
    		             if($(this).val()==""||$(this).val()==undefined){
    		                  alert("第"+groupStarNum+"组"+"起始不能为空");
    		                 flagTemp=  false;
    		              }
    		              groupStarNum++;
    		              text_group_star.push($(this).val());
    		         });
    		         if(!flagTemp){ 
    		             return;
    		         }
    		         $("select[name='select_symbol_star']").each(function() {
    		                select_symbol_star.push($(this).val());
    		         });
    		         $("select[name='select_symbol_end']").each(function() {
    		              select_symbol_end.push($(this).val());
    		         });

    		         $("input[name='text_group_end']").each(function() {
    		              if($(this).val()==""||$(this).val()==undefined){
    		                  alert("第"+groupEndNum+"组"+"结束不能为空");
    		                  flagTemp=  false;
    		              }
    		              groupEndNum++;
    		              text_group_end.push($(this).val());
    		         });
                     if(!flagTemp){ 
    		             return;
    		         }
                     var NoCoverageArea=$("#NoCoverageArea").val();
    		         if(NoCoverageArea==""||NoCoverageArea==undefined){
    		             alert("未覆盖的范围不能为空");
    		             return;
    		         }
    		         for(var i=0;i<text_group_star.length;i++){

    		             if(parseInt(text_group_star[i])>parseInt(text_group_end[i])){

    		            // if(parstext_group_star[i]>text_group_end[i]){

    		                 alert("第"+(i+1)+"组起始不能大于结束");
    		                 return;
    		             }
    		             //判断分组名称是否相同
    		             for(var j=i;j<text_group_name.length;j++){
    		                 if(j==i){
    		                   continue;
    		                 }else if(text_group_name[i]==text_group_name[j]){
    		                    alert("第"+(i+1)+"组分组名称不能与第"+(j+1)+"组相同");
    		                    return;
    		                 }
    		             }
    		             if(text_group_name[i]==NoCoverageArea){
    		            	 alert("第"+(i+1)+"组分组名称不能与未覆盖分组名称相同");
    		            	 return;
    		             }
    		         }
    		        
    		         object["text_group_name"]=text_group_name;
    		         object["text_group_star"]=text_group_star;
    		         object["text_group_end"]=text_group_end;
    		         object["s_symbol_star"]=select_symbol_star;
    		         object["s_symbol_end"]=select_symbol_end;
    		         object["NoCoverageArea"]=NoCoverageArea;
    		         jsonObject={"object":object};
                     newJSONtext = JSON.stringify(jsonObject);
                     groupMode="数值-"+numGroupMode_button;
    		    }
    		 }
    		 var params = {"flag":"addMetadaAndPro","filedName":fieldName,"oldId":oldId,"metaPraentId":metaPraentId,"object":newJSONtext,"groupMode":groupMode,"classId":classId};;
    		 var  url="";
    		 if(metadataId != null){
    			 params.metadataId = metadataId;
    			 params.flag = "edit";
    			 url="/bdp-web/metaqueryGroupFieldController/editGroupField"
    		 }else{
    			 url="/bdp-web/metaqueryGroupFieldController/addGroupField"
    		 }
    		// ajaxSend("/pms-web/ajax/ajaxQueryStepStatistics",params,function  groupPageSet(){
    		 ajaxSend(url,params,function  groupPageSet(){
                if(flagRefresh=="flexibleQuery"){
                    //更新左侧属性
                	businessObjPro(metaPraentId);
                	//清空输出属性
                	$("#queryAttr01").empty();
                	$("#headList").empty();
                }else if(flagRefresh=="Echart"){
	    		       //更新左侧属性
	    		  	  var params = {"metadataId":metaPraentId,"flag":"initMenu"};
					 // ajaxSend("/pms-web/ajax/ajaxQueryIndexAction",params,initMenu);
	    		  	 ajaxSend("/bdp-web/metaqueryChartTemplateController/queryStatisticalObject",params,initMenu);
					  $("#nav-left").empty();
					  $("#search").val("");
					  $("#pro_search").val("");
					  $(".attrshow").hide();
					  $(".filesShow").hide();	
                }else {
                	var params = {"flag":"pro_search", "metadataId":metaPraentId, "name":null};
                	//ajaxSend("/pms-web/ajax/ajaxQueryIndexAction",params,initIndexMenu);
                	ajaxSend("/bdp-web/index/queryProSearchAction",{"param" : JSON.stringify(params)},initIndexMenu);
                }
    		        
    		 });
    		}else{
    		    alert("添加失败...符合要求的分组字段为空");
    		}
    		
    		
    		
    		//var parent = $(layero).find("iframe")[0].contentWindow;
    		
    			
    		layer.close(index); //如果设定了yes回调，需进行手工关闭
	  	}
	});
}

//删除
   function  delGroupFiled(metaPraentId,delId,flagRefresh){
	   
		if(delId != null &&　checkFieldRealtion(delId) == "no"){
			alert("该字段正在被使用，无法修改");
			return;
		}
	   
	    var params={"flag":"delMetadaAndPro","delId":delId};
	    ajaxSend("/bdp-web/metaqueryGroupFieldController/delGroupField",params,function  groupPageSets(){
	    	alert("删除成功");
	    	 if(flagRefresh=="flexibleQuery"){
                 //更新左侧属性
             	businessObjPro(metaPraentId);
            	//清空输出属性
            	$("#queryAttr01").empty();
            	$("#headList").empty();
             }else if(flagRefresh=="Echart"){
  		       //更新左侧属性
  		  	  var params = {"metadataId":metaPraentId,"flag":"initMenu"};
				  //ajaxSend("/pms-web/ajax/ajaxQueryIndexAction",params,initMenu);
  		  	 ajaxSend("/bdp-web/metaqueryChartTemplateController/queryStatisticalObject",params,initMenu);
				  $("#nav-left").empty();
				  $("#search").val("");
				  $("#pro_search").val("");
				  $(".attrshow").hide();
				  $(".filesShow").hide();	
          }else {
          	var params = {"flag":"pro_search", "metadataId":metaPraentId, "name":null};
          	//ajaxSend("/pms-web/ajax/ajaxQueryIndexAction",params,initIndexMenu);
          	ajaxSend("/bdp-web/index/queryProSearchAction",{"param" : JSON.stringify(params)},initIndexMenu);
          }
	    });
	   
   }

// 初始化修改信息
function initEdit(data){
	
	var vo = data.vo;
	var fieldRelationVO = data.fieldRelationVO;
	var groupField = data.groupField;
	var groupMode = data.groupMode;
	var object = eval('(' + data.object + ')');
	var groupType = data.groupType;
	// 回显字段名称
	$("#filedName").val(vo.metadataName);
	// 回显当前选中的字段
	$("#downlist").empty();
	var em = $("<em></em>");
	em.attr("protype", groupType);
	em.attr("code", fieldRelationVO.metadataCode);
	em.attr("id", fieldRelationVO.metadataId);
	em.attr("classid", fieldRelationVO.classId);
	em.text(fieldRelationVO.metadataName);
	$("#downlist").append(em);
	if(groupType != null){
		var split = groupType.split("#");
		if(split[1] == "1"){
			// 回显日期字段的信息
			if(groupMode != null){
				var mode = groupMode.split("-");
				if(mode[1] == "1"){
					changeDateStatus(mode[1]);
					var obj = object.object;
					var data_normal_name = obj.data_normal_name;
					var data_normal_star = obj.data_normal_star;
					var data_normal_end = obj.data_normal_end;
					var NoCoverageArea = obj.NoCoverageArea;
					$("#filed_time_dateType2").empty();
					if(data_normal_name != null){
						data_normal_name.forEach(function(e,i){
							var div = $("<div></div>");
							div.append(createInput("分组名称", "data_normal_name", e));
							div.append(createInput("起始", "data_normal_star", data_normal_star[i], "datepicker", "readonly"));
							div.append("至");
							div.append(createInput("结束", "data_normal_end", data_normal_end[i], "datepicker", "readonly"));
							div.append($("<i class='fa fa-plus addDateGroup'></i>"));
							if(i != 0){
								div.append($("<i class='fa fa-trash-o delico'></i>"));
							}
							$("#filed_time_dateType2").append(div);
						});
						$("#NoCoverageAreaData2").val(NoCoverageArea);
					}
					
					$("#dateGroupMode_button").children("em").attr("value", mode[1]);
					$("#dateGroupMode_button").children("em").text("常规");
				}else if(mode[1] == "2"){
					changeDateStatus(mode[1]);
					var obj = object.object;
					var data_group_yearName = obj.data_group_yearName;
					var data_group_yearStar = obj.data_group_yearStar;
					var data_group_yearEnd = obj.data_group_yearEnd;
					var data_group_yearStarTwo = obj.data_group_yearStarTwo;
					var data_group_yearEndTwo = obj.data_group_yearEndTwo;
					var NoCoverageArea = obj.NoCoverageArea;
					$("#filed_time_dateType").empty();
					if(data_group_yearName != null){
						data_group_yearName.forEach(function(e,i){
							var div = $("<div></div>");
							div.append(createInput("分组名称", "data_group_yearName", e, "", ""));
							div.append(createSelect("data_group_yearStar", data_group_yearStar[i], split[1], "year", null));
							div.append(createSelect("data_group_yearEnd", data_group_yearEnd[i], split[1], "month", null));
							div.append("至");
							div.append(createSelect("data_group_yearStarTwo", data_group_yearStarTwo[i], split[1], "year", null));
							div.append(createSelect("data_group_yearEndTwo", data_group_yearEndTwo[i], split[1], "month", null));
							div.append($("<i class='fa fa-plus addDateGroup2'></i>"));
							if(i != 0){
								div.append($("<i class='fa fa-trash-o delico'></i>"));
							}
							$("#filed_time_dateType").append(div);
						});
						$("#NoCoverageAreaData1").val(NoCoverageArea);
					}
					
					$("#dateGroupMode_button").children("em").attr("value", mode[1]);
					$("#dateGroupMode_button").children("em").text("年内分组");
				}else if(mode[1] == "3"){
					changeDateStatus(mode[1]);
					var obj = object.object;
					var data_group_monthName = obj.data_group_monthName;
					var data_group_monthStar = obj.data_group_monthStar;
					var data_group_monthEnd = obj.data_group_monthEnd;
					var NoCoverageArea = obj.NoCoverageArea;
					$("#filed_time_dateType3").empty();
					if(data_group_monthName != null){
						data_group_monthName.forEach(function(e,i){
							var div = $("<div></div>");
							div.append(createInput("分组名称", "data_group_monthName", e, "", ""));
							div.append(createSelect("data_group_monthStar", data_group_monthStar[i], split[1], "month", null));
							div.append("至");
							div.append(createSelect("data_group_monthEnd", data_group_monthEnd[i], split[1], "month", null));
							div.append($("<i class='fa fa-plus addDateGroup3'></i>"));
							if(i != 0){
								div.append($("<i class='fa fa-trash-o delico'></i>"));
							}
							$("#filed_time_dateType3").append(div);
						});
						$("#NoCoverageAreaData3").val(NoCoverageArea);
					}
					
					$("#dateGroupMode_button").children("em").attr("value", mode[1]);
					$("#dateGroupMode_button").children("em").text("月内分组");
				}else if(mode[1] == "4"){
					changeDateStatus(mode[1]);
					var obj = object.object;
					var data_group_weekName = obj.data_group_weekName;
					var data_group_weekStar = obj.data_group_weekStar;
					var data_group_weekEnd = obj.data_group_weekEnd;
					var NoCoverageArea = obj.NoCoverageArea;
					$("#filed_time_dateType4").empty();
					if(data_group_weekName != null){
						data_group_weekName.forEach(function(e,i){
							var div = $("<div></div>");
							div.append(createInput("分组名称", "data_group_weekName", e, "", ""));
							div.append(createSelect("data_group_weekStar", data_group_weekStar[i], split[1], "week", null));
							div.append("至");
							div.append(createSelect("data_group_weekEnd", data_group_weekEnd[i], split[1], "week", null));
							div.append($("<i class='fa fa-plus addDateGroup4'></i>"));
							if(i != 0){
								div.append($("<i class='fa fa-trash-o delico'></i>"));
							}
							$("#filed_time_dateType4").append(div);
						});
						$("#NoCoverageAreaData4").val(NoCoverageArea);
					}
					
					$("#dateGroupMode_button").children("em").attr("value", mode[1]);
					$("#dateGroupMode_button").children("em").text("周内分组");
				}
				
			}
		}else if(split[1] == "2"){
			// 回显字符字段的信息
		}else if(split[1] == "3"){
			// 回显数值字段的信息
			if(groupMode != null){
				var mode = groupMode.split("-");
				if(mode[1] == "1"){
					$("#numType").attr("style","display: block;");
					$("#dateType2").attr("style","display: none;");
					$("#dateFiled").attr("style","display: none;");
					$("#numFiled").show();
					var obj = object.object;
					$("#star_numType").val(obj.star_numType);
					$("#end_numType").val(obj.end_numType);
					$("#step_numType").val(obj.step_numType);
					$("#numGroupMode_button").children("em").attr("value", mode[1]);
					$("#numGroupMode_button").children("em").text("固定步长");
				}else if(mode[1] == "2"){
					$("#numType").attr("style","display: none;");
					$("#numType2").attr("style","display: block;");
					$("#dateType2").attr("style","display: none;");
					$("#dateFiled").attr("style","display: none;");
					$("#numFiled").show();
					var obj = object.object;
					var text_group_name = obj.text_group_name;
					var text_group_star = obj.text_group_star;
					var text_group_end = obj.text_group_end;
					var s_symbol_star = obj.s_symbol_star;
					var s_symbol_end = obj.s_symbol_end;
					var NoCoverageArea = obj.NoCoverageArea;
					$("#filed_time_num").empty();
					if(text_group_name != null){
						text_group_name.forEach(function(e,i){
							var div = $("<div></div>");
							div.append(createInput("分组名称", "text_group_name", e, "", ""));
							div.append(createInput("起始", "text_group_star", text_group_star[i], "", ""));
							div.append(createSelect("select_symbol_star", s_symbol_star[i], split[1], null, "start"));
							div.append("至");
							div.append(createSelect("select_symbol_end", s_symbol_end[i], split[1], null, "end"));
							div.append(createInput("结束", "text_group_end", text_group_end[i], "", ""));
							div.append($("<i class='fa fa-plus addNumType2'></i>"));
							if(i != 0){
								div.append($("<i class='fa fa-trash-o delico'></i>"));
							}
							$("#filed_time_num").append(div);
						});
						$("#NoCoverageArea").val(NoCoverageArea);
					}
					
					$("#numGroupMode_button").children("em").attr("value", mode[1]);
					$("#numGroupMode_button").children("em").text("自定义步长");
				}
			}
		}
	}
	initDate();
}

/**
 * 生成input标签
 */
function createInput(placeholder, name, value, classed, readonly){
	
	var input = $("<input></input>");
	
	input.attr("type","text");
	input.attr("placeholder",placeholder);
	input.attr("name",name);
	input.val(value);
	input.attr("class", classed);
	if(readonly != "" && readonly != null){
		input.attr("readonly", readonly);
	}
	input.attr("onkeyup","this.value=this.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\_]/g,'')");
	
	return input;
}

/**
 * 生成select标签
 */
function createSelect(name, value, status, type, startOrend){
	
	var select = $("<select></select>");
	
	select.attr("name",name);
	
	if(status == "3"){
		var option1 = $("<option></option>");
		var option2 = $("<option></option>");
		if(startOrend == "start"){
			if(value == ">"){
				option1.attr("value", value);
				option1.attr("selected", "selected");
				option1.text("<");
				select.text("<");
				
				option2.attr("value", ">=");
				option2.text("<=");
			}else if(value == ">="){
				option1.attr("value", value);
				option1.attr("selected", "selected");
				option1.text("<=");
				select.text("<=");
				
				option2.attr("value", ">");
				option2.text("<");
			}
		}else if(startOrend == "end"){
			if(value == "<"){
				option1.attr("value", value);
				option1.attr("selected", "selected");
				option1.text("<");
				select.text("<");
				
				option2.attr("value", "<=");
				option2.text("<=");
			}else if(value == "<="){
				option1.attr("value", value);
				option1.attr("selected", "selected");
				option1.text("<=");
				select.text("<=");
				
				option2.attr("value", "<");
				option2.text("<");
			}
		}
		
		select.append(option1);
		select.append(option2);
	}else if(status == "1"){
		if(type == "month"){
			for(var i=1;i<=31;i++){
				var option = $("<option></option>");
				option.attr("value", i);
				option.text(i);
				if(value == i){
					option.attr("selected","selected");
				}
				select.append(option);
			}
		}else if(type == "year"){
			for(var i=1;i<=12;i++){
				var option = $("<option></option>");
				option.attr("value", i);
				option.text(i + "月");
				if(value == i){
					option.attr("selected","selected");
				}
				select.append(option);
			}
		}else if(type == "week"){
			for(var i=1;i<=7;i++){
				var option = $("<option></option>");
				option.attr("value", i);
				option.text(i);
				if(value == i){
					option.attr("selected","selected");
				}
				select.append(option);
			}
		}
	}
	
	return select;
}

/**
 * 改变状态
 */
function changeDateStatus(status){
	
	// 改变分组方式
	$("#dateFiled").attr("style","display: inline-block;");
	$("#numFiled").attr("style","display: none;");
	// 隐藏数值型的分组方式
	$("#numType").attr("style","display: none;");
	$("#numType2").attr("style","display: none;");
	
	if(status == "1"){
		$("#dateType2").attr("style", "display: block;");
		$("#dateType").attr("style", "display: none;");
		$("#dateType3").attr("style", "display: none;");
		$("#dateType4").attr("style", "display: none;");
	}else if(status == "2"){
		$("#dateType").attr("style", "display: block;");
		$("#dateType2").attr("style", "display: none;");
		$("#dateType3").attr("style", "display: none;");
		$("#dateType4").attr("style", "display: none;");
	}else if(status == "3"){
		$("#dateType3").attr("style", "display: block;");
		$("#dateType2").attr("style", "display: none;");
		$("#dateType").attr("style", "display: none;");
		$("#dateType4").attr("style", "display: none;");
	}else if(status == "4"){
		$("#dateType4").attr("style", "display: block;");
		$("#dateType2").attr("style", "display: none;");
		$("#dateType3").attr("style", "display: none;");
		$("#dateType").attr("style", "display: none;");
	}
	
}

//分组字段弹窗里边的js
//业务对象表id
//var  metaPraentId=null;
//分组字段
   var int ;
function initLeftMenufile(params){		
	//ajaxSend("/pms-web/ajax/ajaxQueryStepStatistics",params,calFunfile);
	ajaxSend("/bdp-web/metaqueryGroupFieldController/queryMetadata",params,calFunfile);
}
function  calFunfile(data){
 $("#downlist").empty();  //  清空上一次
 $("#groupFiled").empty();  //  清空上一次
   var metaList=data.metadataList;
   int=0;
   var numVariable=0;
    var  allcode=null;
   metaList.forEach(function(e,i){
       var  temp=e.propertyValue.charAt(e.propertyValue.length - 1);
	     if(temp=="2"){//跳过字符型的
	       return  true ;
	       }
         if(numVariable=="0"){//首位
            var button=$("#downlist");
            var em=$("<em proType="+e.propertyValue+" code="+allcode+" id="+e.metadataId+" classId="+e.classId+">" +e.metadataName+"</em>");
            button.append(em);
            numVariable++;
         }
     
      var  ul=$("#groupFiled");
      var li = $("<li draggable='true' value='1'  id="+e.metadataId+" parent="+e.parentMetadata+" code="+allcode+" proType="+e.propertyValue+" classId="+e.classId+" >"+e.metadataName+"</li>");
      ul.append(li);
      int++;
   });
}

 //初始化	
function initFun(metaPraentId){ 
   //加载日期
   initDate();	
  //获取对象表id
  //metaPraentId = GetQueryString("metaPraentId");	
  var params={"metaPraentId":metaPraentId,"flag":"allPro"};	  
  initLeftMenufile(params)	
var proType=$("#downlist").children("em").attr("proType");
if(proType!=""&&proType!=undefined){

    var  temp=proType.charAt(proType.length - 1);
    if(temp=="1"){
    //初始化下拉框
 //   $("#dateGroupMode_button").children("em").val("1");
    $("#dateGroupMode_button").children("em").attr("value",1);
    $("#dateGroupMode_button").children("em").text("常规")
        $("#dateType2").show();
        $("#dateType").hide();
        $("#dateType3").hide();
        $("#dateType4").hide();
        $("#charType").hide();
        $("#numType").hide();
        $("#numType2").hide();
        $("#dateFiled").show();
        $("#numFiled").hide();
        $("#charFiled").hide();
    }else if(temp=="2"){
        $("#charType").show();
        $("#dateType2").hide();
        $("#numType").hide();
         $("#charFiled").show();
         $("#dateFiled").hide();
         $("#numFiled").hide();
    }else if(temp=="3"){
    //初始化下拉框
 //   $("#numGroupMode_button").children("em").val("1");
    $("#numGroupMode_button").children("em").attr("value",1);
    $("#numGroupMode_button").children("em").text("固定步长")
        $("#numType").show();
        $("#numType2").hide();
        $("#dateType2").hide();
        $("#dateType").hide();
        $("#dateType3").hide();
        $("#dateType4").hide();
        $("#charType").hide();
        $("#numFiled").show();
        $("#dateFiled").hide();
        $("#charFiled").hide();
    }
    }else{//都是字符类型
        $("#dateType2").hide();
        $("#charType").hide();
        $("#numType").hide();
        $("#dateFiled").hide();
        $("#numFiled").hide();
        $("#charFiled").hide();
    }	    
}

//切换分组字段
$(document).on('click', '#groupFiled >li ', function() {
    //清空上一次记录
    emptyRecords("group");
     var proType=$(this).attr("proType");
     var  temp=proType.charAt(proType.length - 1);	     
     var dateText= $("#dateGroupMode_button").children("em").text();
     var numText= $("#numGroupMode_button").children("em").text();
     var charText= $("#charGroupMode_button").children("em").text();	     
     if(temp=="1"){//日期
    //初始化分组方式下拉框
    	$("#dateGroupMode_button").children("em").attr("value",1);
        $("#dateGroupMode_button").children("em").text("常规")
        $("#dateType2").show();
        $("#dateType").hide();
        $("#dateType3").hide();
        $("#dateType4").hide();
        $("#charType").hide();
        $("#numType").hide();
        $("#numType2").hide();
        $("#dateFiled").show();
        $("#numFiled").hide();
        $("#charFiled").hide();
	     /* if(dateText=="常规"){		     	
	        $("#dateType2").show();
	        $("#dateType").hide();
	        $("#dateType3").hide();
	        $("#dateType4").hide();
	        $("#charType").hide();
	        $("#numType").hide();
	        $("#numType2").hide();
	        $("#dateFiled").show();
	        $("#numFiled").hide();
	        $("#charFiled").hide();
	     }else if(dateText=="年内分组"){
	        $("#dateType2").show();
	        $("#dateType").hide();
	        $("#dateType3").hide();
	        $("#dateType4").hide();
	        $("#charType").hide();
	        $("#numType").hide();
	        $("#numType2").hide();
	        $("#dateFiled").show();
	        $("#numFiled").hide();
	        $("#charFiled").hide();  
	     }else if(dateText=="月内分组"){
	        $("#dateType3").show();
	        $("#dateType").hide();
	        $("#dateType2").hide();
	        $("#dateType4").hide();
	        $("#charType").hide();
	        $("#numType").hide();
	        $("#numType2").hide();
	        $("#dateFiled").show();
	        $("#numFiled").hide();
	        $("#charFiled").hide();
	     }else if(dateText=="周内分组"){		     
	        $("#dateType4").show();
	        $("#dateType").hide();
	        $("#dateType2").hide();
	        $("#dateType3").hide();
	        $("#charType").hide();
	        $("#numType").hide();
	        $("#numType2").hide();
	        $("#dateFiled").show();
	        $("#numFiled").hide();
	        $("#charFiled").hide();
	     } *//* else if(dateText=="固定步长"){
	        $("#dateType2").show();
	        $("#dateType").hide();
	        $("#charType").hide();
	        $("#numType").hide();
	        $("#numType2").hide();
	        $("#dateFiled").show();
	        $("#numFiled").hide();
	        $("#charFiled").hide();
	     }else if(dateText=="表达式"){
	        $("#dateType2").show();
	        $("#dateType").hide();
	        $("#charType").hide();
	        $("#numType").hide();
	        $("#dateFiled").show();
	        $("#numFiled").hide();
	        $("#charFiled").hide();
	        $("#numType").hide();
            $("#numType2").hide();
	     } */
        
     }else if(temp=="2"){//字符
        $("#charType").show();
        $("#dateType").hide();
        $("#dateType2").hide();
        $("#numType").hide();
        $("#charFiled").show();
         $("#dateFiled").hide();
         $("#numFiled").hide();
     }else if(temp=="3"){//数字
        //初始化分组方式下拉框
    	$("#numGroupMode_button").children("em").attr("value",1);
        $("#numGroupMode_button").children("em").text("固定步长")
        $("#numType").show();
        $("#numType2").hide();
        $("#dateType2").hide();
        $("#dateType").hide();
        $("#dateType3").hide();
        $("#dateType4").hide();
        $("#charType").hide();
        $("#numFiled").show();
        $("#dateFiled").hide();
        $("#charFiled").hide();
	     /* if(numText=="固定步长"){
	        $("#numType").show();
	        $("#numType2").hide();
	        $("#dateType").hide();
	        $("#dateType2").hide();
	        $("#dateType3").hide();
	        $("#dateType4").hide();
	        $("#charType").hide();
	        $("#numFiled").show();
	        $("#dateFiled").hide();
	        $("#charFiled").hide();
	     }else if(numText=="自定义步长"){
	        $("#numType2").show();
	        $("#numType").hide();
	        $("#dateType").hide();
	        $("#dateType2").hide();
	        $("#dateType3").hide();
	        $("#dateType4").hide();
	        $("#charType").hide();
	        $("#numFiled").show();
	        $("#dateFiled").hide();
	        $("#charFiled").hide();
	        
	     } */
        
     }
});

 //切换分组方式
$(document).on('click', '#dateGroupMode >li ', function() {
    //清空上一次记录
    emptyRecords("group");
   var temp= $(this).val();
   if(temp=="1"){//常规
      $("#dateType2").show();
      $("#dateType").hide();
      $("#dateType3").hide();
      $("#dateType4").hide();
      $("#numType").hide();
   }else if(temp=="2"){//年内分组
      $("#dateType").show();
      $("#dateType2").hide();
      $("#dateType3").hide();
      $("#dateType4").hide();
      $("#numType").hide();
   }else if(temp=="3"){//月内分组
   	  $("#dateType3").show();
      $("#dateType").hide();
      $("#dateType2").hide();
      $("#dateType4").hide();
   }else if(temp=="4"){//周内分组
   	  $("#dateType4").show();
      $("#dateType").hide();
      $("#dateType2").hide();
      $("#dateType3").hide();
   	  $("#numType").hide();
   }/* else if(temp=="5"){//固定步长
      $("#numType").show();
      $("#dateType").hide();
      $("#dateType2").hide();
      $("#dateType3").hide();
   	  $("#dateType4").hide();	   
   } */
}); 

$(document).on('click', '#numGroupMode >li ', function() {
    //清空上一次记录
    emptyRecords("group");
   var temp= $(this).val();
   if(temp=="1"){//固定步长
        $("#numType").show();
        $("#numType2").hide();
        $("#dateType").hide();	     
   }else if(temp=="2"){//自定义步长
       $("#numType2").show();
       $("#numType").hide();
       
   }
});


//日期初始化
function   initDate(){
   //日期初始化
	$('.datepicker').datetimepicker({ 
	　　minView: "month", //选择日期后，不会再跳转去选择时分秒 
	　　format: "yyyy-mm-dd", //选择日期后，文本框显示的日期格式 
	　　language: 'zh-CN', //汉化 
	　　autoclose:true, //选择日期后自动关闭 
	   todayBtn: true		
	});
}
//增加分组数值自定义步长
$(document).on('click', '.addNumType2 ' , function(){
	  $("#filed_time_num > div .delico").remove();	  
      var div='<div><input type="text" placeholder="分组名称" onkeyup="this.value=this.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\_]/g,\'\')" name="text_group_name">'
      +'<input type="text"  placeholder="起始" onkeyup="this.value=this.value.replace(/[^0-9]/g,\'\')" name="text_group_star">'
      +'<select name="select_symbol_star"><option value=">" selected="selected"> &lt; </option> <option value=">=" > &lt;= </option> </select>'
      +'至<select name="select_symbol_end"> <option value="<" selected="selected"> &lt; </option> <option value="<=" > &lt;= </option> </select>'
      +'<input type="text"  placeholder="结束" onkeyup="this.value=this.value.replace(/[^0-9]/g,\'\')" name="text_group_end"><i class="fa fa-plus addNumType2"></i></div>'
       $("#filed_time_num").append(div);       
       if($("#filed_time_num > div").length > 1){
			var delico = "<i class='fa fa-trash-o delico'></i>"
			$("#filed_time_num > div").append(delico);
		}       
});
$(document).on("click", "#filed_time_num > div .delico" , function(){
	$(this).parent("div").remove();	
	if($("#filed_time_num > div").length == 1){	    	
		$("#filed_time_num > div .delico").remove();
	}	
});	
//增加分组日期常规
$(document).on('click', '.addDateGroup ', function() {
		$("#filed_time_dateType2 > div .delico").remove();
       //加载日期
		initDate();
       var div='<div><input type="text" placeholder="分组名称" onkeyup="this.value=this.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\_]/g,\'\')" name="data_normal_name">'
      +'<input type="text" class="datepicker" placeholder="起始日期" readonly="readonly" name="data_normal_star">'
       +'至<input type="text"  placeholder="截至日期" readonly="readonly" class="datepicker" name="data_normal_end"><i class="fa fa-plus addDateGroup"></i> </div>';
        $("#filed_time_dateType2").append(div);
        if($("#filed_time_dateType2 > div").length > 1){
			var delico = "<i class='fa fa-trash-o delico'></i>"
			$("#filed_time_dateType2 > div").append(delico);
		}
		initDate();
});
$(document).on("click", "#filed_time_dateType2 > div .delico" , function(){
	$(this).parent("div").remove();	
	if($("#filed_time_dateType2 > div").length == 1){	    	
		$("#filed_time_dateType2 > div .delico").remove();
	}	
});
//增加分组年内分组
$(document).on('click', '.addDateGroup2 ', function() {
	   $("#filed_time_dateType > div .delico").remove();
       var  div ='<div><input type="text" placeholder="分组名称" onkeyup="this.value=this.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\_]/g,\'\')" name="data_group_yearName"><select name="data_group_yearStar">'
       +'<option value="1">1月</option>'
       +'<option value="2">2月</option>'
	   +'<option value="3">3月</option>'
	   +'<option value="4">4月</option>'
	   +'<option value="5">5月</option>'
	   +'<option value="6">6月</option>'
	   +'<option value="7">7月</option>'
	   +'<option value="8">8月</option>'
	   +'<option value="9">9月</option>'
	   +'<option value="10">10月</option>'
	   +'<option value="11">11月</option>'
	   +'<option value="12">12月</option></select><select name="data_group_yearEnd">'
	   +'<option value="1">1</option>'
	   +'<option value="2">2</option>'
	   +'<option value="3">3</option>'
	   +'<option value="4">4</option>'
	   +'<option value="5">5</option>'
	   +'<option value="6">6</option>'
	   +'<option value="7">7</option>'
	   +'<option value="8">8</option>'
	   +'<option value="9">9</option>'
	   +'<option value="10">10</option>'
	   +'<option value="11">11</option>'
	   +'<option value="12">12</option>'
	   +'<option value="13">13</option>'
	   +'<option value="14">14</option>'
	   +'<option value="15">15</option>'
	   +'<option value="16">16</option>'
	   +'<option value="17">17</option>'
	   +'<option value="18">18</option>'
	   +'<option value="19">19</option>'
	   +'<option value="20">20</option>'
	   +'<option value="21">21</option>'
	   +'<option value="22">22</option>'
	   +'<option value="23">23</option>'
	   +'<option value="24">24</option>'
	   +'<option value="25">25</option>'
	   +'<option value="26">26</option>'
	   +'<option value="27">27</option>'
	   +'<option value="28">28</option>'
	   +'<option value="29">29</option>'
	   +'<option value="30">30</option>'
	   +'<option value="31">31</option></select>至<select name="data_group_yearStarTwo">'
	   +'<option value="1">1月</option>'
       +'<option value="2">2月</option>'
	   +'<option value="3">3月</option>'
	   +'<option value="4">4月</option>'
	   +'<option value="5">5月</option>'
	   +'<option value="6">6月</option>'
	   +'<option value="7">7月</option>'
	   +'<option value="8">8月</option>'
	   +'<option value="9">9月</option>'
	   +'<option value="10">10月</option>'
	   +'<option value="11">11月</option>'
	   +'<option value="12">12月</option></select><select name="data_group_yearEndTwo">'
	   	   +'<option value="1">1</option>'
	   +'<option value="2">2</option>'
	   +'<option value="3">3</option>'
	   +'<option value="4">4</option>'
	   +'<option value="5">5</option>'
	   +'<option value="6">6</option>'
	   +'<option value="7">7</option>'
	   +'<option value="8">8</option>'
	   +'<option value="9">9</option>'
	   +'<option value="10">10</option>'
	   +'<option value="11">11</option>'
	   +'<option value="12">12</option>'
	   +'<option value="13">13</option>'
	   +'<option value="14">14</option>'
	   +'<option value="15">15</option>'
	   +'<option value="16">16</option>'
	   +'<option value="17">17</option>'
	   +'<option value="18">18</option>'
	   +'<option value="19">19</option>'
	   +'<option value="20">20</option>'
	   +'<option value="21">21</option>'
	   +'<option value="22">22</option>'
	   +'<option value="23">23</option>'
	   +'<option value="24">24</option>'
	   +'<option value="25">25</option>'
	   +'<option value="26">26</option>'
	   +'<option value="27">27</option>'
	   +'<option value="28">28</option>'
	   +'<option value="29">29</option>'
	   +'<option value="30">30</option>'
	   +'<option value="31">31</option></select><i class="fa fa-plus addDateGroup2"></i></div>'	   
	   $("#filed_time_dateType").append(div);
	   if($("#filed_time_dateType > div").length > 1){
			var delico = "<i class='fa fa-trash-o delico'></i>"
			$("#filed_time_dateType > div").append(delico);
		}
});
$(document).on("click", "#filed_time_dateType > div .delico" , function(){
	$(this).parent("div").remove();	
	if($("#filed_time_dateType > div").length == 1){	    	
		$("#filed_time_dateType > div .delico").remove();
	}	
});

//增加分组月内分组
$(document).on('click', '.addDateGroup3 ', function() {
	   $("#filed_time_dateType3 > div .delico").remove();
       var  div ='<div><input type="text" placeholder="分组名称" onkeyup="this.value=this.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\_]/g,\'\')" name="data_group_monthName"><select name="data_group_monthStar">'
	   +'<option value="1">1</option>'
	   +'<option value="2">2</option>'
	   +'<option value="3">3</option>'
	   +'<option value="4">4</option>'
	   +'<option value="5">5</option>'
	   +'<option value="6">6</option>'
	   +'<option value="7">7</option>'
	   +'<option value="8">8</option>'
	   +'<option value="9">9</option>'
	   +'<option value="10">10</option>'
	   +'<option value="11">11</option>'
	   +'<option value="12">12</option>'
	   +'<option value="13">13</option>'
	   +'<option value="14">14</option>'
	   +'<option value="15">15</option>'
	   +'<option value="16">16</option>'
	   +'<option value="17">17</option>'
	   +'<option value="18">18</option>'
	   +'<option value="19">19</option>'
	   +'<option value="20">20</option>'
	   +'<option value="21">21</option>'
	   +'<option value="22">22</option>'
	   +'<option value="23">23</option>'
	   +'<option value="24">24</option>'
	   +'<option value="25">25</option>'
	   +'<option value="26">26</option>'
	   +'<option value="27">27</option>'
	   +'<option value="28">28</option>'
	   +'<option value="29">29</option>'
	   +'<option value="30">30</option>'
	   +'<option value="31">31</option></select>至<select name="data_group_monthEnd">'
	   	   +'<option value="1">1</option>'
	   +'<option value="2">2</option>'
	   +'<option value="3">3</option>'
	   +'<option value="4">4</option>'
	   +'<option value="5">5</option>'
	   +'<option value="6">6</option>'
	   +'<option value="7">7</option>'
	   +'<option value="8">8</option>'
	   +'<option value="9">9</option>'
	   +'<option value="10">10</option>'
	   +'<option value="11">11</option>'
	   +'<option value="12">12</option>'
	   +'<option value="13">13</option>'
	   +'<option value="14">14</option>'
	   +'<option value="15">15</option>'
	   +'<option value="16">16</option>'
	   +'<option value="17">17</option>'
	   +'<option value="18">18</option>'
	   +'<option value="19">19</option>'
	   +'<option value="20">20</option>'
	   +'<option value="21">21</option>'
	   +'<option value="22">22</option>'
	   +'<option value="23">23</option>'
	   +'<option value="24">24</option>'
	   +'<option value="25">25</option>'
	   +'<option value="26">26</option>'
	   +'<option value="27">27</option>'
	   +'<option value="28">28</option>'
	   +'<option value="29">29</option>'
	   +'<option value="30">30</option>'
	   +'<option value="31">31</option></select><i class="fa fa-plus addDateGroup3"></i></div>'	   
	   $("#filed_time_dateType3").append(div);
	   if($("#filed_time_dateType3 > div").length > 1){
			var delico = "<i class='fa fa-trash-o delico'></i>"
			$("#filed_time_dateType3 > div").append(delico);
		}
});
$(document).on("click", "#filed_time_dateType3 > div .delico" , function(){
	$(this).parent("div").remove();	
	if($("#filed_time_dateType3 > div").length == 1){	    	
		$("#filed_time_dateType3 > div .delico").remove();
	}	
});

//增加分组周内分组
$(document).on('click', '.addDateGroup4 ', function() {
	   $("#filed_time_dateType4 > div .delico").remove();
       var  div ='<div><input type="text" placeholder="分组名称" onkeyup="this.value=this.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\_]/g,\'\')" name="data_group_weekName"><select name="data_group_weekStar">'
	   +'<option value="1">1</option>'
	   +'<option value="2">2</option>'
	   +'<option value="3">3</option>'
	   +'<option value="4">4</option>'
	   +'<option value="5">5</option>'
	   +'<option value="6">6</option>'
	   +'<option value="7">7</option></select>至<select name="data_group_weekEnd">'
	   	   +'<option value="1">1</option>'
	   +'<option value="2">2</option>'
	   +'<option value="3">3</option>'
	   +'<option value="4">4</option>'
	   +'<option value="5">5</option>'
	   +'<option value="6">6</option>'
	   +'<option value="7">7</option></select><i class="fa fa-plus addDateGroup4"></i></div>'	   
	   $("#filed_time_dateType4").append(div);
	   if($("#filed_time_dateType4 > div").length > 1){
			var delico = "<i class='fa fa-trash-o delico'></i>"
			$("#filed_time_dateType4 > div").append(delico);
		}
});
$(document).on("click", "#filed_time_dateType4 > div .delico" , function(){
	$(this).parent("div").remove();	
	if($("#filed_time_dateType4 > div").length == 1){	    	
		$("#filed_time_dateType4 > div .delico").remove();
	}	
});

/**
 * 校验该字段是否被使用
 */
function checkFieldRealtion(metadataId){
	
	var params = {"metadataId": metadataId, "flag":"fieldRelation"};
	var result = null;
	//ajaxSend("/pms-web/ajax/ajaxIndexCheckAction", params, function success(data){
	ajaxSend("/bdp-web/calculateField/checkCalculateFieldRelationAction",{"param" : JSON.stringify(params)}, function success(data){
		
		if(data.relationList != null && data.relationList.length>0){
			result = "no";
		}else{
			result = "yes";
		}
	});
	
	return result;
}


