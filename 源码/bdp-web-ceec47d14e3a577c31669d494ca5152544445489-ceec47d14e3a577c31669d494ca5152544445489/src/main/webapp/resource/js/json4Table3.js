(function($) {
	$.json4Table3 = function(options) {
		_options = {
			tableid : "table",
			action : undefined,
			pageIndex : 0,
			pageSize : 10,
			totalPage : undefined,
			totalCount : undefined,
			formatter : undefined,
			events : undefined,
			callback : undefined
		};
		var opt = $.extend(_options, options);
		//保存一份初始化起始查询页数和条数
		_optionsFinalDef = {
				pageIndex : _options.pageIndex,
				pageSize : _options.pageSize,	
			};
		
		
		var userParam;
		this.drewTable = function(_params) {
			
			//进入页面查询、点击查询按钮，恢复起始查询页数和条数
			opt.pageIndex = _optionsFinalDef.pageIndex;
			opt.pageSize = _optionsFinalDef.pageSize;
			
			userParam=_params;
			var params = $.extend(_params, {
				pageIndex : opt.pageIndex,
				pageSize : opt.pageSize
			});
			//alert("参数"+params.pageIndex);
			ajaxSendload(opt.action, params, function(data) {
				//alert("suoyou"+data.list);
				tempflag=data.tempflag;
				if(data.tempflag!=""&&data.tempflag!=null){
					if(data.tempflag=="yes"){
						$('a[name="tran"]').remove();
					}
				}
				var $table = $("#" + opt.tableid);
				$(".pageCon").html("");
				var data_id = $table.attr('data-id-field');
				$table.find("tbody").html("");// alert(JSON.stringify(data.list));
				//表头
				var listHead=data.listHead
		
				if(listHead!=null){
					$("#headList").empty();
					for (var int = 0; int < listHead.length; int++) {
						var th=$("<th>"+listHead[int]+"</th>");
						$("#headList").append(th);
					}

				}
				
				var listcalu=data.list;
				$("#tbody1").empty();
				  for(var i in listcalu) {
			     //alert("a="+listcalu[i].length);
			      //alert("a");
			      var tep="trr-"+i;
			      var str=$("<tr id="+tep+"  > </tr>");
			       //$("#tb").appendTo(strHead);
			       str.appendTo($("#tbody1"));
			      //判断是否为数组
			       if($.isArray(listcalu[i])){
                      //alert(listcalu[i]);
				          for(var j in listcalu[i]){
					    	  if(listcalu[i][j]==null||listcalu[i][j]==""||listcalu[i][j]=="\"null\""||listcalu[i][j]=="null"){
					    		  listcalu[i][j]="-";
					    	  }
					      //alert("b");
					           //alert("bb="+ listcalu[i][j]);
					          var stt=$("<td>"+listcalu[i][j]+"</td>");
					          stt.appendTo($("#"+tep));
					      }
			       }else{
			    	   if(listcalu[i]==null||listcalu[i]==""||listcalu[i]=="\"null\""||listcalu[i]=="null"){
			    		   
				    		  listcalu[i]="-";
				    	  }
			    	   var stt=$("<td>"+listcalu[i]+"</td>");
				          stt.appendTo($("#"+tep));
			       }
			       } 
				opt.totalPage = data.totalPage;
				opt.totalCount = data.totalCount;
				$table.next().remove();
				$table.parent().siblings(".pageCon").append(this.intiPage());
				if (opt.callback) {
					opt.callback(data);
				}
				//分组
			    $("#table").gridgroup({
					//head:[0],
					column:null
				}); 
			});
		};

		updateTable = function() {
			//alert("22"+opt.pageIndex);
			var params = $.extend(userParam, {
				pageIndex : opt.pageIndex,
				pageSize : opt.pageSize
			});
			ajaxSend(opt.action, params, function(data) {
				var $table = $("#" + opt.tableid);
				$(".pageCon").html("");
				var data_id = $table.attr('data-id-field');
				$table.find("tbody").html("");				
				var listcalu=data.list;
				$("#tbody1").empty();
				  for(var i in listcalu) {
			      //alert(listcalu[i]);
			      //alert("a");
			      var tep="trr-"+i;
			      var str=$("<tr id="+tep+"  > </tr>");
			       //$("#tb").appendTo(strHead);
			       str.appendTo($("#tbody1"));
			     //判断是否为数组
			       if($.isArray(listcalu[i])){
				          for(var j in listcalu[i]){
					    	  //alert("lala");
					    	  if(listcalu[i][j]==null||listcalu[i][j]==""||listcalu[i][j]=="\"null\""||listcalu[i][j]=="null"){
					    		  listcalu[i][j]="-";
					    	  }
					      //alert("b");
					          // alert("bb="+ listcalu[i][j]);
					          var stt=$("<td>"+listcalu[i][j]+"</td>");
					          stt.appendTo($("#"+tep));
					      }
			       }else{
			    	   if(listcalu[i]==null||listcalu[i]==""||listcalu[i]=="\"null\""||listcalu[i]=="null"){
				    		  listcalu[i]="-";
				    	  }
			    	   var stt=$("<td>"+listcalu[i]+"</td>");
				          stt.appendTo($("#"+tep));
			       }
			   }
				opt.totalPage = data.totalPage;
				opt.totalCount = data.totalCount;
				$table.next().remove();
				$table.parent().siblings(".pageCon").append(this.intiPage());
			});
			   //分组
		    $("#table").gridgroup({
				//head:[0],
				column:null
			}); 
		    console.log('0000');

			
		};

		intiPage = function() {	
			
			var pagehtml = "<div class='page'><nav><ul class='pagination'>";
			if (opt.pageIndex == 0) {
				pagehtml += "<li><a aria-label='first'><span aria-hidden='true'>首页</span></a></li>";
				pagehtml += "<li><a aria-label='Previous'><span aria-hidden='true'>上一页</span></a></li>";
			} else {
				pagehtml += "<li><a href='javascript:onPageFirst();' aria-label='first'><span aria-hidden='true'>首页</span></a></li>";
				pagehtml += "<li><a href='javascript:onPagePrevious();' aria-label='Previous'><span aria-hidden='true'>上一页</span></a></li>";
			}
			var j = 0;
			var e = opt.totalPage;
			for ( var i = 1; i <= opt.totalPage; i++) {
				if (opt.pageIndex - 3 >= 0 && i <= opt.pageIndex - 3) {
					if (i == opt.pageIndex - 3) {
						pagehtml += "<li><a href='javascript:onPageIndex("
								+ (i - 1) + ");'>...</a></li>";
					}
				} else {
					if (i <= opt.pageIndex + 4) {
						if (opt.pageIndex + 1 == i) {
							pagehtml += "<li class='active'><a href='#'>" + i
									+ "</a></li>";
						} else {
							pagehtml += "<li><a href='javascript:onPageIndex("
									+ (i - 1) + ");'>" + i + "</a></li>";
						}
					}else{
						if (i == opt.pageIndex + 5) {
							pagehtml += "<li><a href='javascript:onPageIndex("
									+ (i - 1) + ");'>...</a></li>";
						}
					}
				}
			}
			if (opt.pageIndex + 1 == opt.totalPage) {
				pagehtml += "<li><a aria-label='Next'><span aria-hidden='true'>下一页</span></a></li>";
				pagehtml += "<li><a aria-label='last'><span aria-hidden='true'>最后一页</span></a></li>";
			} else {
				pagehtml += "<li><a href='javascript:onPageNext();' aria-label='Next'><span aria-hidden='true'>下一页</span></a></li>";
				pagehtml += "<li><a href='javascript:onPageLast();' aria-label='last'><span aria-hidden='true'>最后一页</span></a></li>";
			}
			pagehtml += "</ul>";
			pagehtml += "<span class='count' totalCount="+opt.totalCount+">总共 " + opt.totalCount + "条记录，每页显示"
					+ opt.pageSize + "条记录，共" + opt.totalPage + "页</span>";
			pagehtml += "</nav></div>";
			return pagehtml;
		};
		onPageFirst = function() {
			opt.pageIndex = 0;
			updateTable();
		};
		onPagePrevious = function() {
			opt.pageIndex = opt.pageIndex - 1;
			updateTable();
		};
		onPageIndex = function(i) {
			opt.pageIndex = i;
			updateTable();
		};
		onPageNext = function() {
			opt.pageIndex = opt.pageIndex + 1;
			updateTable();
			
		};
		onPageLast = function() {
			opt.pageIndex = opt.totalPage - 1;
			updateTable();
		};
	};
})(jQuery);
